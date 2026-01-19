package task3_4.features.requests;

import di_module.di_annotation.Component;
import di_module.di_annotation.Inject;
import di_module.di_annotation.Singleton;
import task3_4.common.status.BookStatus;
import task3_4.config.BookstoreConfig;
import task3_4.cvs.applier.RequestCsvDtoApplier;
import task3_4.cvs.exporter.RequestCsvExporter;
import task3_4.cvs.importer.RequestCsvImporter;
import task3_4.dao.db.TransactionManager;
import task3_4.exceptions.domain.DomainException;
import task3_4.exceptions.domain.RequestNotFoundException;
import task3_4.features.books.Book;
import task3_4.features.books.BookRepository;
import task3_4.features.orders.Order;
import task3_4.features.orders.OrderRepository;

import java.sql.Connection;
import java.util.List;

@Component
@Singleton
public class RequestService {

    @Inject
    private TransactionManager tm;

    @Inject
    private RequestRepository requestRepo;

    @Inject
    private BookRepository bookRepo;

    @Inject
    private OrderRepository orderRepo;

    @Inject
    private BookstoreConfig config;

    public RequestService() {
    }

    public RequestRepository getRequestRepository() {
        return requestRepo;
    }

    public BookRepository getBookRepository() {
        return bookRepo;
    }

    public OrderRepository getOrderRepository() {
        return orderRepo;
    }

    public List<Request> getAllRequests() {
        return requestRepo.findAllRequests();
    }

    public Request getRequestById(long id) {
        Request request = requestRepo.findRequestById(id);
        if (request == null) {
            throw new RequestNotFoundException(id);
        }
        return request;
    }

    public Request findRequestByBook(Book book) {
        if (book == null) {
            throw new DomainException("Невозможно найти запрос: книга не указана.");
        }
        return requestRepo.findRequestByBook(book);
    }

    public Request createOrAppendRequest(Book missingBook, Order waitingOrder) {
        if (missingBook == null) {
            throw new DomainException("Невозможно создать запрос: книга не указана.");
        }
        if (waitingOrder == null) {
            throw new DomainException("Невозможно создать запрос: заказ не указан.");
        }

        return tm.inTransaction(c -> createOrAppendRequest(c, missingBook, waitingOrder));
    }

    public Request createOrAppendRequest(Connection c, Book missingBook, Order waitingOrder) {
        if (c == null) throw new DomainException("Невозможно создать запрос: connection=null");
        if (missingBook == null) throw new DomainException("Невозможно создать запрос: книга не указана.");
        if (waitingOrder == null) throw new DomainException("Невозможно создать запрос: заказ не указан.");

        Request existing = requestRepo.findRequestByBook(c, missingBook);
        if (existing != null) {
            existing.addWaitingOrder(waitingOrder);
            requestRepo.updateRequest(c, existing);
            return existing;
        }

        Request request = new Request(missingBook);
        request.addWaitingOrder(waitingOrder);

        requestRepo.addRequest(c, request);
        return request;
    }

    public void updateRequest(Request incoming) {
        if (incoming == null) throw new DomainException("Невозможно обновить запрос: данные не указаны.");

        tm.inTransactionVoid(c -> updateRequest(c, incoming));
    }

    public void updateRequest(Connection c, Request incoming) {
        if (c == null) throw new DomainException("Невозможно обновить запрос: connection=null");
        if (incoming == null) throw new DomainException("Невозможно обновить запрос: данные не указаны.");

        Request existing = requestRepo.findRequestById(c, incoming.getId());
        if (existing == null) throw new RequestNotFoundException(incoming.getId());

        existing.setBook(incoming.getBook());
        existing.setResolved(incoming.isResolved());

        existing.getWaitingOrders().clear();
        existing.getWaitingOrders().addAll(incoming.getWaitingOrders());

        requestRepo.updateRequest(c, existing);
    }

    public void completeRequest(long id) {
        tm.inTransactionVoid(c -> completeRequest(c, id));
    }

    public void completeRequest(Connection c, long id) {
        if (c == null) throw new DomainException("Невозможно завершить запрос: connection=null");

        Request r = requestRepo.findRequestById(c, id);
        if (r == null) throw new RequestNotFoundException(id);
        if (r.isResolved()) {
            throw new DomainException("Запрос ID=" + id + " уже помечен как выполненный.");
        }

        r.setResolved(true);
        requestRepo.updateRequest(c, r);
    }


    public void fulfillRequest(Book arrivedBook) {
        if (arrivedBook == null) throw new DomainException("Невозможно выполнить запрос: книга не указана.");

        if (!config.isAutoResolveRequestsEnabled()) {
            throw new DomainException(
                    "Автоматическое выполнение запросов при поступлении книги отключено настройками приложения."
            );
        }

        tm.inTransactionVoid(c -> fulfillRequest(c, arrivedBook));
    }

    public void fulfillRequest(Connection c, Book arrivedBook) {
        if (c == null) throw new DomainException("Невозможно выполнить запрос: connection=null");
        if (arrivedBook == null) throw new DomainException("Невозможно выполнить запрос: книга не указана.");

        Request request = requestRepo.findRequestByBook(c, arrivedBook);
        if (request == null) {
            throw new DomainException("Для книги ID=" + arrivedBook.getId() + " нет активного запроса.");
        }

        if (request.isResolved()) {
            throw new DomainException("Запрос ID=" + request.getId() + " уже выполнен.");
        }

        request.resolve();
        requestRepo.updateRequest(c, request);

        arrivedBook.setStatus(BookStatus.AVAILABLE);
        bookRepo.updateBook(c, arrivedBook);
    }


    public void deleteRequest(long id) {
        tm.inTransactionVoid(c -> {
            boolean removed = requestRepo.deleteRequestById(c, id);
            if (!removed) throw new RequestNotFoundException(id);
        });
    }


    public void importRequestsFromCsv(String filePath,
                                      RequestCsvImporter importer,
                                      RequestCsvDtoApplier applier) {
        List<RequestCsvDto> dtos = importer.importFrom(filePath);
        for (RequestCsvDto dto : dtos) {
            applier.apply(dto);
        }
    }

    public void exportRequestsToCsv(String filePath,
                                    RequestCsvExporter exporter) {
        exporter.exportTo(filePath, requestRepo.findAllRequests());
    }
}

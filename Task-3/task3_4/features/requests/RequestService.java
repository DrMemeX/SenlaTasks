package task3_4.features.requests;

import task3_4.common.status.BookStatus;
import task3_4.exceptions.domain.DomainException;
import task3_4.exceptions.domain.RequestNotFoundException;
import task3_4.features.books.Book;
import task3_4.features.books.BookRepository;
import task3_4.features.orders.Order;
import task3_4.features.orders.OrderRepository;

import java.util.List;

public class RequestService {

    private final RequestRepository requestRepo;
    private final BookRepository bookRepo;
    private final OrderRepository orderRepo;

    public RequestService(RequestRepository requestRepo,
                          BookRepository bookRepo,
                          OrderRepository orderRepo) {
        this.requestRepo = requestRepo;
        this.bookRepo = bookRepo;
        this.orderRepo = orderRepo;
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
        return requestRepo.findAllRequests().stream()
                .filter(r -> r.getBook().equals(book))
                .findFirst()
                .orElse(null);
    }

    public Request createOrAppendRequest(Book missingBook, Order waitingOrder) {
        if (missingBook == null) {
            throw new DomainException("Невозможно создать запрос: книга не указана.");
        }
        if (waitingOrder == null) {
            throw new DomainException("Невозможно создать запрос: заказ не указан.");
        }

        Request existing = findRequestByBook(missingBook);
        if (existing != null) {
            existing.addWaitingOrder(waitingOrder);
            return existing;
        }

        Request request = new Request(missingBook);
        request.addWaitingOrder(waitingOrder);

        requestRepo.addRequest(request);
        return request;
    }

    public void fulfillRequest(Book arrivedBook) {
        if (arrivedBook == null) {
            throw new DomainException("Невозможно выполнить запрос: книга не указана.");
        }

        Request request = findRequestByBook(arrivedBook);
        if (request == null) {
            throw new DomainException(
                    "Для книги ID=" + arrivedBook.getId() + " нет активного запроса."
            );
        }

        if (!request.isResolved()) {
            request.resolve();
            arrivedBook.setStatus(BookStatus.AVAILABLE);
        } else {
            throw new DomainException(
                    "Запрос ID=" + request.getId() + " уже выполнен."
            );
        }
    }

    public void deleteRequest(long id) {
        boolean removed = requestRepo.deleteRequestById(id);
        if (!removed) {
            throw new RequestNotFoundException(id);
        }
    }

    public void updateRequest(Request incoming) {
        if (incoming == null) {
            throw new DomainException("Невозможно обновить запрос: данные не указаны.");
        }

        Request existing = requestRepo.findRequestById(incoming.getId());
        if (existing == null) {
            throw new RequestNotFoundException(incoming.getId());
        }

        existing.setBook(incoming.getBook());
        existing.setResolved(incoming.isResolved());

        existing.getWaitingOrders().clear();
        existing.getWaitingOrders().addAll(incoming.getWaitingOrders());
    }

    public void completeRequest(long id) {
        Request r = requestRepo.findRequestById(id);
        if (r == null) {
            throw new RequestNotFoundException(id);
        }
        if (r.isResolved()) {
            throw new DomainException(
                    "Запрос ID=" + id + " уже помечен как выполненный."
            );
        }

        r.setResolved(true);
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

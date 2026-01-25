package cvs.applier;

import exceptions.csv.CsvMappingException;
import features.books.Book;
import features.books.BookRepository;
import features.orders.Order;
import features.orders.OrderRepository;
import features.requests.Request;
import features.requests.RequestCsvDto;
import features.requests.RequestRepository;

public class RequestCsvDtoApplier extends AbstractCsvDtoApplier<RequestCsvDto, Request> {

    private final RequestRepository requestRepo;
    private final BookRepository bookRepo;
    private final OrderRepository orderRepo;

    public RequestCsvDtoApplier(RequestRepository requestRepo,
                                BookRepository bookRepo,
                                OrderRepository orderRepo) {
        this.requestRepo = requestRepo;
        this.bookRepo = bookRepo;
        this.orderRepo = orderRepo;
    }

    @Override
    public Request apply(RequestCsvDto dto) {

        Book book = bookRepo.findBookById(dto.bookId);
        if (book == null) {
            throw new CsvMappingException(
                    "Ошибка маппинга: книга ID=" + dto.bookId + " не найдена"
            );
        }

        Request existing = requestRepo.findRequestById(dto.id);

        if (existing == null) {
            existing = new Request(dto.id, book);
            requestRepo.addRequest(existing);
        }

        existing.setBook(book);
        existing.setResolved(dto.resolved);

        existing.getWaitingOrders().clear();

        for (Long orderId : dto.waitingOrderIds) {

            Order order = orderRepo.findOrderById(orderId);

            if (order == null) {
                throw new CsvMappingException(
                        "Ошибка маппинга: в запросе ID=" + dto.id +
                                " указан отсутствующий orderId=" + orderId
                );
            }

            existing.getWaitingOrders().add(order);
        }

        return existing;
    }
}

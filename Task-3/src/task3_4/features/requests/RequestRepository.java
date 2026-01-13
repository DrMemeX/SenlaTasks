package task3_4.features.requests;

import di_module.di_annotation.Component;
import di_module.di_annotation.Inject;
import di_module.di_annotation.Singleton;
import task3_4.dao.jdbc.RequestJdbcDao;
import task3_4.features.books.Book;

import java.util.List;

@Component
@Singleton
public class RequestRepository {

    @Inject
    private RequestJdbcDao dao;

    public List<Request> findAllRequests() {
        return dao.findAll();
    }

    public Request findRequestById(long id) {
        return dao.findById(id).orElse(null);
    }

    public void addRequest(Request request) {
        dao.create(request);
    }

    public boolean deleteRequestById(long id) {
        return dao.deleteById(id);
    }

    public void restoreState(List<Request> requestState) {
    }

    public Request findRequestByBook(Book book) {
        if (book == null) return null;
        if (book.getId() == 0) return null;
        return dao.findByBookId(book.getId()).orElse(null);
    }

    public void updateRequest(Request request) {
        dao.update(request);
    }
}

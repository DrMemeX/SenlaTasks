package features.requests;

import di.annotation.Component;
import di.annotation.Inject;
import di.annotation.Singleton;
import dao.jdbc.RequestJdbcDao;
import features.books.Book;

import java.sql.Connection;
import java.util.List;

@Component
@Singleton
public class RequestRepository {

    @Inject
    private RequestJdbcDao dao;

    public void addRequest(Request request) {
        dao.create(request);
    }
    public void addRequest(Connection c, Request request) {
        dao.create(c, request);
    }

    public Request findRequestById(long id) {
        return dao.findById(id).orElse(null);
    }
    public Request findRequestById(Connection c, long id) {
        return dao.findById(c, id).orElse(null);
    }

    public Request findRequestByBook(Book book) {
        if (book == null) return null;
        if (book.getId() == 0) return null;
        return dao.findByBookId(book.getId()).orElse(null);
    }
    public Request findRequestByBook(Connection c, Book book) {
        if (book == null || book.getId() == 0) return null;
        return dao.findByBookId(c, book.getId()).orElse(null);
    }

    public List<Request> findAllRequests() {
        return dao.findAll();
    }

    public void updateRequest(Request request) {
        dao.update(request);
    }
    public void updateRequest(Connection c, Request request) {
        dao.update(c, request);
    }

    public boolean deleteRequestById(long id) {
        return dao.deleteById(id);
    }
    public boolean deleteRequestById(Connection c, long id) {
        return dao.deleteById(c, id);
    }

    public void restoreState(List<Request> requestState) {
    }
}

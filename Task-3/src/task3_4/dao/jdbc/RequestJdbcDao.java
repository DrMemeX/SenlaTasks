package task3_4.dao.jdbc;

import di_module.di_annotation.Component;
import di_module.di_annotation.Singleton;
import task3_4.dao.db.DataSource;
import task3_4.dao.generic.GenericDao;
import task3_4.dao.mapper.BookRowMapper;
import task3_4.dao.mapper.RequestRowMapper;
import task3_4.exceptions.dao.DaoException;
import task3_4.features.books.Book;
import task3_4.features.orders.Order;
import task3_4.features.requests.Request;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Singleton
public class RequestJdbcDao implements GenericDao<Request, Long> {

    private static final String SQL_INSERT_REQUEST =
            "INSERT INTO requests (book_id, resolved) VALUES (?, ?) RETURNING id";

    private static final String SQL_UPDATE_REQUEST =
            "UPDATE requests SET book_id = ?, resolved = ? WHERE id = ?";

    private static final String SQL_FIND_REQUEST_BY_ID =
            "SELECT " +
                    "r.id AS r_id, r.book_id, r.resolved, " +
                    "b.id, b.title, b.author, b.price, b.status, b.release_date, b.added_date, b.description " +
                    "FROM requests r " +
                    "LEFT JOIN books b ON b.id = r.book_id " +
                    "WHERE r.id = ?";

    private static final String SQL_FIND_ALL_REQUESTS =
            "SELECT " +
                    "r.id AS r_id, r.book_id, r.resolved, " +
                    "b.id, b.title, b.author, b.price, b.status, b.release_date, b.added_date, b.description " +
                    "FROM requests r " +
                    "LEFT JOIN books b ON b.id = r.book_id " +
                    "ORDER BY r.id";

    private static final String SQL_FIND_REQUEST_BY_BOOK_ID =
            "SELECT " +
                    "r.id AS r_id, r.book_id, r.resolved, " +
                    "b.id, b.title, b.author, b.price, b.status, b.release_date, b.added_date, b.description " +
                    "FROM requests r " +
                    "LEFT JOIN books b ON b.id = r.book_id " +
                    "WHERE r.book_id = ? " +
                    "ORDER BY r.id LIMIT 1";

    private static final String SQL_DELETE_REQUEST_ORDERS_BY_REQUEST =
            "DELETE FROM request_orders WHERE request_id = ?";

    private static final String SQL_INSERT_REQUEST_ORDER =
            "INSERT INTO request_orders (request_id, order_id) VALUES (?, ?)";

    private static final String SQL_FIND_ORDER_IDS_BY_REQUEST =
            "SELECT order_id FROM request_orders WHERE request_id = ? ORDER BY order_id";

    private static final String SQL_DELETE_REQUEST_BY_ID =
            "DELETE FROM requests WHERE id = ?";

    private final BookRowMapper bookMapper = new BookRowMapper();
    private final RequestRowMapper requestMapper = new RequestRowMapper();

    @Override
    public Request create(Request entity) {
        if (entity == null) throw new DaoException("Ошибка: request=null");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return inTx(c, () -> {
                long id = insertRequestRow(c, entity);
                entity.setId(id);
                insertRequestOrders(c, entity);
                return entity;
            });
        } catch (SQLException e) {
            throw new DaoException("Ошибка создания запроса: " + entity, e);
        }
    }

    @Override
    public Request update(Request entity) {
        if (entity == null) throw new DaoException("Ошибка: request=null");
        if (entity.isNew()) throw new DaoException("Ошибка: нельзя обновить request без id");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return inTx(c, () -> {
                updateRequestRow(c, entity);
                deleteRequestOrders(c, entity.getId());
                insertRequestOrders(c, entity);
                return entity;
            });
        } catch (SQLException e) {
            throw new DaoException("Ошибка обновления запроса id=" + entity.getId(), e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) throw new DaoException("Ошибка: id=null");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return inTx(c, () -> {
                deleteRequestOrders(c, id);
                try (PreparedStatement ps = c.prepareStatement(SQL_DELETE_REQUEST_BY_ID)) {
                    ps.setLong(1, id);
                    return ps.executeUpdate() > 0;
                }
            });
        } catch (SQLException e) {
            throw new DaoException("Ошибка удаления запроса id=" + id, e);
        }
    }

    @Override
    public Optional<Request> findById(Long id) {
        if (id == null) throw new DaoException("Ошибка: id=null");

        try (Connection c = DataSource.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_FIND_REQUEST_BY_ID)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                Request r = mapRequestWithBook(rs);
                r.getWaitingOrders().addAll(loadOrdersStub(c, r.getId()));
                return Optional.of(r);
            }
        } catch (SQLException e) {
            throw new DaoException("Ошибка чтения запроса по id=" + id, e);
        }
    }

    @Override
    public List<Request> findAll() {
        try (Connection c = DataSource.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_FIND_ALL_REQUESTS);
             ResultSet rs = ps.executeQuery()) {

            List<Request> res = new ArrayList<>();
            while (rs.next()) {
                Request r = mapRequestWithBook(rs);
                r.getWaitingOrders().addAll(loadOrdersStub(c, r.getId()));
                res.add(r);
            }
            return res;

        } catch (SQLException e) {
            throw new DaoException("Ошибка чтения списка запросов", e);
        }
    }

    public Optional<Request> findByBookId(long bookId) {
        if (bookId <= 0) throw new DaoException("Ошибка: bookId некорректен: " + bookId);

        try (Connection c = DataSource.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_FIND_REQUEST_BY_BOOK_ID)) {

            ps.setLong(1, bookId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                Request r = mapRequestWithBook(rs);
                r.getWaitingOrders().addAll(loadOrdersStub(c, r.getId()));
                return Optional.of(r);
            }
        } catch (SQLException e) {
            throw new DaoException("Ошибка чтения запроса по bookId=" + bookId, e);
        }
    }

    private <T> T inTx(Connection c, TxWork<T> work) throws SQLException {
        boolean old = c.getAutoCommit();
        c.setAutoCommit(false);
        try {
            T res = work.run();
            c.commit();
            return res;
        } catch (Exception e) {
            c.rollback();
            if (e instanceof RuntimeException re) throw re;
            throw new DaoException("Ошибка выполнения транзакции", e);
        } finally {
            c.setAutoCommit(old);
        }
    }

    @FunctionalInterface
    private interface TxWork<T> {
        T run() throws Exception;
    }

    private long insertRequestRow(Connection c, Request entity) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(SQL_INSERT_REQUEST)) {
            long bookId = entity.getBookId();
            if (bookId > 0) ps.setLong(1, bookId);
            else ps.setNull(1, Types.BIGINT);

            ps.setBoolean(2, entity.isResolved());

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new DaoException("INSERT requests не вернул id");
                return rs.getLong(1);
            }
        }
    }

    private void updateRequestRow(Connection c, Request entity) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(SQL_UPDATE_REQUEST)) {
            long bookId = entity.getBookId();
            if (bookId > 0) ps.setLong(1, bookId);
            else ps.setNull(1, Types.BIGINT);

            ps.setBoolean(2, entity.isResolved());
            ps.setLong(3, entity.getId());

            int updated = ps.executeUpdate();
            if (updated == 0) throw new DaoException("Request не найден для обновления, id=" + entity.getId());
        }
    }

    private void deleteRequestOrders(Connection c, long requestId) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(SQL_DELETE_REQUEST_ORDERS_BY_REQUEST)) {
            ps.setLong(1, requestId);
            ps.executeUpdate();
        }
    }

    private void insertRequestOrders(Connection c, Request entity) throws SQLException {
        List<Order> orders = entity.getWaitingOrders();
        if (orders == null) orders = Collections.emptyList();
        if (orders.isEmpty()) return;

        try (PreparedStatement ps = c.prepareStatement(SQL_INSERT_REQUEST_ORDER)) {
            for (Order o : orders) {
                if (o == null) continue;
                if (o.getId() == 0) throw new DaoException("Нельзя привязать заказ без id к запросу (order.id=0)");
                ps.setLong(1, entity.getId());
                ps.setLong(2, o.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private List<Order> loadOrdersStub(Connection c, long requestId) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(SQL_FIND_ORDER_IDS_BY_REQUEST)) {
            ps.setLong(1, requestId);

            try (ResultSet rs = ps.executeQuery()) {
                List<Order> res = new ArrayList<>();
                while (rs.next()) {
                    long orderId = rs.getLong("order_id");
                    res.add(new Order(orderId));
                }
                return res;
            }
        }
    }

    private Request mapRequestWithBook(ResultSet rs) throws SQLException {
        Request r = requestMapper.map(rs);

        long bookIdFromJoin = rs.getLong("id");
        if (!rs.wasNull()) {
            Book book = bookMapper.map(rs);
            r.setBook(book);
        } else {
            r.setBook(null);
        }
        return r;
    }
}

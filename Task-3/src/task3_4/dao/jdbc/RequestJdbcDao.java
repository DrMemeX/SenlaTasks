package task3_4.dao.jdbc;

import di_module.di_annotation.Component;
import di_module.di_annotation.Singleton;
import task3_4.common.status.BookStatus;
import task3_4.dao.db.DataSource;
import task3_4.dao.generic.GenericDao;
import task3_4.dao.mapper.BookRowMapper;
import task3_4.dao.mapper.RequestRowMapper;
import task3_4.exceptions.dao.DaoException;
import task3_4.features.books.Book;
import task3_4.features.orders.Order;
import task3_4.features.requests.Request;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Singleton
public class RequestJdbcDao extends AbstractJdbcDao implements GenericDao<Request, Long> {

    private static final String SQL_INSERT_REQUEST =
            "INSERT INTO requests (book_id, resolved) VALUES (?, ?) RETURNING id";

    private static final String SQL_UPDATE_REQUEST =
            "UPDATE requests SET book_id = ?, resolved = ? WHERE id = ?";

    private static final String SQL_FIND_REQUEST_BY_ID =
            "SELECT " +
                    "r.id AS r_id, r.book_id, r.resolved, " +
                    "b.id AS b_id, b.title AS b_title, b.author AS b_author, b.price AS b_price, " +
                    "b.status AS b_status, b.release_date AS b_release_date, b.added_date AS b_added_date, b.description AS b_description " +
                    "FROM requests r " +
                    "LEFT JOIN books b ON b.id = r.book_id " +
                    "WHERE r.id = ?";

    private static final String SQL_FIND_ALL_REQUESTS =
            "SELECT " +
                    "r.id AS r_id, r.book_id, r.resolved, " +
                    "b.id AS b_id, b.title AS b_title, b.author AS b_author, b.price AS b_price, " +
                    "b.status AS b_status, b.release_date AS b_release_date, b.added_date AS b_added_date, b.description AS b_description " +
                    "FROM requests r " +
                    "LEFT JOIN books b ON b.id = r.book_id " +
                    "ORDER BY r.id";

    private static final String SQL_FIND_REQUEST_BY_BOOK_ID =
            "SELECT " +
                    "r.id AS r_id, r.book_id, r.resolved, " +
                    "b.id AS b_id, b.title AS b_title, b.author AS b_author, b.price AS b_price, " +
                    "b.status AS b_status, b.release_date AS b_release_date, b.added_date AS b_added_date, b.description AS b_description " +
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

    private final RequestRowMapper requestMapper = new RequestRowMapper();

    @Override
    public Request create(Request entity) {
        if (entity == null) throw new DaoException("Ошибка: request=null");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return create(c, entity);
        } catch (SQLException e) {
            throw new DaoException("Ошибка получения соединения при создании запроса: " + entity, e);
        }
    }

    public Request create(Connection c, Request entity) {
        if (c == null) throw new DaoException("Ошибка: connection=null");
        if (entity == null) throw new DaoException("Ошибка: request=null");

        long id = insertReturningId(c, SQL_INSERT_REQUEST, ps -> {
            long bookId = entity.getBookId();
            if (bookId > 0) ps.setLong(1, bookId);
            else ps.setNull(1, Types.BIGINT);

            ps.setBoolean(2, entity.isResolved());
        }, "Ошибка создания запроса: " + entity);

        entity.setId(id);
        insertRequestOrders(c, entity);
        return entity;
    }

    @Override
    public Optional<Request> findById(Long id) {
        if (id == null) throw new DaoException("Ошибка: id=null");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return findById(c, id);
        } catch (SQLException e) {
            throw new DaoException("Ошибка получения соединения при чтении запроса по id=" + id, e);
        }
    }

    public Optional<Request> findById(Connection c, Long id) {
        if (c == null) throw new DaoException("Ошибка: connection=null");
        if (id == null) throw new DaoException("Ошибка: id=null");

        Optional<Request> opt = queryOne(
                c,
                SQL_FIND_REQUEST_BY_ID,
                ps -> ps.setLong(1, id),
                this::mapRequestWithBook,
                "Ошибка чтения запроса по id=" + id
        );

        if (opt.isEmpty()) return Optional.empty();

        Request r = opt.get();
        r.getWaitingOrders().addAll(loadOrdersStub(c, r.getId()));
        return Optional.of(r);
    }

    @Override
    public List<Request> findAll() {
        try (Connection c = DataSource.getInstance().getConnection()) {
            return findAll(c);
        } catch (SQLException e) {
            throw new DaoException("Ошибка получения соединения при чтении списка запросов", e);
        }
    }

    public List<Request> findAll(Connection c) {
        if (c == null) throw new DaoException("Ошибка: connection=null");

        List<Request> res = queryList(
                c,
                SQL_FIND_ALL_REQUESTS,
                null,
                this::mapRequestWithBook,
                "Ошибка чтения списка запросов"
        );

        for (Request r : res) {
            r.getWaitingOrders().addAll(loadOrdersStub(c, r.getId()));
        }
        return res;
    }

    public Optional<Request> findByBookId(long bookId) {
        if (bookId <= 0) throw new DaoException("Ошибка: bookId некорректен: " + bookId);

        try (Connection c = DataSource.getInstance().getConnection()) {
            return findByBookId(c, bookId);
        } catch (SQLException e) {
            throw new DaoException("Ошибка получения соединения при чтении запроса по bookId=" + bookId, e);
        }
    }

    public Optional<Request> findByBookId(Connection c, long bookId) {
        if (c == null) throw new DaoException("Ошибка: connection=null");
        if (bookId <= 0) throw new DaoException("Ошибка: bookId некорректен: " + bookId);

        Optional<Request> opt = queryOne(
                c,
                SQL_FIND_REQUEST_BY_BOOK_ID,
                ps -> ps.setLong(1, bookId),
                this::mapRequestWithBook,
                "Ошибка чтения запроса по bookId=" + bookId
        );

        if (opt.isEmpty()) return Optional.empty();

        Request r = opt.get();
        r.getWaitingOrders().addAll(loadOrdersStub(c, r.getId()));
        return Optional.of(r);
    }

    @Override
    public Request update(Request entity) {
        if (entity == null) throw new DaoException("Ошибка: request=null");
        if (entity.isNew()) throw new DaoException("Ошибка: нельзя обновить request без id");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return update(c, entity);
        } catch (SQLException e) {
            throw new DaoException("Ошибка получения соединения при обновлении запроса id=" + entity.getId(), e);
        }
    }

    public Request update(Connection c, Request entity) {
        if (c == null) throw new DaoException("Ошибка: connection=null");
        if (entity == null) throw new DaoException("Ошибка: request=null");
        if (entity.isNew()) throw new DaoException("Ошибка: нельзя обновить request без id");

        int updated = execUpdate(c, SQL_UPDATE_REQUEST, ps -> {
            long bookId = entity.getBookId();
            if (bookId > 0) ps.setLong(1, bookId);
            else ps.setNull(1, Types.BIGINT);

            ps.setBoolean(2, entity.isResolved());
            ps.setLong(3, entity.getId());
        }, "Ошибка обновления запроса id=" + entity.getId());

        if (updated == 0) throw new DaoException("Request не найден для обновления, id=" + entity.getId());

        execUpdate(
                c,
                SQL_DELETE_REQUEST_ORDERS_BY_REQUEST,
                ps -> ps.setLong(1, entity.getId()),
                "Ошибка удаления связок request_orders для request_id=" + entity.getId()
        );

        insertRequestOrders(c, entity);
        return entity;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) throw new DaoException("Ошибка: id=null");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return deleteById(c, id);
        } catch (SQLException e) {
            throw new DaoException("Ошибка получения соединения при удалении запроса id=" + id, e);
        }
    }

    public boolean deleteById(Connection c, Long id) {
        if (c == null) throw new DaoException("Ошибка: connection=null");
        if (id == null) throw new DaoException("Ошибка: id=null");

        execUpdate(
                c,
                SQL_DELETE_REQUEST_ORDERS_BY_REQUEST,
                ps -> ps.setLong(1, id),
                "Ошибка удаления связок request_orders для request_id=" + id
        );

        int deleted = execUpdate(
                c,
                SQL_DELETE_REQUEST_BY_ID,
                ps -> ps.setLong(1, id),
                "Ошибка удаления запроса id=" + id
        );

        return deleted > 0;
    }

    private void insertRequestOrders(Connection c, Request entity) {
        List<Order> orders = entity.getWaitingOrders();
        if (orders == null) orders = Collections.emptyList();
        if (orders.isEmpty()) return;

        try (var ps = c.prepareStatement(SQL_INSERT_REQUEST_ORDER)) {
            for (Order o : orders) {
                if (o == null) continue;
                if (o.getId() == 0) throw new DaoException("Нельзя привязать заказ без id к запросу (order.id=0)");
                ps.setLong(1, entity.getId());
                ps.setLong(2, o.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new DaoException("Ошибка вставки связок request_orders для request_id=" + entity.getId(), e);
        }
    }

    private List<Order> loadOrdersStub(Connection c, long requestId) {
        return queryList(
                c,
                SQL_FIND_ORDER_IDS_BY_REQUEST,
                ps -> ps.setLong(1, requestId),
                rs -> new Order(rs.getLong("order_id")),
                "Ошибка чтения request_orders (order_id) для request_id=" + requestId
        );
    }

    private Request mapRequestWithBook(ResultSet rs) throws SQLException {
        Request r = requestMapper.map(rs);

        Long bId = (Long) rs.getObject("b_id");
        if (bId == null) {
            r.setBook(null);
            return r;
        }

        Book book = new Book(
                bId,
                rs.getString("b_title"),
                rs.getString("b_author"),
                rs.getDouble("b_price"),
                BookStatus.fromDb(rs.getString("b_status")),
                rs.getObject("b_release_date", java.time.LocalDate.class),
                rs.getObject("b_added_date", java.time.LocalDate.class),
                rs.getString("b_description")
        );

        r.setBook(book);
        return r;
    }
}

package task3_4.dao.jdbc;

import di_module.di_annotation.Component;
import di_module.di_annotation.Singleton;
import task3_4.dao.db.DataSource;
import task3_4.dao.generic.GenericDao;
import task3_4.dao.mapper.BookRowMapper;
import task3_4.dao.mapper.OrderRowMapper;
import task3_4.exceptions.dao.DaoException;
import task3_4.features.books.Book;
import task3_4.features.orders.Order;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Singleton
public class OrderJdbcDao extends AbstractJdbcDao implements GenericDao<Order, Long> {

    private static final String SQL_INSERT_ORDER =
            "INSERT INTO orders (customer_id, status, creation_date, completion_date, total_price) " +
                    "VALUES (?, ?, ?, ?, ?) RETURNING id";

    private static final String SQL_UPDATE_ORDER =
            "UPDATE orders SET customer_id=?, status=?, creation_date=?, completion_date=?, total_price=? " +
                    "WHERE id=?";

    private static final String SQL_FIND_ORDER_BY_ID =
            "SELECT id, customer_id, status, creation_date, completion_date, total_price " +
                    "FROM orders WHERE id=?";

    private static final String SQL_FIND_ALL_ORDERS =
            "SELECT id, customer_id, status, creation_date, completion_date, total_price " +
                    "FROM orders ORDER BY id";

    private static final String SQL_DELETE_ORDER_BOOKS_BY_ORDER =
            "DELETE FROM order_books WHERE order_id=?";

    private static final String SQL_INSERT_ORDER_BOOK =
            "INSERT INTO order_books (order_id, book_id) VALUES (?, ?)";

    private static final String SQL_DELETE_ORDER_BY_ID =
            "DELETE FROM orders WHERE id=?";

    private static final String SQL_FIND_BOOKS_BY_ORDER_ID =
            "SELECT b.id, b.title, b.author, b.price, b.status, b.release_date, b.added_date, b.description " +
                    "FROM books b " +
                    "JOIN order_books ob ON ob.book_id = b.id " +
                    "WHERE ob.order_id = ? " +
                    "ORDER BY b.id";

    private final OrderRowMapper orderMapper = new OrderRowMapper();
    private final BookRowMapper bookMapper = new BookRowMapper();

    @Override
    public Order create(Order entity) {
        if (entity == null) throw new DaoException("Ошибка: order=null");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return create(c, entity);
        } catch (SQLException e) {
            throw new DaoException("Ошибка получения соединения при создании заказа: " + entity, e);
        }
    }

    public Order create(Connection c, Order entity) {
        if (c == null) throw new DaoException("Ошибка: connection=null");
        if (entity == null) throw new DaoException("Ошибка: order=null");

        long id = insertReturningId(c, SQL_INSERT_ORDER, ps -> {
            long customerId = entity.getCustomerId();
            if (customerId > 0) ps.setLong(1, customerId);
            else ps.setNull(1, Types.BIGINT);

            ps.setString(2, entity.getStatus() == null ? null : entity.getStatus().toDb());

            if (entity.getCreationDate() != null) ps.setObject(3, entity.getCreationDate());
            else ps.setNull(3, Types.DATE);

            if (entity.getCompletionDate() != null) ps.setObject(4, entity.getCompletionDate());
            else ps.setNull(4, Types.DATE);

            ps.setBigDecimal(5, java.math.BigDecimal.valueOf(entity.getTotalPrice()));
        }, "Ошибка создания заказа: " + entity);

        entity.setId(id);
        insertOrderBooks(c, entity);
        return entity;
    }

    @Override
    public Optional<Order> findById(Long id) {
        if (id == null) throw new DaoException("Ошибка: id=null");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return findById(c, id);
        } catch (SQLException e) {
            throw new DaoException("Ошибка получения соединения при чтении заказа по id=" + id, e);
        }
    }

    public Optional<Order> findById(Connection c, Long id) {
        if (c == null) throw new DaoException("Ошибка: connection=null");
        if (id == null) throw new DaoException("Ошибка: id=null");

        Optional<Order> opt = queryOne(
                c,
                SQL_FIND_ORDER_BY_ID,
                ps -> ps.setLong(1, id),
                orderMapper,
                "Ошибка чтения заказа по id=" + id
        );

        if (opt.isEmpty()) return Optional.empty();

        Order o = opt.get();
        o.setOrderedBooks(loadBooksForOrder(c, o.getId()));
        return Optional.of(o);
    }

    @Override
    public List<Order> findAll() {
        try (Connection c = DataSource.getInstance().getConnection()) {
            return findAll(c);
        } catch (SQLException e) {
            throw new DaoException("Ошибка получения соединения при чтении списка заказов", e);
        }
    }

    public List<Order> findAll(Connection c) {
        if (c == null) throw new DaoException("Ошибка: connection=null");

        List<Order> res = queryList(
                c,
                SQL_FIND_ALL_ORDERS,
                null,
                orderMapper,
                "Ошибка чтения списка заказов"
        );

        for (Order o : res) {
            o.setOrderedBooks(loadBooksForOrder(c, o.getId()));
        }
        return res;
    }

    @Override
    public Order update(Order entity) {
        if (entity == null) throw new DaoException("Ошибка: order=null");
        if (entity.isNew()) throw new DaoException("Ошибка: нельзя обновить заказ без id");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return update(c, entity);
        } catch (SQLException e) {
            throw new DaoException("Ошибка получения соединения при обновлении заказа id=" + entity.getId(), e);
        }
    }

    public Order update(Connection c, Order entity) {
        if (c == null) throw new DaoException("Ошибка: connection=null");
        if (entity == null) throw new DaoException("Ошибка: order=null");
        if (entity.isNew()) throw new DaoException("Ошибка: нельзя обновить заказ без id");

        int updated = execUpdate(c, SQL_UPDATE_ORDER, ps -> {
            long customerId = entity.getCustomerId();
            if (customerId > 0) ps.setLong(1, customerId);
            else ps.setNull(1, Types.BIGINT);

            ps.setString(2, entity.getStatus() == null ? null : entity.getStatus().toDb());

            if (entity.getCreationDate() != null) ps.setObject(3, entity.getCreationDate());
            else ps.setNull(3, Types.DATE);

            if (entity.getCompletionDate() != null) ps.setObject(4, entity.getCompletionDate());
            else ps.setNull(4, Types.DATE);

            ps.setBigDecimal(5, java.math.BigDecimal.valueOf(entity.getTotalPrice()));
            ps.setLong(6, entity.getId());
        }, "Ошибка обновления заказа id=" + entity.getId());

        if (updated == 0) throw new DaoException("Заказ не найден для обновления, id=" + entity.getId());

        execUpdate(
                c,
                SQL_DELETE_ORDER_BOOKS_BY_ORDER,
                ps -> ps.setLong(1, entity.getId()),
                "Ошибка очистки связок order_books для заказа id=" + entity.getId()
        );

        insertOrderBooks(c, entity);
        return entity;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) throw new DaoException("Ошибка: id=null");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return deleteById(c, id);
        } catch (SQLException e) {
            throw new DaoException("Ошибка получения соединения при удалении заказа id=" + id, e);
        }
    }

    public boolean deleteById(Connection c, Long id) {
        if (c == null) throw new DaoException("Ошибка: connection=null");
        if (id == null) throw new DaoException("Ошибка: id=null");

        execUpdate(
                c,
                SQL_DELETE_ORDER_BOOKS_BY_ORDER,
                ps -> ps.setLong(1, id),
                "Ошибка удаления связок order_books для заказа id=" + id
        );

        int deleted = execUpdate(
                c,
                SQL_DELETE_ORDER_BY_ID,
                ps -> ps.setLong(1, id),
                "Ошибка удаления заказа id=" + id
        );

        return deleted > 0;
    }

    private void insertOrderBooks(Connection c, Order entity) {
        List<Book> books = entity.getOrderedBooks();
        if (books == null) books = Collections.emptyList();
        if (books.isEmpty()) return;

        try (var ps = c.prepareStatement(SQL_INSERT_ORDER_BOOK)) {
            for (Book b : books) {
                if (b == null) continue;
                if (b.getId() == 0) {
                    throw new DaoException("Нельзя привязать к заказу книгу без id (в БД её нет): " + b);
                }
                ps.setLong(1, entity.getId());
                ps.setLong(2, b.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new DaoException("Ошибка привязки книг к заказу id=" + entity.getId(), e);
        }
    }

    private List<Book> loadBooksForOrder(Connection c, long orderId) {
        return queryList(
                c,
                SQL_FIND_BOOKS_BY_ORDER_ID,
                ps -> ps.setLong(1, orderId),
                bookMapper,
                "Ошибка загрузки книг для заказа id=" + orderId
        );
    }
}


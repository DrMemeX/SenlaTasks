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

import java.sql.*;
import java.util.*;

@Component
@Singleton
public class OrderJdbcDao implements GenericDao<Order, Long> {

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
            boolean oldAutoCommit = c.getAutoCommit();
            c.setAutoCommit(false);

            try {
                long newId = insertOrderRow(c, entity);
                entity.setId(newId);

                insertOrderBooks(c, entity);

                c.commit();
                return entity;
            } catch (Exception e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(oldAutoCommit);
            }

        } catch (Exception e) {
            throw new DaoException("Ошибка создания заказа: " + entity, e);
        }
    }

    @Override
    public Optional<Order> findById(Long id) {
        if (id == null) throw new DaoException("Ошибка: id=null");

        try (Connection c = DataSource.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_FIND_ORDER_BY_ID)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                Order o = orderMapper.map(rs);
                o.setOrderedBooks(loadBooksForOrder(c, o.getId()));

                return Optional.of(o);
            }

        } catch (Exception e) {
            throw new DaoException("Ошибка чтения заказа по id=" + id, e);
        }
    }

    @Override
    public List<Order> findAll() {
        try (Connection c = DataSource.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_FIND_ALL_ORDERS);
             ResultSet rs = ps.executeQuery()) {

            List<Order> res = new ArrayList<>();
            while (rs.next()) {
                Order o = orderMapper.map(rs);
                o.setOrderedBooks(loadBooksForOrder(c, o.getId()));
                res.add(o);
            }
            return res;

        } catch (Exception e) {
            throw new DaoException("Ошибка чтения списка заказов", e);
        }
    }

    @Override
    public Order update(Order entity) {
        if (entity == null) throw new DaoException("Ошибка: order=null");
        if (entity.isNew()) throw new DaoException("Ошибка: нельзя обновить заказ без id");

        try (Connection c = DataSource.getInstance().getConnection()) {
            boolean oldAutoCommit = c.getAutoCommit();
            c.setAutoCommit(false);

            try {
                updateOrderRow(c, entity);

                try (PreparedStatement del = c.prepareStatement(SQL_DELETE_ORDER_BOOKS_BY_ORDER)) {
                    del.setLong(1, entity.getId());
                    del.executeUpdate();
                }

                insertOrderBooks(c, entity);

                c.commit();
                return entity;
            } catch (Exception e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(oldAutoCommit);
            }

        } catch (Exception e) {
            throw new DaoException("Ошибка обновления заказа id=" + entity.getId(), e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) throw new DaoException("Ошибка: id=null");

        try (Connection c = DataSource.getInstance().getConnection()) {
            boolean oldAutoCommit = c.getAutoCommit();
            c.setAutoCommit(false);

            try {
                try (PreparedStatement ps = c.prepareStatement(SQL_DELETE_ORDER_BOOKS_BY_ORDER)) {
                    ps.setLong(1, id);
                    ps.executeUpdate();
                }

                int deleted;
                try (PreparedStatement ps = c.prepareStatement(SQL_DELETE_ORDER_BY_ID)) {
                    ps.setLong(1, id);
                    deleted = ps.executeUpdate();
                }

                c.commit();
                return deleted > 0;
            } catch (Exception e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(oldAutoCommit);
            }

        } catch (Exception e) {
            throw new DaoException("Ошибка удаления заказа id=" + id, e);
        }
    }

    private long insertOrderRow(Connection c, Order entity) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(SQL_INSERT_ORDER)) {

            long customerId = entity.getCustomerId();
            if (customerId > 0) ps.setLong(1, customerId);
            else ps.setNull(1, Types.BIGINT);

            ps.setString(2, entity.getStatus() == null ? null : entity.getStatus().toDb());

            if (entity.getCreationDate() != null) ps.setObject(3, entity.getCreationDate());
            else ps.setNull(3, Types.DATE);

            if (entity.getCompletionDate() != null) ps.setObject(4, entity.getCompletionDate());
            else ps.setNull(4, Types.DATE);

            ps.setBigDecimal(5, java.math.BigDecimal.valueOf(entity.getTotalPrice()));

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new DaoException("INSERT orders не вернул id");
                return rs.getLong(1);
            }
        }
    }

    private void updateOrderRow(Connection c, Order entity) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(SQL_UPDATE_ORDER)) {

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

            int updated = ps.executeUpdate();
            if (updated == 0) throw new DaoException("Заказ не найден для обновления, id=" + entity.getId());
        }
    }

    private void insertOrderBooks(Connection c, Order entity) throws SQLException {
        List<Book> books = entity.getOrderedBooks();
        if (books == null) books = Collections.emptyList();

        try (PreparedStatement ps = c.prepareStatement(SQL_INSERT_ORDER_BOOK)) {
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
        }
    }

    private List<Book> loadBooksForOrder(Connection c, long orderId) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(SQL_FIND_BOOKS_BY_ORDER_ID)) {
            ps.setLong(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                List<Book> res = new ArrayList<>();
                while (rs.next()) res.add(bookMapper.map(rs));
                return res;
            }
        }
    }
}


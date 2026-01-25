package dao.jdbc;

import di.annotation.Component;
import di.annotation.Singleton;
import dao.db.DataSource;
import dao.generic.GenericDao;
import dao.mapper.CustomerRowMapper;
import exceptions.dao.DaoException;
import features.customers.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@Singleton
public class CustomerJdbcDao extends AbstractJdbcDao implements GenericDao<Customer, Long> {

    private static final String SQL_FIND_BY_ID =
            "SELECT id, name, phone, email, address FROM customers WHERE id = ?";

    private static final String SQL_FIND_ALL =
            "SELECT id, name, phone, email, address FROM customers ORDER BY id";

    private static final String SQL_FIND_BY_EMAIL =
            "SELECT id, name, phone, email, address FROM customers WHERE LOWER(email) = LOWER(?) " +
                    "ORDER BY id LIMIT 1";

    private static final String SQL_INSERT =
            "INSERT INTO customers (name, phone, email, address) VALUES (?, ?, ?, ?) RETURNING id";

    private static final String SQL_UPDATE =
            "UPDATE customers SET name=?, phone=?, email=?, address=? WHERE id=?";

    private static final String SQL_DELETE_BY_ID =
            "DELETE FROM customers WHERE id=?";

    private final CustomerRowMapper mapper = new CustomerRowMapper();

    @Override
    public Customer create(Customer entity) {
        if (entity == null) throw new DaoException("Ошибка: customer=null");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return create(c, entity);
        } catch (SQLException e) {
            throw new DaoException("Ошибка получения соединения при создании клиента", e);
        }
    }

    public Customer create(Connection c, Customer entity) {
        if (c == null) throw new DaoException("Ошибка: connection=null");
        if (entity == null) throw new DaoException("Ошибка: customer=null");

        long id = insertReturningId(
                c,
                SQL_INSERT,
                ps -> {
                    ps.setString(1, entity.getName());
                    ps.setString(2, entity.getPhone());
                    ps.setString(3, entity.getEmail());
                    ps.setString(4, entity.getAddress());
                },
                "Ошибка создания клиента: " + entity
        );

        entity.setId(id);
        return entity;
    }

    @Override
    public Optional<Customer> findById(Long id) {
        if (id == null) throw new DaoException("Ошибка: id=null");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return findById(c, id);
        } catch (SQLException e) {
            throw new DaoException("Ошибка получения соединения при чтении клиента id=" + id, e);
        }
    }

    public Optional<Customer> findById(Connection c, Long id) {
        if (c == null) throw new DaoException("Ошибка: connection=null");
        if (id == null) throw new DaoException("Ошибка: id=null");

        return queryOne(
                c,
                SQL_FIND_BY_ID,
                ps -> ps.setLong(1, id),
                mapper,
                "Ошибка чтения клиента по id=" + id
        );
    }

    @Override
    public List<Customer> findAll() {
        try (Connection c = DataSource.getInstance().getConnection()) {
            return findAll(c);
        } catch (SQLException e) {
            throw new DaoException("Ошибка получения соединения при чтении списка клиентов", e);
        }
    }

    public List<Customer> findAll(Connection c) {
        if (c == null) throw new DaoException("Ошибка: connection=null");

        return queryList(
                c,
                SQL_FIND_ALL,
                null,
                mapper,
                "Ошибка чтения списка клиентов"
        );
    }

    public Optional<Customer> findByEmail(String email) {
        if (email == null) throw new DaoException("Ошибка: email=null");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return findByEmail(c, email);
        } catch (SQLException e) {
            throw new DaoException("Ошибка получения соединения при чтении клиента по email=" + email, e);
        }
    }

    public Optional<Customer> findByEmail(Connection c, String email) {
        if (c == null) throw new DaoException("Ошибка: connection=null");
        if (email == null) throw new DaoException("Ошибка: email=null");

        return queryOne(
                c,
                SQL_FIND_BY_EMAIL,
                ps -> ps.setString(1, email),
                mapper,
                "Ошибка чтения клиента по email=" + email
        );
    }

    @Override
    public Customer update(Customer entity) {
        if (entity == null) throw new DaoException("Ошибка: customer=null");
        if (entity.isNew()) throw new DaoException("Ошибка: нельзя обновить клиента без id");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return update(c, entity);
        } catch (SQLException e) {
            throw new DaoException("Ошибка получения соединения при обновлении клиента id=" + entity.getId(), e);
        }
    }

    public Customer update(Connection c, Customer entity) {
        if (c == null) throw new DaoException("Ошибка: connection=null");
        if (entity == null) throw new DaoException("Ошибка: customer=null");
        if (entity.isNew()) throw new DaoException("Ошибка: нельзя обновить клиента без id");

        int updated = execUpdate(
                c,
                SQL_UPDATE,
                ps -> {
                    ps.setString(1, entity.getName());
                    ps.setString(2, entity.getPhone());
                    ps.setString(3, entity.getEmail());
                    ps.setString(4, entity.getAddress());
                    ps.setLong(5, entity.getId());
                },
                "Ошибка обновления клиента id=" + entity.getId()
        );

        if (updated == 0) throw new DaoException("Клиент не найден для обновления, id=" + entity.getId());
        return entity;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) throw new DaoException("Ошибка: id=null");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return deleteById(c, id);
        } catch (SQLException e) {
            throw new DaoException("Ошибка получения соединения при удалении клиента id=" + id, e);
        }
    }

    public boolean deleteById(Connection c, Long id) {
        if (c == null) throw new DaoException("Ошибка: connection=null");
        if (id == null) throw new DaoException("Ошибка: id=null");

        int deleted = execUpdate(
                c,
                SQL_DELETE_BY_ID,
                ps -> ps.setLong(1, id),
                "Ошибка удаления клиента id=" + id
        );
        return deleted > 0;
    }
}

package task3_4.dao.jdbc;

import di_module.di_annotation.Component;
import di_module.di_annotation.Singleton;
import task3_4.dao.db.DataSource;
import task3_4.dao.generic.GenericDao;
import task3_4.dao.mapper.CustomerRowMapper;
import task3_4.exceptions.dao.DaoException;
import task3_4.features.customers.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Singleton
public class CustomerJdbcDao implements GenericDao<Customer, Long> {

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

        try (Connection c = DataSource.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_INSERT)) {

            ps.setString(1, entity.getName());
            ps.setString(2, entity.getPhone());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getAddress());

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new DaoException("INSERT не вернул id: " + entity);
                entity.setId(rs.getLong(1));
                return entity;
            }

        } catch (SQLException e) {
            throw new DaoException("Ошибка создания клиента: " + entity, e);
        }
    }

    @Override
    public Optional<Customer> findById(Long id) {
        if (id == null) throw new DaoException("Ошибка: id=null");

        try (Connection c = DataSource.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapper.map(rs));
            }

        } catch (SQLException e) {
            throw new DaoException("Ошибка чтения клиента по id=" + id, e);
        }
    }

    @Override
    public List<Customer> findAll() {
        try (Connection c = DataSource.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            List<Customer> res = new ArrayList<>();
            while (rs.next()) res.add(mapper.map(rs));
            return res;

        } catch (SQLException e) {
            throw new DaoException("Ошибка чтения списка клиентов", e);
        }
    }

    public Optional<Customer> findByEmail(String email) {
        if (email == null) throw new DaoException("Ошибка: email=null");

        try (Connection c = DataSource.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_FIND_BY_EMAIL)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapper.map(rs));
            }

        } catch (SQLException e) {
            throw new DaoException("Ошибка чтения клиента по email=" + email, e);
        }
    }

    @Override
    public Customer update(Customer entity) {
        if (entity == null) throw new DaoException("Ошибка: customer=null");
        if (entity.isNew()) throw new DaoException("Ошибка: нельзя обновить клиента без id");

        try (Connection c = DataSource.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, entity.getName());
            ps.setString(2, entity.getPhone());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getAddress());
            ps.setLong(5, entity.getId());

            int updated = ps.executeUpdate();
            if (updated == 0) throw new DaoException("Клиент не найден для обновления, id=" + entity.getId());
            return entity;

        } catch (SQLException e) {
            throw new DaoException("Ошибка обновления клиента id=" + entity.getId(), e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) throw new DaoException("Ошибка: id=null");

        try (Connection c = DataSource.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_DELETE_BY_ID)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DaoException("Ошибка удаления клиента id=" + id, e);
        }
    }
}


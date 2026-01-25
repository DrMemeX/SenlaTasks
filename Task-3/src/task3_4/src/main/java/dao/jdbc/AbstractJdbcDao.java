package dao.jdbc;

import dao.mapper.RowMapper;
import exceptions.dao.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractJdbcDao {
    protected <T> Optional<T> queryOne(Connection c, String sql,
                                       SqlBinder binder,
                                       RowMapper<T> mapper,
                                       String err) {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            if (binder != null) binder.bind(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapper.map(rs));
            }
        } catch (SQLException e) {
            throw new DaoException(err, e);
        }
    }

    protected <T> List<T> queryList(Connection c, String sql,
                                    SqlBinder binder,
                                    RowMapper<T> mapper,
                                    String err) {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            if (binder != null) binder.bind(ps);
            try (ResultSet rs = ps.executeQuery()) {
                List<T> res = new ArrayList<>();
                while (rs.next()) res.add(mapper.map(rs));
                return res;
            }
        } catch (SQLException e) {
            throw new DaoException(err, e);
        }
    }

    protected int execUpdate(Connection c, String sql, SqlBinder binder, String err) {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            if (binder != null) binder.bind(ps);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(err, e);
        }
    }

    protected long insertReturningId(Connection c, String sql, SqlBinder binder, String err) {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            if (binder != null) binder.bind(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new DaoException("INSERT не вернул id");
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new DaoException(err, e);
        }
    }

    @FunctionalInterface
    protected interface SqlBinder {
        void bind(PreparedStatement ps) throws SQLException;
    }
}

package dao.db;

import di.annotation.Component;
import di.annotation.Singleton;
import exceptions.dao.DaoException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
@Singleton
public class TransactionManager {

    public <T> T inTransaction(Function<Connection, T> action) {
        if (action == null) throw new DaoException("Ошибка: action=null");

        try (Connection c = DataSource.getInstance().getConnection()) {
            boolean old = c.getAutoCommit();
            c.setAutoCommit(false);

            try {
                T res = action.apply(c);
                c.commit();
                return res;
            } catch (Exception e) {
                rollbackQuietly(c);
                throw e;
            } finally {
                setAutoCommitQuietly(c, old);
            }
        } catch (Exception e) {
            throw (e instanceof DaoException) ? (DaoException) e : new DaoException("Ошибка транзакции", e);
        }
    }

    public void inTransactionVoid(Consumer<Connection> action) {
        if (action == null) throw new DaoException("Ошибка: action=null");
        inTransaction(c -> {
            action.accept(c);
            return null;
        });
    }

    private void rollbackQuietly(Connection c) {
        try {
            c.rollback();
        } catch (SQLException ignored) {
        }
    }

    private void setAutoCommitQuietly(Connection c, boolean value) {
        try {
            c.setAutoCommit(value);
        } catch (SQLException ignored) {
        }
    }
}


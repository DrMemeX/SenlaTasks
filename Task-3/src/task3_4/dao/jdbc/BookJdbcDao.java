package task3_4.dao.jdbc;

import di_module.di_annotation.Component;
import di_module.di_annotation.Singleton;
import task3_4.dao.db.DataSource;
import task3_4.dao.generic.GenericDao;
import task3_4.dao.mapper.BookRowMapper;
import task3_4.exceptions.dao.DaoException;
import task3_4.features.books.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Singleton
public class BookJdbcDao implements GenericDao<Book, Long> {

    private static final String SQL_FIND_BY_ID =
            "SELECT id, title, author, price, status, release_date, added_date, description " +
                    "FROM books WHERE id = ?";

    private static final String SQL_FIND_ALL =
            "SELECT id, title, author, price, status, release_date, added_date, description " +
                    "FROM books ORDER BY id";

    private static final String SQL_FIND_BY_TITLE =
            "SELECT id, title, author, price, status, release_date, added_date, description " +
                    "FROM books WHERE LOWER(title) = LOWER(?) " +
                    "ORDER BY id LIMIT 1";

    private static final String SQL_FIND_BY_TITLE_AND_AUTHOR =
            "SELECT id, title, author, price, status, release_date, added_date, description " +
                    "FROM books WHERE LOWER(title) = LOWER(?) AND LOWER(author) = LOWER(?) " +
                    "ORDER BY id LIMIT 1";

    private static final String SQL_INSERT =
            "INSERT INTO books (title, author, price, status, release_date, added_date, description) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

    private static final String SQL_UPDATE =
            "UPDATE books SET title=?, author=?, price=?, status=?, release_date=?, added_date=?, description=? " +
                    "WHERE id=?";

    private static final String SQL_DELETE_BY_ID =
            "DELETE FROM books WHERE id=?";

    private final BookRowMapper mapper = new BookRowMapper();

    @Override
    public Book create(Book entity) {
        if (entity == null) throw new DaoException("Ошибка: book=null");
        try (Connection c = DataSource.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_INSERT)) {

            ps.setString(1, entity.getTitle());
            ps.setString(2, entity.getAuthor());
            ps.setBigDecimal(3, java.math.BigDecimal.valueOf(entity.getPrice()));
            ps.setString(4, entity.getStatus() == null ? null : entity.getStatus().toDb());

            if (entity.getReleaseDate() != null) ps.setObject(5, entity.getReleaseDate());
            else ps.setNull(5, Types.DATE);

            if (entity.getAddedDate() != null) ps.setObject(6, entity.getAddedDate());
            else ps.setNull(6, Types.DATE);

            ps.setString(7, entity.getDescription());

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new DaoException("INSERT не вернул id: " + entity);
                entity.setId(rs.getLong(1));
                return entity;
            }

        } catch (SQLException e) {
            throw new DaoException("Ошибка создания книги: " + entity, e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        if (id == null) throw new DaoException("Ошибка: id=null");
        try (Connection c = DataSource.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapper.map(rs));
            }

        } catch (SQLException e) {
            throw new DaoException("Ошибка чтения книги по id=" + id, e);
        }
    }

    @Override
    public List<Book> findAll() {
        try (Connection c = DataSource.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            List<Book> res = new ArrayList<>();
            while (rs.next()) res.add(mapper.map(rs));
            return res;

        } catch (SQLException e) {
            throw new DaoException("Ошибка чтения списка книг", e);
        }
    }

    public Optional<Book> findByTitle(String title) {
        if (title == null) throw new DaoException("Ошибка: title=null");
        try (Connection c = DataSource.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_FIND_BY_TITLE)) {

            ps.setString(1, title);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapper.map(rs));
            }

        } catch (SQLException e) {
            throw new DaoException("Ошибка чтения книги по title=" + title, e);
        }
    }

    public Optional<Book> findByTitleAndAuthor(String title, String author) {
        if (title == null) throw new DaoException("Ошибка: title=null");
        if (author == null) throw new DaoException("Ошибка: author=null");

        try (Connection c = DataSource.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_FIND_BY_TITLE_AND_AUTHOR)) {

            ps.setString(1, title);
            ps.setString(2, author);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(mapper.map(rs));
            }

        } catch (SQLException e) {
            throw new DaoException("Ошибка чтения книги по title+author=" + title + "/" + author, e);
        }
    }

    @Override
    public Book update(Book entity) {
        if (entity == null) throw new DaoException("Ошибка: book=null");
        if (entity.isNew()) throw new DaoException("Ошибка: нельзя обновить книгу без id");

        try (Connection c = DataSource.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, entity.getTitle());
            ps.setString(2, entity.getAuthor());
            ps.setBigDecimal(3, java.math.BigDecimal.valueOf(entity.getPrice()));
            ps.setString(4, entity.getStatus() == null ? null : entity.getStatus().toDb());

            if (entity.getReleaseDate() != null) ps.setObject(5, entity.getReleaseDate());
            else ps.setNull(5, Types.DATE);

            if (entity.getAddedDate() != null) ps.setObject(6, entity.getAddedDate());
            else ps.setNull(6, Types.DATE);

            ps.setString(7, entity.getDescription());
            ps.setLong(8, entity.getId());

            int updated = ps.executeUpdate();
            if (updated == 0) throw new DaoException("Книга не найдена для обновления, id=" + entity.getId());
            return entity;

        } catch (SQLException e) {
            throw new DaoException("Ошибка обновления книги id=" + entity.getId(), e);
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
            throw new DaoException("Ошибка удаления книги id=" + id, e);
        }
    }
}

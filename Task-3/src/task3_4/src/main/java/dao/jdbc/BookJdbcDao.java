package dao.jdbc;

import di.annotation.Component;
import di.annotation.Singleton;
import dao.db.DataSource;
import dao.generic.GenericDao;
import dao.mapper.BookRowMapper;
import exceptions.dao.DaoException;
import features.books.Book;

import java.sql.Connection;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Component
@Singleton
public class BookJdbcDao extends AbstractJdbcDao implements GenericDao<Book, Long> {

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

        try (Connection c = DataSource.getInstance().getConnection()) {
            return create(c, entity);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Ошибка создания книги: " + entity, e);
        }
    }

    public Book create(Connection c, Book entity) {
        if (c == null) throw new DaoException("Ошибка: connection=null");
        if (entity == null) throw new DaoException("Ошибка: book=null");

        long id = insertReturningId(c, SQL_INSERT, ps -> {
            ps.setString(1, entity.getTitle());
            ps.setString(2, entity.getAuthor());
            ps.setBigDecimal(3, java.math.BigDecimal.valueOf(entity.getPrice()));
            ps.setString(4, entity.getStatus() == null ? null : entity.getStatus().toDb());

            if (entity.getReleaseDate() != null) ps.setObject(5, entity.getReleaseDate());
            else ps.setNull(5, Types.DATE);

            if (entity.getAddedDate() != null) ps.setObject(6, entity.getAddedDate());
            else ps.setNull(6, Types.DATE);

            ps.setString(7, entity.getDescription());
        }, "Ошибка создания книги: " + entity);

        entity.setId(id);
        return entity;
    }


    @Override
    public Optional<Book> findById(Long id) {
        if (id == null) throw new DaoException("Ошибка: id=null");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return queryOne(c, SQL_FIND_BY_ID,
                    ps -> ps.setLong(1, id),
                    mapper,
                    "Ошибка чтения книги по id=" + id
            );
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Ошибка чтения книги по id=" + id, e);
        }
    }

    @Override
    public List<Book> findAll() {
        try (Connection c = DataSource.getInstance().getConnection()) {
            return queryList(c, SQL_FIND_ALL,
                    null,
                    mapper,
                    "Ошибка чтения списка книг"
            );
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Ошибка чтения списка книг", e);
        }
    }

    public Optional<Book> findByTitle(String title) {
        if (title == null) throw new DaoException("Ошибка: title=null");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return queryOne(c, SQL_FIND_BY_TITLE,
                    ps -> ps.setString(1, title),
                    mapper,
                    "Ошибка чтения книги по title=" + title
            );
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Ошибка чтения книги по title=" + title, e);
        }
    }

    public Optional<Book> findByTitleAndAuthor(String title, String author) {
        if (title == null) throw new DaoException("Ошибка: title=null");
        if (author == null) throw new DaoException("Ошибка: author=null");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return queryOne(c, SQL_FIND_BY_TITLE_AND_AUTHOR,
                    ps -> {
                        ps.setString(1, title);
                        ps.setString(2, author);
                    },
                    mapper,
                    "Ошибка чтения книги по title+author=" + title + "/" + author
            );
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Ошибка чтения книги по title+author=" + title + "/" + author, e);
        }
    }

    @Override
    public Book update(Book entity) {
        if (entity == null) throw new DaoException("Ошибка: book=null");
        if (entity.isNew()) throw new DaoException("Ошибка: нельзя обновить книгу без id");

        try (Connection c = DataSource.getInstance().getConnection()) {
            return update(c, entity);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Ошибка обновления книги id=" + entity.getId(), e);
        }
    }

    public Book update(Connection c, Book entity) {
        if (c == null) throw new DaoException("Ошибка: connection=null");
        if (entity == null) throw new DaoException("Ошибка: book=null");
        if (entity.isNew()) throw new DaoException("Ошибка: нельзя обновить книгу без id");

        int updated = execUpdate(c, SQL_UPDATE, ps -> {
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
        }, "Ошибка обновления книги id=" + entity.getId());

        if (updated == 0) throw new DaoException("Книга не найдена для обновления, id=" + entity.getId());
        return entity;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) throw new DaoException("Ошибка: id=null");

        try (Connection c = DataSource.getInstance().getConnection()) {
            int deleted = execUpdate(c, SQL_DELETE_BY_ID,
                    ps -> ps.setLong(1, id),
                    "Ошибка удаления книги id=" + id
            );
            return deleted > 0;
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Ошибка удаления книги id=" + id, e);
        }
    }
}

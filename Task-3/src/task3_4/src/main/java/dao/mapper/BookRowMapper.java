package dao.mapper;

import common.status.BookStatus;
import features.books.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class BookRowMapper implements RowMapper<Book> {

    @Override
    public Book map(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String title = rs.getString("title");
        String author = rs.getString("author");
        double price = rs.getDouble("price");
        BookStatus status = BookStatus.fromDb(rs.getString("status"));

        LocalDate releaseDate = rs.getObject("release_date", LocalDate.class);
        LocalDate addedDate = rs.getObject("added_date", LocalDate.class);
        String description = rs.getString("description");

        return new Book(id, title, author, price, status, releaseDate, addedDate, description);
    }
}

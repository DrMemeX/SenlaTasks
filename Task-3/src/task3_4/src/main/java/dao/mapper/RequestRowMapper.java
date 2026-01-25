package dao.mapper;

import features.requests.Request;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestRowMapper implements RowMapper<Request> {

    @Override
    public Request map(ResultSet rs) throws SQLException {
        long id = rs.getLong("r_id");
        boolean resolved = rs.getBoolean("resolved");

        long bookId = rs.getLong("book_id");
        if (rs.wasNull()) bookId = 0;

        Request r = new Request();
        r.setId(id);
        r.setResolved(resolved);
        r.setBookId(bookId);
        return r;
    }
}

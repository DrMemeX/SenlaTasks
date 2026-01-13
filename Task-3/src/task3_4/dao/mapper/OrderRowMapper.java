package task3_4.dao.mapper;

import task3_4.common.status.OrderStatus;
import task3_4.features.orders.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class OrderRowMapper implements RowMapper<Order> {

    @Override
    public Order map(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");

        Order o = new Order(id);

        String rawStatus = rs.getString("status");
        OrderStatus status = rawStatus == null ? null : OrderStatus.fromDb(rawStatus);
        o.setStatus(status);

        LocalDate creationDate = rs.getObject("creation_date", LocalDate.class);
        LocalDate completionDate = rs.getObject("completion_date", LocalDate.class);

        o.setCreationDate(creationDate);
        o.setCompletionDate(completionDate);

        o.setTotalPrice(rs.getDouble("total_price"));

        long customerId = rs.getLong("customer_id");
        if (rs.wasNull()) customerId = 0;
        o.setCustomerId(customerId);

        return o;
    }
}


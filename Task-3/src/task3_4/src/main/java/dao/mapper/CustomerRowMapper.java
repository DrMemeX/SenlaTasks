package dao.mapper;

import features.customers.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRowMapper implements RowMapper<Customer> {

    @Override
    public Customer map(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String phone = rs.getString("phone");
        String email = rs.getString("email");
        String address = rs.getString("address");
        return new Customer(id, name, phone, email, address);
    }
}


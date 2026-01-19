package task3_4.features.orders;

import di_module.di_annotation.Component;
import di_module.di_annotation.Inject;
import di_module.di_annotation.Singleton;
import task3_4.dao.jdbc.OrderJdbcDao;

import java.sql.Connection;
import java.util.List;

@Component
@Singleton
public class OrderRepository {

    @Inject
    private OrderJdbcDao dao;

    public List<Order> findAllOrders() {
        return dao.findAll();
    }

    public Order findOrderById(long id) {
        return dao.findById(id).orElse(null);
    }

    public void addOrder(Order order) {
        dao.create(order);
    }
    public void addOrder(Connection c, Order order) { dao.create(c, order); }

    public void updateOrder(Order order) {dao.update(order);}
    public void updateOrder(Connection c, Order order) { dao.update(c, order); }

    public boolean deleteOrderById(long id) { return dao.deleteById(id); }
    public boolean deleteOrderById(Connection c, long id) { return dao.deleteById(c, id); }

    public void restoreState(List<Order> orderState) {
    }
}

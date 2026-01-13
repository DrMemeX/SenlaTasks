package task3_4.features.orders;

import di_module.di_annotation.Component;
import di_module.di_annotation.Inject;
import di_module.di_annotation.Singleton;
import task3_4.dao.jdbc.OrderJdbcDao;

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

    public boolean deleteOrderById(long id) {
        return dao.deleteById(id);
    }

    public void restoreState(List<Order> orderState) {
    }

    public void updateOrder(Order order) {
        dao.update(order);
    }
}

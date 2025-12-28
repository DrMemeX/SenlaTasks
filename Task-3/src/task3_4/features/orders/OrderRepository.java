package task3_4.features.orders;

import di_module.di_annotation.Component;
import di_module.di_annotation.Singleton;

import java.util.ArrayList;
import java.util.List;

@Component
@Singleton
public class OrderRepository {

    private List<Order> orderList = new ArrayList<>();

    public List<Order> findAllOrders() {
        return orderList;
    }

    public Order findOrderById(long id) {
        return orderList.stream()
                .filter(o -> o.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void addOrder(Order order) {
        orderList.add(order);
    }

    public boolean deleteOrderById(long id) {
        return orderList.removeIf(o -> o.getId() == id);
    }

    public void restoreState(List<Order> orderState) {
        if (orderState != null) {
            orderList.clear();
            orderList.addAll(orderState);
        }
    }

}

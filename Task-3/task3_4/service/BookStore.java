package task3_4.service;

import task3_4.catalog.*;
import task3_4.customer.Customer;
import task3_4.status.*;

import java.util.ArrayList;
import java.util.List;

public class BookStore {
    private final Inventory inventory;
    private final List<Order> orders;

    public BookStore() {
        this.inventory = new Inventory();
        this.orders = new ArrayList<>();
    }

    public Inventory getInventory() {return inventory;}
    public List<Order> getOrders() {return orders;}

    public void createOrder(List<Book> books) {
        Order order = new Order(orders.size() + 1);
        for (Book book : books) {
            order.addBook(book);
            if (book.getStatus() == BookStatus.MISSING) {
                inventory.markBookMissing(book);
                inventory.requestBook(book, order);
            }
        }
        orders.add(order);
        System.out.println("Создан заказ №" + order.getId() + " (статус: " + order.getStatus() + ")");

        if (order.hasMissingBooks()) {
            System.out.println("В заказе есть отсутствующая книга, оформлен запрос на её поставку.\n");
        }
    }

    public void createOrder(List<Book> books, Customer customer) {
        createOrder(books);
        orders.get(orders.size() - 1).setCustomer(customer);
    }

    public void attachCustomerToOrder(int orderId, Customer customer) {
        for (Order o : orders) {
            if (o.getId() == orderId) {
                o.setCustomer(customer);
                return;
            }
        }
    }

    public void cancelOrder(int id) {
        for (Order order : orders) {
            if (order.getId() == id) {
                order.changeStatus(OrderStatus.CANCELLED);
                System.out.println("Заказ №" + id + " отменён.");
                return;
            }
        }
        System.out.println("Заказ №" + id + " не найден.");
    }

    public void completeOrder(int id) {
        for (Order order : orders) {
            if (order.getId() == id) {
                if (order.hasMissingBooks()) {
                    System.out.println("Нельзя завершить заказ №" + id + ": в заказе есть отсутствующие книги.");
                    return;
                }
                order.changeStatus(OrderStatus.COMPLETED);
                System.out.println("Заказ №" + id + " завершён.");
                return;
            }
        }
        System.out.println("Заказ №" + id + " не найден.");
    }

    public Order findOrderById(int orderId) {
        for (Order o : orders) {
            if (o.getId() == orderId) return o;
        }
        return null;
    }

    public void addBookToStock(Book book) {
        inventory.addBook(book);
        inventory.fulfillRequest(book);
        System.out.println("Добавлена книга: " + book.getTitle() + " [статус: " + book.getStatus() + "]");
    }

    public void showAllOrders() {
        System.out.println("\n### Текущие заказы ###");
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    public void showActiveRequests() {
        List<BookRequest> requests = inventory.getRequests();
        if (!requests.isEmpty()) {
            System.out.println("\nАктивные запросы:");
            for (BookRequest req : requests) {
                System.out.println(" — " + req.getBook().getTitle() +
                        " — " + req.getBook().getAuthor() +
                        " (" + req.getBook().getPrice() + " руб.)");
            }
        } else {
            System.out.println("\nАктивных запросов нет.");
        }
    }

    public void showBooksInStock() {
        System.out.println("\nКниги на складе:");
        for (Book b : inventory.getBooks()) {
            System.out.println(" — " + b.getTitle() + " — " + b.getAuthor() +
                               " (" + b.getPrice() + " руб.)" + " [" + b.getStatus() + "]");
        }
    }

    public Book findBookByTitle(String title) {
        for (Book b : inventory.getBooks()) {
            if (b.getTitle() != null && b.getTitle().equalsIgnoreCase(title)) return b;
        }
        return null;
    }

    public Book findBookByTitleAndAuthor(String title, String author) {
        for (Book b : inventory.getBooks()) {
            if (b.getTitle() != null && b.getAuthor() != null
                    && b.getTitle().equalsIgnoreCase(title)
                    && b.getAuthor().equalsIgnoreCase(author)) {
                return b;
            }
        }
        return null;
    }
}

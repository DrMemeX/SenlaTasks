package task3_4;

import task3_4.catalog.*;
import task3_4.status.*;
import task3_4.service.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        BookStore store = new BookStore();

        Book b1 = new Book("Преступление и наказание", "Ф. Достоевский", 500, BookStatus.AVAILABLE);
        Book b2 = new Book("Мастер и Маргарита", "М. Булгаков", 600, BookStatus.MISSING);
        Book b3 = new Book("Отцы и дети", "И. Тургенев", 450, BookStatus.AVAILABLE);
        Book b4 = new Book("Идиот", "Ф. Достоевский", 480, BookStatus.AVAILABLE);
        Book b5 = new Book("Обломов", "И. Гончаров", 520, BookStatus.MISSING);

        store.addBookToStock(b1);
        store.addBookToStock(b2);
        store.addBookToStock(b3);
        store.addBookToStock(b4);
        store.addBookToStock(b5);

        System.out.println("\nОФОРМЛЕНИЕ ЗАКАЗА №1");
        store.createOrder(List.of(b1, b2, b3));

        System.out.println("\nОФОРМЛЕНИЕ ЗАКАЗА №2");
        store.createOrder(List.of(b4, b5));

        store.showAllOrders();

        System.out.println("\nПОПЫТКА ЗАВЕРШИТЬ ЗАКАЗ №1");
        store.completeOrder(1);

        store.addBookToStock(b2);

        System.out.println("\nПОВТОРНАЯ ПОПЫТКА ЗАВЕРШИТЬ ЗАКАЗ №1");
        store.completeOrder(1);

        System.out.println("\nОТМЕНА ЗАКАЗА №2");
        store.cancelOrder(2);

        System.out.println("\nИТОГОВОЕ СОСТОЯНИЕ");
        store.showAllOrders();
        store.showActiveRequests();
        store.showBooksInStock();
    }
}

package task3_4.catalog;

public class Book {
    private String title;
    private String author;
    private double price;
    private String status;

    public Book(String title, String author, double price, String status) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.status = status;
    }

    public String getTitle() {return title;}
    public String getAuthor() {return author;}
    public double getPrice() {return price;}
    public String getStatus() {return status;}

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Книга: «%s»\n автор: %s\n цена: %.2f руб.\n статус: %s\n",
                title, author, price, status);
    }
}

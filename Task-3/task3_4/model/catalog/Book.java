package task3_4.model.catalog;

import task3_4.model.status.BookStatus;

import java.time.LocalDate;

public class Book {
    private String title;
    private String author;
    private double price;
    private BookStatus status;
    private LocalDate releaseDate;
    private LocalDate addedDate;
    private String description;

    public Book(String title, String author, double price, BookStatus status,
                LocalDate releaseDate, LocalDate addedDate, String description) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.status = status;
        this.releaseDate = releaseDate;
        this.addedDate = addedDate;
        this.description = description;
    }

    public String getTitle() {return title;}

    public String getAuthor() {return author;}

    public double getPrice() {return price;}

    public BookStatus getStatus() {return status;}
    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public LocalDate getReleaseDate() {return releaseDate;}

    public LocalDate getAddedDate() {return addedDate;}

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return String.format("Книга: «%s»\nАвтор: %s\nЦена: %.2f руб.\nСтатус: %s\n",
                title, author, price, status);
    }
}

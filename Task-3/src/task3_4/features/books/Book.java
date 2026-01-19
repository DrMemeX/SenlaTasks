package task3_4.features.books;

import task3_4.common.status.BookStatus;

import java.io.Serializable;
import java.time.LocalDate;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String title;
    private String author;
    private double price;
    private BookStatus status;
    private LocalDate releaseDate;
    private LocalDate addedDate;
    private String description;

    public Book(String title,
                String author,
                double price,
                BookStatus status,
                LocalDate releaseDate,
                LocalDate addedDate,
                String description) {

        this.title = title;
        this.author = author;
        this.price = price;
        this.status = status;
        this.releaseDate = releaseDate;
        this.addedDate = addedDate;
        this.description = description;
    }

    public Book(long id,
                String title,
                String author,
                double price,
                BookStatus status,
                LocalDate releaseDate,
                LocalDate addedDate,
                String description) {
        this.id = id;

        this.title = title;
        this.author = author;
        this.price = price;
        this.status = status;
        this.releaseDate = releaseDate;
        this.addedDate = addedDate;
        this.description = description;

    }

    public long getId() {return id;}
    public void setId(long id) {
        this.id = id;
    }

    public boolean isNew() {
        return id == 0;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public double getPrice() { return price; }
    public BookStatus getStatus() { return status; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public LocalDate getAddedDate() { return addedDate; }
    public String getDescription() { return description; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setPrice(double price) { this.price = price; }
    public void setStatus(BookStatus status) { this.status = status; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    public void setAddedDate(LocalDate addedDate) { this.addedDate = addedDate; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return String.format(
                "Книга #%d: «%s»\nАвтор: %s\nЦена: %.2f руб.\nСтатус: %s\n",
                id, title, author, price, status
        );
    }
}

package jp.mynavi.azurejava.booklist.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.PartitionKey;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Document(collection = "Book")
public class Book {
    @Id
    private String id;
    private String title;
    @PartitionKey
    private String category;
    private String author;
    private String publisher;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate publicationDate;

    public Book() {
    }

    public Book(String id, String title, String category, String author, String publisher, LocalDate publicationDate) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.author = author;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publicationDate=" + publicationDate +
                '}';
    }
}

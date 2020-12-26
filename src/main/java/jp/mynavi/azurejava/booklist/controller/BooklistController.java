package jp.mynavi.azurejava.booklist.controller;

import com.azure.data.cosmos.PartitionKey;
import jp.mynavi.azurejava.booklist.dao.BookRepository;
import jp.mynavi.azurejava.booklist.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class BooklistController {

    private static final Logger logger = LoggerFactory.getLogger(BooklistController.class);

    @Autowired
    private BookRepository bookRepository;

    @RequestMapping("/api/hello")
    public String hello() {
        return "Hello Spring Boot!";
    }

    @RequestMapping("/api/home")
    public Map<String, Object> home() {
        final Map<String, Object> model = new HashMap<String, Object>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", "home");
        return model;
    }

    /**
     * リストの取得
     */
    @RequestMapping(value = "/api/booklist", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getBooklist() {
        try {
            logger.info("BOOKLIST - Return all books.");
            final Flux<Book> findedItems = bookRepository.findAll();
            logger.info("BOOKLIST - Find items: " + findedItems.collectList().block().toString());
            return new ResponseEntity<>(findedItems.collectList().block(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("BOOKLIST ERROR: ", e.fillInStackTrace());
            return new ResponseEntity<>("Nothing found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * IDでの検索
     */
    @GetMapping(value = "/api/booklist/search/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> searchById(@PathVariable("id") String id) {
        try {
            logger.info("SEARCH - Seach param: " + id);
            final Book findedItem = bookRepository.findById(id).block();
            logger.info("SEARCH - Find an item: " + findedItem.toString());
            return new ResponseEntity<Book>(findedItem, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("SEARCH ERROR: ", e.fillInStackTrace());
            return new ResponseEntity<String>(id + " not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * タイトルでの検索
     */
    @GetMapping(value = "/api/booklist/titlesearch/{title}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> searchByTitle(@PathVariable("title") String title) {
        try {
            logger.info("SEARCH - Seach param: " + title);
            final Flux<Book> findedItems = bookRepository.findByTitleContaining(title);
            logger.info("SEARCH - Find an item: " + findedItems.collectList().block().toString());
            return new ResponseEntity<>(findedItems.collectList().block(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("SEARCH ERROR: ", e.fillInStackTrace());
            return new ResponseEntity<String>(title + " not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 新規追加
     */
    @PostMapping(value = "/api/booklist/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewBook(@RequestBody Book book) {
        try {
            logger.info("ADD - Received an item: " + book.toString());
            book.setId(UUID.randomUUID().toString());
            final Book savedItem = bookRepository.save(book).block();
            logger.info("ADD - Saved an item: " + savedItem.toString());
            return new ResponseEntity<String>("Entity created", HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("ADD ERROR: ", e.fillInStackTrace());
            return new ResponseEntity<String>("Entity creation failed", HttpStatus.CONFLICT);
        }
    }

    /**
     * 更新
     */
    @PostMapping(value = "/api/booklist/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateBook(@RequestBody Book book) {
        try {
            logger.info("UPDATE - Received an item: " + book.toString());
            bookRepository.deleteById(book.getId());
            final Book savedItem = bookRepository.save(book).block();
            logger.info("UPDATE - Saved an item: " + savedItem.toString());
            return new ResponseEntity<String>("Entity updated", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("UPDATE ERROR: ", e.fillInStackTrace());
            return new ResponseEntity<String>("Entity updating failed", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 削除
     */
    @GetMapping(value = "/api/booklist/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") String id) {
        try {
            logger.info("DELETE - param: " + id);
            final Book findedItem = bookRepository.findById(id).block();
            logger.info("DELETE - Find an item: " + findedItem.toString());
            bookRepository.deleteById(id, new PartitionKey(findedItem.getCategory())).block();
            logger.info("DELETE - Deleted an item: " + findedItem.toString());
            return new ResponseEntity<String>("Entity deleted", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("DELETE ERROR: ", e.fillInStackTrace());
            return new ResponseEntity<String>("Entity deletion failed", HttpStatus.NOT_FOUND);
        }
    }
}

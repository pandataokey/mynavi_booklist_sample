package jp.mynavi.azurejava.booklist.dao;

import com.microsoft.azure.spring.data.cosmosdb.repository.ReactiveCosmosRepository;
import jp.mynavi.azurejava.booklist.model.Book;
import reactor.core.publisher.Flux;

public interface BookRepository extends ReactiveCosmosRepository<Book, String> {
    Flux<Book> findByTitleContaining(String title);
}

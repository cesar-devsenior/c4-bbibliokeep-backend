package com.devsenior.cdiaz.bibliokeep.repository;

import com.devsenior.cdiaz.bibliokeep.model.entity.Book;
import com.devsenior.cdiaz.bibliokeep.model.entity.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByOwnerId(UUID ownerId);

    Optional<Book> findByOwnerIdAndIsbn(UUID ownerId, String isbn);

    List<Book> findByOwnerIdAndStatus(UUID ownerId, BookStatus status);
}

package com.devsenior.cdiaz.bibliokeep.service.impl;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.devsenior.cdiaz.bibliokeep.client.GoogleBooksClient;
import com.devsenior.cdiaz.bibliokeep.exception.BadRequestException;
import com.devsenior.cdiaz.bibliokeep.exception.ResourceNotFoundException;
import com.devsenior.cdiaz.bibliokeep.mapper.BookMapper;
import com.devsenior.cdiaz.bibliokeep.model.dto.book.BookRequest;
import com.devsenior.cdiaz.bibliokeep.model.dto.book.BookResponse;
import com.devsenior.cdiaz.bibliokeep.model.entity.BookStatus;
import com.devsenior.cdiaz.bibliokeep.repository.BookRepository;
import com.devsenior.cdiaz.bibliokeep.repository.UserRepository;
import com.devsenior.cdiaz.bibliokeep.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private static final Duration CACHE_TTL = Duration.ofHours(24);

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;
    private final GoogleBooksClient googleBooksClient;
    // private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public BookResponse createBook(BookRequest request, UUID ownerId) {
        var owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (bookRepository.findByOwnerIdAndIsbn(ownerId, request.isbn()).isPresent()) {
            throw new BadRequestException("El libro ya existe en la colección");
        }

        var book = bookMapper.toEntity(request);
        book.setOwner(owner);
        book.setIsLent(false);

        var saved = bookRepository.save(book);
        return bookMapper.toResponse(saved);
    }

    @Override
    public BookResponse updateBookStatus(Long id, BookStatus status, UUID ownerId) {
        var book = bookRepository.findById(id)
                .filter(b -> b.getOwner().getId().equals(ownerId))
                .orElseThrow(() -> new ResourceNotFoundException("Book not found or not owned by user"));

        book.setStatus(status);
        var saved = bookRepository.save(book);
        return bookMapper.toResponse(saved);
    }

    @Override
    public List<BookResponse> searchBooks(String query, UUID ownerId) {
        log.info("Query: {}; OwnerId: {}", query, ownerId);
        var normalizedQ = query == null ? "" : query.strip();

        if (normalizedQ.isEmpty()) {
            return getLocalBooks(ownerId);
        }

        if (isIsbn(normalizedQ)) {
            var localBook = bookRepository.findByOwnerIdAndIsbn(ownerId, normalizedQ);
            if (localBook.isPresent()) {
                return List.of(bookMapper.toResponse(localBook.get()));
            }

            // var cached = getCachedBookResponses("isbn:" + normalizedQ);
            // if (!cached.isEmpty()) {
            //     return cached;
            // }

            // var googleBooks = googleBooksClient.searchByIsbn(normalizedQ);
            // cacheBookResponses("isbn:" + normalizedQ, googleBooks);
            // return googleBooks;
        }

        var searchResult = getLocalBooks(ownerId, normalizedQ);
        if (!searchResult.isEmpty()) {
            return searchResult;
        }

        // var cacheKey = "search:" + normalizedQ.toLowerCase();
        // var cached = getCachedBookResponses(cacheKey);
        // if (!cached.isEmpty()) {
        //     return cached;
        // }

        // var googleBooks = googleBooksClient.searchByQuery(normalizedQ);
        // cacheBookResponses(cacheKey, googleBooks);
        // return googleBooks;

        return List.of();
    }

    private List<BookResponse> getLocalBooks(UUID ownerId) {
        return bookRepository.findByOwnerId(ownerId).stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    private List<BookResponse> getLocalBooks(UUID ownerId, String query) {
        var queryLower = query.toLowerCase();
        return bookRepository.findByOwnerId(ownerId).stream()
                .filter(book ->
                        book.getIsbn().toLowerCase().contains(queryLower)
                                || book.getTitle().toLowerCase().contains(queryLower)
                                || book.getAuthors().stream().anyMatch(a -> a.toLowerCase().contains(queryLower)))
                .map(bookMapper::toResponse)
                .toList();
    }

    private boolean isIsbn(String term) {
        return term.matches("^(?:\\d{10}|\\d{13})$");
    }

    // private List<BookResponse> getCachedBookResponses(String key) {
    //     var cachedJson = redisTemplate.opsForValue().get(key);
    //     if (cachedJson == null) {
    //         return Collections.emptyList();
    //     }
    //     try {
    //         return Arrays.asList(objectMapper.readValue(cachedJson, BookResponse[].class));
    //     } catch (JsonProcessingException e) {
    //         return Collections.emptyList();
    //     }
    // }

    // private void cacheBookResponses(String key, List<BookResponse> responses) {
    //     if (responses == null || responses.isEmpty()) {
    //         return;
    //     }

    //     try {
    //         var json = objectMapper.writeValueAsString(responses);
    //         redisTemplate.opsForValue().set(key, json, CACHE_TTL);
    //     } catch (JsonProcessingException e) {
    //         // swallow cache serialization errors intentionally
    //     }
    // }
}

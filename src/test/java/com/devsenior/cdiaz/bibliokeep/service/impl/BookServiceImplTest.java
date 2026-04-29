package com.devsenior.cdiaz.bibliokeep.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devsenior.cdiaz.bibliokeep.exception.BadRequestException;
import com.devsenior.cdiaz.bibliokeep.exception.ResourceNotFoundException;
import com.devsenior.cdiaz.bibliokeep.mapper.BookMapper;
import com.devsenior.cdiaz.bibliokeep.model.dto.book.BookRequest;
import com.devsenior.cdiaz.bibliokeep.model.dto.book.BookResponse;
import com.devsenior.cdiaz.bibliokeep.model.entity.Book;
import com.devsenior.cdiaz.bibliokeep.model.entity.BookStatus;
import com.devsenior.cdiaz.bibliokeep.model.entity.User;
import com.devsenior.cdiaz.bibliokeep.repository.BookRepository;
import com.devsenior.cdiaz.bibliokeep.repository.UserRepository;

import lombok.var;

class BookServiceImplTest {

    private BookRepository bookRepositoryMock;
    private UserRepository userRepositoryMock;
    private BookMapper bookMapperMock;
    private BookServiceImpl bookService;

    @BeforeEach
    void init() {
        bookRepositoryMock = mock(BookRepository.class);
        userRepositoryMock = mock(UserRepository.class);
        bookMapperMock = mock(BookMapper.class);
        bookService = new BookServiceImpl(bookRepositoryMock, userRepositoryMock, bookMapperMock);
    }

    // DBB Style: should<R>_When<C>()
    @Test
    void shouldPersistBook_WhenCorrectData() {
        // Given
        var mockUser = new User();
        when(userRepositoryMock.findById(any(UUID.class)))
                .thenReturn(Optional.of(mockUser));

        var mockBook = new Book();
        when(bookMapperMock.toEntity(any(BookRequest.class)))
                .thenReturn(mockBook);

        when(bookRepositoryMock.save(any(Book.class)))
                .thenReturn(mockBook);

        var mockBookResponse = mockedBookResponse();
        when(bookMapperMock.toResponse(any(Book.class)))
                .thenReturn(mockBookResponse);

        var request = mockedBookRequest();
        var ownerId = mockedOwnerId();

        // When
        var result = bookService.createBook(request, ownerId);

        // Then
        assertNotNull(result);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenOwnerIdNotExist() {
        // Arrange
        when(userRepositoryMock.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        var request = mockedBookRequest();
        var ownerId = mockedOwnerId();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> bookService.createBook(request, ownerId));
    }

    @Test
    void shouldThrowBadRequestException_WhenOwnerAlreadyHasTheBook() {
        // Arrange
        var mockedUser = new User();
        when(userRepositoryMock.findById(any(UUID.class)))
                .thenReturn(Optional.of(mockedUser));

        var mockedBook = new Book();
        when(bookRepositoryMock.findByOwnerIdAndIsbn(any(UUID.class), anyString()))
                .thenReturn(Optional.of(mockedBook));

        var request = mockedBookRequest();
        var ownerId = mockedOwnerId();

        // Act & Assert
        assertThrows(BadRequestException.class,
                () -> bookService.createBook(request, ownerId));
    }

    @Test
    void shouldUpdateBookStatus_WhenCorrectData() {
        // Arrage
        var mockedId = 1L;
        var ownerId = mockedOwnerId();

        var mockedOwner = new User();
        mockedOwner.setId(ownerId);

        var mockedBook = new Book();
        mockedBook.setOwner(mockedOwner);
        when(bookRepositoryMock.findById(mockedId))
                .thenReturn(Optional.of(mockedBook));

        when(bookRepositoryMock.save(any(Book.class)))
                .thenReturn(mockedBook);

        var response = new BookResponse(1L, "123456789", "Test Book",
                List.of("Test Author"), "Test Description",
                "http://test.com/test.png", BookStatus.LEIDO,
                1, false);
        when(bookMapperMock.toResponse(any(Book.class)))
                .thenReturn(response);

        // Act
        var result = bookService.updateBookStatus(mockedId, BookStatus.LEIDO, ownerId);

        // Assert
        assertNotNull(result);
        assertEquals(BookStatus.LEIDO, result.status());
    }

    private BookRequest mockedBookRequest() {
        return new BookRequest("123456789", "Test Book",
                List.of("Test Author"), "Test Description",
                "http://test.com/test.png", BookStatus.DESEADO, 1);
    }

    private BookResponse mockedBookResponse() {
        return new BookResponse(1L, "123456789", "Test Book",
                List.of("Test Author"), "Test Description",
                "http://test.com/test.png", BookStatus.DESEADO,
                1, false);
    }

    private UUID mockedOwnerId() {
        return UUID.fromString("11111111-2222-3333-4444-555555555555");
    }
}

package com.devsenior.cdiaz.bibliokeep.mapper;

import com.devsenior.cdiaz.bibliokeep.model.dto.book.BookRequest;
import com.devsenior.cdiaz.bibliokeep.model.dto.book.BookResponse;
import com.devsenior.cdiaz.bibliokeep.model.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isLent", ignore = true)
    Book toEntity(BookRequest request);

    BookResponse toResponse(Book book);
}

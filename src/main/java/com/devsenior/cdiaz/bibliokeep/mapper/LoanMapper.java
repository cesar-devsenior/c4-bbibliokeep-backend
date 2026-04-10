package com.devsenior.cdiaz.bibliokeep.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.devsenior.cdiaz.bibliokeep.model.dto.loan.LoanResponse;
import com.devsenior.cdiaz.bibliokeep.model.entity.Loan;

@Mapper(componentModel = "spring")
public interface LoanMapper {

    @Mapping(target = "book", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "returned", ignore = true)
    Loan toEntity(com.devsenior.cdiaz.bibliokeep.model.dto.loan.LoanRequest request);

    @Mapping(target = "bookId", source = "book.id")
    LoanResponse toResponse(Loan loan);
}

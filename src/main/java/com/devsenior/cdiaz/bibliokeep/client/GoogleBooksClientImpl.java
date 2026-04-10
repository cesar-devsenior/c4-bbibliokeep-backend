package com.devsenior.cdiaz.bibliokeep.client;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.devsenior.cdiaz.bibliokeep.model.dto.book.BookResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class GoogleBooksClientImpl implements GoogleBooksClient {

    private final RestClient restClient;

    @Value("${app.google-books.base-url:https://www.googleapis.com}")
    private String googleBooksBaseUrl;

    @Override
    public List<BookResponse> searchByIsbn(String isbn) {
        try {
            var response = restClient.get()
                    .uri(googleBooksBaseUrl + "/books/v1/volumes?q=isbn:{isbn}", isbn)
                    .retrieve()
                    .body(GoogleBooksResponse.class);

            return mapResponseToBookResponses(response);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<BookResponse> searchByQuery(String query) {
        try {
            var response = restClient.get()
                    .uri(googleBooksBaseUrl + "/books/v1/volumes?q={query}", query)
                    .retrieve()
                    .body(GoogleBooksResponse.class);

            return mapResponseToBookResponses(response);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<BookResponse> mapResponseToBookResponses(GoogleBooksResponse response) {
        if (response == null || response.items == null) {
            return Collections.emptyList();
        }

        return response.items.stream()
                .map(item -> {
                    var volumeInfo = item.volumeInfo;
                    if (volumeInfo == null) return null;
                    return new BookResponse(
                            null,
                            findIsbn(volumeInfo),
                            volumeInfo.title,
                            volumeInfo.authors == null ? List.of() : volumeInfo.authors,
                            volumeInfo.description,
                            volumeInfo.imageLinks != null ? volumeInfo.imageLinks.thumbnail : null,
                            null,
                            null,
                            false
                    );
                })
                .filter(java.util.Objects::nonNull)
                .toList();
    }

    private String findIsbn(GoogleBookVolumeInfo volumeInfo) {
        if (volumeInfo == null || volumeInfo.industryIdentifiers == null) {
            return null;
        }

        return volumeInfo.industryIdentifiers.stream()
                .filter(id -> "ISBN_13".equals(id.type) || "ISBN_10".equals(id.type))
                .map(id -> id.identifier)
                .findFirst()
                .orElse(null);
    }

    private static record GoogleBooksResponse(List<GoogleBookItem> items) {
    }

    private static record GoogleBookItem(GoogleBookVolumeInfo volumeInfo) {
    }

    private static record GoogleBookVolumeInfo(
            String title,
            List<String> authors,
            String description,
            GoogleBookImageLinks imageLinks,
            List<GoogleBookIndustryIdentifier> industryIdentifiers
    ) {
    }

    private static record GoogleBookImageLinks(String thumbnail) {
    }

    private static record GoogleBookIndustryIdentifier(String type, String identifier) {
    }
}

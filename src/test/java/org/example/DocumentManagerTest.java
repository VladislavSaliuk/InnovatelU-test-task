package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentManagerTest {

    DocumentManager documentManager;
    DocumentManager.Author author;
    @BeforeEach
    void setUp() {
        documentManager = new DocumentManager();

        author = new DocumentManager.Author
                .AuthorBuilder()
                .id("Test author Id")
                .name("Test name")
                .build();

        DocumentManager.Document document1 = new DocumentManager.Document
                .DocumentBuilder()
                .id("Test document Id 1")
                .title("Test document title 1")
                .author(author)
                .content("Test content 1")
                .created(Instant.parse("2010-06-01T00:00:00Z"))
                .build();

        DocumentManager.Document document2 = new DocumentManager.Document
                .DocumentBuilder()
                .id("Test document Id 2")
                .title("Test document title 2")
                .author(author)
                .content("Test content 2")
                .created(Instant.parse("2024-02-10T00:00:00Z"))
                .build();

        documentManager.save(document1);
        documentManager.save(document2);
    }

    @Test
    void save_shouldReturnDocument_withGeneratedId() {
        DocumentManager.Document document3 = new DocumentManager.Document
                .DocumentBuilder()
                .title("Test document title 3")
                .author(author)
                .content("Test content 3")
                .build();

        DocumentManager.Document createdDocument = documentManager.save(document3);

        assertNotNull(createdDocument.getId());
        assertNotNull(createdDocument.getCreated());
        assertEquals("Test document title 3", createdDocument.getTitle());
        assertEquals(3, documentManager.getDocumentCount());
    }

    @Test
    void save_shouldReturnDocument_withExistingId() {
        DocumentManager.Document document4 = new DocumentManager.Document
                .DocumentBuilder()
                .id("existing-id")
                .title("Existing Document")
                .author(author)
                .content("Content")
                .build();

        DocumentManager.Document createdDocument = documentManager.save(document4);
        assertEquals("existing-id", createdDocument.getId());
        assertNotNull(createdDocument.getCreated());
        assertEquals(3, documentManager.getDocumentCount());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test", "T", "Test document", "Tes", "Test doc", "Te", "Test ", "Test document title"})
    void searchByTitlePrefixes_shouldReturnDocumentList_whenInputContainsCorrectPrefixes(String titlePrefix) {
        DocumentManager.SearchRequest searchRequest = new DocumentManager.SearchRequest(
                Arrays.asList(titlePrefix), null, null, null, null
        );

        List<DocumentManager.Document> filteredDocumentList = documentManager.search(searchRequest);
        assertEquals(2, filteredDocumentList.size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test content 1", "Test content 2"})
    void searchByContents_shouldReturnDocumentList_whenInputContainsCorrectPrefixes(String content) {
        DocumentManager.SearchRequest searchRequest = new DocumentManager.SearchRequest(
                null, Arrays.asList(content), null, null, null
        );

        List<DocumentManager.Document> filteredDocumentList = documentManager.search(searchRequest);
        assertEquals(1, filteredDocumentList.size());
    }

    @ParameterizedTest
    @CsvSource({"Test,Test content 1", "Te,Test content 2"})
    void searchByTitlePrefixesAndContents_shouldReturnDocumentList_whenInputContainsCorrectData(String titlePrefix, String content) {
        DocumentManager.SearchRequest searchRequest = new DocumentManager.SearchRequest(
                Arrays.asList(titlePrefix), Arrays.asList(content), null, null, null
        );

        List<DocumentManager.Document> filteredDocumentList = documentManager.search(searchRequest);
        assertEquals(1, filteredDocumentList.size());
    }

    @Test
    void searchByAuthorIds_shouldReturnDocumentList_whenInputContainsCorrectAuthorId() {
        String authorId = "Test author Id";
        DocumentManager.SearchRequest searchRequest = new DocumentManager.SearchRequest(
                null, null, Arrays.asList(authorId), null, null
        );

        List<DocumentManager.Document> filteredDocumentList = documentManager.search(searchRequest);
        assertEquals(2, filteredDocumentList.size());
    }

    @ParameterizedTest
    @CsvSource({"Test,Test content 1,Test author Id", "Te,Test content 2,Test author Id"})
    void searchByTitlePrefixesAndContentsAndAuthorIds_shouldReturnDocumentList_whenInputContainsCorrectData(String titlePrefix,
                                                                                                            String content,
                                                                                                            String authorId) {
        DocumentManager.SearchRequest searchRequest = new DocumentManager.SearchRequest(
                Arrays.asList(titlePrefix), Arrays.asList(content), Arrays.asList(authorId), null, null
        );

        List<DocumentManager.Document> filteredDocumentList = documentManager.search(searchRequest);
        assertEquals(1, filteredDocumentList.size());
    }

    @ParameterizedTest
    @CsvSource({
            "2010-06-01T00:00:00Z,2024-02-10T00:00:00Z",
            "2010-06-01T00:00:00Z,2010-06-01T23:59:59Z",
            "2024-02-10T00:00:00Z,2024-02-10T23:59:59Z",
            "2024-02-11T00:00:00Z,2024-02-12T00:00:00Z"
    })
    void searchByCreatedTime_shouldReturnDocumentList_whenInputContainsCorrectData(String createdFrom, String createdTo) {
        DocumentManager.SearchRequest searchRequest = new DocumentManager.SearchRequest(
                null, null, null, Instant.parse(createdFrom), Instant.parse(createdTo)
        );

        List<DocumentManager.Document> filteredDocumentList = documentManager.search(searchRequest);
        assertEquals(2, filteredDocumentList.size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test document Id 1", "Test document Id 2"})
    void findById_shouldReturnDocument_whenIdExists(String existingId) {
        Optional<DocumentManager.Document> foundDocument = documentManager.findById(existingId);
        assertTrue(foundDocument.isPresent());
    }

    @Test
    void findById_shouldReturnEmpty_whenIdDoesNotExist() {
        Optional<DocumentManager.Document> foundDocument = documentManager.findById("non-existing-id");
        assertFalse(foundDocument.isPresent());
    }

}

package org.example;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * For implement this task focus on clear code, and make this solution as simple readable as possible
 * Don't worry about performance, concurrency, etc
 * You can use in Memory collection for sore data
 * <p>
 * Please, don't change class name, and signature for methods save, search, findById
 * Implementations should be in a single class
 * This class could be auto tested
 */
public class DocumentManager {

    /**
     * Implementation of this method should upsert the document to your storage
     * And generate unique id if it does not exist, don't change [created] field
     *
     * @param document - document content and author data
     * @return saved document
     */

    private List<Document> documentList = new LinkedList<>();

    public Document save(Document document) {

        if (document.getId() == null) {
            document.setId(UUID.randomUUID().toString());
        }

        document.setCreated(Instant.now());
        documentList.add(document);
        return document;
    }

    /**
     * Implementation this method should find documents which match with request
     *
     * @param request - search request, each field could be null
     * @return list matched documents
     */
    public List<Document> search(SearchRequest request) {
        return documentList.stream()
                .filter(doc -> request.getTitlePrefixes() == null ||
                        request.getTitlePrefixes().stream().anyMatch(prefix -> doc.getTitle().startsWith(prefix)))
                .filter(doc -> request.getContainsContents() == null ||
                        request.getContainsContents().stream().anyMatch(content -> doc.getContent().contains(content)))
                .filter(doc -> request.getAuthorIds() == null ||
                        request.getAuthorIds().stream().anyMatch(authorId -> doc.getAuthor().getId().equals(authorId)))
                .filter(doc -> request.getCreatedFrom() == null || doc.getCreated().isAfter(request.getCreatedFrom()))
                .collect(Collectors.toList());
    }



    /**
     * Implementation this method should find document by id
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {
        return documentList.stream().filter(doc -> doc.getId().equals(id)).findFirst();
    }

    public long getDocumentCount() {
        return documentList.stream().count();
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}

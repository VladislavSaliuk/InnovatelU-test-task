# InnovatelU Test Task

## Overview
The purpose of this project is to implement a simple document management system that allows users to save, search, and retrieve documents with associated metadata, including titles, contents, authors, and creation timestamps. The solution prioritizes clear, readable code while being straightforward and efficient for basic usage.

## Features
- **Document Management**: Save new documents and retrieve existing ones by ID.
- **Search Functionality**: Search documents based on title prefixes, content, author IDs, and creation timestamps.
- **In-Memory Storage**: Uses an in-memory list to store documents, making it easy to test and modify.

## Technologies Used
- Java 8
- Lombok (for boilerplate code reduction)
- JUnit (for testing)

## Getting Started

### Prerequisites
- Java 8 or higher
- Maven (for dependency management)

### Installation
1. Clone the repository:
   ```bash
   git clone <repository-url>
2. Navigate to the project directory:
   ```bash
   cd <project-directory>
3. Compile the project using Maven:
   ```bash
   mvn clean install

### Usage
1. Create an instance of DocumentManager
2. Use the save(Document document) method to add documents.
3. Search documents using the search(SearchRequest request) method with various criteria.


### Code Example

#### Example
```java
DocumentManager documentManager = new DocumentManager();
DocumentManager.Author author = DocumentManager.Author.builder()
    .id("1")
    .name("Author Name")
    .build();

DocumentManager.Document document = DocumentManager.Document.builder()
    .title("Document Title")
    .content("Document Content")
    .author(author)
    .build();

documentManager.save(document);

// Search by title prefix
List<DocumentManager.Document> results = documentManager.search(new DocumentManager.SearchRequest(
    Arrays.asList("Doc"), null, null, null, null
));

```

## Testing 

Unit tests are provided using JUnit. You can run tests with Maven:
  ```bash
  mvn test
  ```

### Test Cases

The tests cover various scenarios, including:

1. Document saving and retrieval
2. Searching by title prefixes, content, and author IDs
3. Filtering documents by creation dates

### Contributing

Contributions are welcome! Feel free to submit a pull request or open an issue.

### License

This project is licensed under the MIT License - see the LICENSE file for details.

```bash
  Just replace `<repository-url>` and `<project-directory>` with the appropriate values!
  ```

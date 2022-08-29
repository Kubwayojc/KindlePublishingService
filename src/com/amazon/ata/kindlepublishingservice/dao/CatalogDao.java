package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatus;
import com.amazon.ata.kindlepublishingservice.publishing.KindleFormattedBook;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import javax.inject.Inject;

public class CatalogDao {

    private final DynamoDBMapper dynamoDbMapper;

    /**
     * Instantiates a new CatalogDao object.
     *
     * @param dynamoDbMapper The {@link DynamoDBMapper} used to interact with the catalog table.
     */
    @Inject
    public CatalogDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    /**
     * Returns the latest version of the book from the catalog corresponding to the specified book id.
     * Throws a BookNotFoundException if the latest version is not active or no version is found.
     * @param bookId Id associated with the book.
     * @return The corresponding CatalogItem from the catalog table.
     */
    public CatalogItemVersion getBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        return book;
    }

    // Returns null if no version exists for the provided bookId

    public CatalogItemVersion getLatestVersionOfBook(String bookId) {
        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(bookId);

        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
            .withHashKeyValues(book)
            .withScanIndexForward(false)
            .withLimit(1);

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    public CatalogItemVersion removeBookFromCatalog(String bookId) {

        CatalogItemVersion catalogItemVersion = getBookFromCatalog(bookId);

        catalogItemVersion.setInactive(true);

        dynamoDbMapper.save(catalogItemVersion);

        return catalogItemVersion;

    }

    public CatalogItemVersion validateBookExists(String bookId) {
        CatalogItemVersion catalogItemVersion = getLatestVersionOfBook(bookId);

        if(catalogItemVersion == null){
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        return catalogItemVersion;

    }

    public void addOrUpdateBook(CatalogItemVersion book) {

        dynamoDbMapper.save(book);

    }


    public CatalogItemVersion createOrUpdateBook(KindleFormattedBook book){



        String bookId_New = book.getBookId();

        CatalogItemVersion new_book = new CatalogItemVersion();

        new_book.setInactive(false);
        new_book.setAuthor(book.getAuthor());
        new_book.setGenre(book.getGenre());
        new_book.setText(book.getText());
        new_book.setTitle(book.getTitle());

        if(bookId_New ==  null) {

            new_book.setVersion(1);
            new_book.setBookId(KindlePublishingUtils.generateBookId());
            addOrUpdateBook(new_book);

        } else {

            CatalogItemVersion existing_Book = validateBookExists(bookId_New);

            new_book.setBookId(bookId_New);
            new_book.setVersion(existing_Book.getVersion() + 1);

            removeBookFromCatalog(existing_Book.getBookId());

            addOrUpdateBook(new_book);
        }

        return new_book;
    }


}

package com.amazon.ata.kindlepublishingservice.publishing;


import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatus;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import javax.inject.Inject;

import static com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus.FAILED;
import static com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus.IN_PROGRESS;

/**
 * A Runnable that processes a publish request from the BookPublishRequeManager
 */

public class BookPublishTask implements Runnable {
    private BookPublishRequestManager bookPublishRequestManager;
    private PublishingStatusDao publishingStatusDao;
    private CatalogDao catalogDao;



    @Inject
    public BookPublishTask(BookPublishRequestManager bookPublishRequestManager,
                           PublishingStatusDao publishingStatusDao, CatalogDao catalogDao) {
        this.bookPublishRequestManager = bookPublishRequestManager;
        this.publishingStatusDao = publishingStatusDao;
        this.catalogDao = catalogDao;

    }

    @Override
    public void run() {

        BookPublishRequest bookPublishRequest = bookPublishRequestManager.getBookPublishRequestToProcess();

        if(bookPublishRequest == null){

            return;
        } else {

            publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
                    PublishingRecordStatus.IN_PROGRESS, bookPublishRequest.getBookId());
        }


            KindleFormattedBook kindleFormattedBook = KindleFormatConverter.format(bookPublishRequest);

            try {
                CatalogItemVersion catalogItemVersion = catalogDao.createOrUpdateBook(kindleFormattedBook);

                publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
                        PublishingRecordStatus.SUCCESSFUL, catalogItemVersion.getBookId());

            } catch (BookNotFoundException e) {

                publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
                        PublishingRecordStatus.FAILED, bookPublishRequest.getBookId(), "BooK Id not found");

            } catch (Exception e) {

                publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
                        PublishingRecordStatus.FAILED, bookPublishRequest.getBookId(), "Could not find the requested book");

            }




    }
}

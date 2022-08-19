package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazonaws.services.dynamodbv2.xspec.B;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class BookPublishRequestManager {
    private ConcurrentLinkedDeque<BookPublishRequest> bookPublishRequestCollection;


    @Inject
    public BookPublishRequestManager(ConcurrentLinkedDeque<BookPublishRequest> bookPublishRequestCollection) {
        this.bookPublishRequestCollection = bookPublishRequestCollection;
    }

    public void addBookPublishRequest(BookPublishRequest bookPublishRequest) {

        bookPublishRequestCollection.add(bookPublishRequest);
    }


    public BookPublishRequest getBookPublishRequestToProcess(){


            return bookPublishRequestCollection.poll();

    }

}

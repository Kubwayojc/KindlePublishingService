package com.amazon.ata.kindlepublishingservice.converters;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;

import java.util.ArrayList;
import java.util.List;

public class PublishingStatusItemConverter {

    public PublishingStatusItemConverter() {
    }

    public static List<PublishingStatusRecord> testPublishingStatusRecord(List<PublishingStatusItem> publishingStatusItems) {
        List<PublishingStatusRecord> statusRecordList = new ArrayList<>();

        for(PublishingStatusItem statusItem : publishingStatusItems) {
            PublishingStatusRecord testRecords = PublishingStatusRecord.builder()
                    .withStatus(statusItem.getStatus().toString())
                    .withStatusMessage(statusItem.getStatusMessage())
                    .withBookId(statusItem.getBookId())
                    .build();

            statusRecordList.add(testRecords);
        }
        return statusRecordList;
    }
}

## Project Mastery Tasks

#### Under each mastery task I have linked to parts of the project that are my code.  
### [Mastery Task 1: Killing me softly](tasks/MasteryTask01.md)

My Contributions:   
- [Class diagram](src/resources/mastery-task1-kindle-publishing-CD.puml)  
- [Sequence diagram](src/resources/mastery-task1-remove-book-SD.puml)  
- [Soft Delete](src/com/amazon/ata/kindlepublishingservice/dao/CatalogDao.java)  

### [Mastery Task 2: Submit to the process](tasks/MasteryTask02.md)

My Contributions:
- [BookPublishRequestManger](src/com/amazon/ata/kindlepublishingservice/publishing/BookPublishingRequestManager.java)  
- [SubmitBookForPublishingActivity](src/com/amazon/ata/kindlepublishingservice/activity/SubmitBookForPublishingActivity.java) 

### [Mastery Task 3: Query, query on the wall, don’t load one, get them all!](tasks/MasteryTask03.md)

My Contributions:
- [DynamoDB Query](src/com/amazon/ata/kindlepublishingservice/dao/PublishingStatusDao.java)
- [GetPublishingStatus](src/com/amazon/ata/kindlepublishingservice/activity/GetPublishingStatusActivity.java) 

### [Mastery Task 4: Make a run(nable) for it](tasks/MasteryTask04.md)

My Contributions:
- [BookPublishTask (Runnable)](src/com/amazon/ata/kindlepublishingservice/publishing/BookPublishTask.java)
- [BookPublishingRequestManager(Updated for Thread Safety)](src/com/amazon/ata/kindlepublishingservice/publishing/BookPublishingRequestManager.java)
- [BookPublishRequest(Updated for Thread Safety)](src/com/amazon/ata/kindlepublishingservice/publishing/BookPublishRequest.java)

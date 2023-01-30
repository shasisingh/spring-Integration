package nl.shashi.playground.jms.service.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.metadata.ConcurrentMetadataStore;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@Slf4j
public class FileHandler implements MessageHandler {

    private final ConcurrentMetadataStore concurrentMetadataStore;

    public FileHandler(ConcurrentMetadataStore concurrentMetadataStore) {
        this.concurrentMetadataStore = concurrentMetadataStore;
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {

        File payload = (File) message.getPayload();

        log.info("Received file : filename {}", payload.getName());
        tableMetaDataCleanup(payload, concurrentMetadataStore);
        try {
            Files.delete(payload.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void tableMetaDataCleanup(final File messagePayload, ConcurrentMetadataStore concurrentMetadataStore) {
        String metaValue = concurrentMetadataStore.remove("uploadTxtFile_" + messagePayload.getAbsolutePath());
        log.info("Deleted from in-memory database : fileMetaValue {}", metaValue);
    }
}

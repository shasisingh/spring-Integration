package nl.shashi.playground.jms.config;

import nl.shashi.playground.jms.service.handler.FileHandler;
import nl.shashi.playground.jms.util.FilePollerUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.RecursiveDirectoryScanner;
import org.springframework.integration.file.filters.FileSystemPersistentAcceptOnceFileListFilter;
import org.springframework.integration.metadata.ConcurrentMetadataStore;
import org.springframework.integration.metadata.SimpleMetadataStore;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Configuration
public class FilePollerConfiguration {

    private static final int MAX_DIR_DEPTH = 2;

    public static final String LEADER_ROLE = "leader";

    @Value("${file.poller.input.directory}")
    private String pollingBaseInputDirectory;

    @Bean
    public IntegrationFlow filePoller(MessageSource<File> fileSourceDirectory, FileHandler fileHandler) {
        return IntegrationFlows.from(fileSourceDirectory,
                        configurer ->
                                configurer
                                        .role(LEADER_ROLE)
                                        .autoStartup(false)
                                        .poller(Pollers.fixedDelay(1000, TimeUnit.MILLISECONDS)
                                                .maxMessagesPerPoll(100))
                )
                .filter(FilePollerUtility::onlyFiles)
                .filter(FilePollerUtility::fileExists)
                .filter(FilePollerUtility::onlyTxt)
                .handle(fileHandler)
                .get();
    }

    @Bean
    public ConcurrentMetadataStore concurrentMetadataStore() {
        return new SimpleMetadataStore();
    }

    @Bean
    public MessageSource<File> fileSourceDirectory(RecursiveDirectoryScanner recursiveDirectoryScanner) {
        var messageSource = new FileReadingMessageSource();
        messageSource.setDirectory(createIfNotExists());
        messageSource.setScanner(recursiveDirectoryScanner);
        messageSource.setScanEachPoll(true);
        return messageSource;
    }

    @Bean("swiftFileActDirectoryScanner")
    public RecursiveDirectoryScanner recursiveDirectoryScanner(ConcurrentMetadataStore concurrentMetadataStore) {
        var scanner = new RecursiveDirectoryScanner();
        scanner.setMaxDepth(MAX_DIR_DEPTH);
        scanner.setFilter(new FileSystemPersistentAcceptOnceFileListFilter(concurrentMetadataStore, "uploadTxtFile_"));
        return scanner;
    }

    private File createIfNotExists(){
        var pollerDir = new File(pollingBaseInputDirectory);
        pollerDir.mkdirs();
        return pollerDir;
    }

}

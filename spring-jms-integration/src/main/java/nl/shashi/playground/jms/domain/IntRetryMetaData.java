package nl.shashi.playground.jms.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "INT_RETRY_METADATA")
@IdClass(InitIdClass.class)
public class IntRetryMetaData {

    @Id
    @Column(name = "SOURCE_DIR", nullable = false)
    private String sourceDir;

    @Id
    @Column(name = "FILE_UUID", nullable = false)
    private String fileUuid;

    @Column(name = "INCOMING_FILES_STATUS",nullable = false)
    private String incomingFilesStatus;

    @Column(name = "INCOMING_FILES_CHANNEL",nullable = false)
    private String incomingFilesChannel;

    public IntRetryMetaData(String sourceDir, String fileUuid, String incomingFilesStatus, String incomingFilesChannel) {
        this.sourceDir = sourceDir;
        this.fileUuid = fileUuid;
        this.incomingFilesStatus = incomingFilesStatus;
        this.incomingFilesChannel = incomingFilesChannel;
    }

    public IntRetryMetaData() {
    }
}

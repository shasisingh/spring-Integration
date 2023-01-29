package nl.shashi.playground.jms.domain;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class InitIdClass implements Serializable {

    private static final long serialVersionUID = 11091977911L;

    private String sourceDir;
    private String fileUuid;

    public InitIdClass(String sourceDir, String fileUuid) {
        this.sourceDir = sourceDir;
        this.fileUuid = fileUuid;
    }

    public InitIdClass() {

    }
}

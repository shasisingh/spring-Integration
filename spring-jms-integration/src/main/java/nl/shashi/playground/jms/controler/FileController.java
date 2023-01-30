package nl.shashi.playground.jms.controler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.IntStream;

@RestController
@RequestMapping(path = "/api/v1/files")
public class FileController {

    @Value("${file.poller.input.directory}")
    private String pollingBaseInputDirectory;

    @PostMapping(value = "/create/{numberOfFiles}")
    public String create(
            @PathVariable(value = "numberOfFiles") final int numberOfFiles,
            @RequestParam(value = "extension", required = false, defaultValue = ".txt") final String extension) {
        File baseDir = new File(pollingBaseInputDirectory);

        IntStream.range(0, numberOfFiles).forEach(v -> {
            try {
                Files.write(Paths.get(baseDir.getPath(), UUID.randomUUID().toString().concat(extension)), UUID.randomUUID().toString().getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return "Files created.";
    }
}

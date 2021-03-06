package com.littlepay.busfareservice.service;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class FileManager {

    public String fileToString(String fileName) throws IOException {
        File file = new File(fileName);

        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }
}

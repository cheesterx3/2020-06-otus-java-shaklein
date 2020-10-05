package ru.otus.helpers;

import lombok.val;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public final class FileSystemHelper {

    private FileSystemHelper() {
    }

    public static String localFileNameOrResourceNameToFullPath(String fileOrResourceName) {
        val file = new File(String.format("./%s", fileOrResourceName));
        if (file.exists())
            return URLDecoder.decode(file.toURI().getPath(), StandardCharsets.UTF_8);

        return Optional.ofNullable(FileSystemHelper.class.getClassLoader().getResource(fileOrResourceName))
                .orElseThrow(() -> new RuntimeException(String.format("File \"%s\" not found", fileOrResourceName)))
                .toExternalForm();


    }
}

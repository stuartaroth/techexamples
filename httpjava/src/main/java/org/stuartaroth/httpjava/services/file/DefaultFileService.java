package org.stuartaroth.httpjava.services.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class DefaultFileService implements FileService {
    private static Logger logger = LoggerFactory.getLogger(DefaultFileService.class);

    @Override
    public String readFile(String filename) throws Exception {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }

    @Override
    public void writeFile(String filename, String content) throws Exception {
        Files.write(Paths.get(filename), content.getBytes());
    }

    @Override
    public void deleteFile(String filename) throws Exception {
        Files.delete(Paths.get(filename));
    }

    @Override
    public String readFileFromResources(String filename) throws Exception {
        StringBuilder result = new StringBuilder("");

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}

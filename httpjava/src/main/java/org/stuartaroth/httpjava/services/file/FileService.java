package org.stuartaroth.httpjava.services.file;

public interface FileService {
    String readFile(String filename) throws Exception;
    void writeFile(String filename, String content) throws Exception;
    void deleteFile(String filename) throws Exception;
    String readFileFromResources(String filename) throws Exception;
}

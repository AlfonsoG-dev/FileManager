package application;

import application.operation.FileOperation;

import java.io.File;

class FileManager {
    public static void main(String[] args) {
        String localPath = "." + File.separator;
        FileOperation op = new FileOperation();
        op.searchWordInDirectory("src/application/", "ioexception", 2);
    }
}

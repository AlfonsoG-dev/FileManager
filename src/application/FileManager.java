package application;

import application.operation.FileOperation;

import java.util.Arrays;
import java.io.File;

class FileManager {
    public static void main(String[] args) {
        String localPath = "." + File.separator;
        FileOperation op = new FileOperation();
        op.copyFilesToTargets(
                Arrays.asList("bin/application/FileManager.class", "src/application/FileManager.java"),
                Arrays.asList("docs", "lib")
        );
    }
}

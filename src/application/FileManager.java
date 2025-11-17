package application;

import application.operation.FileOperation;

import java.util.Arrays;
import java.io.File;

class FileManager {
    public static void main(String[] args) {
        String localPath = "." + File.separator;
        FileOperation op = new FileOperation();
        op.copyDirsToTargets(
                Arrays.asList("bin/application", "src/application"),
                Arrays.asList("docs", "lib"),
                "--r"
        );
    }
}

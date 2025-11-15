package application;

import java.io.File;
import java.nio.file.Paths;

import application.utils.FileUtils;

class FileManager {
    public static void main(String[] args) {
        String localPath = "." + File.separator;
        FileUtils fu = new FileUtils(localPath);
        fu.copyFileToTarget(Paths.get("src/application/utils/FileUtils.java"), "docs");
    }
}

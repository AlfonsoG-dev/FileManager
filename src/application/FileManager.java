package application;

import java.io.File;

import application.utils.FileUtils;

class FileManager {
    public static void main(String[] args) {
        String localPath = "." + File.separator;
        FileUtils fu = new FileUtils(localPath);
    }
}

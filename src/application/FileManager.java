package application;

import java.io.File;

import java.nio.file.Paths;

import application.utils.TextUtils;

class FileManager {
    public static void main(String[] args) {
        String localPath = "." + File.separator;
        TextUtils tu = new TextUtils();
        boolean contains = tu.lineContainsWord("public static void main(String[] args) {", "public");
        if(contains) {
            System.out.println("Its contained");
        }
    }
}

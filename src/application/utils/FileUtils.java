package application.utils;

import java.util.List;

import java.io.File;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.FileVisitOption;
import java.nio.file.StandardCopyOption;


public class FileUtils {

    public boolean create(String pathURI) {
        File f = new File(pathURI);
        if(f.isFile()) {
            System.out.println("[Info] Only directories allowed");
            return false;
        }
        if(f.toPath().getNameCount() > 2) {
            return f.mkdirs();
        }
        return f.mkdir();

    }

    public List<Path> listDirContent(String pathURI, int level) {
        if (!(level > 0)) level = Integer.MAX_VALUE;
        try {
            return Files.walk(Paths.get(pathURI), level, FileVisitOption.FOLLOW_LINKS).toList();
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void copyFileToTarget(Path sourcePath, String targetURI) {
        try {
            Path destination = Paths.get(targetURI).resolve(sourcePath.getFileName());
            Path result = Files.copy(sourcePath, destination, StandardCopyOption.COPY_ATTRIBUTES);
            if(result != null) {
                System.out.println(String.format("[Info] copy %s into \n\t=>[%s]", sourcePath.toString(), destination.toString()));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}

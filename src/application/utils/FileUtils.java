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
    private String localPath;
    public FileUtils(String localPath) {
        this.localPath = localPath;
    }

    /**
     * create a directories if they're not created.
     * @param pathURI - the path to create the directory.
     * @return true if its created, false otherwise.
     */
    public boolean create(String pathURI) {
        File f = new File(pathURI);
        if(f.isFile()) return false;
        if(f.isDirectory() && f.exists()) return true;
        if(f.toPath().getNameCount() > 2) {
            return f.mkdirs();
        }
        return f.mkdir();

    }
    /**
     * List directory content by level, if nested level is 0, it will search recursively.
     * @param pathURI - the path to list its content.
     * @param level - the nested level to reach.
     * @return the list with the path content without filtering directories or files.
     */
    public List<Path> listDirContent(String pathURI, int level) {
        if (!(level > 0)) level = Integer.MAX_VALUE;
        try {
            return Files.walk(Paths.get(pathURI), level, FileVisitOption.FOLLOW_LINKS).toList();
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Copy a file to a destination target.
     * @param sourcePath - the file to copy.
     * @param targetURI - the path where to copy the source file.
     */
    public void copyFileToTarget(Path sourcePath, String targetURI) {
        if(!sourcePath.toFile().isFile()) return;
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
    /**
     * Copy a directory into a target path.
     * @param sourcepath - the directory path to copy.
     * @param targetURI - the destination where to copy the source directory.
     * @param level - the nested level to reach.
     */
    public void copyDirToTarget(Path sourcePath, String targetURI, int level) {
        if(!sourcePath.toFile().isDirectory()) return;
        try {
            Path targetPath = Paths.get(targetURI);
            // first list and create the directory structure.
            List<Path> paths = listDirContent(sourcePath.toString(), level);
            for(Path p: paths) {
                Path relative = sourcePath.relativize(p);
                Path destination = targetPath.resolve(relative);
                if(Files.isDirectory(p)) {
                    create(destination.toString());
                    System.out.println(String.format("[Info] Creating {%s}", destination.toString()));
                } else {
                    Path r = Files.copy(p, destination, StandardCopyOption.COPY_ATTRIBUTES);
                    System.out.println(String.format("[Info] Copy %s into \n\t=>[%s]", p, r));
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}

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
    public boolean createDirectory(String pathURI) {
        File f = new File(pathURI);
        if(f.isFile()) return false;
        if(f.isDirectory() && f.exists()) return true;
        if(f.toPath().getNameCount() > 2 && f.isDirectory()) return f.mkdirs();
        return f.mkdir();
    }
    /**
     * creates a file it its not already created, if the file its in a nested structure created the directories first.
     * @param pathURI - the file to create.
     * @return true if the file was created, false otherwise.
     */
    public boolean createFile(String pathURI) {
        Path p = Paths.get(pathURI);
        try {
            // create nested structure for directories
            if(p.getNameCount() > 2) {
                Path parent = p.getParent();
                if(parent != null) createDirectory(parent.toString());
            }
            System.out.println(String.format("[Info] Creating => %s", p.toString()));
            return p.toFile().createNewFile();
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Deletes a directory if its empty, otherwise you must provide --r to delete it.
     * @param pathURI - the directory to delete.
     * @param recursively - to delete a directory that isn't empty.
     */
    public boolean deleteDirectory(String pathURI, boolean recursively) {
        File f = new File(pathURI);
        if(!f.isDirectory() || !f.exists()) return false;
        try {
            if(recursively) {
                // get files to delete.
                List<Path> paths = listDirContent(pathURI, 0)
                    .stream()
                    .filter(Files::isRegularFile)
                    .toList();
                for(Path p: paths) {
                    System.out.println(String.format("[Info] Deleting => %s", p.toString()));
                    Files.deleteIfExists(p);
                }
                return deleteDirectory(f.getPath(), false);
            } else {
                System.out.println(String.format("[Info] Deleting => %s", f.toString()));
                return Files.deleteIfExists(f.toPath());
            }
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
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
        if(!sourcePath.toFile().isFile() || !sourcePath.toFile().exists()) return;
        try {
            Path destination = Paths.get(targetURI).resolve(sourcePath.getFileName());
            Path result = Files.copy(sourcePath, destination, StandardCopyOption.COPY_ATTRIBUTES);
            if(result != null) {
                System.out.println(String.format("[Info] copy %s \n\tinto \t=>[%s]", sourcePath.toString(), destination.toString()));
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
        if(!sourcePath.toFile().isDirectory() || !sourcePath.toFile().exists()) return;
        try {
            Path targetPath = Paths.get(targetURI);
            // first list and create the directory structure.
            List<Path> paths = listDirContent(sourcePath.toString(), level);
            for(Path p: paths) {
                Path relative = sourcePath.relativize(p);
                Path destination = targetPath.resolve(relative);
                if(Files.isDirectory(p)) {
                    createDirectory(destination.toString());
                    System.out.println(String.format("[Info] Creating {%s}", destination.toString()));
                } else {
                    Path r = Files.copy(p, destination, StandardCopyOption.COPY_ATTRIBUTES);
                    System.out.println(String.format("[Info] Copy %s \n\tinto \t=>[%s]", p, r));
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}

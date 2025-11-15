package Application.Utils;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileVisitOption;

import java.util.ArrayList;
import java.util.List;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtils {
    private TextUtils textUtils;

    public FileUtils() {
        textUtils = new TextUtils();
    }
    /**
     * get the local name
     * @param localPath - local path
     * @return the name of the given path
     */
    public String getLocalName(String localPath) {
        try {
            String local = new File(localPath).getCanonicalPath();
            return new File(local).getName();
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * A list of directories.
     * @param myFile - the file path to search for directories.
     * @return A list of directories.
     */
    public List<File> listDirectoryNames(File myFile) {
        List<File> names = new ArrayList<>();
        try {
            names = Files.walk(myFile.toPath(), FileVisitOption.FOLLOW_LINKS)
                .map(Path::toFile)
                .filter(p -> p.isDirectory())
                .toList();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return names;
    }
    /**
     * A list of files of a directory.
     * @param f - the file to list its content.
     * @return a list if files.
     */
    public List<File> listDirectoryFiles(File f) {
        List<File> files = new ArrayList<>();
        try {
            files = Files.walk(f.toPath(), FileVisitOption.FOLLOW_LINKS)
                .map(Path::toFile)
                .filter(p -> p.isFile())
                .toList();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return files;
    }
    /**
     * A list of files of a particular path.
     * @param fileURI - the file path to list its files.
     * @return a list of files.
     */
    public List<File> listFilesFromPath(String fileURI) {
        List<File> files = new ArrayList<>();
        try {
            File f = new File(fileURI);
            files = Files.walk(f.toPath(), FileVisitOption.FOLLOW_LINKS)
                .map(Path::toFile)
                .filter(p -> p.isFile())
                .toList();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    /**
     * helper method to print the file path.
     * @param f - file to print its path
     */
    public void printFilePath(File f) {
        System.out.println(
            String.format(
                "| %s |",
                Colors.GREEN_UNDERLINE + f.getPath() + Colors.RESET
            )
        );
    }
    /**
     * Get the file content on a string concatenation by \n.
     * @param f - the file to read its content
     * @return the file content.
     */
    public String getFileLines(File f) {
        StringBuffer b = new StringBuffer();
        try(BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line;
            while((line = reader.readLine()) != null) {
                b.append(Colors.YELLOW +  line + "\n" + Colors.RESET);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return b.toString();
    }
    /**
     * compare the directory names.
     * @param f: file to compare its name
     * @param cliOption: cli options to compare the directory names
     * @param cliContext: name to compare with the file name
     * @return true if the files are similar otherwise false
     */
    public boolean areSimilarDirs(File f, String cliOption, String cliContext) {
        boolean similar = false;
        String c = cliContext.toLowerCase();
        switch(cliOption) {
            case "-td":
                if(f.isDirectory() && f.listFiles() != null) {
                    listDirectoryNames(f)
                        .parallelStream()
                        .filter(e -> e.getName().toLowerCase().contains(c))
                        .forEach(e -> {
                            printFilePath(e);
                        });
                } else if(f.getName().toLowerCase().contains(c)) {
                    printFilePath(f);
                }
            break;
        }
        return similar;
    }
    /**
     * compare 2 files or folders with CLIOption
     * @param cliOption: Cli options -e or -n
     * @param first: first file
     * @param second: second file
     * @return true if the file have similarities, false otherwise
     */
    public boolean areSimilar(File first, String cliOption, String second) {
        boolean similar = false;
        String s = "";
        if(second.contains(".")) {
            s = second.split("\\.")[1].toLowerCase();
        } else {
            s = second.toLowerCase();
        }
        switch(cliOption) {
            case "-e":
                if(first.isFile() && first.getName().contains(".")) {
                    String f = first.getName().split("\\.")[1].toLowerCase();
                    if(f.equals(s)) {
                        similar = true;
                    }
                }
                break;
            case "-tf":
            case "-n":
                String f = first.getName().toLowerCase();
                if(f.contains(s)) {
                    similar = true;
                }
                break;
        }
        return similar;
    }
    /**
     * Compare two lines with ignore case or smartcase.
     * @param cliOption the option -i or -s to enable ignore case or smartcase option.
     * @param first the line to compare with the context.
     * @param cliContext the line to compare with.
     * @return true if the lines are similar, false otherwise.
     */
    public boolean areSimilarLines(String cliOption, String first, String cliContext) {
        boolean isSimilar = false;
        switch(cliOption) {
            // ignore case
            case "-i":
                if(first.compareToIgnoreCase(cliContext) == 0) {
                    isSimilar = true;
                }
                break;
            // smart case
            case "-s":
                if(first.toLowerCase().contains(cliContext.toLowerCase())) {
                    isSimilar = true;
                }
                break;
        }
        return isSimilar;
    }
    /**
     * Create a directory if the provided path doesn't exists; if the path has a nested structure create from parent to children.
     * @param targetFilePath - where to create the directory.
     * @param parentFileNames - the parent path to create in target.
     */
    public void createParentFile(String targetFilePath, String parentFileNames) {
        String[] pn = parentFileNames.split("\n");
        for(String p: pn) {
            String fileName = p.replace(targetFilePath, "");
            File miFile = new File(p);
            int c = textUtils.countNestedLevels(fileName);
            if(!miFile.exists() && c > 1) {
                miFile.mkdirs();
            } else if(!miFile.exists() && c <= 1) {
                miFile.mkdir();
            }
            System.out.println(
                    Colors.YELLOW_UNDERLINE + "[ CREATED ]: " + Colors.RESET
                    + miFile.getPath()
            );
        }
    }
    private void addZipFile(File source, String base, ZipOutputStream zop) {
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(source);
            ZipEntry zEntry = new ZipEntry(base);
            zop.putNextEntry(zEntry);

            byte[] buffer = new byte[1024];
            int length;
            while((length = fileInput.read(buffer)) > 0) {
                zop.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileInput != null) {
                try {
                    fileInput.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
                fileInput = null;
            }
        }
    }
    /**
     * recursive add file to the zip 
     * @param source: source of the files
     * @param base: name of the file
     * @param zop: {@link ZipOutputStream}
     * @param includeFiles - the files to include in the zip creation.
     */
    private void addFilesToZip(File source, String base, ZipOutputStream zop, String includeFiles) {
        if(source.isDirectory() && source.listFiles() != null) {
            File[] sourceFiles = source.listFiles();
            if(includeFiles == null) {
                for(File sf: sourceFiles) {
                    addFilesToZip(
                        sf,
                        base + File.separator + sf.getName(),
                        zop,
                        includeFiles
                    );
                }
            }
            if(includeFiles != null && includeFiles.contains(",")) {
                String[] includes = includeFiles.split(",");
                for(File sf: sourceFiles) {
                    for(String ic: includes) {
                        if(sf.getPath().contains(ic.trim())) {
                            addFilesToZip(
                                sf,
                                base + File.separator + sf.getName(),
                                zop,
                                includeFiles
                            );
                        }
                    }
                }
            } else if(includeFiles != null && !includeFiles.contains(",")) {
                for(File sf: sourceFiles) {
                    if(sf.getPath().contains(includeFiles)) {
                        addFilesToZip(
                            sf,
                            base + File.separator + sf.getName(),
                            zop,
                            includeFiles
                        );
                    }
                }

            }
        } else {
            addZipFile(
                source,
                base,
                zop
            );
        }
    }
    /**
     * create the zip destination with the source file
     * @param source: source file
     * @param destination: destination file
     */
    public void createZipFile(File source, File destination, String includeFiles) {
        FileOutputStream fileOutput = null;
        ZipOutputStream zipOutput = null;
        try {
            fileOutput = new FileOutputStream(destination);
            zipOutput = new ZipOutputStream(fileOutput);
            addFilesToZip(
                source,
                source.getName(),
                zipOutput,
                includeFiles
            );
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(zipOutput != null) {
                try {
                    zipOutput.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                zipOutput = null;
            }
            if(fileOutput != null) {
                try {
                    fileOutput.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                fileOutput = null;
            }
        }
    }
    /**
     * de-compress files from a zip file 
     * @param zipIn: {@link java.io.InputStream} to read the {@link ZipEntry}
     * @param path: filePath of the zip file
     */
    public void extractZipFiles(ZipInputStream zipIn, String path) {
        FileOutputStream myFileOutputStream = null;
        try {
            myFileOutputStream = new FileOutputStream(path);
            byte[] buffer = new byte[1024];
            int readBytes;
            while((readBytes = zipIn.read(buffer)) != -1) {
                myFileOutputStream.write(buffer, 0, readBytes);
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(myFileOutputStream != null) {
                try {
                    myFileOutputStream.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                myFileOutputStream = null;
            }
        }
    }
    /**
     * create the file destination for the unzipped files
     */
    public void createUnZipFile(String zipFilePath, String directoryPath) {
        try {
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry entry = zipIn.getNextEntry();
            File miFile = null;

            outter: while(entry != null) {
                String filePath = directoryPath + entry.getName();
                String entryParent = new File(filePath).getParent();
                miFile = new File(entryParent);
                if(!miFile.exists()) {
                    System.out.println(
                        String.format(
                            Colors.YELLOW_UNDERLINE +
                            "THE FOLDER STRUCTURE: | %s | ARE NEEDED." + 
                            Colors.RESET,
                            miFile.getPath()
                        )
                    );
                    break outter;
                } else if(!entry.isDirectory()) {
                    extractZipFiles(zipIn, filePath);
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            System.out.println(
                Colors.YELLOW_UNDERLINE + "de-compress operation finished" + Colors.RESET
            );

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * list the zip entries given an option
     * <br> pre: </br> the zip archive has entries
     * @param 
     */
    public void zipEntries(String zipFilePath) {
        ZipInputStream zipIn = null;
        try {
            zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry zEntry = zipIn.getNextEntry();
            if(zEntry == null) {
                System.err.println(
                    "[ ERROR ]: " +
                    Colors.YELLOW_UNDERLINE + "EMPTY ZIP FILE" + Colors.RESET
                );
            } else {
                int count = 1;
                while(zEntry != null) {
                    System.out.println(
                        String.format(
                            "%s: %s",
                            count,
                            Colors.GREEN_UNDERLINE + zEntry.getName() + Colors.RESET
                        )
                    );
                    ++count;
                    zipIn.closeEntry();
                    zEntry = zipIn.getNextEntry();
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(zipIn != null) {
                try {
                    zipIn.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                zipIn = null;
            }
        }
    }
}

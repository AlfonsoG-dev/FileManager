package org.example.operation;

import java.util.List;
import java.util.ArrayList;

public class Operation {
    private FileOperation fileOperation;
    private String[] arguments;
    public Operation(FileOperation fileOperation, String[] arguments) {
        this.fileOperation = fileOperation;
        this.arguments = arguments;
    }
    public Operation(String[] arguments) {
        this.arguments = arguments;
        this.fileOperation = new FileOperation();
    }
    /**
     * Get the index of a particular prefix.
     * <p> the prefix correspond to a command line argument.
     * @param prefix - the command line argument to search its index.
     * @return the index of the prefix, if its not found return -1.
     */
    private int getPrefixIndex(String prefix) {
        for(int i=0; i<arguments.length; ++i) {
            String options = arguments[i];
            if(options.equals(prefix)) {
                return i;
            }
        }
        return -1;
    }
    /**
     * Get the value attach to the prefix.
     * <p> the value is the next argument on the command line.
     * @param prefix - the prefix to search its value.
     * @return the value of the prefix, if not present null.
     */
    private String getPrefixValue(String prefix) {
        int index = getPrefixIndex(prefix);
        if(index == -1) return null;
        if((index+1) >= arguments.length) return null;
        return arguments[index+1];
    }
    /**
     * Create a file given the prefix "--ni path".
     * <p> if more than 1 file is provide, this will create one by one, or will be interrupted if any of them is not a file.
     * <p> in case the user didn't provided a value a warning message its shown to them.
     * <p> this process will interrupt the execution if no value is present.
     */
    public void createFile() {
        String fileURI = getPrefixValue("--ni");
        if(fileURI == null) {
            System.out.println("[Warning] No path provided...");
            return;
        }
        int fileIndex = getPrefixIndex("--ni");
        if(fileIndex == -1) return;
        if((fileIndex+2) == arguments.length) {
            fileOperation.createFile(fileURI);
        } else if(!fileURI.equals(arguments[arguments.length-1])) {
            for(int i=fileIndex+1; i<arguments.length; ++i) {
                fileOperation.createFile(arguments[i]);
            }
        }
    }
    /**
     * Create a directory given the prefix "--md path".
     * <p> if more than 1 directory is provided, this will create one by one, or will be interrupted if any of them is not a file.
     */
    public void createDirectory() {
        String fileURI = getPrefixValue("--md");
        if(fileURI == null) {
            System.out.println("[Warning] No path provided...");
            return;
        }
        int fileIndex = getPrefixIndex("--md");
        if(fileIndex == -1) return;
        if((fileIndex+2) == arguments.length) {
            fileOperation.createDirectory(fileURI);
        } else if(!fileURI.equals(arguments[arguments.length-1])) {
            for(int i=fileIndex+1; i<arguments.length; ++i) {
                fileOperation.createDirectory(arguments[i]);
            }
        }
    }
    /**
     * Deletes a directory given the prefix "--dd path".
     * <p> if the directory is not empty the user needs to provide --r prefix.
     */
    public void deleteDirectory() {
        String fileURI = getPrefixValue("--dd");
        if(fileURI == null) {
            System.out.println("[Warning] No path provided...");
            return;
        }
        int fileIndex = getPrefixIndex("--dd");
        if(fileIndex == -1) return;
        String p = "";
        int permission = getPrefixIndex("--r");
        if(permission != -1) p = "-re";
        if((fileIndex+2) == arguments.length) {
            fileOperation.deleteDirectory(fileURI, p);
        } else if(!fileURI.equals(arguments[arguments.length-1])) {
            for(int i=fileIndex+1; i<arguments.length; ++i) {
                fileOperation.deleteDirectory(arguments[i], p);
            }
        }
    }
    /**
     * Deletes a file given the prefix "--df path"
     */
    public void deleteFile() {
        String fileURI = getPrefixValue("--df");
        if(fileURI == null) {
            System.out.println("[Warning] No path provided...");
            return;
        }
        int fileIndex = getPrefixIndex("--dd");
        if(fileIndex == -1) return;
        if((fileIndex+2) == arguments.length) {
            fileOperation.deleteFile(fileURI);
        } else if(!fileURI.equals(arguments[arguments.length-1])) {
            for(int i=fileIndex+1; i<arguments.length; ++i) {
                fileOperation.deleteFile(arguments[i]);
            }
        }
    }
    public void list() {
        String pathURI = getPrefixValue("--ls");
        int permission = getPrefixIndex("--r");
        if(pathURI == null) {
            System.err.println("[Warning] No path provided...");
            return;
        }
        if(permission != -1) {
            fileOperation.listContent(pathURI, "--r");
        } else {
            fileOperation.listContent(pathURI, "");
        }
    }
    /**
     * Copy files.
     * <p> Copy 1 file to 1 target: readme.md To docs
     * <p> Copy 2 files to 1 target: readme.md example.txt To docs
     * <p> Copy 1 file to 2 targets: readme.md To docs lib
     * <p> Copy 2 files to 2 target: readme.md example.txt To docs lib
     */
    public void copyFiles() {
        String source = getPrefixValue("--cp");
        String target = getPrefixValue("To");
        if(source == null || target == null) {
            System.err.println("[Error] No path provided...");
            return;
        }

        int sourceIndex = getPrefixIndex("--cp");
        int assignIndex = getPrefixIndex("To");
        if(sourceIndex == -1 || assignIndex == -1) {
            System.err.println("[Error] No arguments provided.");
            return;
        }
        // means to copy one file to one target.
        if((sourceIndex+2) == assignIndex && (assignIndex+2) == arguments.length) {
            fileOperation.copyFile(source, target);
        } else if((sourceIndex+2) != assignIndex && (assignIndex+2) == arguments.length) {
            System.out.println("here");
            // means to copy multiple files to one target
            List<String> sources = new ArrayList<>();
            for(int i=sourceIndex+1; i<assignIndex; ++i) {
                sources.add(arguments[i]);
            }
            fileOperation.copyFilesToTarget(sources, target);
        } else if((sourceIndex+2) == assignIndex && (assignIndex+2) < arguments.length) {
            // means to copy one file to multiple targets
            List<String> targets = new ArrayList<>();
            for(int i=assignIndex+1; i<arguments.length; ++i) {
                targets.add(arguments[i]);
            }
            fileOperation.copyFileToTargets(source, targets);
        } else if((sourceIndex+1) != assignIndex && (assignIndex+2) < arguments.length) {
            // means to copy multiple files to multiple targets
            List<String> sources = new ArrayList<>();
            for(int i=sourceIndex+1; i<assignIndex; ++i) {
                sources.add(arguments[i]);
            }
            List<String> targets = new ArrayList<>();
            for(int i=assignIndex+1; i<arguments.length; ++i) {
                targets.add(arguments[i]);
            }
            fileOperation.copyFilesToTargets(sources, targets);
        }
    }
    public void move() {
        // TODO: implement move multiple files to target or targets
        String source = getPrefixValue("--mv");
        String target = getPrefixValue("To");
        if(source == null || target == null) {
            System.err.println("[Error] No path provided...");
            return;
        }
        fileOperation.moveFile(source, target);
    }
    public void listEntries() {
        String fileURI = getPrefixValue("--le");
        if(fileURI == null) {
            System.err.println("[Error] No path provided...");
            return;
        }
        fileOperation.readCompressedFile(fileURI);
    }
}

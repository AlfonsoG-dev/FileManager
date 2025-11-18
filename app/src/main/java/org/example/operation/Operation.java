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
     * Create a file or directory given the prefix "--md path".
     * <p> in case the user didn't provide a value a warning message its shown to them.
     * <p> this process will interrupt the execution if no value is present.
     */
    public void createFile() {
        String fileURI = getPrefixValue("--ni");
        if(fileURI == null) {
            System.out.println("[Warning] No path provided...");
            return;
        }
        fileOperation.createFile(fileURI);
    }
    public void createDirectory() {
        String fileURI = getPrefixValue("--md");
        if(fileURI == null) {
            System.out.println("[Warning] No path provided...");
            return;
        }
        fileOperation.createDirectory(fileURI);
    }
    /**
     * Deletes a directory given the prefix "--dd path".
     * <p> if the directory is not empty the user needs to provide --r prefix.
     */
    public void deleteDirectory() {
        String fileURI = getPrefixValue("--dd");
        int permission = getPrefixIndex("--r");
        if(fileURI == null) {
            System.out.println("[Warning] No path provided...");
            return;
        }
        // TODO: implement the option to delete 1 or more directories.
        if(permission != -1) {
            fileOperation.deleteDirectory(fileURI, "--r");
        } else {
            fileOperation.deleteDirectory(fileURI, "");
        }
    }
    /**
     * Deletes a file given the prefix "--df path"
     */
    public void deleteFile() {
        // TODO: implement the option to delete 1 or more files.
        String fileURI = getPrefixValue("--df");
        if(fileURI == null) {
            System.out.println("[Warning] No path provided...");
            return;
        }
        fileOperation.deleteFile(fileURI);
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
    public void copy() {
        // TODO: test me please.
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

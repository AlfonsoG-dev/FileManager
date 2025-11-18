package org.example.operation;

import java.io.File;

public class Operation {
    private FileOperation fileOperation;
    private String[] arguments;
    public Operation(FileOperation fileOperation, String[] arguments) {
        this.fileOperation = fileOperation;
        this.arguments = arguments;
    }
    public Operation(String[] arguments) {
        this.arguments = arguments;
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
            if(options.contains(prefix)) {
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
    public void create() {
        String fileURI = getPrefixValue("--md");
        if(fileURI == null) {
            System.out.println("[Warning] No path provided...");
            return;
        }
        File f = new File(fileURI);
        if(f.isFile()) {
            fileOperation.createFile(fileURI);
        } else {
            fileOperation.createDirectory(fileURI);
        }
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
}

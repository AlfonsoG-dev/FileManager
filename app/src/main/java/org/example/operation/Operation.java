package org.example.operation;

import org.example.utils.CommandUtils;

import java.io.Console;

public class Operation {
    private static final String NO_PATH_WARNING = "[Warning] No path provided...";
    private static final String NO_ARGS_WARNING = "[Warning] No arguments provided";
    private static Console console = System.console();
    private static final String CONSOLE_FORMAT = "%s%n";

    private FileOperation fileOperation;
    private CommandUtils commandUtils;
    private String[] arguments;

    public Operation(FileOperation fileOperation, String[] arguments) {
        this.fileOperation = fileOperation;
        this.commandUtils = new CommandUtils(arguments);
        this.arguments = arguments;
    }
    public Operation(String[] arguments) {
        this.arguments = arguments;
        this.fileOperation = new FileOperation();
        this.commandUtils = new CommandUtils(arguments);
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
            console.printf(CONSOLE_FORMAT, NO_PATH_WARNING);
            return;
        }
        if(commandUtils.showHelpOnCreateFile()) return;
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
            console.printf(CONSOLE_FORMAT, NO_PATH_WARNING);
            return;
        }
        if(commandUtils.showHelpOnCreateDirectory()) return;
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
            console.printf(CONSOLE_FORMAT, NO_PATH_WARNING);
            return;
        }
        if(commandUtils.showHelpOnDeleteDirectory()) return;
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
     * Deletes a file given the prefix "--df path".
     * <p> If more than one file path to delete is present, it will delete multiple files.
     */
    public void deleteFile() {
        String fileURI = getPrefixValue("--df");
        if(fileURI == null) {
            console.printf(CONSOLE_FORMAT, "[Warning]", NO_PATH_WARNING);
            return;
        }
        if(commandUtils.showHelpOnDeleteFile()) return;
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
    /**
     * List the immediate content of a path unless the user provides --r to recursively list elements.
     */
    public void list() {
        String pathURI = getPrefixValue("--ls");
        int permission = getPrefixIndex("--r");
        if(pathURI == null) {
            console.printf(CONSOLE_FORMAT, "[Warning]", NO_PATH_WARNING);
            return;
        }
        if(commandUtils.showHelpOnList()) return;
        if(permission != -1) {
            fileOperation.listContent(pathURI, "--r");
        } else {
            fileOperation.listContent(pathURI, "");
        }
    }
    /**
     * Copy files.
     * <p> Copy 1 file to 1 target - example: readme.md To docs
     * <p> Copy 2 files to 1 target - example: readme.md example.txt To docs
     * <p> Copy 1 file to 2 targets - example: readme.md To docs lib
     * <p> Copy 2 files to 2 target - example: readme.md example.txt To docs lib
     */
    public void copyFiles() {
        String source = getPrefixValue("--cpf");
        String target = getPrefixValue("To");
        if(commandUtils.showHelpOnCopyFiles()) return;
        if(source == null || target == null) {
            console.printf(CONSOLE_FORMAT, NO_ARGS_WARNING);
            return;
        }

        int sourceIndex = getPrefixIndex("--cpf");
        int assignIndex = getPrefixIndex("To");
        // means to copy one file to one target.
        if(arguments[assignIndex-1].equals(source) || !arguments[assignIndex-1].equals(source) && arguments.length-2 == assignIndex) {
            for(int i=sourceIndex+1; i<assignIndex; ++i) {
                fileOperation.copyFile(arguments[i], target);
            }
        } else if(!arguments[assignIndex-1].equals(source) && arguments.length-2 > assignIndex) {
            for(int i=sourceIndex+1; i<assignIndex; ++i) {
                for(int j=assignIndex+1; j<arguments.length; ++j) {
                    fileOperation.copyFile(arguments[i], arguments[j]);
                }
            }
        }
    }
    /**
     * Helper method to copy from multiple sources to multiple targets.
     */
    private void copyMultipleSourceToTargetsDir(int sourceIndex, int assignIndex, String permission) {
        for(int i=sourceIndex+1; i<assignIndex; ++i) {
            for(int j=assignIndex+1; j<arguments.length-1; ++j) {
                fileOperation.copyDir(arguments[i], arguments[j], permission);
            }
        }
    }
    /**
     * Copy directories.
     * <p> Copy 1 directory to 1 target - example: bin To docs.
     * <p> Copy 2 files to 1 target - example: bin lib To docs.
     * <p> Copy 2 files to 2 targets - example: bin lib to docs other-path.
     */
    public void copyDirs() {
        String prefix = "--cpd";
        String source = getPrefixValue(prefix);
        String target = getPrefixValue("To");
        if(commandUtils.showHelpOnCopyDirectories()) return;
        if(source == null || target == null) {
            console.printf(CONSOLE_FORMAT, NO_PATH_WARNING);
            return;
        }
        // fix: only one or multiple source to one target copies are allowed.
        int sourceIndex = getPrefixIndex(prefix);
        int assignIndex = getPrefixIndex("To");
        String permission = getPrefixIndex("--r") != -1 ? "--r":"";
        boolean targetCondition = (arguments.length-2 == assignIndex || arguments.length-3 == assignIndex);

        // First from one source to one target
        if(arguments[assignIndex-1].equals(source) || !arguments[assignIndex-1].equals(source) && targetCondition) {
            for(int i=sourceIndex+1; i<assignIndex; ++i) {
                fileOperation.copyDir(arguments[i], target, permission);
            }
        } else if(!arguments[assignIndex-1].equals(source) && !targetCondition) {
            copyMultipleSourceToTargetsDir(sourceIndex, assignIndex, permission);
        }
    }
    /**
     * Move files.
     * <p> Move 1 file to 1 target - example: readme.md To docs.
     * <p> Move 2 files to 1 target - example: readme.md example.txt To docs.
     */
    public void moveFile() {
        String prefix = "--mvf";
        String source = getPrefixValue(prefix);
        if(commandUtils.showHelpOnMoveFile()) return;
        String target = getPrefixValue("To");
        if(source == null || target == null) {
            console.printf(CONSOLE_FORMAT, NO_PATH_WARNING);
            return;
        }
        int sourceIndex = getPrefixIndex(prefix);
        int assignIndex = getPrefixIndex("To");
        // move one file to one target or multiple sources to 1 target.
        if(arguments[assignIndex-1].equals(source) || !arguments[assignIndex-1].equals(source) && arguments.length-2 == assignIndex) {
            for(int i=sourceIndex+1; i<assignIndex; ++i) {
                fileOperation.moveFile(arguments[i], target);
            }
        } else {
            console.printf(CONSOLE_FORMAT, "[Warning] Can't move file to multiple targets");
        }
    }
    /**
     * Move directories.
     * <p> Move 1 directory to 1 target - example: bin To docs.
     * <p> Move 2 directories to 1 target - example: bin lib To docs.
     */
    public void moveDirs() {
        String prefix = "--mvd";
        String source = getPrefixValue(prefix);
        if(commandUtils.showHelpOnMoveDirectory()) return;
        String target = getPrefixValue("To");
        if(source == null || target == null) {
            console.printf(CONSOLE_FORMAT, NO_PATH_WARNING);
            return;
        }

        int p = getPrefixIndex("--r");
        String permission = "";
        if(p != -1) permission = "--r";

        int sourceIndex = getPrefixIndex(prefix);
        int assignIndex = getPrefixIndex("To");
        boolean targetCondition = (arguments.length-2 == assignIndex || arguments.length-3 == assignIndex);
        // move one file to one target
        if(arguments[assignIndex-1].equals(source) || !arguments[assignIndex-1].equals(source) && targetCondition) {
            for(int i=sourceIndex+1; i<assignIndex; ++i) {
                fileOperation.moveDir(arguments[i], target, permission);
            }
        } else {
            console.printf(CONSOLE_FORMAT, "[Warning] Can't move directory into multiple targets.");
        }
    }
    /**
     * If you provide a path to a compressed file it will show the file entries.
     * <p> A file entries are the compressed content inside a zip file.
     */
    public void listEntries() {
        String fileURI = getPrefixValue("--le");
        if(fileURI == null) {
            console.printf(CONSOLE_FORMAT, NO_PATH_WARNING);
            return;
        }
        if(commandUtils.showHelpOnListEntries()) return;
        fileOperation.readCompressedFile(fileURI);
    }
    /**
     * Make a compressed file with a specific content given the path.
     * <p> You can compress one source to a single target - example: src To folder.zip.
     */
    public void compress() {
        String sourceURI = getPrefixValue("--cm");
        if(commandUtils.showHelpOnCompress()) return;
        String targetURI = getPrefixValue("To");
        if(sourceURI == null || targetURI == null) {
            console.printf(CONSOLE_FORMAT, NO_PATH_WARNING);
            return;
        }
        if(arguments.length > 5) return;

        // recursively compress files command
        int r = getPrefixIndex("--r");
        String permission = "";
        if(r != -1) {
            permission = "--r";
        }

        fileOperation.compressPath(sourceURI, targetURI, permission);
    }
    /**
     * De-compressed a file into a directory.
     * <p> You can de-compress one source to a single target - example: folder.zip.
     */
    public void deCompress() {
        String sourceURI = getPrefixValue("--dcm");
        if(commandUtils.showHelpOnDeCompress()) return;
        String targetURI = getPrefixValue("To");
        if(sourceURI == null || targetURI == null) {
            console.printf(CONSOLE_FORMAT, NO_PATH_WARNING);
            return;
        }
        int assignIndex = getPrefixIndex("To");
        if(arguments[assignIndex-1].equals(sourceURI) && arguments.length-2 == assignIndex) {
            fileOperation.deCompressFile(sourceURI, targetURI);
        } else if(arguments[assignIndex-1].equals(sourceURI) && arguments.length-2 > assignIndex) {
            for(int i=assignIndex+1; i<arguments.length; ++i) {
                fileOperation.deCompressFile(sourceURI, arguments[i]);
            }
        }
    }
    /**
     * Print a file lines given a path.
     * <p> If you provide more than one file it will print the all the lines separated by file path.
     */
    public void printLines() {
        String fileURI = getPrefixValue("--rl");
        if(fileURI == null) {
            console.printf(CONSOLE_FORMAT, NO_PATH_WARNING);
            return;
        }
        if(commandUtils.showHelpOnPrintLines()) return;
        int fileIndex = getPrefixIndex("--rl");
        if(fileIndex == -1) return;
        // read one file
        if(fileURI.equals(arguments[arguments.length-1])) {
            fileOperation.printFileLines(fileURI);
        } else {
            for(int i=fileIndex+1; i<arguments.length; ++i) {
                console.printf("%s%n", "[Info] File: " + arguments[i]);
                fileOperation.printFileLines(arguments[i]);
                console.printf("%s%n", "");
            }
        }
    }
    /**
     * Print the file lines that are in range.
     * <p> The range variate and can be from 0 to 1 - example: --rl execute.ps1 0:4
     * <p> If the range ends with 0 it will mean that the lines to print are all - example: --rl execute 0:0
     */
    public void printLinesInRange() {
        String fileURI = getPrefixValue("--rlr");
        if(fileURI == null) {
            console.printf(CONSOLE_FORMAT, NO_PATH_WARNING);
            return;
        }
        if(commandUtils.showHelpOnPrintLinesInRange()) return;
        // read one file
        if(fileURI.equals(arguments[arguments.length-2]) && arguments[arguments.length-1].contains(":")) {
            String range = arguments[arguments.length-1];
            int start = Integer.parseInt(range.split(":")[0]);
            int stop = Integer.parseInt(range.split(":")[1]);
            fileOperation.printFileLines(fileURI, start, stop);
        }
    }
    /**
     * Search for a word inside a file.
     * <p> For now you have to provide the relative path of that file.
     * <p> The search its ignore-case search.
     * <p> If you provide more than 1 file the word will be searched inside those files. The result will be separated by end of line.
     */
    public void searchWordInFile() {
        String word = getPrefixValue("--sf");
        if(word == null && arguments.length < 2) {
            console.printf(CONSOLE_FORMAT, "[Error] No word or file provided to search");
            return;
        }
        if(commandUtils.showHelpOnSearchWordInFile()) return;
        int wordIndex = getPrefixIndex("--sf");
        if(arguments.length < 3) {
            fileOperation.searchWordInFile(arguments[arguments.length-1], word);
        } else if(arguments.length >= 3) {
            for(int i=wordIndex+2; i<arguments.length; ++i) {
                fileOperation.searchWordInFile(arguments[i], word);
                console.printf(CONSOLE_FORMAT, "");
            }
        }
    }
    /**
     * Search for a word inside a directory.
     * <p> For now you have to provide the relative path of that directory.
     * <p> The search its ignore-case search.
     */
    public void searchWordInDirectory() {
        String word = getPrefixValue("--sd");
        if(word == null && arguments.length < 2) {
            console.printf(CONSOLE_FORMAT, NO_PATH_WARNING);
            return;
        }
        if(commandUtils.showHelpOnSearchWordInDirectory()) return;
        int permission = getPrefixIndex("--r");
        permission = permission != -1 ? 0:1;

        int wordIndex = getPrefixIndex("--sd");
        if((wordIndex+2) < arguments.length) {
            fileOperation.searchWordInDirectory(arguments[wordIndex+2], word, permission);
        }
    }
}

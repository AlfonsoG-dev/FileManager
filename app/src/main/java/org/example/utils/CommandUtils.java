package org.example.utils;

import java.io.Console;
import java.io.File;

public class CommandUtils {
    private String[] args;
    private static Console console = System.console(); 
    private static final String CONSOLE_FORMAT = "%s%n";

    public CommandUtils(String[] args) {
        this.args = args;
    }
    private String getPrefixValue(String prefix) {
        if(prefix.isBlank()) return null;
        for(int i=0; i<args.length; ++i) {
            if(args[i].equals(prefix) && (i+1) < args.length) {
                return args[i+1];
            }
        }
        return null;
    }
    public boolean showHelpOnCreateFile() {
        String prefix = "--ni";
        String value = getPrefixValue(prefix);
        if(value == null || !value.equals("--h")) return false;

        StringBuilder help = new StringBuilder();
        help.append("Use [--ni new.txt] to create a new empty file.");
        help.append("\n\t");
        help.append("If you want to create multiple files at the same time:");
        help.append("\n\t");
        help.append(" => Use [--ni new.txt other.txt] to create the files.");
        help.append("\n\t");
        help.append("If you want to create a file in a nested structure:");
        help.append("\n\t");
        help.append(" => Use [--ni custom");
        help.append(File.separator);
        help.append("new.txt] to create the file in the nested structure.");
        console.printf(CONSOLE_FORMAT, help);

        return true;
    }

    public boolean showHelpOnCreateDirectory() {
        String prefix = "--md";
        String value = getPrefixValue(prefix);
        if(value == null || !value.equals("--h")) return false;

        StringBuilder help = new StringBuilder();
        help.append("Use [--md new-path] to create a new empty directory.");
        help.append("\n\t");
        help.append("If you want to create multiple directories at the same time:");
        help.append("\n\t");
        help.append(" => Use [--md new-path other-path] to create the directories.");
        help.append("\n\t");
        help.append("If you want to create a directory in a nested structure:");
        help.append("\n\t");
        help.append(" => Use [--md custom");
        help.append(File.separator);
        help.append("new-path] to create the directory in the nested structure.");
        console.printf(CONSOLE_FORMAT, help);

        return true;
    }
    public boolean showHelpOnDeleteDirectory() {
        String prefix = "--dd";
        String value = getPrefixValue(prefix);
        if(value == null || !value.equals("--h")) return false;

        StringBuilder help = new StringBuilder();
        help.append("Use [--dd path] to delete an empty directory.");
        help.append("\n\t");
        help.append("If you want to delete a directory that isn't empty:");
        help.append("\n\t");
        help.append(" => Use [--dd path --r] to delete the directory and its content.");
        help.append("\n\t");
        help.append("If you want to delete multiple directories:");
        help.append("\n\t");
        help.append(" => Use [--dd path other-path --r] to delete the directories and they're content.");
        console.printf(CONSOLE_FORMAT, help);

        return true;
    }

    public boolean showHelpOnDeleteFile() {
        String prefix = "--df";
        String value = getPrefixValue(prefix);
        if(value == null || !value.equals("--h")) return false;

        StringBuilder help = new StringBuilder();
        help.append("Use [--df path.txt] to delete an empty file.");
        help.append("\n\t");
        help.append("If you want to delete multiple files:");
        help.append("\n\t");
        help.append(" => Use [--df path.txt other-path.txt] to delete the files.");
        console.printf(CONSOLE_FORMAT, help);

        return true;
    }
    public boolean showHelpOnList() {
        String prefix = "--ls";
        String value = getPrefixValue(prefix);
        if(value == null || !value.equals("--h")) return false;

        StringBuilder help = new StringBuilder();
        help.append("Use [--ls path] to list the immediate content.");
        help.append("\n\t");
        help.append("If you want to see the total items on the path:");
        help.append("\n\t");
        help.append(" => Use [--ls path --r] to recursively list the items.");
        console.printf(CONSOLE_FORMAT, help);

        return true;
    }

    public boolean showHelpOnCopyFiles() {
        String prefix = "--cpf";
        String value = getPrefixValue(prefix);
        if(value == null || !value.equals("--h")) return false;

        StringBuilder help = new StringBuilder();
        help.append("Use [--cpf path.txt To other-path] to copy the file into the destination.");
        help.append("\n\t");
        help.append("If you want to copy multiple files into one destination:");
        help.append("\n\t");
        help.append(" => Use [--cpf path.txt other-file.txt To other-path] to copy the files into the destination.");
        help.append("\n\t");
        help.append("If you want to copy a file into multiple destinations:");
        help.append("\n\t");
        help.append(" => Use [--cpf path.txt To other-file other-path] to copy the file into the destinations.");
        help.append("\n\t");
        help.append("If you want to copy multiple files into multiple destinations:");
        help.append("\n\t");
        help.append(" => Use [--cpf path.txt path2.txt To other-file other-path] to copy the files into the destinations.");
        console.printf(CONSOLE_FORMAT, help);

        return true;
}
    }

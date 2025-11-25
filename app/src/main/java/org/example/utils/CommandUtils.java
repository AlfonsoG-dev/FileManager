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
        StringBuilder help = new StringBuilder();
        if(value != null && !value.equals("--h")) return false;

        help.append("Use --ni new.txt to create a new empty file.");
        help.append("\n\t");
        help.append("If you want to create multiple files at the same time");
        help.append("\n");
        help.append(" => Use --ni new.txt other.txt to create the files.");
        help.append("\n\t");
        help.append("If you want to create a file in a nested structure");
        help.append("\n");
        help.append(" => Use --ni custom");
        help.append(File.separator);
        help.append("new.txt to create the file in the nested structure.");
        console.printf(CONSOLE_FORMAT, help);

        return true;
    }

    public boolean showHelpOnCreateDirectory() {
        String prefix = "--md";
        String value = getPrefixValue(prefix);
        StringBuilder help = new StringBuilder();
        if(value != null && !value.equals("--h")) return false;

        help.append("Use --md new-path to create a new empty directory.");
        help.append("\n\t");
        help.append("If you want to create multiple directories at the same time");
        help.append("\n");
        help.append(" => Use --md new-path other-path to create the directories.");
        help.append("\n\t");
        help.append("If you want to create a directory in a nested structure");
        help.append("\n");
        help.append(" => Use --md custom");
        help.append(File.separator);
        help.append("new-path to create the directory in the nested structure.");
        console.printf(CONSOLE_FORMAT, help);

        return true;
    }
}

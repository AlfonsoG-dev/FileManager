package application.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;

public record TextUtils() {

    public static String getFileLines(String fileURI) {
        StringBuilder lines = new StringBuilder();
        try(BufferedReader buffer = new BufferedReader(new FileReader(new File(fileURI)))) {
            String l;
            while((l = buffer.readLine()) != null) {
                lines.append(l);
                lines.append("\n");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return lines.toString();
    }
    public static void writeLines(String fileURI, String lines, boolean append) {
        if(lines.isBlank()) return; 
        try(FileWriter writer = new FileWriter(new File(fileURI), append)) {
            writer.write(lines);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}

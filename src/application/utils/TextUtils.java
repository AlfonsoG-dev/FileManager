package application.utils;

import java.util.stream.Stream;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

public record TextUtils() {

    /**
     * statically get a file content as String line.
     * @param fileURI - the file to read its content.
     * @return the file content.
     */
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
    /**
     * A stream with the file content to be consumed.
     * <p> use this with a try-resource to close on end.
     * @param fileURI - the file to get its content.
     * @return a stream with the file content.
     */
    public static Stream<String> getLazilyFileLines(String fileURI) {
        try {
            return Files.lines(Paths.get(fileURI));
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * statically write a file with the provided lines.
     * @param fileURI - the file to write the lines.
     * @param lines - the lines to write.
     * @param append - if you want to re-write or append to the existent content.
     */
    public static void writeLines(String fileURI, String lines, boolean append) {
        if(lines.isBlank()) return; 
        try(FileWriter writer = new FileWriter(new File(fileURI), append)) {
            writer.write(lines);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}

package application.operation;

import application.utils.*;

import java.util.stream.Stream;
import java.util.List;

import java.io.File;

public class FileOperation {

    private FileUtils fileUtils;
    private TextUtils textUtils;

    public FileOperation(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
        this.textUtils = textUtils;
    }
    public FileOperation() {
        fileUtils = new FileUtils();
        textUtils = new TextUtils();
    }

    public void sarchWordInFile(String fileURI, String word) {
        File f = new File(fileURI);
        if(!f.isFile()) return;
        try(Stream<String> fileLines = TextUtils.getLazilyFileLines(fileURI)) {
            List<String> lines = fileLines.toList();
            for(String l: lines) {
                int lineNumber = TextUtils.getLineNumber(fileURI, l);
                if(textUtils.lineContainsWord(l, word)) {
                    System.out.println(String.format("%d%s", lineNumber, l));
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}

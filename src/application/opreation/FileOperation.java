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
            for(int i=0; i<lines.size(); ++i) {
                String l = lines.get(i);
                if(textUtils.lineContainsWord(l, word)) {
                    int lineNumber = i;
                    System.out.println(String.format("%s:%d\t\t%s",fileURI, ++lineNumber, l));
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}

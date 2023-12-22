package org.kopacz;


import lombok.Getter;
import lombok.SneakyThrows;
import org.kopacz.exceptions.InvalidStringContainerValueException;
import org.kopacz.exceptions.DuplicatedElementOnListException;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class StringContainer {
    private UUID file_id = UUID.randomUUID();
    private String pattern;
    private Boolean duplicatedNotAllowed = false;
    private LocalDateTime timeOfAdding;

    public StringContainer(String pattern) {
        this.pattern = pattern;
    }

    public StringContainer(String pattern, Boolean duplicatedNotAllowed) {
        this.pattern = pattern;
        this.duplicatedNotAllowed = duplicatedNotAllowed;
    }

    public void add(String string){

        checkIfStringIsExist(string);

        Pattern actualPattern = Pattern.compile(this.pattern);
        Matcher matcher = actualPattern.matcher(string);

        if(!matcher.find()){
            throw new InvalidStringContainerValueException(string);
        }

        this.timeOfAdding = LocalDateTime.now();

        String path = String.valueOf(file_id) + ".txt";
        String datePath = "date-" + String.valueOf(file_id) + ".txt";
        File file = new File(path);
        File dateFile = new File(datePath);
        try (PrintWriter pw = new PrintWriter(new FileWriter(file, true));
             PrintWriter pwd = new PrintWriter(new FileWriter(dateFile, true))) {
            pw.write(string + "\n");
            pwd.write(string + " " + this.timeOfAdding + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String get(int index){
        String path = String.valueOf(file_id) + ".txt";
        try (BufferedReader bf = new BufferedReader(new FileReader(path))) {

            int currentI = 0;
            String line;

            do {
                line = bf.readLine();
            }
            while (index != currentI++);

            if(Objects.isNull(line)){
                throw new IndexOutOfBoundsException();
            }

            return line;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer size(){
        String path = String.valueOf(file_id) + ".txt";
        try (BufferedReader bf = new BufferedReader(new FileReader(path))) {

            int size = 0;

            while (bf.readLine() != null){
                size++;
            }

            return size;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkIfStringIsExist(String string) {
        String path = String.valueOf(file_id) + ".txt";

        File file = new File(path);

        if(duplicatedNotAllowed && file.exists()){
            for(int i=0; i < size(); i++){
                if(get(i).equals(string)){
                    throw new DuplicatedElementOnListException();
                }
            }
        }
    }

    public void remove(int index){

        if(size() <= index){
            throw new IndexOutOfBoundsException();
        }

        String path = String.valueOf(file_id) + ".txt";
        File inputFile = new File(path);
        File tempFile = new File("myTemp" + path);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String lineToRemove;
            String currentLine;

            int numberOfCurrentLine = 0;

            while((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                if(numberOfCurrentLine == index) {
                    numberOfCurrentLine++;
                    continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }

    public void remove(String string) {
        String path = String.valueOf(file_id) + ".txt";
        File inputFile = new File(path);
        File tempFile = new File("myTemp" + path);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String lineToRemove = string;
            String currentLine;

            int isExist = 0;

            while((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                if(trimmedLine.equals(lineToRemove)) {
                    isExist++;
                    continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }

            if(isExist == 0){
                throw new IllegalArgumentException();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }

    public void getDataBetween(LocalDateTime dateFrom, LocalDateTime dateTo){
        String path = "date-" + String.valueOf(file_id) + ".txt";
        try (BufferedReader bf = new BufferedReader(new FileReader(path))) {


            String line;

            while((line = bf.readLine())!= null){
                String[] actualSplitLine = line.split(" ");
                LocalDateTime localDateTime = LocalDateTime.parse(actualSplitLine[1]);
                if(localDateTime.isAfter(dateFrom) && localDateTime.isBefore(dateTo)){
                    System.out.println(actualSplitLine[0]);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void storeToFile(String name){
        String path = String.valueOf(file_id) + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(path));
             PrintWriter writer = new PrintWriter(new FileWriter(name, true))) {

            String line;
            while((line = reader.readLine()) != null) {
                writer.write(line + "\n");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void fromFile(String name) {
        String path = String.valueOf(file_id) + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(name));
             PrintWriter writer = new PrintWriter(new FileWriter(path, true))) {

            String line;
            while((line = reader.readLine()) != null) {
                writer.write(line + "\n");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

package com.company;

import java.util.ArrayList;
import java.nio.file.*;
import java.util.*;
import java.io.*;


public class Parser {
    String commandName;
    String[] args;
    ArrayList<String> commands = new ArrayList<String>();

    public Parser() {
        commands.add("echo");
        commands.add("pwd");
        commands.add("cd");
        commands.add("ls");
        commands.add("ls -r");
        commands.add("mkdir");
        commands.add("rmdir");
        commands.add("touch");
        commands.add("cp");
        commands.add("cp -r");
        commands.add("rm");
        commands.add("cat");
        commands.add(">");
        commands.add(">>");
        commands.add("exit");
    }

    public boolean parse(String input) {
        if(!input.contains(" ")||input.endsWith("-r")){
            input=input+" ";
        }
        if(input.contains("-r")){
            commandName=input.substring(0,input.indexOf("-r")+2);
            input=input.substring(input.indexOf("-r")+3,input.length());
            args=input.split(" ");
        }
        else{
            commandName=input.substring(0,input.indexOf(" "));
            input=input.substring(input.indexOf(" ")+1);
            args=input.split(" ");
        }
        if (commands.contains(commandName)) {
            return true;
        } else {
            return false;
        }
    }

    public String getCommandName(){
        return commandName;
    }
    public String[] getArgs(){
        return args;
    }
}

public class Terminal {
    static Parser parser;
    static String dir = System.getProperty("user.dir");
    static String dir2 = System.getProperty("user.home");
    static Path path;

    public Terminal() {
        parser = new Parser();
    }

    public void echo(String[] args) {
        System.out.println(args[0]);
    }

    public void pwd() {
        System.out.println(dir);
    }

    public void cd(String[] args) throws IOException {
        if (args[0].equals("..")) {
            File file = new File(dir);
            String cPath = file.getAbsoluteFile().getParent();
            System.setProperty("user.dir", cPath);
            dir = System.getProperty("user.dir");
            System.out.println(dir);

        } else {
            path = Paths.get(args[0]);
            if (args[0].equals("")) {
                path = Paths.get(dir2);
                System.setProperty("user.dir", path.toString());
                dir = System.getProperty("user.dir");
            } else if (path.toString().contains("\\")) {
                if (Files.exists(path)) {
                    System.setProperty("user.dir", path.toString());
                } else {
                    System.out.println("Entered path not exist");
                }
            } else {
                path = Paths.get(dir + "\\" + args[0]);
                if (Files.exists(path)) {
                    System.setProperty("user.dir", path.toString());
                } else {
                    System.out.println("Entered path not exist");
                }
            }
            dir = System.getProperty("user.dir");
            System.out.println(dir);
        }
    }

    public void ls() {
        File file = new File(dir);
        String[] filesInDir = file.list();
        for (int i = 0; i < filesInDir.length; i++) {
            System.out.println(filesInDir[i]);
        }
    }

    public void ls_r() {
        File file = new File(dir);
        String[] filesInDir = file.list();
        for (int i = filesInDir.length - 1; i > -1; i--) {
            System.out.println(filesInDir[i]);
        }
    }

    public void touch(String[] args) throws IOException {
            if (args[0].contains("\\")) {
                String parentPath = args[0].substring(0, args[0].lastIndexOf("\\"));
                path = Paths.get(parentPath);
                if (Files.exists(path)) {
                    File file = new File(args[0]);
                    file.createNewFile();
                } else {
                    System.out.println("Entered path not exist");
                }
            } else {

                File file = new File(dir + "\\" + args[0]);
                file.createNewFile();
            }
        }

    public void mkdir(String[] args) throws IOException {
        if (args[0].length() > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].contains("\\")) {
                    String parentPath = args[i].substring(0, args[0].lastIndexOf("\\"));
                    path = Paths.get(parentPath);
                    if (Files.exists(path)) {
                        File file = new File(args[i]);
                        file.mkdir();
                    } else {
                        System.out.println("Entered path not exist");
                    }
                } else {
                    File file = new File(dir + "\\" + args[i]);
                    file.mkdir();
                }
            }
        } else {
            System.out.println("Please enter a path (Full or Short)");
        }
    }

    public void cp(String[] args, boolean append) throws IOException {

        try {
            String f = args[0];
            String f1 = args[1];
            if (!args[0].contains("\\")) {
                f = dir + "\\" + args[0];

            }
            if (!args[0].contains(".txt")) {
                f = f + ".txt";
            }
            if (!args[1].contains("\\")) {
                f1 = dir + "\\" + args[1];

            }
            if (!args[1].contains(".txt")) {
                f1 = f1 + ".txt";
            }
            File f2 = new File(f);
            File f3 = new File(f1);
            Scanner sourceFile = new Scanner(f2);
            FileWriter destFile = new FileWriter(f3, append);
            String data = sourceFile.nextLine();
            destFile.write(data);
            sourceFile.close();
            destFile.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void cat(String[] args) throws IOException {
        if (args.length == 2) {
            cp(args, true);
        }
        String f = args[1];
        if (!args[1].contains("\\")) {
            f = dir + "\\" + args[1];

        }
        if (!args[1].contains(".txt")) {
            f = f + ".txt";
        }
        File f1 = new File(f);
        Scanner fileRead = new Scanner(f1);
        String s = fileRead.nextLine();
        System.out.println(s);
    }

    private void cp_r(String source, String destination) throws IOException {
        Path p1 = Paths.get(source);
        Path p2 = Paths.get(destination);
        File file1 = new File(source);
        File file2 = new File(destination);
        if (!p1.isAbsolute() && !p2.isAbsolute()) {
            file1 = new File(file1.getAbsolutePath());
            file2 = new File(file2.getAbsolutePath());
            source = file1.getAbsolutePath().toString();
            destination = file2.getAbsolutePath().toString();
        } else if (!p1.isAbsolute()) {
            file1 = new File(file1.getAbsolutePath());
            source = file1.getAbsolutePath().toString();
        } else if (!p2.isAbsolute()) {
            file2 = new File(file2.getAbsolutePath());
            destination = file2.getAbsolutePath().toString();
        }
        if (file1.exists() && file2.exists()) {
            String[] temp = file1.list();
            // destination=getClass().getResource(source).getPath();
            destination = destination + source.substring(source.lastIndexOf("\\"), source.length());
            File file3 = new File(destination);
            file3.mkdir();
            if (temp.length != 0) {
                for (int i = 0; i < temp.length; i++) {
                    file3 = new File(source + "\\" + temp[i]);
                    if (file3.isDirectory()) {
                        file3 = new File(destination + "\\" + temp[i]);
                        if (!file3.exists()) {
                            cp_r(source + "\\" + temp[i], destination);
                        }
                    } else {
                        Files.copy(Paths.get(source + "\\" + temp[i]), Paths.get(destination + "\\" + temp[i]));

                    }
                }
            }
        } else {
            System.out.println("Paths do not exists");
        }
    }

    public void rmdir(String[] args) throws IOException {
        if (args[0].equals("*")) {
            File file = new File(dir);
            String[] filesInDir = file.list();
            for (int i = 0; i < filesInDir.length; i++) {
                Path p = Paths.get(dir + "\\" + filesInDir[i]);
                if (Files.isDirectory(p)) {
                    if (!Files.list(p).findAny().isPresent()) {
                        Files.delete(p);
                    }
                }

            }

        } else if (args[0].contains("\\")) {
            Path p = Paths.get(args[0]);
            if (Files.list(p).findAny().isEmpty()) {
                Files.deleteIfExists(p);
            } else {
                System.out.println("Directory is not empty");
            }
        } else {
            Path p = Paths.get(dir + "\\" + args[0]);
            if (!Files.list(p).findAny().isPresent()) {
                Files.deleteIfExists(p);
            } else {
                System.out.println("Directory is not empty");
            }
        }
    }

    public void rm(String[] args) throws IOException {
        path = Paths.get(args[0]);
        if (Files.exists(path)) {
            Files.deleteIfExists(path);
        } else {
            System.out.println("File do not exists");
        }
    }

    public void runTerminal(String input) throws IOException {
        parser.parse(input);
        if (!parser.parse(input)) {
            System.out.println("Command not found");

        } else {
            chooseCommandAction();
        }
    }

    public void chooseCommandAction() throws IOException {
        switch (parser.getCommandName()) {
            case "echo" -> echo(parser.getArgs());
            case "pwd" -> pwd();
            case "cd" -> cd(parser.getArgs());
            case "ls" -> ls();
            case "ls -r" -> ls_r();
            case "touch" -> touch(parser.getArgs());
            case "mkdir" -> mkdir(parser.getArgs());
            case "cp" -> cp(parser.getArgs(), false);
            case "cp -r" -> cp_r(parser.getArgs()[0], parser.getArgs()[1]);
            case "rm" -> rm(parser.getArgs());
            case "rmdir" -> rmdir(parser.getArgs());
            case "cat" -> {
                cat(parser.getArgs());
                System.out.println("The file is copied to newFile.");
            }
        }

    }


    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        String userInput = "";
        while (!userInput.equals("exit")) {
            userInput = in.nextLine();
            Terminal terminal = new Terminal();
            terminal.runTerminal(userInput);
        }
        in.close();
    }

}
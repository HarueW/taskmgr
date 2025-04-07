package org.example.CommandClasses;
import org.example.CommandClasses.add;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.regex.Pattern;

public class update {
    public static String[] currentArguments;
    public static String syntax = "taskmgr update {id} {parameter <name>|<description>|<status>} {new_value}";
    static {
        currentArguments = new String[2];
    }

    public static String description = "Update Task";

    public static void update() throws FileNotFoundException {
        String regex = "\\b(name|id|description|status)\\b";
        Pattern pattern = Pattern.compile(regex);
        try {
            if(pattern.matcher(currentArguments[2]).find()) {
                String fileContent = add.readFile(add.FileDir);
                JSONArray existingTasks = new JSONArray(fileContent);
                JSONObject oldtarget = existingTasks.getJSONObject(Integer.parseInt(currentArguments[1]));
                JSONObject target = existingTasks.getJSONObject(Integer.parseInt(currentArguments[1]));
                target.put(currentArguments[2], currentArguments[3]);
                try(FileWriter fw = new FileWriter(add.FileDir)) {
                    fw.write(existingTasks.toString(4));
                }
            } else {
                System.out.println("[ERROR] The parameter you provided is incorrect");
                System.out.println(syntax);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid Argument, Please try again and provide a number next time.");
            System.out.println(syntax);
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found, Please try again with the add command.");
            System.out.println(syntax);
        } catch (Exception e) {
            System.out.println("Something went wrong " + e.getMessage());
            System.out.println(syntax);
        }
    }
}

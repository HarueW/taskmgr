package org.example.CommandClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class add {
    public static String currentArguments[];
    public static String syntax = "taskmgr add {name} {description} [status]";
    public static int fileCreated;
    static {

        currentArguments = new String[2];
    }
    public static int FileExists;
    public static String FileDir = System.getProperty("user.dir") + "/tasks.json";
    public static String description = "to manage the application tasks";
    public static String readFile(String FileDir) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader(FileDir));
        StringBuilder sb = new StringBuilder();
        while(in.hasNext()) {
            sb.append(in.next());
        }
        in.close();
        return sb.toString();
    }
    public static JSONObject addtask(String taskName, String taskDesc, String status) throws IOException {
        // 1. Create and POPULATE the new task object FIRST
        int id = 0;
        JSONObject task = new JSONObject();
        task.put("name", taskName);
        task.put("description", taskDesc);
        if(status != null) {
            task.put("status", status);
        }
        JSONArray jsonArray; // This will hold the final list to write

        // System.out.println(System.getProperty("user.dir")); // Optional debug line
        File config = new File(FileDir);

        if(!config.exists()) {
            System.out.println("File does not exist, Recreating one..");
            config.createNewFile();
            System.out.println("File is created.");
            fileCreated = 1;
            task.put("id", id);
            jsonArray = new JSONArray(); // Start with an empty array
            jsonArray.put(task);         // Add the already populated task
            // System.out.println("First task added: " + task.toString()); // Optional debug line
        } else {

            System.out.println("[LOADED] Task file");
            String fileContent = readFile(FileDir);
            // System.out.println("File content read: " + fileContent); // Optional debug line
            JSONArray existingTasks;
            try {
                // Ensure content isn't effectively empty before parsing
                if (fileContent == null || fileContent.trim().isEmpty() || fileContent.trim().equals("[]")) {
                    existingTasks = new JSONArray();
                } else {
                    existingTasks = new JSONArray(fileContent);
                }
            } catch (JSONException e) {
                System.err.println("Warning: tasks.json was invalid or corrupted. Starting with a new list.");
                existingTasks = new JSONArray(); // On failure, use an empty array
            }
            task.put("id", existingTasks.length());
            existingTasks.put(task);             // Add the already populated new task

            jsonArray = existingTasks;           // Assign the updated list to jsonArray
        }

        // Write the final jsonArray back to the file
        try (FileWriter fw = new FileWriter(config)) { // Use try-with-resources
            fw.write(jsonArray.toString(4)); // Pretty print
        }
        System.out.println("Task added/list updated."); // This 'task' now has name/desc

        return task; // Return the populated task object
    }
    public static void add() throws IOException {
        String status = null;
        try {
            String regex = "\\b(done|true|false|undone|inprogress)\\b";
            Pattern pattern = Pattern.compile(regex);
            if(currentArguments.length > 3 && pattern.matcher(currentArguments[3]).find()) {
                status = currentArguments[3];
            }
            JSONObject task = addtask(currentArguments[1], currentArguments[2], status);
            System.out.println("Task " + task.getString("name"));
        } catch(IndexOutOfBoundsException e) {
            System.out.println("Some required parameters are missing. Please try again.");
            System.out.println(syntax);
        }
    }
}
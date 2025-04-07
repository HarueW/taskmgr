package org.example.CommandClasses;
import org.example.CommandClasses.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;

public class delete {
    public static String[] currentArguments;
    static {
        currentArguments = new String[2];
    }
    public static String description = "Deletes a task by ID";
    public static String syntax = "delete {id}";
    public static void delete() {
        try {
            int SUCCESS = 0;
            JSONArray existingTasks = new JSONArray(add.readFile(add.FileDir));
            int id = Integer.parseInt(currentArguments[1]);
            for (int i = 0; i < existingTasks.length(); i++) {
                JSONObject task = existingTasks.optJSONObject(i); // Use optJSONObject for safety
                if(task.getInt("id") == id) {
                    System.out.printf("Task %s deleted.[ID=%d]", task.getString("name"), task.getInt("id"));
                    existingTasks.remove(i);
                    SUCCESS = 1;
                }
            }
            if(SUCCESS == 1) {
                try(FileWriter fw = new FileWriter(add.FileDir)) {
                    fw.write(existingTasks.toString(4));
                    fw.close();
                }
            } else {
                System.out.println("Task with ID " + id + " not deleted.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(syntax);
        }
    }
}

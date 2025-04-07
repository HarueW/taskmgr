package org.example.CommandClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.regex.Pattern;

import static org.example.initiate.Initiate.Syntaxes;

public class list {
    public static String[] currentArguments;
    public static String description = "Lists all the tasks in a task list.";
    public static String syntax = "taskmgr list [done|inprogress]";
    public static void listTasks(JSONArray ExistingTasks, String StatusType) throws FileNotFoundException, IndexOutOfBoundsException, JSONException {
        String regex = "\\b(done|inprogress)\\b";
        Pattern pattern = Pattern.compile(regex);
        for(int i = 0; i < ExistingTasks.length(); i++) {
            String status;
            JSONObject task = ExistingTasks.getJSONObject(i);
            try {
                if(task.has("status") && task.get("status").toString().equals(StatusType)) {
                    if(pattern.matcher(StatusType).matches()) {
                        if(StatusType.equals("done")) {
                            System.out.printf("Task %s with ID %d has been completed\n", task.getString("name"), task.getInt("id"));
                        } else if(StatusType.equals("inprogress")) {
                            System.out.printf("Task %s with ID %d is in progress\n", task.getString("name"), task.getInt("id"));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public static void list() throws FileNotFoundException, IndexOutOfBoundsException, JSONException {
        try {
            JSONArray ExistingTasks = new JSONArray(add.readFile(add.FileDir));
            if(currentArguments.length > 1) {
                listTasks(ExistingTasks, currentArguments[1]);
            } else {
                for(int i = 0; i < ExistingTasks.length(); i++) {
                    String status;
                    JSONObject task = ExistingTasks.getJSONObject(i);
                    try {
                        status = task.getString("status");
                    } catch (JSONException e) {
                        status = "UNSPECIFIED";
                    }
                    System.out.printf("TASK: %s|ID: %d| DESCRIPTION: %s| STATUS %s%n", task.get("name"), task.getInt("id"), task.get("description"), status.toUpperCase());
                    //System.out.println("TASK: " + task.getString("name") + "|ID: " + task.getInt("id") + "| DESCRIPTION: " + task.get("description") + "|status: " + status);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("WARNING, ERRORS MIGHT COME FROM NOT USING THE ADD COMMAND FIRST, please refer to");
            System.out.println(Syntaxes.get("add"));
        }
    }
}

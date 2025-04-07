package org.example.CommandClasses;

import java.util.HashMap;

public class info {
    public static String currentArguments[];
    static {
        currentArguments = new String[2];
    }
    public static String description = "to get information about the application/dev";
    public static String syntax = "taskmgr info";
    public static void info() {
        System.out.println("""
        taskmgr v0.1
        Application Developer; Zeyad Ehab,
        Application made in; Friday, 2025-4-4
        """);
    }
}

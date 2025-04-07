package org.example.initiate;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.lang.ClassLoader;
import java.lang.reflect.Method;

public class Initiate {
    public static HashMap<String, String> commands;
    public static HashMap<String, String> Syntaxes;
    public static int d_int = 0;
    public static HashMap<String, String> parameters;
    static {

        commands = new HashMap<>();
        Syntaxes = new HashMap<>();
        parameters = new HashMap<>();
    }
    public static void debug(String output) {
        if(d_int != 0) {
            System.out.println("[DEBUG] "+output);
        }
    }
    public static void callMethod(String Name) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        try {

            Class<?> currentClass = Class.forName("org.example.CommandClasses." + Name);
            Method currentMethod = currentClass.getDeclaredMethod(Name);
            currentMethod.invoke(null);
            debug("Command '" + Name + "' executed successfully");
        } catch (ClassNotFoundException e) {
            System.out.println("METHOD ERROR: Command Not Found, " + e.getMessage());
        }
    }
    public static String getDescription(String Name) throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
        Field currentField = null;
        try {
            Class<?> currentClass = Class.forName("org.example.CommandClasses." + Name);
            currentField = currentClass.getDeclaredField("description");
            debug("Command '" + Name + "' description found");
            currentField.setAccessible(true);
            return (String) currentField.get(null);
        } catch (Exception e){
            System.out.println("DESCRIPTION ERROR: Command Not Found, " + e.getMessage());
            return null;
        }
    }
    public static String getSyntax(String Name) throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
        Field currentField = null;
        try {
            Class<?> currentClass = Class.forName("org.example.CommandClasses." + Name);
            currentField = currentClass.getDeclaredField("syntax");
            debug("Command '" + Name + "' syntax found");
            currentField.setAccessible(true);
            return (String) currentField.get(null);
        } catch (Exception e){
            System.out.println("SYNTAX_FIELD ERROR: Command Not Found, " + e.getMessage());
            return null;
        }
    }
    public static void setParameters(String Name, String[] args) {
        Field currentField = null;
        try {
            Class<?> currentClass = Class.forName("org.example.CommandClasses." + Name);
            currentField = currentClass.getDeclaredField("currentArguments");
            currentField.setAccessible(true);
            currentField.set(null, args);
            //System.out.println("DEBUG: Args array right before setting via reflection: " +args[3]);
            debug("Command '" + Name + "' parameter set successfully");
        } catch (Exception e){
            System.out.println("PARAMETERS ERROR: Command Not Found, " + e.getMessage());
        }
    }
    public static void helpCommand() {
        for(String command : commands.keySet()) {
            System.out.println(command+": "+commands.get(command).replace("_syntax", ""));
            System.out.println("SYNTAX | "+Syntaxes.get(command));
            debug(command);
        }
    }
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, NoSuchFieldException {
        Enumeration<URL> roots = ClassLoader.getSystemResources("org/example/CommandClasses");
        while (roots.hasMoreElements()) {
            File file = new File(roots.nextElement().getFile());
            for (String s: file.list((x,y) -> y.endsWith(".class"))) {
                s = s.replace(".class", "");
                debug(s+" Loaded Successfully");
                commands.put(s, getDescription(s));
                Syntaxes.put(s, getSyntax(s));
            }
        }
        if(args.length != 0) {
            String calledcommand = args[0];
            if(calledcommand.equals("help")) {
                debug("Help Command");
                helpCommand();
            } else {
                setParameters(calledcommand, args);

                callMethod(calledcommand);
            }

        } else {
            helpCommand();
            System.out.println("No Arguments Specified");
        }
    }
}

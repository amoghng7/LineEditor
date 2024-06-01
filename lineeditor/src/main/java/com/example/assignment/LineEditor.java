package com.example.assignment;

import java.io.*;
import java.util.*;

/**
 * LineEditor is a simple line text editor that allows basic editing
 * commands such as listing, deleting, inserting lines, saving, and quitting.
 * 
 * @author Amogh Gangadhar
 */
public class LineEditor {
    // Help text to use the editor
    private static final String HELP_TEXT = "Available options are: \n\n list - Lists the content of the file.\n ins n - inserts text on the line 'n'.\n del n - deletes the nth line.\n save - saves the current state of the file.\n help - for editor documentation.\n quit - exit the editor.\n";

    // Used for storing the current state of the file, all manipulations happens in
    // this list before saving onto the file
    private List<String> lines;
    // The file which in which needs to be edited
    private File file;

    // Error Strings
    private String insertErrorText = "Invalid line number! text can be inserted on %s!";
    private String deleteErrorText = "Line number %d does not exist!";

    /**
     * Constructor for LineEditor.
     *
     * @param filePath The path to the file to be edited.
     * @throws IOException If an I/O error occurs.
     */
    public LineEditor(String filePath) throws IOException {
        file = new File(filePath);

        // Exit if the file doesn't exist
        if (!file.exists()) {
            System.out.println("The file provided does not exist, please provide a valid file path!");
            System.exit(1);
        }
        lines = new ArrayList<>();
        loadFile();
    }

    /**
     * Loads the file content into the list of lines.
     */
    private void loadFile() {
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                System.out.println("Error while Loading the file!\n" + e.getMessage());
            }
        }
    }

    /**
     * Saves the current list of lines to the file.
     */
    void saveFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error while saving the file!\n" + e.getMessage());
        }
    }

    /**
     * Lists all the lines in the file with line numbers.
     */
    public void listLines() {
        for (int i = 0; i < lines.size(); i++) {
            System.out.println((i + 1) + ": " + lines.get(i));
        }
    }

    /**
     * Deletes a line at the specified line number.
     *
     * @param lineNumber The line number to delete.
     */
    public void deleteLine(int lineNumber) {
        if (lineNumber > 0 && lineNumber <= lines.size()) {
            lines.remove(lineNumber - 1);
        } else {
            System.out.println(String.format(deleteErrorText, lineNumber));
        }
    }

    /**
     * Inserts a line at the specified line number.
     *
     * @param lineNumber The line number to insert the line at.
     * @param text       The text to insert.
     */
    public void insertLine(int lineNumber, String text) {
        if (lineNumber > 0 && lineNumber <= lines.size() + 1) {
            lines.add(lineNumber - 1, text);
        } else {
            System.out.println(
                    String.format(insertErrorText, (lines.size() > 1 ? "lines 1 to " + (lines.size() + 1) : "line 1")));
        }
    }

    /**
     * Starts the interactive editor session, allowing the user to enter commands.
     */
    public void startEditor() {
        try (Scanner scanner = new Scanner(System.in)) {
            String command;

            System.out.println("Line Editor started. Enter your commands.");
            while (true) {
                System.out.print(">> ");
                command = scanner.nextLine();
                String[] parts = command.split(" ", 2);
                String cmd = parts[0];

                try {
                    switch (cmd) {
                        case "list":
                            listLines();
                            break;
                        case "del":
                            deleteLine(Integer.parseInt(parts[1]));
                            break;
                        case "ins":
                            System.out.print("Enter text to insert: ");
                            var text = scanner.nextLine();
                            var lineNumber = Integer.parseInt(parts[1]);
                            if (lineNumber > 0 && lineNumber <= lines.size() + 1) {
                                insertLine(lineNumber, text);
                            } else {
                                System.out.println("Invalid line number! text can be inserted on "
                                        + (lines.size() > 1 ? "lines 1 to " + lines.size() + 1 : "line 1"));
                            }
                            break;
                        case "save":
                            saveFile();
                            break;
                        case "help":
                            System.out.println(HELP_TEXT);
                            break;
                        case "quit":
                            return;
                        default:
                            System.out.println("Unknown command!");
                            System.out.println(HELP_TEXT);
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * The main method to start the LineEditor.
     *
     * @param args Command line arguments, where the first argument is the file
     *             path.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: LineEditor <file_path>");
            return;
        }

        try {
            LineEditor editor = new LineEditor(args[0]);
            editor.startEditor();
        } catch (IOException e) {
            System.out.println("Error while running the LineEditor!\n" + e.getMessage());
        }
    }
}

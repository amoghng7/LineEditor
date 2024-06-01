package com.example.assignment;

import org.junit.*;

import java.io.*;
import java.nio.file.*;
import java.util.List;

/**
 * Test class for the LineEditor.
 * 
 * @author Amogh Gangadhar
 */
public class LineEditorTest {
    private LineEditor editor;
    private Path tempFile;

    /**
     * Sets up the test environment by creating a temporary file and initializing
     * the LineEditor.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Before
    public void setUp() throws IOException {
        tempFile = Files.createTempFile("testfile", ".txt");
        Files.write(tempFile, "Line 1\nLine 2\nLine 3".getBytes());
        editor = new LineEditor(tempFile.toString());
    }

    /**
     * Cleans up the test environment by deleting the temporary file.
     *
     * @throws IOException If an I/O error occurs.
     */
    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    /**
     * Given: A file with three lines of text.
     * When: The listLines method is called.
     * Then: The output should display the three lines with correct line numbers.
     */
    @Test
    public void testListLines() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        editor.listLines();

        String expectedOutput = "1: Line 1\n2: Line 2\n3: Line 3\n";
        Assert.assertEquals(expectedOutput, outContent.toString());
    }

    /**
     * Given: A file with three lines of text.
     * When: The deleteLine method is called with line number 2.
     * Then: The second line should be removed, and the remaining lines should be
     * listed correctly.
     */
    @Test
    public void testDeleteLine() {
        editor.deleteLine(2);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        editor.listLines();

        String expectedOutput = "1: Line 1\n2: Line 3\n";
        Assert.assertEquals(expectedOutput, outContent.toString());
    }

    /**
     * Given: A file with three lines of text.
     * When: The insertLine method is called to insert a line at position 2.
     * Then: The new line should be inserted at the specified position, and the
     * lines should be listed correctly.
     */
    @Test
    public void testInsertLine() {
        editor.insertLine(2, "Inserted Line");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        editor.listLines();

        String expectedOutput = "1: Line 1\n2: Inserted Line\n3: Line 2\n4: Line 3\n";
        Assert.assertEquals(expectedOutput, outContent.toString());
    }

    /**
     * Given: A file with three lines of text.
     * When: The deleteLine method is called with line number 2, and then the
     * saveFile method is called.
     * Then: The file should be saved with the second line removed.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Test
    public void testSaveFile() throws IOException {
        editor.deleteLine(2);
        editor.saveFile();

        List<String> lines = Files.readAllLines(tempFile);
        Assert.assertEquals(2, lines.size());
        Assert.assertEquals("Line 1", lines.get(0));
        Assert.assertEquals("Line 3", lines.get(1));
    }

    /**
     * Given: A file with three lines of text.
     * When: The deleteLine method is called with an invalid line number (e.g., 5).
     * Then: An error message should be displayed indicating the invalid line
     * number.
     */
    @Test
    public void testInvalidLineDeletion() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        editor.deleteLine(5);

        String expectedOutput = "Line number 5 does not exist!\n";
        Assert.assertEquals(expectedOutput, outContent.toString());
    }

    /**
     * Given: A file with three lines of text.
     * When: The insertLine method is called with an invalid line number (e.g., 10).
     * Then: An error message should be displayed indicating the invalid line
     * number.
     */
    @Test
    public void testInvalidLineInsertion() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        editor.insertLine(10, "This should not be inserted");

        String expectedOutput = "Invalid line number! text can be inserted on lines 1 to 4!\n";
        Assert.assertEquals(expectedOutput, outContent.toString());
    }
}

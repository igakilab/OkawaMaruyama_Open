
package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

public class Test06_4Test {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void testMainMethod() {
        String userInput = "10 20 30\n";
        String expectedOutput = "\nHit&Blow\n\n一意な整数を3つ入力してください（例: 12 34 56）:\n答えの生成ロジック完了: 2, 3, 4";

        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        // Execute the main method of the class under test
        Test06_4.main(new String[0]);

        // Capture and normalize the output
        String actualOutput = outContent.toString().replace("\r\n", "\n").trim();
        assertEquals(
            expectedOutput.replaceAll("\\s+", " ").trim(),
            actualOutput.replaceAll("\\s+", " ").trim(),
            "Output did not match expected result"
        );
    }
}

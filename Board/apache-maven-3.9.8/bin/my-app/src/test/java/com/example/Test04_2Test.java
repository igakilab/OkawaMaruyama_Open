
package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

public class Test04_2Test {
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
        String userInput = "123 234 345\n";
        String expectedOutput = "「3桁以上の数字を3つ入力してください」\n変換されたカード番号: 6\n変換されたカード番号: 0\n変換されたカード番号: 7";

        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        // Execute the main method of the class under test
        Test04_2.main(new String[0]);

        // Capture and normalize the output
        String actualOutput = outContent.toString().replace("\r\n", "\n").trim();
        assertEquals(
            expectedOutput.replaceAll("\\s+", " ").trim(),
            actualOutput.replaceAll("\\s+", " ").trim(),
            "Output did not match expected result"
        );
    }
}

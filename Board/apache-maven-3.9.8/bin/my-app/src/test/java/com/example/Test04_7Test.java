
package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

public class Test04_7Test {
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
        String userInput = "123 234 345\nhigh\nlow\n";
        String expectedOutput = "「3桁以上の数字を3つ入力してください」\nHigh and Low Start\n現在のカードは7です\n「次のカードがhighかlowかあなたの予想を入力してください (降りる場合はexitと入力してください)」\n次のカードはAです\nあなたの負けです\n現在のカードはAです\n「次のカードがhighかlowかあなたの予想を入力してください (降りる場合はexitと入力してください)」\n次のカードは8です\nあなたの負けです\nゲーム終了\n勝敗引き分けの数\nwin:0\nlose:2\ndraw:0";

        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        // Execute the main method of the class under test
        Test04_7.main(new String[0]);

        // Capture and normalize the output
        String actualOutput = outContent.toString().replace("\r\n", "\n").trim();
        assertEquals(
            expectedOutput.replaceAll("\\s+", " ").trim(),
            actualOutput.replaceAll("\\s+", " ").trim(),
            "Output did not match expected result"
        );
    }
}

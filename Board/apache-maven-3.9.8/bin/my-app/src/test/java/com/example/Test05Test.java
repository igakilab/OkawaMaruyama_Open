package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

public class Test05Test {
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
        String userInput = "10 20 30\n2 3 4\nyes\n25 50 75\n0 0 0\n8 6 4\nno\n";
        String expectedOutput = "\nHit&Blow\n\n一意な整数を3つ入力してください（例: 12 34 56）:\n数字を3個SPACE区切りで入力してください\n[1回目]\nヒント: 2 3 4\nHit:3 Blow:0\nおめでとう！ 答えを正解しました！\nゲームを続けますか?[yes/no]\n一意な整数を3つ入力してください（例: 12 34 56）:\n数字を3個SPACE区切りで入力してください\n[1回目]\nヒント: 8 6 4\nHit:0 Blow:0\n\n[2回目]\nヒント: 8 6 4\nHit:3 Blow:0\nおめでとう！ 答えを正解しました！\nゲームを続けますか?[yes/no]";

        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        // Execute the main method of the class under test
        Test05.main(new String[0]);

        // Capture and normalize the output
        String actualOutput = outContent.toString().replace("\r\n", "\n").trim();
        assertEquals(
            expectedOutput.replaceAll("\\s+", " ").trim(),
            actualOutput.replaceAll("\\s+", " ").trim(),
            "Output did not match expected result"
        );
    }
}

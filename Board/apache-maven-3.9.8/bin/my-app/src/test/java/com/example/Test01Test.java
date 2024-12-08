
package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

public class Test01Test {
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
        String userInput = "-1\n3\n-1\n-2\n-3\ngu\ngu\ngu\n";
        String expectedOutput = "[じゃんけんゲームを何回戦実施するか入力してください]\n0以下の数値が入力されたのでもう一度入力し直しましょう\n[CPUの手として任意の数字をゲーム数分だけ入力してください]\nCPUの手を決定します\n負の数は正の数に変換します\n負の数は正の数に変換します\n負の数は正の数に変換します\n[あなたの手を入力してください|gu/choki/pa]\nあなたの勝ちです\n[あなたの手を入力してください|gu/choki/pa]\nあなたの負けです\n[あなたの手を入力してください|gu/choki/pa]\n引き分けです\nじゃんけんを終了します\n対戦成績は1勝1敗1引き分けでした";

        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        // Execute the main method of the class under test
        Test01.main(new String[0]);

        // Capture and normalize the output
        String actualOutput = outContent.toString().replace("\r\n", "\n").trim();
        assertEquals(
            expectedOutput.replaceAll("\\s+", " ").trim(),
            actualOutput.replaceAll("\\s+", " ").trim(),
            "Output did not match expected result"
        );
    }
}

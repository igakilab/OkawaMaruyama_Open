
package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

public class Work48_2Test {
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
        String userInput = "";
        String expectedOutput = "(45,8,3)\n(45,8,31)\n(45,8,1)\n(45,8,9)\n(45,8,22)\n(45,3,31)\n(45,3,1)\n(45,3,9)\n(45,3,22)\n(45,31,1)\n(45,31,9)\n(45,31,22)\n(45,1,9)\n(45,1,22)\n(45,9,22)\n(8,3,31)\n(8,3,1)\n(8,3,9)\n(8,3,22)\n(8,31,1)\n(8,31,9)\n(8,31,22)\n(8,1,9)\n(8,1,22)\n(8,9,22)\n(3,31,1)\n(3,31,9)\n(3,31,22)\n(3,1,9)\n(3,1,22)\n(3,9,22)\n(31,1,9)\n(31,1,22)\n(31,9,22)\n(1,9,22)";

        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        // 被テストクラスのmainメソッドを実行
        Work48_2.main(new String[0]);

        // 実際の出力をキャプチャして正規化
        String actualOutput = outContent.toString().replace("\r\n", "\n").trim();
        assertEquals(
            expectedOutput.replaceAll("\\s+", " ").trim(),
            actualOutput.replaceAll("\\s+", " ").trim(),
            "Output did not match expected result"
        );
    }
}

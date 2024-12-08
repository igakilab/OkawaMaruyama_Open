import os
import sys  # sysのインポートを追加
from io import BytesIO  # BytesIOをインポート
from dotenv import load_dotenv
import re  # 正規表現ライブラリをインポート

# .envファイルから環境変数を読み込み、PATH_TO_BOARDへのアクセスを取得
load_dotenv()
board_path = os.getenv("PATH_TO_BOARD")

# board_pathが正しく設定されているか確認
if not board_path:
    print("Error: PATH_TO_BOARD environment variable is not set.")
    sys.exit(1)

# テストファイル保存パスを修正
output_dir = os.path.join(board_path, "apache-maven-3.9.8", "bin", "my-app", "src", "test", "java", "com", "example")
response_file_path = os.path.join(board_path, "response.txt")  # Board/response.txtを指定

# 出力ディレクトリが存在しない場合は作成
os.makedirs(output_dir, exist_ok=True)


def normalize_spaces(s):
    """
    空白を正規化する関数。
    - 連続する空白を1つのスペースに置き換え、先頭と末尾の空白を削除する。
    """
    return re.sub(r'\s+', ' ', s).strip()


def extract_inputs_and_outputs(execution_example, class_name):
    """
    実行例から入力と出力を抽出する関数。
    - 行末に "<-" が含まれるものを入力とみなします。
    - "$ java {class_name}" を除外します。
    - それ以外を出力とみなします。
    """
    inputs = []
    outputs = []

    for line in execution_example.splitlines():
        stripped_line = line.strip()

        # Skip execution command
        if stripped_line.startswith(f"$ java {class_name}"):
            continue

        # Categorize as input or output
        if stripped_line.endswith("<-"):
            inputs.append(stripped_line[:-2].strip())  # Remove "<-"
        else:
            outputs.append(stripped_line)

    return inputs, outputs


def parse_response_file(file_path):
    """
    response.txtファイルを解析して、Javaクラス情報と実行例を抽出する関数。
    """
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    classes = []
    current_class = None
    lines = content.splitlines()

    for i, line in enumerate(lines):
        if line.startswith("### JavaClass"):
            # 現在のクラスを保存
            if current_class:
                classes.append(current_class)

            # クラス名を取得
            if i + 1 < len(lines) and lines[i + 1].strip():
                class_name = lines[i + 1].strip()
                current_class = {
                    "name": class_name,
                    "examples": []
                }
            else:
                print(f"[WARNING] No class name found after JavaClass at line {i}")
                current_class = None

        elif line.startswith("**実行例**") and current_class:
            # 実行例を収集
            example_lines = []
            for example_line in lines[i + 1:]:
                if example_line.startswith("###") or example_line.startswith("**実行例**"):
                    break
                example_lines.append(example_line)
            execution_example = '\n'.join(example_lines).strip()
            inputs, outputs = extract_inputs_and_outputs(execution_example, current_class["name"])
            current_class["examples"].append({
                "execution_example": execution_example,
                "inputs": inputs,
                "outputs": outputs
            })

    # 最後のクラスを保存
    if current_class:
        classes.append(current_class)

    return classes


def generate_junit_code(test_class_name, code_under_test, inputs, outputs):
    """
    Generates JUnit test code based on the provided test class and inputs/outputs.
    """
    user_input = "\\n".join(inputs) + "\\n"  # Join inputs with \n and append final \n
    expected_output = "\\n".join(outputs)  # Join outputs with \n

    test_code = f"""
package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

public class {test_class_name}Test {{
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    public void setUpStreams() {{
        System.setOut(new PrintStream(outContent));
    }}

    @AfterEach
    public void restoreStreams() {{
        System.setOut(originalOut);
        System.setIn(originalIn);
    }}

    @Test
    public void testMainMethod() {{
        String userInput = "{user_input}";
        String expectedOutput = "{expected_output}";

        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        // Execute the main method of the class under test
        {code_under_test}.main(new String[0]);

        // Capture and normalize the output
        String actualOutput = outContent.toString().replace("\\r\\n", "\\n").trim();
        assertEquals(
            expectedOutput.replaceAll("\\\\s+", " ").trim(),
            actualOutput.replaceAll("\\\\s+", " ").trim(),
            "Output did not match expected result"
        );
    }}
}}
"""
    return test_code



def main():
    classes = parse_response_file(response_file_path)  # Javaクラスと実行例を解析
    for java_class in classes:
        class_name = java_class["name"]
        examples = java_class["examples"]

        for step_number, example in enumerate(examples, start=1):
            inputs = example["inputs"]  # 入力データ
            outputs = example["outputs"]  # 出力データ

            # JUnitテストコードを生成
            test_code = generate_junit_code(class_name, class_name, inputs, outputs)

            # テストコードを保存
            test_file_name = f"{class_name}Test.java"
            test_file_path = os.path.join(output_dir, test_file_name)

            with open(test_file_path, 'w', encoding='utf-8') as f:
                f.write(test_code)

            print(f"テストコードを生成しました: {test_file_path}")



if __name__ == "__main__":
    main()

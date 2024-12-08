// 必要なモジュールの読み込み
const axios = require('axios');
const dotenv = require('dotenv');
const fs = require('fs');
const path = require('path');
const { exec } = require('child_process');
const readline = require('readline'); // readlineモジュールを読み込み


dotenv.config({ path: 'C:/.env' }); // .envファイルのパスを指定

const apiKey = process.env.CHATGPT_API_KEY;
const boardPath = process.env.PATH_TO_BOARD;

if (!apiKey) {
    console.error("APIキーが設定されていません.");
    process.exit(1);
}

if (!boardPath) {
    console.error("Boardへのパスが設定されていません.");
    process.exit(1);
}

// コマンドライン引数からファイル名を取得
const className = process.argv[2];
if (!className) {
    console.error("プロンプトファイル名が指定されていません。例: separate2.js Work49");
    process.exit(1);
}

// 引数からファイルパスを生成
const promptFilePath = `${boardPath}/prompts/${className}.txt`;

// テストコード、テストされるコード、およびテスト結果ファイルを削除する関数
async function deleteExistingFiles(className) {
  const testCodePath = path.join(boardPath, 'apache-maven-3.9.8/bin/my-app/src/test/java/com/example');
  const mainCodePath = path.join(boardPath, 'apache-maven-3.9.8/bin/my-app/src/main/java/com/example');
  const reportPath = path.join(boardPath, 'apache-maven-3.9.8/bin/my-app/target/custom-test-reports');
  const saveJavaPath = path.join(boardPath, 'saveJava'); // saveJavaディレクトリのパス

  const testPattern = new RegExp(`^${className}_\\d+Test\\.java$`);
  const mainPattern = new RegExp(`^${className}_\\d+\\.java$`);
  const reportPattern = new RegExp(`^com\\.example\\.${className}_\\d+Test\\.txt$`);
  const saveJavaPattern = new RegExp(`^${className}_\\d+$`); // saveJava内のファイルパターン

  // 各ディレクトリのファイルを削除
  await Promise.all([testCodePath, mainCodePath, reportPath, saveJavaPath].map(async (dir, idx) => {
      await fs.promises.readdir(dir).then(files => {
          return Promise.all(files.map(file => {
              if ([testPattern, mainPattern, reportPattern, saveJavaPattern][idx].test(file)) {
                  return fs.promises.unlink(path.join(dir, file));
              }
          }));
      }).catch(err => console.error(`ディレクトリ${dir}のファイル削除中にエラーが発生しました: ${err}`));
  }));
}


// ファイルの内容を読み込む
let userInput;
let answerExample;
try {
    userInput = fs.readFileSync(promptFilePath, 'utf8');
    answerExample = fs.readFileSync(promptFilePath, 'utf8');
    console.log(`プロンプトファイル '${promptFilePath}' を読み込みました`);
} catch (err) {
    console.error(`プロンプトファイル '${promptFilePath}' の読み込み中にエラーが発生しました:`, err);
    process.exit(1);
}

// save.jsを実行する関数
async function runSaveJs(className) {
    return new Promise((resolve, reject) => {
        exec(`node save.js ${className}`, (error, stdout, stderr) => {
            if (error) return reject(error);
            if (stderr) return reject(stderr);
            console.log(`save.jsの出力: ${stdout}`);
            resolve(stdout.trim());
        });
    });
}

const initialPrompt = `
## 設定
userInputから与えられたプログラムコードをSTEPごとに分割し,STEP名,分割したコード,実行例を生成してください.
もし,userInput内に"生徒のコードという項目"で作成途中のプログラムコードがある場合は,そのコードを1つ目のSTEPとして生成してください.
もし,userInput内に"生徒のコードという項目"がない場合は,最初のSTEPはmainメソッドの宣言のみから始めて下さい.
以下に生成物の詳しい要件を記すので,それに従ってください.

## 分割要件
・プログラムを段階的に作成する手順のSampleを与えるのでそれを参考にして分割してください.
・生成物はブロックごとの小見出しと分割されたコードとそれらコードの実行例,加えて標準入力が存在する場合は入力例のみで,それ以外の文章は必要ありません.
・分割されたコードのクラス名はクラス名_STEP番号(例:Work41_1)としてください.
・入れ子構造のコード(if文やfor文)を作成するときは,1つの入れ子につき外の入れ子から1つずつSTEPごとに作成してください.
・分割するコードにmain以外の複数のメソッドが存在する場合は,メソッドの宣言するステップをそれぞれ作成して下さい.
・main以外のメソッドについて,複数のfor文,if文等から構成されている場合は,複数のSTEPに分けて作成して下さい.
・各STEPで実行結果が存在しない場合,テスト用の出力文を追加して,必ず実行結果を返すようにしてください.
・途中段階のコードで,テスト用の出力として,計算結果等を出力する場合はなるべく完成したコード(userInputから与えられたコード)と同じ体裁に合わせてください.
・宣言した配列をテストする時は,for文で配列の要素をすべて出力するテストをしてください.
・あるSTEPで生成したコードは後のSTEPでも絶対に省略せずに,全文生成してください.
・各STEPのコードにおいて,標準入力がある場合,適切な例を標準入力とし,入力した内容を出力するテストコードを生成してください.
・最後のSTEPは完成したコード(userInputから与えられたコード)とその実行例にしてください.ただし,クラス名はクラス名_STEP番号(例:Work41_1)としてください.
・メソッドを宣言するSTEPとメソッドの詳細を作成するSTEPは,別々のSTEPに分けてください.
・mainメソッド以外のメソッドを宣言するSTEPでは,必ず,mainメソッドで呼び出してテスト出力を出せるコードに細分化してください.
・JavaClassにはそのコードのクラス名のみを体裁通りに格納してください.
・double型の数値を出力するコードを生成する際は,特にプログラムの仕様に指定がない限りは,String.format("%.1f")を用いて,小数第1位まで出力するようにしてください.

## 実行例生成要件
・**important:実行例の表示は,以下のSampleの体裁で生成してください.また,標準入力がある場合は,Sampleのように行末に""<-""を記述(例: ""10 20 <-"")してください.**
・**important:実行例の出力について,標準入力があり,期待される出力が条件分岐(if文)によって複数個存在すると考えられる場合は,それらの理想的な入力と,期待される出力をすべて(入力によって出力パターンが変わるものすべて)表示してください.**
・**important:実行例の出力文において,必ず省略せずに全文表示してください.**
・for文の実行例を生成するときは,コードのループ範囲を参照し,出力文の内容やループ数を間違いのないように生成してください.
・**most important:生成された実行ファイルは後でテスト・コードを生成するために使用されるので,「絶対に」省略しないでください！省略すると正しいテストコードが生成できず,エラーになります！**
・**most important:生成された実行ファイルは後でテスト・コードを生成するために使用されるので,「絶対に」行末の「<-」の後ろに不要な空白を入れないでください！**
・double型の数値を出力する際は,特にプログラムの仕様に指定がない限りは,String.format("%.1f")を用いて,小数第1位までの実行例を生成してください.
## Sample42.javaをブロックごとに分割するSample(This Java code is a program that takes input from the user and initializes each element of an array with that value.)

### JavaClass
Sample42_1

### mainメソッドを宣言
\`\`\`java
public class Sample42_1 {
  public static void main(String[] args) {
    //テスト用コード
    System.out.println("mainメソッド");
  }
}
\`\`\`

**実行例**
$ java Sample42_1
mainメソッド

### JavaClass
Sample42_2

### 配列を宣言し、入力を受け取る文の定義
\`\`\`java
import java.util.Scanner;

public class Sample42_2 {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int[] numbers = new int[5];
    System.out.println("配列の初期化値を入力してください: ");
    int input = scanner.nextInt();
    System.out.println("入力値: " + input);
  }
}
\`\`\`java
**実行例**
$ java Sample42_2
配列の初期化値を入力してください: 
5 <-
入力値: 5


**実行例**
$ java Sample42_2
配列の初期化値を入力してください: 
1 <-
入力値: 1


### JavaClass
Sample42_3

### 配列の要素を入力された値で初期化するfor文を作成
\`\`\`java
import java.util.Scanner;

public class Sample42_3 {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int[] numbers = new int[5];
    System.out.println("配列の初期化値を入力してください: ");
    int input = scanner.nextInt();
    for (int i = 0; i < numbers.length; i++) {
      numbers[i] = input;
    }
    System.out.println("配列が初期化されました");
  }
}
\`\`\`

**実行例**
$ java Sample42_3
配列の初期化値を入力してください: 
5 <-
配列が初期化されました


**実行例**
$ java Sample42_3
配列の初期化値を入力してください: 
1 <-
配列が初期化されました

### JavaClass
Sample42_4

### printArrayメソッドを宣言
\`\`\`java
import java.util.Scanner;

public class Sample42_4 {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int[] numbers = new int[5];
    System.out.println("配列の初期化値を入力してください: ");
    int input = scanner.nextInt();
    for (int i = 0; i < numbers.length; i++) {
      numbers[i] = input;
    }
    printArray(numbers);
  }

  public static void printArray(int[] numbers) {
    //テスト用の出力
    System.out.println("printArrayメソッドが呼び出されました");
  }
}
\`\`\`

**実行例**
$ java Sample42_4
配列の初期化値を入力してください: 
5 <-
printArrayメソッドが呼び出されました


**実行例**
$ java Sample42_4
配列の初期化値を入力してください: 
1 <-
printArrayメソッドが呼び出されました

### JavaClass
Sample42_5

### printArrayメソッドを作成
\`\`\`java
import java.util.Scanner;

public class Sample42_5 {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int[] numbers = new int[5];
    System.out.println("配列の初期化値を入力してください: ");
    int input = scanner.nextInt();
    for (int i = 0; i < numbers.length; i++) {
      numbers[i] = input;
    }
    printArray(numbers);
  }

  public static void printArray(int[] numbers) {
    for (int number : numbers) {
      System.out.println(number);
    }
  }
}
\`\`\`

**実行例**
$ java Sample42_5
配列の初期化値を入力してください: 
5 <-
5
5
5
5
5


**実行例**
$ java Sample42_5
配列の初期化値を入力してください: 
1 <-
1
1
1
1
1

### JavaClass
Sample42_6

### mainメソッドでprintArrayメソッドを使用
\`\`\`java
import java.util.Scanner;

public class Sample42_6 {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int[] numbers = new int[5];
    System.out.println("配列の初期化値を入力してください: ");
    int input = scanner.nextInt();
    for (int i = 0; i < numbers.length; i++) {
      numbers[i] = input;
    }
    printArray(numbers);
  }

  public static void printArray(int[] numbers) {
    for (int number : numbers) {
      System.out.println(number);
    }
  }
}
\`\`\`

**実行例**
$ java Sample42_6
配列の初期化値を入力してください: 
5 <-
5
5
5
5
5


**実行例**
$ java Sample42_6
配列の初期化値を入力してください: 
1 <-
1
1
1
1
1

## Sample43.javaにおいて2重for文の実行例を出力するSample(This Java program demonstrates the use of nested for loops to iterate over a 2D array, printing each element in row-major order. The program begins with a predefined 3x3 integer array and then uses an outer loop to navigate through each row. Within each row, an inner loop iterates over the elements, printing them separated by spaces. At the end of each row, a newline is added to structure the output neatly. The final output displays each row on a new line in sequence.)

### JavaClass
Sample43_x

\`\`\`java
public class Sample43_x {
  public static void main(String[] args) {
      // Sample 2D array definition
      int[][] array = {
          {1, 2, 3, 4, 5},
          {6, 7, 8, 9, 10},
          {11, 12, 13, 14, 15},
          {16, 17, 18, 19, 20},
          {21, 22, 23, 24, 25},
          {26, 27, 28, 29, 30},
          {31, 32, 33, 34, 35},
          {36, 37, 38, 39, 40},
          {41, 42, 43, 44, 45},
          {46, 47, 48, 49, 50}
      };

      // Use nested for-loops to list elements in the array
      for (int i = 0; i < array.length; i++) {
          for (int j = 0; j < array[i].length; j++) {
              System.out.print(array[i][j] + " ");
          }
          System.out.println();  // Newline at the end of each row
      }
  }
}

\`\`\`java
**実行例**
$ java Sample43_x
1 2 3 4 5 
6 7 8 9 10 
11 12 13 14 15 
16 17 18 19 20 
21 22 23 24 25 
26 27 28 29 30 
31 32 33 34 35 
36 37 38 39 40 
41 42 43 44 45 
46 47 48 49 50 
`;

const nextPrompt = `
### 解答例
${answerExample}

### 要件
assistantから得られる情報は,課題の細分化されたコードのSTEPの見出し,STEPごとのコード,STEPごとの実行例です.これらの情報を参考にして,以下の要件をみたしてください.
・**important:コードを使った説明は不要です**
・細分化されたコードからクラス名を読み取り,クラス名_STEP番号(例:Work41_1)を,JavaClassという項目で生成してください.
・STEP名の下に,上記作成STEPが仕様のどの部分を完成させることを目標としているかを,作成STEPの項目を参考にしてサブタイトルという項目で生成してください.
・STEPごとのコードを参考にして,詳細なコードの作成手順を文章のみでサブタイトルの下に作成手順という項目で生成してください.
・**important:作成手順でfor文やif文の条件式について説明するときは,条件の範囲等について,何故そのようになるのかを説明してください.**
・**important:作成手順の下に,assistantから得られた各STEPの実行例を実行例という項目で表示してください.このとき,assistantから得られたものをそのまま表示してください.(複数個実行例がある場合も全て表示してください)**
・実行例において,標準入力がある場合は,入力の行末に""<-""を記述(例: ""10 20 <-"")してください.
・**important:読み取った細分化されたコードの数だけ,JavaClass,サブタイトル,作成手順,実行例を生成してください.**
・JavaClass,サブタイトル,作成手順,実行例の生成は以下のSampleの体裁で必ず生成してください.
・Sampleは42_2までしかないですが,生成するときは細分化されたSTEPの数だけJavaClass,サブタイトル,作成手順,実行例を生成してください.
・作成手順や実行例の項目で前後に余分な空白や空の改行を入れずに,Sampleの体裁で改行してください.

### Sample
JavaClass: Sample42_1
サブタイトル: STEPのサブタイトル
作成手順:
作成手順の説明
実行例:
\`\`\`
$ java Sample42_1
mainメソッド
\`\`\`

JavaClass: Sample42_2
サブタイトル: STEPのサブタイトル
作成手順:
作成手順の説明
実行例:
\`\`\`
$ java Sample42_2
配列の初期化値を入力してください: 
5 <-
入力値: 5
\`\`\`
\`\`\`
$ java Sample42_2
配列の初期化値を入力してください: 
1 <-
入力値: 1
\`\`\`
`

// ユーザーにYes/Noを入力させるプロンプト関数
function askRetry() {
  return new Promise((resolve) => {
      const rl = readline.createInterface({
          input: process.stdin,
          output: process.stdout
      });
      rl.question("テストが失敗しました。APIの質問を再生成しますか？ (Y/N): ", (answer) => {
          rl.close();
          const shouldRetry = answer.trim().toUpperCase() === 'Y';
          if (shouldRetry) {
              console.log("APIを再生成しています...");
          }
          resolve(shouldRetry);
      });
  });
}

async function sendMessage(message) {
  try {
      let testSuccess = false;
      let result = ""; // ループ外で result を宣言して初期化

      // ループでAPIへの再送信とテストの再試行
      while (!testSuccess) {
          // 既存のテストコード、結果ファイルの削除
          await deleteExistingFiles(className);

          // ChatGPT APIにメッセージを送信
          const response = await axios.post('https://api.openai.com/v1/chat/completions', {
              model: "gpt-4o",
              messages: [
                  { role: "system", content: initialPrompt },
                  { role: "user", content: message }
              ],
          }, {
              headers: {
                  'Authorization': `Bearer ${apiKey}`,
                  'Content-Type': 'application/json'
              }
          });

          result = response.data.choices[0].message.content; // ループ外の result を更新
          console.log("Response: ", result);

          await fs.promises.writeFile('response.txt', result);
          console.log('返答がresponse.txtに保存されました');

          // save.jsを実行して初期設定のファイルを作成
          await runSaveJs(className);

          // JUnitテストコードの生成
          await new Promise((resolve, reject) => {
              exec(`python ResponseToJunit.py ${className}`, (error, stdout, stderr) => {
                  if (error) {
                      console.error(`Error executing ResponseToJunit.py: ${error.message}`);
                      return reject(error);
                  }
                  console.log(`ResponseToJunit.py output: ${stdout}`);
                  resolve();
              });
          });

          // Mavenテストの実行
          await new Promise((resolve) => {
              exec(`mvn -Dtest=${className}_*Test test`, { cwd: `${boardPath}/apache-maven-3.9.8/bin/my-app` }, async (error, stdout, stderr) => {
                  if (error) {
                      console.error("Maven test failed. エラーログ:");
                      console.error(stderr); // エラーログを表示

                      const retry = await askRetry(); // ユーザーに再生成を促す
                      if (!retry) {
                          console.log("プログラムを終了します。");
                          process.exit(1); // プログラムを終了
                      }
                      return resolve(false); // 失敗時はループを継続
                  }
                  console.log(`Maven test succeeded: ${stdout}`);
                  testSuccess = true; // テストが成功したらループを終了
                  resolve(true);
              });
          });
      }

      // テストが成功した場合のみ次のプロンプトに進む
      askNextStep(result);

  } catch (error) {
      console.error("Error: ", error.response ? error.response.data : error.message);
  }
}


// 次のプロンプトに対する質問として送信
async function askNextStep(previousResult) {
  try {
      const response = await axios.post('https://api.openai.com/v1/chat/completions', {
          model: "gpt-4o",
          messages: [
              { role: "assistant", content: previousResult },
              { role: "user", content: nextPrompt }
          ],
      }, {
          headers: {
              'Authorization': `Bearer ${apiKey}`,
              'Content-Type': 'application/json'
          }
      });

      const result = response.data.choices[0].message.content;
      console.log("Next Step Response: ", result);

      await fs.promises.writeFile('response_explain.txt', result);
      console.log('返答がresponse_explain.txtに保存されました');

  } catch (error) {
      console.error("Error: ", error.response ? error.response.data : error.message);
  }
}

(async function main() {
    await deleteExistingFiles(className);
    await sendMessage(userInput);
})();
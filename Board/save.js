const fs = require('fs');
const path = require('path');
const dotenv = require('dotenv');

// .envファイルから設定を読み込む
dotenv.config({ path: 'C:/.env' }); // .envファイルのパスを指定

// .envからディレクトリパスを取得
const javaSaveTest = process.env.PATH_TO_JAVA;
const javaSaveDir = process.env.PATH_TO_SAVE;

// コマンドライン引数からクラス名を取得
const className = process.argv[2];
if (!className) {
  console.error("クラス名が指定されていません。使用方法: node save.js クラス名（例: node save.js Work48）");
  process.exit(1);
}

// パッケージ名を指定
const packageName = 'com.example';
const saveJavaPackageName = 'saveJava';

// response.txtから各Javaコードブロックを読み込んでJavaファイルとして保存する関数
function extractAndSaveJavaFiles() {
  // 1. response.txtを読み込む
  const responseFile = 'response.txt';
  const content = fs.readFileSync(responseFile, 'utf-8');

  // 2. コードブロックを正規表現で抽出する
  const codeBlocks = content.match(/```java[\s\S]*?```/g);

  if (!codeBlocks || codeBlocks.length === 0) {
    console.error('Javaコードブロックが見つかりませんでした');
    return;
  }

  // 3. 各コードブロックをJavaファイルとして保存する
  codeBlocks.forEach((block, index) => {
    // ブロックから```java と ``` を取り除く
    let code = block.replace(/```java/g, '').replace(/```/g, '').trim();

    // ファイル名を定義（例: Work48_STEP_${index + 1}.java）=> Work48_1.java のように変更
    const fileName = `${className}_${index + 1}.java`;

    // javaSaveTestにコードを保存
    const javaCodeTest = `package ${packageName};\n\n${code}`;
    if (!fs.existsSync(javaSaveTest)) {
      fs.mkdirSync(javaSaveTest); // ディレクトリが存在しない場合は作成
    }
    const filePathTest = path.join(javaSaveTest, fileName);
    fs.writeFileSync(filePathTest, javaCodeTest);
    console.log(`Javaコードが ${filePathTest} に保存されました`);

    // javaSaveDir (Board\saveJava) にコードを保存
    const javaCodeSave = `package ${saveJavaPackageName};\n\n${code}`;
    if (!fs.existsSync(javaSaveDir)) {
      fs.mkdirSync(javaSaveDir); // ディレクトリが存在しない場合は作成
    }
    const filePathSave = path.join(javaSaveDir, fileName);
    fs.writeFileSync(filePathSave, javaCodeSave);
    console.log(`Javaコードが ${filePathSave} に保存されました`);
  });
}

// 関数を呼び出して処理を実行
extractAndSaveJavaFiles();

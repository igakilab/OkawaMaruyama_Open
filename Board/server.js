require('dotenv').config({ path: 'C:\\.env' });
const express = require('express');
const multer = require('multer');
const path = require('path');
const fs = require('fs');
const { spawn } = require('child_process');
const iconv = require('iconv-lite');
const { Transform } = require('stream');
const { OpenAI } = require('openai');

const openai = new OpenAI({
  apiKey: process.env.CHATGPT_API_KEY,
});

const boardPath = process.env.PATH_TO_BOARD;

const app = express();
const port = 3000;
const host = '0.0.0.0';

const uploadsDir = path.join(__dirname, 'uploads');
if (!fs.existsSync(uploadsDir)) {
  fs.mkdirSync(uploadsDir);
}

const serverStartTime = Date.now();

app.get('/server-status', (req, res) => {
    res.json({ serverStartTime });
});

const postsFile = path.join(__dirname, 'posts.json');
let posts = [];
if (fs.existsSync(postsFile)) {
  const data = fs.readFileSync(postsFile, 'utf8');
  posts = JSON.parse(data);
}

// `savePosts`関数の定義
function savePosts() {
  fs.writeFileSync(postsFile, JSON.stringify(posts, null, 2));
}

const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, 'uploads/');
  },
  filename: (req, file, cb) => {
    cb(null, Date.now() + path.extname(file.originalname));
  }
});
const upload = multer({ storage: storage });

app.use(express.static('public'));
app.use(express.urlencoded({ extended: true }));
app.use(express.json()); // JSONデータの受信を許可
app.use('/uploads', express.static(uploadsDir));

// filterOutput 関数の定義
function filterOutput(data) {
  const lines = data.split('\n');
  const filteredLines = lines.filter(line => {
    return line.includes('[INFO] BUILD SUCCESS') || line.includes('テストが成功しました');
  });
  return filteredLines.join('\n');
}

// 投稿処理
app.post('/upload', upload.single('file'), (req, res) => {
  if (!req.file) {
    res.json({ compileResult: 'ファイルがアップロードされていません。', testSuccess: false });
    return;
  }

  const filePath = path.join(__dirname, 'uploads', req.file.filename);
  let compileResult = '';
  let testSuccess = false;

  // アップロードされたJavaファイルの内容を読み込む
  const javaCode = fs.readFileSync(filePath, 'utf8');

  // クラス名を取得
  const classNameMatch = javaCode.match(/public class (\w+)/);
  if (classNameMatch) {
    const className = classNameMatch[1];
    const javaFileName = `${className}.java`;
    const newFilePath = path.join(__dirname, 'uploads', javaFileName);

    // ファイル名をクラス名に基づいてリネーム
    fs.renameSync(filePath, newFilePath);

    // Board\apache-maven-3.9.8\bin\my-app\src\main\java\com\example に保存
    const mavenProjectPath = path.join(boardPath, 'apache-maven-3.9.8', 'bin', 'my-app', 'src', 'main', 'java', 'com', 'example', javaFileName);

    // `package com.example;` を追記して保存
    const updatedJavaCode = `package com.example;\n\n${javaCode}`;
    fs.writeFileSync(mavenProjectPath, updatedJavaCode, 'utf8');

    console.log(`Compiling: javac "${newFilePath}"`);

    const javac = spawn('javac', [newFilePath], { encoding: 'buffer' });

    javac.stderr.on('data', (data) => {
      const errorMessage = iconv.decode(data, 'Shift_JIS');
      compileResult += `コンパイルエラー: ${errorMessage}`;
      console.error(`stderr: ${errorMessage}`);
    });

    javac.on('close', (code) => {
      if (code !== 0) {
        res.json({ compileResult: compileResult, testSuccess: false });
        return;
      }

      // JUnitテストの実行処理を追加
      const mavenProjectDir = process.env.MAVEN_PROJECT_DIR;
      const mvn = spawn('mvn', ['-Dtest=com.example.' + className + 'Test', 'test'], { cwd: mavenProjectDir, shell: true });

      const filteredStdout = new Transform({
        transform(chunk, encoding, callback) {
          const output = iconv.decode(chunk, 'Shift_JIS');
          compileResult += filterOutput(output);
          callback(null, output);
        }
      });

      mvn.stdout.pipe(filteredStdout);

      mvn.on('close', (code) => {
        testSuccess = (code === 0);
        if (testSuccess) {
          compileResult += '\nテストが成功しました。\n';
        } else {
          compileResult += '\nテストに失敗しました。\n';
        }

        // 新しい投稿データをpost.jsonに保存
        const postData = {
          fileName: javaFileName,
          content: javaCode,
          timestamp: new Date().toISOString()
        };
        posts.push(postData);
        savePosts();

        res.json({ compileResult: compileResult, testSuccess: testSuccess });
      });
    });
  } else {
    compileResult = 'クラス名が見つかりません。';
    res.json({ compileResult: compileResult, testSuccess: false });
  }
});

// ヒント生成のエンドポイント
app.post('/generate-hint', async (req, res) => {
  const problemId = req.body.problemId;
  const correctCodePath = path.join(boardPath, 'saveJava', `${problemId}.java`);
  const submittedCodePath = path.join(boardPath, 'apache-maven-3.9.8', 'bin', 'my-app', 'src', 'main', 'java', 'com', 'example', `${problemId}.java`);
  const testResultPath = path.join(boardPath, 'apache-maven-3.9.8', 'bin', 'my-app', 'target', 'custom-test-reports', `com.example.${problemId}Test.txt`);

  try {
    const correctCode = fs.readFileSync(correctCodePath, 'utf8');
    const submittedCode = fs.readFileSync(submittedCodePath, 'utf8');
    const testResult = fs.readFileSync(testResultPath, 'utf8');

    const prompt = `
      これは学生の提出コードと正解コードの比較結果です。
      正解コードを示唆しない範囲で適切なヒントを提供してください。

      正解コード:
      ${correctCode}

      提出コード:
      ${submittedCode}

      テスト結果:
      ${testResult}

      ヒント: 
    `;

    const response = await openai.chat.completions.create({
      model: 'gpt-4o-mini',
      messages: [
        { role: 'system', content: 'You are a helpful assistant that provides coding hints.' },
        { role: 'user', content: prompt },
      ],
      // max_tokens: 150,
      temperature: 0.7,
    });

    const hint = response.choices[0].message.content.trim();
    res.json({ hint });
  } catch (error) {
    console.error('Error generating hint:', error);
    res.status(500).json({ error: 'ヒント生成に失敗しました。' });
  }
});



// コードのコンパイルおよび実行のみの処理
app.post('/run', (req, res) => {
  const code = req.body.code;
  let executionResult = '';

  if (code.includes('public class')) {
    const classNameMatch = code.match(/public class (\w+)/);
    if (classNameMatch) {
      const className = classNameMatch[1];
      const javaFileName = `${className}.java`;
      const javaFilePath = path.join(uploadsDir, javaFileName);

      fs.writeFileSync(javaFilePath, code.trim());

      console.log(`Compiling: javac "${javaFilePath}"`);

      const javac = spawn('javac', [javaFilePath], { encoding: 'buffer' });

      javac.stderr.on('data', (data) => {
        const errorMessage = iconv.decode(data, 'Shift_JIS');
        executionResult += `コンパイルエラー: ${errorMessage}\n`;
      });

      javac.on('close', (code) => {
        if (code !== 0) {
          res.json({ executionResult: executionResult });
          return;
        }

        const java = spawn('java', ['-cp', uploadsDir, className], { encoding: 'buffer' });

        java.stdout.on('data', (data) => {
          executionResult += iconv.decode(data, 'Shift_JIS');
        });

        java.stderr.on('data', (data) => {
          const errorMessage = iconv.decode(data, 'Shift_JIS');
          executionResult += `実行エラー: ${errorMessage}\n`;
        });

        java.on('close', (code) => {
          res.json({ executionResult: executionResult });
        });
      });
    } else {
      res.json({ executionResult: 'クラス名が見つかりませんでした。' });
    }
  } else {
    res.json({ executionResult: 'Javaコードではありません。' });
  }
});

// problemsフォルダのJSONファイルを提供
app.use('/problems', express.static(path.join(__dirname, 'problems')));

// 投稿一覧の取得
app.get('/posts', (req, res) => {
  res.json(posts);
});

// 投稿を保存するエンドポイント
app.post('/save-post', (req, res) => {
  const postData = req.body;
  posts.push(postData);
  savePosts();
  res.json({ message: '投稿が保存されました' });
});

app.use(express.static('public'));

app.listen(port, host, () => {
  console.log(`Server running at http://${host}:${port}/`);
});

// プロセス終了時に posts.json をリセット
function resetPosts() {
  const initialPosts = [];
  fs.writeFileSync(postsFile, JSON.stringify(initialPosts, null, 2));
  console.log('posts.json has been reset.');
}

// プロセス終了イベントを監視
process.on('SIGINT', () => {
  resetPosts();
  process.exit();
});

process.on('SIGTERM', () => {
  resetPosts();
  process.exit();
});

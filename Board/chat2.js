const fs = require('fs');
const path = require('path');
const axios = require('axios');
const dotenv = require('dotenv');
dotenv.config({ path: 'C:/.env' }); // .envファイルのパスを指定

const apiKey = process.env.CHATGPT_API_KEY;
const jsonMakeDir = process.env.PATH_TO_JSON;

if (!apiKey) {
    console.error("APIキーが設定されていません。");
    process.exit(1);
}

// 引数からファイル名を取得
const args = process.argv.slice(2);
if (args.length === 0) {
    console.error("保存するファイル名の基になる引数が指定されていません。");
    process.exit(1);
}
const fileName = args[0] + '.json';

// AIの初期設定プロンプト（ResponseフォーマットのJsonモード設定）
const initialPrompt = `
### 設定
あなたは与えられた文章をResponseフォーマットのJson形式に変換するAIです.以下の要件に従って変換してください.

### 要件
・与えられる文章の構成は,JavaClass,STEP番号と名前,サブタイトル,作成手順,実行例の5つからなっています.それぞれを"step","subtitle","instructions","expectedOutput"に格納します.
・SampleのようにJsonオブジェクトキーの名前はJavaClassから読み取ってください.
・Sampleのように1つのSTEPにつき1つのオブジェクトキーを作成してください.
・外側のオブジェクト名は不要です.
・"expectedOutput"において入力例には文末に「<-」があります.Jsonで表記できるように「&lt;-」と記述してください.
・**important:"expectedOutput"において,ファイルから読み込んだ内容を全く変えずに生成してください.**

### Sample
{
  "Work41_1": {
    "step": "STEP1 mainメソッドの宣言",
    "subtitle": "プログラムのエントリーポイントの設定",
    "instructions": "mainメソッドを宣言し、「mainメソッド」と出力させてください。",
    "expectedOutput": "$ java Work41_1<br>mainメソッド"
  },
  "Work41_2": {
    "step": "STEP2 配列の定義",
    "subtitle": "三角形の辺の候補となる数値の配列の宣言",
    "instructions": "mainメソッド内で整数型の配列 numbers を宣言し、具体的な値を初期化します。この配列には三角形の辺の長さとして考えうる値が格納されます。また、配列の中身を適切にテストするため、配列をループで一つずつ出力するテストコードを追加します。",
    "expectedOutput": "$ java Work41_2<br>i = 45<br>i = 8<br>i = 3<br>i = 31<br>i = 1"
  }
}
`;

// メッセージを送信して結果を返す関数
async function sendMessage(message) {
    try {
        const response = await axios.post('https://api.openai.com/v1/chat/completions', {
            model: "gpt-4o", // 使用するモデルを指定
            messages: [
                { role: "system", content: initialPrompt }, // 初期設定のプロンプト
                { role: "user", content: message } // response.txtの内容をユーザー入力として扱う
            ],
        }, {
            headers: {
                'Authorization': `Bearer ${apiKey}`,
                'Content-Type': 'application/json'
            }
        });

        let apiContent = response.data.choices[0].message.content;

        // 「```json」と「```」を削除する処理
        apiContent = apiContent.replace(/```json|```/g, '').trim();

        return apiContent;  // 応答のコンテンツを返す
    } catch (error) {
        console.error("Error: ", error.response ? error.response.data : error.message);
        return null;  // エラー時にはnullを返す
    }
}

// APIの応答をファイルに保存する関数
async function saveResponseToFile(apiResponse) {
    if (apiResponse) {
        const filePath = path.join(jsonMakeDir, fileName);

        // ファイルが存在しない場合、新規作成
        try {
            if (!fs.existsSync(filePath)) {
                console.log(`ファイル ${filePath} は存在しません。新しく作成します。`);
            }

            // JSON形式で応答データを保存
            fs.writeFile(filePath, apiResponse, (err) => {
                if (err) {
                    console.error('JSONファイル書き込み中にエラー:', err);
                } else {
                    console.log(`APIの応答が ${filePath} に正常に保存されました。`);
                }
            });
        } catch (err) {
            console.error(`ファイルの作成・保存中にエラーが発生しました: ${err}`);
        }
    } else {
        console.error('保存する応答が存在しません。');
    }
}

// response.txtを読み込み、内容を取得する関数
function readResponseTxt() {
    const filePath = path.join('response_explain.txt');
    try {
        const data = fs.readFileSync(filePath, 'utf-8');
        return data;
    } catch (error) {
        console.error(`response_explain.txtの読み込み中にエラーが発生しました: ${error.message}`);
        return null;
    }
}

// メインの実行関数
async function main() {
    try {
        // response.txtの内容を読み込む
        const responseContent = readResponseTxt();

        if (!responseContent) {
            console.error('response_explain.txtの内容を読み取れませんでした。');
            return;
        }

        // response.txtの内容をAPIに送信
        const apiResponse = await sendMessage(responseContent);

        // APIの応答をファイルに保存
        await saveResponseToFile(apiResponse);

    } catch (error) {
        console.error(`メイン実行中にエラーが発生しました: ${error}`);
    }
}

main();

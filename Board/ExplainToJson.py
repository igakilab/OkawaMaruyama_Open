import sys
import json
import os
from dotenv import load_dotenv

# .env ファイルを読み込む
load_dotenv()

# 引数が指定されているかチェック
if len(sys.argv) < 2:
    raise ValueError("引数が指定されていません。保存するJSONファイルの名前（例: Work49）を引数として指定してください。")

# 引数からファイル名を取得
json_filename = sys.argv[1] + ".json"

# 環境変数からBoardのパスを取得
board_path = os.getenv('PATH_TO_BOARD')

if board_path is None:
    raise ValueError("環境変数 'PATH_TO_BOARD' が設定されていません。")

# テキストをJSON形式に変換する関数
def convert_to_json(input_text):
    steps = input_text.strip().split("\n\n")  # ステップごとに空行で区切る
    json_data = {}

    for step in steps:
        lines = step.strip().split("\n")
        
        # JavaClassの取得
        if len(lines) > 0 and ": " in lines[0]:
            class_name = lines[0].split(": ")[1].strip()  # JavaClassを取得
        else:
            print(f"エラー: 'JavaClass' が見つかりません。次の行でエラーが発生しました: {lines}")
            continue  # エラーがあった場合、そのステップをスキップ
        
        # サブタイトルと作成手順を取得
        subtitle = lines[1].split(": ")[1].strip()
        # 空白を削除して "作成手順:" を探す
        instructions_index = [i for i, line in enumerate(lines) if line.strip() == "作成手順:"][0] + 1
        # 空白行を無視しながら "実行例:" を探す
        example_start = next(i for i, line in enumerate(lines) if line.strip() == "実行例:")
        instructions = " ".join(lines[instructions_index:example_start]).strip()
        
        # 複数の実行例を取得し、「```」を除去
        example_lines = lines[example_start + 1:]
        example_lines = [line for line in example_lines if line != "```"]
        
        # expectedOutputを<br>で結合して整形
        expected_output = "<br>".join(example_lines).replace("<-", "&lt;-")

        # 抽出した情報をJSONデータに追加
        json_data[class_name] = {
            "step": f"STEP {len(json_data) + 1}: {subtitle}",
            "subtitle": subtitle,
            "instructions": instructions,
            "expectedOutput": expected_output
        }
    
    return json_data

# Boardのパスからresponse_explain.txtを参照
response_explain_file_path = os.path.join(board_path, "response_explain.txt")

# response_explain.txtの内容を読み込む
with open(response_explain_file_path, "r", encoding="utf-8") as file:
    input_text = file.read()

# JSONに変換
json_data = convert_to_json(input_text)

# JSONファイルを保存するパス
output_file = os.path.join(board_path, 'public', 'problems', json_filename)

# 新しいJSONファイルとして保存
with open(output_file, "w", encoding="utf-8") as json_file:
    json.dump(json_data, json_file, ensure_ascii=False, indent=4)

print(f"JSONファイルが正常に作成されました: {output_file}")
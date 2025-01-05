# 提案システムに用いた仕様やケーススタディに用いた問題情報
演習問題自動分割システムを使用する際に用いたプロンプトや分割する際の流れの説明及び，ケーススタディに使用された問題の詳細情報について掲載している．

### 分割プロンプト
separate2.js 85行目～381行目

### 仕様生成プロンプト
separate2.js 384行目～429行目

### Json生成プロンプト
chat2.js 25行目～50行目


### 分割の概要
1. promptsフォルダ内にある問題の仕様・解答例コード・実行例を読み込む.
2. 上記分割プロンプト内と1で読み込んだ内容を合わせて,OpenAiのAPIを用いて問題を分割する.分割された問題(APIの出力)はResponse.txtに保存される．
3. 分割された問題の内容を読み込み,正規表現を用いて,分割された問題のJUnitのテストコードを作成する.
4. 作成されたJUnitのテストコードが正常かを確かめるため,Mavenを用いてテストを行う.このとき,テストが失敗すれば,2からやり直す.
5. テストが成功すると,上記仕様生成プロンプトとResponse.txtの内容を合わせて,OpenAiのAPIを用いて問題の仕様生成を行う.この出力はResponse_Explain.txtに保存される.
6. 上記Json生成プロンプトとResponse_Explain.txtの内容を合わせて,OpenAiのAPIを用いて問題の仕様を実施システム上に表示するためのJsonファイルを生成する．


## その他

### ケーススタディで使用した問題情報

#### 使用した問題内容
問題X じゃんけんゲーム <br>
問題Y High&Low <br>
問題Z Hit&Blow <br>

promptsフォルダ内
#### 分割前の仕様及び解答例
問題X Test01.txt <br>
問題Y Test03.txt <br>
問題Z Test05.txt <br>

speciificationsフォルダ内
#### 分割後の仕様
問題X Test02.txt <br>
問題Y Test04.txt <br>
問題Z Test06.txt <br>

#### 分割後の解答例
path\to\Board\apache-maven-3.9.8\bin\my-app\src\main\java\com\example内参照

問題X Test02から始まるJavaファイル<br>
問題Y Test04から始まるJavaファイル<br>
問題Z Test06から始まるJavaファイル<br>

<br>

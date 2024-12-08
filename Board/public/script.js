document.addEventListener('DOMContentLoaded', function() {
  let problems = {};  // グローバルにproblemsオブジェクトを定義

  // サーバーが再起動されたかどうか確認
  fetch('/server-status')
    .then(response => response.json())
    .then(data => {
      const savedStartTime = localStorage.getItem('serverStartTime');
      if (savedStartTime !== String(data.serverStartTime)) {
        localStorage.clear();
        localStorage.setItem('serverStartTime', data.serverStartTime);
      }
    })
    .catch(error => console.error('サーバーのステータス取得に失敗しました:', error));

  // URLパラメータから問題IDを取得
  const urlParams = new URLSearchParams(window.location.search);
  const problemId = urlParams.get('problemId');

  // 前のステップのIDを取得
  const [work, step] = problemId.split('_');
  const previousStepId = `${work}_${parseInt(step) - 1}`;

  // JSONファイルのロード
  fetch(`/problems/${work}.json`)
    .then(response => response.json())
    .then(data => {
      problems = data;  // 取得したデータをグローバルなproblemsに格納

      // 現在のSTEPの問題を取得
      const currentProblem = problems[problemId];
      if (currentProblem) {
        document.getElementById('problemTitle').textContent = currentProblem.step;
        document.getElementById('problemSubtitle').textContent = currentProblem.subtitle;
        // JSONファイルのロード時に問題の説明と実行例を挿入
        document.getElementById('problemDescription').innerHTML = currentProblem.instructions.replace(/\n/g, '<br>') +
        `<div class="execution-example">実行結果:<br>${currentProblem.expectedOutput.replace(/\n/g, '<br>')}</div>`;
      } else {
        document.getElementById('problemTitle').textContent = 'エラー';
        document.getElementById('problemDescription').textContent = '問題が見つかりませんでした。';
      }

      // 前のSTEPのコードを表示
      const lastCode = localStorage.getItem(previousStepId + '_lastCode');
      document.getElementById('previousCode').innerHTML = lastCode ? `<b>前回のコード:</b><pre>${lastCode}</pre>` : 'コードがありません';
    })
    .catch(error => console.error('問題の読み込みに失敗しました:', error));

  // ファイルを選択してテストを実行する処理
  const form = document.getElementById('uploadForm');
  form.addEventListener('submit', function(event) {
    event.preventDefault(); // フォーム送信をキャンセル

    const formData = new FormData(form);
    const fileInput = document.getElementById('javaFile');
    const file = fileInput.files[0];

    // 提出されたコードをローカルストレージに保存
    if (file) {
      const reader = new FileReader();
      reader.onload = function(event) {
        const submittedCode = event.target.result;
        localStorage.setItem(problemId + '_lastCode', submittedCode);  // 現在のSTEPに対応するコードを保存
      };
      reader.readAsText(file);
    }

    fetch('/upload', {
      method: 'POST',
      body: formData
    })
    .then(response => response.json())
    .then(data => {
      document.getElementById('compileResult').textContent = data.compileResult;

      // テストが成功した場合、次のステップへ進むボタンを表示
      if (data.testSuccess) {
        const nextStepButton = document.getElementById('nextStepButton');
        nextStepButton.style.display = 'block';

        // ボタンクリックイベントを正しく設定
        nextStepButton.onclick = function() {
          const nextStep = parseInt(step) + 1;
          const nextProblemId = `${work}_${nextStep}`;
          
          // 次の問題が存在するか確認して遷移
          if (problems[nextProblemId]) {
            window.location.href = `board.html?problemId=${nextProblemId}`;
          } else {
            alert('次の問題が見つかりません。');
          }
        };
      } else {
        hintButton.style.display = 'block'; // テスト失敗でヒントボタンを表示
      }

      loadPosts();
    })
    .catch(error => console.error('Error:', error));
  });

  // ヒントボタンの作成と初期設定
  const hintButton = document.createElement('button');
  hintButton.textContent = "ヒントを取得";
  hintButton.style.display = 'none'; // 初期状態では非表示
  document.body.appendChild(hintButton);

  hintButton.addEventListener('click', function() {
    fetch('/generate-hint', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ problemId })
    })
    .then(response => response.json())
    .then(data => {
      alert(data.hint); // ヒントをアラートで表示。必要に応じてカスタマイズ可能
    })
    .catch(error => console.error('ヒント生成エラー:', error));
  });

  // 投稿の表示を更新する関数
  function loadPosts() {
    fetch('/posts')
      .then(response => response.json())
      .then(data => {
        const postsContainer = document.getElementById('posts');
        postsContainer.innerHTML = '';
        data.forEach(post => {
          const postElement = document.createElement('div');
          postElement.classList.add('post');
          const textElement = document.createElement('pre');
          textElement.textContent = post.content;
          postElement.appendChild(textElement);
          if (post.filePath) {
            const fileLink = document.createElement('a');
            fileLink.href = post.filePath;
            fileLink.textContent = 'Uploaded file';
            postElement.appendChild(fileLink);
          }
          postsContainer.appendChild(postElement);
        });
      });
  }

  loadPosts();
});

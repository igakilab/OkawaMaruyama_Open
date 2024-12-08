document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const problemId = urlParams.get('problemId');  // 問題IDをURLから取得
  
    // JSONファイルのパス
    const jsonFilePath = `/problems/${problemId}.json`;
  
    // JSONファイルをロード
    fetch(jsonFilePath)
      .then(response => response.json())
      .then(data => {
        const stepLinksContainer = document.getElementById('stepLinks');
        const problemTitle = document.getElementById('problemTitle');
  
        // JSONの最初のオブジェクトを取得
        const firstStep = data[Object.keys(data)[0]];
  
        // 問題タイトルを設定 (titleがなければデフォルトのタイトルを使用)
        problemTitle.textContent = firstStep.title || `問題 ${problemId} の目次`;
  
        // 各STEPのリンクを生成
        Object.keys(data).forEach(stepKey => {
          const step = data[stepKey];
  
          // STEPのリンクを作成
          const stepLink = document.createElement('a');
          stepLink.href = `board.html?problemId=${stepKey}`;
          stepLink.className = 'step-link';
          stepLink.textContent = step.step;  // STEP名をリンクのテキストとして表示
  
          // リンクをコンテナに追加
          stepLinksContainer.appendChild(stepLink);
        });
      })
      .catch(error => console.error('問題の読み込みに失敗しました:', error));
  });
  
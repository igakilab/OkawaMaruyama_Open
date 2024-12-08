package saveJava;

import java.util.Scanner;

public class Test06_4 {
  public static void main(String[] args) {
    String title = "\nHit&Blow\n";
    System.out.println(title);

    Scanner scanner = new Scanner(System.in);
    boolean conti = true; // ゲームを続けるかどうかを判定
    while (conti) {
      int numberOfAnswers = 3; // 答えの数字の数を固定
      int[] answer = new int[numberOfAnswers]; // 答えが入る
      while (true) {
        System.out.println("一意な整数を3つ入力してください（例: 12 34 56）:");
        int input1 = scanner.nextInt();
        int input2 = scanner.nextInt();
        int input3 = scanner.nextInt();

        answer[0] = (input1 % 9) + 1;
        answer[1] = (input2 % 9) + 1;
        answer[2] = (input3 % 9) + 1;

        // 重複チェック
        if(answer[0] != answer[1] && answer[1] != answer[2] && answer[0] != answer[2]){
          break;
        } else {
          System.out.println("生成された数字が重複しています。別の値を入力してください。");
        }
      }

      // テスト用コード
      System.out.println("答えの生成ロジック完了: " + answer[0] + ", " + answer[1] + ", " + answer[2]);
      conti = false; // 仮でループを一回で終了
    }
    
    scanner.close();
  }
}
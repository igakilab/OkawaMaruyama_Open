package com.example;

import java.util.Scanner;

public class Test06_5 {
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

      // ゲーム部
      System.out.println("数字を" + numberOfAnswers + "個SPACE区切りで入力してください");
      int count = 0; // チャレンジ回数
      int[] input = new int[numberOfAnswers]; // 入力が入る
      int hit = 0; // ヒットの数
      int blow = 0; // ブローの数

      count++;
      System.out.println("[" + count + "回目]");

      // 入力値を取得
      for (int i = 0; i < numberOfAnswers; i++) {
        input[i] = scanner.nextInt();
      }

      // 答え判断
      hit = 0;
      blow = 0;
      for (int i = 0; i < numberOfAnswers; i++) {
        for (int j = 0; j < numberOfAnswers; j++) {
          if (i == j && input[i] == answer[j]) {
            hit++;
          } else if (input[i] == answer[j]) {
            blow++;
          }
        }
      }

      // テスト用コード
      System.out.println("ゲームロジックの雛形完了");
      conti = false; // 仮でループを一回で終了
    }
    
    scanner.close();
  }
}
package com.example;

import java.util.Scanner;

public class Test06_3 {
  public static void main(String[] args) {
    String title = "\nHit&Blow\n";
    System.out.println(title);

    Scanner scanner = new Scanner(System.in);
    boolean conti = true; // ゲームを続けるかどうかを判定
    while (conti) {
      // テスト用コード
      System.out.println("ループ内");
      conti = false; // 仮でループを一回で終了
    }
    
    scanner.close();
  }
}
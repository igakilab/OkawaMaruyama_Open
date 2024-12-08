package com.example;

import java.util.Scanner;

public class Test02_2 {
  public static void main(String[] args) {
    System.out.println("[じゃんけんゲームを何回戦実施するか入力してください]");
    Scanner sc = new Scanner(System.in);
    int gameNum = sc.nextInt();
    while (gameNum <= 0) {
      System.out.println("0以下の数値が入力されたのでもう一度入力し直しましょう");
      gameNum = sc.nextInt();
    }

    System.out.println("[CPUの手として任意の数字をゲーム数分だけ入力してください]");
    int[] cpuHands = new int[gameNum];
    for (int i = 0; i < gameNum; i++) {
      cpuHands[i] = sc.nextInt();
    }
    // テスト用の出力
    for (int num : cpuHands) {
      System.out.print(num + " ");
    }
    System.out.println();
  }
}
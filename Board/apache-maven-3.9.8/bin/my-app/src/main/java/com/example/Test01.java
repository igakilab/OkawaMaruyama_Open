package com.example;

import java.util.Scanner;

public class Test01 {
  public static void main(String[] args) {
    System.out.println("[じゃんけんゲームを何回戦実施するか入力してください]");
    Scanner sc = new Scanner(System.in);
    int gamenum;
    gamenum = sc.nextInt();
    while (gamenum < 0) {
      System.out.println("0以下の数値が入力されたのでもう一度入力し直しましょう");
      gamenum = sc.nextInt();
    }

    int[] cpuHands;
    cpuHands = new int[gamenum];
    int cpuHandNum = 0;
    System.out.println("[CPUの手として任意の数字をゲーム数分だけ入力してください]");
    while (cpuHandNum < gamenum) {
      int cpuHand;
      cpuHand = sc.nextInt();
      cpuHands[cpuHandNum] = cpuHand;
      cpuHandNum++;
    }

    System.out.println("CPUの手を決定します");
    DcideCPUHand(cpuHands);

    int round = 0;
    int win = 0;
    int lose = 0;
    int draw = 0;
    while (round < gamenum) {
      System.out.println("[あなたの手を入力してください|gu/choki/pa]");
      String userHand = "";
      userHand = sc.next();
      int HandNum = Hand2Num(userHand);

      int janken = HandNum - cpuHands[round];
      // 0:gu、1:choki、2:pa
      if (HandNum == -1) {
        System.out.println("手の入力が間違っていたのであなたの負けです");
        lose++;
      } else {
        if (janken == 0) {
          System.out.println("引き分けです");
          draw++;
        } else {
          if (janken == 2 || janken == -1) {
            System.out.println("あなたの勝ちです");
            win++;
          } else {
            System.out.println("あなたの負けです");
            lose++;
          }
        }
      }
      round++;
    }
    System.out.println("じゃんけんを終了します");
    System.out.println("対戦成績は" + win + "勝" + lose + "敗" + draw + "引き分けでした");
    sc.close();
  }

  public static void DcideCPUHand(int cpuHand[]) {
    for (int i = 0; i < cpuHand.length; i++) {
      if (cpuHand[i] < 0) {
        System.out.println("負の数は正の数に変換します");
        cpuHand[i] = cpuHand[i] * -1;
      }
      cpuHand[i] = cpuHand[i] % 3;
    }
  }

  public static int Hand2Num(String userHand) {
    switch (userHand) {
      case "gu":
        return 0;

      case "choki":
        return 1;

      case "pa":
        return 2;

      default:
        return -1;
    }
  }
}

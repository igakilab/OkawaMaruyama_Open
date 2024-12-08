package saveJava;

import java.util.Scanner;

public class Test02_4 {
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

    decideEnemyHand(cpuHands);
  }

  public static void decideEnemyHand(int[] hands) {
    System.out.println("CPUの手を決定します");
    for (int i = 0; i < hands.length; i++) {
      if (hands[i] < 0) {
        System.out.println("負の数は正の数に変換します");
        hands[i] = hands[i] * -1;
      }
      hands[i] = hands[i] % 3;
    }
    // テスト用の出力
    for (int hand : hands) {
      System.out.print(hand + " ");
    }
    System.out.println();
  }
}
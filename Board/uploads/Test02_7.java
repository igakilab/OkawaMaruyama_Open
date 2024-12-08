import java.util.Scanner;

public class Test02_7 {
  public static void main(String[] args) {
    System.out.println("[じゃんけんゲームを何回戦実施するか入力してください]");
    Scanner sc = new Scanner(System.in);

    int gamenum = sc.nextInt();
    while (gamenum < 0) {
      System.out.println("0以下の数値が入力されたのでもう一度入力し直しましょう");
      gamenum = sc.nextInt();
    }
    System.out.println("[CPUの手として任意の数字をゲーム数分だけ入力してください]");
    int[] cpuHands;
    cpuHands = new int[gamenum];
    for (int i = 0; i < gamenum; i++) {
      cpuHands[i] = sc.nextInt();
    }
    System.out.println("CPUの手を決定します");
    decideEnemyHand(cpuHands);
    /*
     * System.out.println("決定されたCPUの手: ");
     * for (int i = 0; i < gamenum; i++) {
     * System.out.print(cpuHands[i] + " ");
     * }
     */

    int round = 0;
    int win = 0;
    int lose = 0;
    int draw = 0;
    String playerHands = "";
    int playerHandNum = 0;
    while (round < gamenum) {
      System.out.println("[あなたの手を入力してください|gu/choki/pa]");
      playerHands = sc.next();
      playerHandNum = Hand2Num(playerHands);
      int i = Janken(playerHandNum, cpuHands[round]);
      if (i == 0) {
        draw++;
      }
      if (i == 1) {
        win++;
      }
      if (i == -1) {
        lose++;
      }
      round++;
    }

    System.out.println("じゃんけんを終了します");
    System.out.println("対戦成績は" + win + "勝" + lose + "敗" + draw + "引き分けでした");

  }

  public static void decideEnemyHand(int[] cpuHands) {
    for (int i = 0; i < cpuHands.length; i++) {
      if (cpuHands[i] < 0) {
        System.out.println("負の数は正の数に変換します");
        cpuHands[i] = cpuHands[i] * -1;
      }
      cpuHands[i] %= 3;
    }
  }

  // 0:グー、1:チョキ、2:パー
  public static int Janken(int user, int cpu) {
    int diff = user - cpu;
    // System.out.println(diff + "," + user + "," + cpu);
    // if (user == -1) {
    // return -1;
    // }
    if (diff == 0) {
      System.out.println("引き分けです");
      return 0;
    } else {
      if (diff == 2 || diff == -1) {
        System.out.println("あなたの勝ちです");
        return 1;
      } else {
        System.out.println("あなたの負けです");
        return -1;
      }
    }
  }

  public static int Hand2Num(String hand) {
    if (hand.contentEquals("gu")) {
      return 0;
    }
    if (hand.contentEquals("choki")) {
      return 1;
    }
    if (hand.contentEquals("pa")) {
      return 2;
    }
    return -1;
  }
}

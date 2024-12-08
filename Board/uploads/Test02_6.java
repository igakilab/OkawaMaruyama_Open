import java.util.Scanner;

public class Test02_6 {
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
    System.out.println("決定されたCPUの手: ");
    for (int i = 0; i < gamenum; i++) {
      System.out.print(cpuHands[i] + " ");
    }

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
}

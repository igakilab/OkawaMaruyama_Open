import java.util.Scanner;

public class Test02_5 {
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
    decideEnemyHand(cpuHands);

  }

  public static void decideEnemyHand(int[] cpuHands) {
    System.out.println("decideEnemyHandメソッドが呼び出されました");
  }
}

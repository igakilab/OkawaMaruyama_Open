import java.util.Scanner;

public class Test02_3 {
  public static void main(String[] args) {
    System.out.println("[じゃんけんゲームを何回戦実施するか入力してください]");
    Scanner sc = new Scanner(System.in);

    int gamenum = sc.nextInt();
    while (gamenum < 0) {
      System.out.println("0以下の数値が入力されたのでもう一度入力し直しましょう");
      gamenum = sc.nextInt();
    }
    System.out.println("入力された試合回数: " + gamenum);
  }
}

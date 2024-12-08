package saveJava;

import java.util.Scanner;

public class Test02_1 {
  public static void main(String[] args) {
    System.out.println("[じゃんけんゲームを何回戦実施するか入力してください]");
    Scanner sc = new Scanner(System.in);
    int gameNum = sc.nextInt();
    while (gameNum <= 0) {
      System.out.println("0以下の数値が入力されたのでもう一度入力し直しましょう");
      gameNum = sc.nextInt();
    }
    // テスト用の出力
    System.out.println("ゲーム回数: " + gameNum);
  }
}
package saveJava;

import java.util.Scanner;

/**
 * High&Lowとはトランプを一枚ずつめくり,現在のカードに対して次のカードが大きい(high)か小さい(low)かを予想するゲームである
 */
public class Test01_8 {
  public static void main(String[] args) {
    System.out.println("「3桁以上の数字を6つ入力してください」");
    Scanner sc = new Scanner(System.in);
    int[] cardNum = new int[6];

    // 数字が3桁かと数が6つかのチェックが必要かも
    for (int i = 0; i < 6; i++) {
      cardNum[i] = Integer.parseInt(sc.next()) % 13;
    }
    System.out.println("High and Low Start");

    int win = 0, lose = 0, draw = 0;

    for (int i = 0; i < 5; i++) {
      System.out.print("現在の");
      drawCardNum(cardNum[i]);

      System.out.println("「次のカードがhighかlowかあなたの予想を入力してください (降りる場合はexitと入力してください)」");
      String userHand = sc.next();

      if (userHand.equalsIgnoreCase("exit")) {
        System.out.println("ゲームを終了します");
        break;
      }

      System.out.print("次の");
      drawCardNum(cardNum[i + 1]);
      String result = judgeResult(userHand, cardNum[i], cardNum[i + 1]);

      switch (result) {
        case "win":
          win++;
          System.out.println("あなたの勝ちです");
          break;
        case "lose":
          lose++;
          System.out.println("あなたの負けです");
          break;
        case "draw":
          draw++;
          System.out.println("引き分けです");
          break;
      }
    }

    System.out.println("ゲーム終了");
    showResults(win, lose, draw);
    sc.close();
  }

  static String judgeResult(String userHand, int first, int second) {
    if (userHand.equals("high")) {
      if (second - first > 0) {
        return "win";
      } else if (second == first) {
        return "draw";
      } else {
        return "lose";
      }
    } else if (userHand.equals("low")) {
      if (second - first < 0) {
        return "win";
      } else if (second == first) {
        return "draw";
      } else {
        return "lose";
      }
    } else {
      return "lose";// 入力がhighでもlowでもない場合
    }
  }

  static void showResults(int win, int lose, int draw) {
    System.out.println("勝敗引き分けの数");
    System.out.println(" win:" + win);
    System.out.println(" lose:" + lose);
    System.out.println(" draw:" + draw);
  }

  static void drawCardNum(int num) {
    System.out.print("カードは");
    switch (num) {
      case 0:
        System.out.print("A");
        break;
      case 10:
        System.out.print("J");
        break;
      case 11:
        System.out.print("Q");
        break;
      case 12:
        System.out.print("K");
        break;
      default:
        System.out.print(num + 1);
    }
    System.out.println("です");
  }
}
package saveJava;

import java.util.Scanner;
import java.util.Arrays;

public class Test01_9 {
  public static void main(String[] args) {
    System.out.println("「3桁以上の数字を6つ入力してください」");
    Scanner sc = new Scanner(System.in);
    int coins = 2;
    int[] cardNum = new int[6];
    for (int i = 0; i < 6; i++) {
      cardNum[i] = Integer.parseInt(sc.next()) % 13;
    }
    System.out.println("High and Low Start");

    int win = 0, lose = 0, draw = 0;
    int[] coinCnt = new int[6];
    Arrays.fill(coinCnt, -1);
    coinCnt[0] = coins;

    for (int i = 0; i < 5; i++) {
      System.out.print("現在の");
      drawCardNum(cardNum[i]);

      System.out.println("「賭けるコインの枚数を入力してください/現在" + coins + "枚」");
      int bet = Integer.parseInt(sc.next());
      while (bet < 0 || coins < bet) {
        System.out.println("「不正な枚数のため入力し直して下さい」");
        bet = Integer.parseInt(sc.next());
      }
      if (bet == 0) {
        System.out.println("0が入力されたので,勝負を降ります");
        break;
      }

      System.out.println("「次のカードがhighかlowかあなたの予想を入力してください」");
      String userHand = sc.next();

      System.out.print("次の");
      drawCardNum(cardNum[i + 1]);
      String result = judgeResult(userHand, cardNum[i], cardNum[i + 1]);
      switch (result) {
        case "win":
          win++;
          break;
        case "lose":
          lose++;
          break;
        case "draw":
          draw++;
          break;
      }
      coins = calcCoin(result, bet, coins);
      coinCnt[i + 1] = coins;
      if (coins <= 0) {
        System.out.println("コイン枚数が0になったので終了します");
        break;
      }
    }
    System.out.println("あなたのコイン枚数は" + coins + "枚です");

    sc.close();
  }

  static int calcCoin(String result, int bet, int coins) {
    if (result.equals("win")) {
      System.out.println("あなたの勝ちです");
      System.out.println("あなたは" + bet + "枚のコインを得ました");
      coins = coins + bet;
    } else if (result.equals("lose")) {
      System.out.println("あなたの負けです");
      System.out.println("あなたは" + bet + "枚のコインを失いました");
      coins = coins - bet;
    } else if (result.equals("draw")) {
      System.out.println("引き分けです");
      System.out.println("賭けたコインがそのまま返却されます");
    }
    return coins;
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
      return "lose";
    }
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
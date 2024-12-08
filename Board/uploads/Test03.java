import java.util.Scanner;
import java.lang.String;

public class Test03 {
  public static void main(String[] args) {
    System.out.println("「3桁以上の数字を3つ入力してください」");
    Scanner sc = new Scanner(System.in);
    int[] cards;
    cards = new int[3];
    for (int i = 0; i < 3; i++) {
      cards[i] = sc.nextInt();
    }

    System.out.println("High and Low Start");
    for (int i = 0; i < 3; i++) {
      cards[i] = num2card(cards[i]);
    }
    String playing[];
    playing = new String[3];
    for (int i = 0; i < 3; i++) {
      playing[i] = card2playing(cards[i]);
    }

    int win = 0;
    int lose = 0;
    int draw = 0;
    String input = "";

    for (int i = 0; i < 2; i++) {
      String first = playing[i];
      String second = playing[i + 1];
      System.out.println("現在のカードは" + first + "です");
      System.out.println("「次のカードがhighかlowかあなたの予想を入力してください (降りる場合はexitと入力してください)」");
      input = sc.next();
      if (input.equals("exit")) {
        System.out.println("ゲームを終了します");
        i = 3;
      } else {
        int HighAndLow = judge(input, first, second);
        System.out.println("次のカードは" + second + "です");

        if (HighAndLow == 0) {
          System.out.println("引き分けです");
          draw++;
        } else if ((HighAndLow == 1)) {
          System.out.println("あなたの勝ちです");
          win++;
        } else {
          System.out.println("あなたの負けです");
          lose++;
        }
      }
    }
    System.out.println("ゲーム終了");
    System.out.println("勝敗引き分けの数");
    System.out.println(" win:" + win);
    System.out.println(" lose:" + lose);
    System.out.println(" draw:" + draw);
    sc.close();
  }

  public static int num2card(int card) {
    return card %= 13;
  }

  public static String card2playing(int card) {
    switch (card) {
      case 0:
        return "A";

      case 10:
        return "J";

      case 11:
        return "Q";

      case 12:
        return "K";
      default:
        card = card + 1;
        String play = java.lang.String.valueOf(card);
        return play;
    }
  }

  public static int judge(String input, String first, String second) {
    int judge = 0;
    if (play2num(first) == play2num(second)) {
      return judge;
    }
    if (input.equals("high")) {
      if (play2num(first) < play2num(second)) {
        judge = 1;
        return judge;
      } else {
        judge = -1;
        return judge;
      }
    }
    if (input.equals("low")) {
      if (play2num(first) > play2num(second)) {
        judge = -1;
        return judge;
      } else {
        judge = 1;
        return judge;
      }
    }
    judge = -1;
    return judge;
  }

  public static int play2num(String play) {
    switch (play) {
      case "A":
        return 1;
      case "J":
        return 11;
      case "Q":
        return 12;
      case "K":
        return 13;
      default:
        return Integer.parseInt(play);
    }
  }
}

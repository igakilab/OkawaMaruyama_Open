package saveJava;

import java.util.Scanner;

public class Test01_2 {
  public static void main(String[] args) {
    System.out.println("「3桁以上の数字を3つ入力してください」");
    Scanner sc = new Scanner(System.in);
    int[] cardNum = new int[3];

    for (int i = 0; i < 3; i++) {
      cardNum[i] = Integer.parseInt(sc.next()) % 13;
    }
    System.out.println("High and Low Start");

    //テスト用コード
    for (int i = 0; i < 3; i++) {
      System.out.println(cardNum[i]);
    }
    sc.close();
  }
}
package saveJava;

import java.util.Scanner;

public class Work49_4 {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("身長(cm)と体重(kg)を入力してください");
    int height = Integer.parseInt(scanner.next());
    int weight = Integer.parseInt(scanner.next());
    calcBmi(height, weight);
    scanner.close();
  }

  public static double calcBmi(int height, int weight) {
    //テスト用の出力
    System.out.println("calcBmiメソッドが呼び出されました");
    return 0.0;
  }
}
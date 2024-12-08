package saveJava;

import java.util.Scanner;

public class Work49_6 {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("身長(cm)と体重(kg)を入力してください");
    int height = Integer.parseInt(scanner.next());
    int weight = Integer.parseInt(scanner.next());
    double bmi = calcBmi(height, weight);
    System.out.printf("計算されたBMI: %.1f\n", bmi);
    String eval = evalWeight(bmi);
    scanner.close();
  }

  public static double calcBmi(int height, int weight) {
    return weight / (((double) height / 100) * ((double) height / 100));
  }

  public static String evalWeight(double bmi) {
    //テスト用の出力
    System.out.println("evalWeightメソッドが呼び出されました");
    return "";
  }
}
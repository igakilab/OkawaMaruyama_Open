package com.example;

import java.util.Scanner;

public class Work49_8 {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("身長(cm)と体重(kg)を入力してください");
    int height = Integer.parseInt(scanner.next());
    int weight = Integer.parseInt(scanner.next());
    double bmi = calcBmi(height, weight);
    String eval = evalWeight(bmi);
    System.out
        .println("身長は" + height + "cmで体重が" + weight + "kgの場合BMIは" + String.format("%.1f", bmi) + "で「" + eval + "」です");
    scanner.close();
  }

  public static double calcBmi(int height, int weight) {
    return weight / (((double) height / 100) * ((double) height / 100));
  }

  public static String evalWeight(double bmi) {
    String result;
    if (bmi < 18.5) {
      result = "低体重";
    } else {
      if (bmi >= 25) {
        result = "肥満";
      } else {
        result = "普通体重";
      }
    }
    return result;
  }
}
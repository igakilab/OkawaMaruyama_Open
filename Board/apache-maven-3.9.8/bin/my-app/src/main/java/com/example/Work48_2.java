package com.example;

public class Work48_2 {
  public static void main(String[] args) {
    int[] numbers = { 45, 8, 3, 31, 1, 9, 22 };

    for (int i = 0; i < numbers.length - 2; i++) {
      for (int j = i + 1; j < numbers.length - 1; j++) {
        for (int k = j + 1; k < numbers.length; k++) {
          //テスト用出力
          System.out.println("(" + numbers[i] + "," + numbers[j] + "," + numbers[k] + ")");
        }
      }
    }
  }
}
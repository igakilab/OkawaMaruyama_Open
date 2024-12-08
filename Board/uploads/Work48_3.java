package com.example;

public class Work48_3 {
  public static void main(String[] args) {
    int[] numbers = { 45, 8, 3, 31, 1, 9, 22 };
    
    for (int i = 0; i < numbers.length; i++) {
      for (int j = i + 1; j < numbers.length - 1; j++) {
        for (int k = j + 1; k < numbers.length - 2; k++) {
          // テスト用コード: 組み合わせを表示
          System.out.println("(" + numbers[i] + "," + numbers[j] + "," + numbers[k] + ")");
        }
      }
    }
  }
}
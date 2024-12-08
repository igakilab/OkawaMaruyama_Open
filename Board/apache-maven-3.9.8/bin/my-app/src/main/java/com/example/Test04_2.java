package com.example;

import java.util.Scanner;

public class Test04_2 {
    public static void main(String[] args) {
        System.out.println("「3桁以上の数字を3つ入力してください」");
        Scanner sc = new Scanner(System.in);
        int[] cardNum = new int[3];

        for (int i = 0; i < 3; i++) {
            cardNum[i] = Integer.parseInt(sc.next()) % 13;
        }

        // テスト用出力
        for (int num : cardNum) {
            System.out.println("変換されたカード番号: " + num);
        }
    }
}
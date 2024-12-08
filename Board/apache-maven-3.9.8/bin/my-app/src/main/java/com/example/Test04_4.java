package com.example;

import java.util.Scanner;

public class Test04_4 {
    public static void main(String[] args) {
        System.out.println("「3桁以上の数字を3つ入力してください」");
        Scanner sc = new Scanner(System.in);
        int[] cardNum = new int[3];

        for (int i = 0; i < 3; i++) {
            cardNum[i] = Integer.parseInt(sc.next()) % 13;
        }
        System.out.println("High and Low Start");

        int win = 0, lose = 0, draw = 0;

        for (int i = 0; i < 2; i++) {
            System.out.print("現在の");
            drawCardNum(cardNum[i]);
        }
        
        // テスト用出力
        System.out.println("カード表示完了");
    }

    // 0~12までの数字をトランプのA～Kまでに変換して返す
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
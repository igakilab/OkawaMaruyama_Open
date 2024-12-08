package saveJava;

import java.util.Scanner;

public class Test04_3 {
    public static void main(String[] args) {
        System.out.println("「3桁以上の数字を3つ入力してください」");
        Scanner sc = new Scanner(System.in);
        int[] cardNum = new int[3];

        for (int i = 0; i < 3; i++) {
            cardNum[i] = Integer.parseInt(sc.next()) % 13;
        }
        System.out.println("High and Low Start");

        int win = 0, lose = 0, draw = 0;
        // テスト用出力
        System.out.println("ゲーム初期化完了");
    }
}
package saveJava;

import java.util.Scanner;

public class Test01_13 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int length = 5;
        boolean validInput;
        String input;

        do {
            System.out.println("ボールの色を入力してください(例 pgrby)");
            input = scanner.nextLine();
            validInput = isValidInput(input);
        } while (!validInput);

        // 配列変換と答えの生成
        char[] balls = input.toCharArray();
        char[] target = { 'R', 'G', 'B', 'Y', 'P' };

        System.out.println("並べ替えゲームへようこそ");
        System.out.println("ボールをRGBYPの順に並べ替えてください");

        int moves = 0;
        while (!isSorted(balls, target)) {
            System.out.println("現在のボールの状態");
            printBalls(balls);

            System.out.println("交換する位置(0から4の番号)を2つ入力してください(例 0 1)");
            int pos1 = scanner.nextInt();
            int pos2 = scanner.nextInt();

            // スワップ処理
            swap(balls, pos1, pos2);
            moves++;
        }

        System.out.println("現在のボールの状態");
        printBalls(balls);
        System.out.println("正解です");
        System.out.println(moves + "回の操作で正しい順に並べました");

        scanner.close();
    }

    public static boolean isValidInput(String input) {
        if (input.length() != 5) {
            System.out.println("入力された文字列の長さが5ではありません");
            return false;
        }

        int[] counts = countBalls(input);
        if (counts == null) {
            // countBallsで無効な文字が検出された場合
            return false;
        }

        // 必要な文字が不足している場合
        for (int count : counts) {
            if (count == 0) {
                System.out.println("入力された文字列にrgbypすべてが含まれていません");
                return false;
            }
        }

        return true;
    }

    private static int[] countBalls(String input) {
        int[] counts = new int[5]; // 'r', 'g', 'b', 'y', 'p'のカウント
        for (char ball : input.toCharArray()) {
            switch (ball) {
                case 'r':
                    counts[0]++;
                    break;
                case 'g':
                    counts[1]++;
                    break;
                case 'b':
                    counts[2]++;
                    break;
                case 'y':
                    counts[3]++;
                    break;
                case 'p':
                    counts[4]++;
                    break;
                default:
                    System.out.println("入力された文字列にrgbyp以外の文字が含まれています");
                    return null; // 無効な入力
            }
        }
        return counts;
    }

    private static void swap(char[] balls, int pos1, int pos2) {
        char temp = balls[pos1];
        balls[pos1] = balls[pos2];
        balls[pos2] = temp;
    }

    private static void printBalls(char[] balls) {
        for (char ball : balls) {
            System.out.print(ball + " ");
        }
        System.out.println();
    }

    private static boolean isSorted(char[] balls, char[] correctArrangement) {
        boolean allMatch = true;

        for (int i = 0; i < balls.length; i++) {
            if (Character.toLowerCase(balls[i]) == Character.toLowerCase(correctArrangement[i])) {
                balls[i] = Character.toUpperCase(correctArrangement[i]); // 正しい位置のものを大文字に
            } else {
                balls[i] = Character.toLowerCase(balls[i]); // 正しくない場合は小文字に戻す
                allMatch = false;
            }
        }

        return allMatch;
    }
}
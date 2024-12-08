import java.util.Scanner;

public class Test02_2 {
  public static void main(String[] args) {
    System.out.println("[じゃんけんゲームを何回戦実施するか入力してください]");
    Scanner sc = new Scanner(System.in);

    int gamenum = sc.nextInt();

    System.out.println("入力された試合回数: " + gamenum);
  }
}

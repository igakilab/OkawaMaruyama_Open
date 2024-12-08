package saveJava;

import java.util.Scanner;

public class Work49_3 {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("身長(cm)と体重(kg)を入力してください");
    int height = Integer.parseInt(scanner.next());
    int weight = Integer.parseInt(scanner.next());
    System.out.println("身長: " + height + "cm, 体重: " + weight + "kg");
    scanner.close();
  }
}
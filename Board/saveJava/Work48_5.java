package saveJava;

public class Work48_5 {
  public static void main(String[] args) {
    int[] numbers = { 45, 8, 3, 31, 1, 9, 22 };
    for (int i = 0; i < numbers.length - 2; i++) {
      for (int j = i + 1; j < numbers.length - 1; j++) {
        for (int k = j + 1; k < numbers.length; k++) {
          if (isTriangle(numbers[i], numbers[j], numbers[k]) == true) {
            System.out.println("(" + numbers[i] + "," + numbers[j] + "," + numbers[k] + ")");
          }
        }
      }
    }
  }

  public static boolean isTriangle(int a, int b, int c) {
    if (a + b <= c) {
      return false;
    } else if (a + c <= b) {
      return false;
    } else if (b + c <= a) {
      return false;
    }
    return true;
  }
}
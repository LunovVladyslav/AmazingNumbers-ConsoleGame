import java.util.Scanner;

public class Sunny {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String input = sc.next();

        boolean res = isSunnyNumber(input);
    }

    static boolean isSunnyNumber(String input) {
        long tmp = Long.parseLong(input) + 1;
        return isSquareNumber(String.valueOf(tmp));
    }
    static boolean isSquareNumber(String input) {
        long tmp = Long.parseLong(input);
        double sqrt = Math.floor(Math.sqrt(tmp));

        return Math.pow(sqrt, 2) == tmp;
    }
}

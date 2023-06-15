//package numbers;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to Amazing Numbers!");
        printInstructions();

        while (true) {
            System.out.print("Enter a request: ");

            String input = sc.nextLine().trim();
            System.out.println();

            if (input.isEmpty() || input.isBlank()) {
                printInstructions();
            }

            if ("0".equals(input)) {
                System.out.print("Goodbye!\n");
                return;
            }

            String[] tmp = input.split(" ");

            if (!checkRightInput(tmp)) {
                continue;
            }

            if ( tmp.length > 1 ) {
                long num = Long.parseLong(String.valueOf(tmp[0]));
                int r = Integer.parseInt(String.valueOf(tmp[1]));

                if (tmp.length >= 3) {
                    String[] props = getUserProps(tmp);

                    if (!testUserProps(props)) {
                        continue;
                    }

                    filteredByProperty(num, r, props);
                    continue;
                }
                for (int i = r; i > 0; i--) {
                    String in = String.valueOf(num);
                    System.out.println(num + " is " + simpleOutput(in, num));
                    num++;
                }
            } else {
                input = tmp[0];
                long num = Long.parseLong(String.valueOf(tmp[0]));
                if (isNaturalNumber(num)) {
                    formatOutput(input, num);
                }
            }
        }
    }


    static String[] getUserProps(String[] arr) {
        StringBuilder props = new StringBuilder();
        StringBuilder unProps = new StringBuilder();

        for (int i = 2; i < arr.length; i++) {
            if (arr[i].startsWith("-")) {
                unProps.append(arr[i].substring(1)).append(" ");
                continue;
            }

            props.append(arr[i]).append(" ");
        }

        return new String[] {props.toString().trim(), unProps.toString().trim()};
    }
    static boolean checkRightInput(String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (i == 0) {
                try {
                    long num = Long.parseLong(String.valueOf(arr[i]));
                    if (!isNaturalNumber(num)) {
                        throw new NumberFormatException();
                    };
                } catch (Exception e) {
                    System.out.println("The first parameter should be a natural number or zero.");
                    return false;
                }
            } else if (i == 1) {
                try {
                    long num = Long.parseLong(String.valueOf(arr[i]));
                    if (!isNaturalNumber(num)) {
                        throw new NumberFormatException();
                    };
                } catch (Exception e) {
                    System.out.println("The second parameter should be a natural number or zero.");
                    return false;
                }
            }
        }
        return true;
    }

    static void printError(String prop, String mistake, boolean isUnProps) {
        String p = prop.toUpperCase();
        String m = mistake.toUpperCase();

        if (isUnProps) {
            p = "-" + p;
            m = "-" + m;
        }
        System.out.printf("The request contains mutually exclusive properties: [%s, %s]%n" +
                "There are no numbers with these properties.%n", p, m);
    }

    static boolean checkUserProps(String input, boolean isUnProps) {
        String[] tmpProps = input.split(" ");
        for (String prop : tmpProps) {
            String mistake = Property.valueOf(prop.toUpperCase()).getMistake();
            if (input.toUpperCase().contains(mistake) && input.toUpperCase().contains(prop.toUpperCase())) {
                printError(prop, mistake, isUnProps);
                return false;
            }
        }
        return true;
    }

    static void filteredByProperty(long input, int amount, String[] prop) {
        long tmpNum = input;
        int count = amount;
        String[] userProps = prop[0].split(" ");
        String[] userUnProps = prop[1].split(" ");

        while (count != 0) {
            int res = 0;
            boolean containUnProp = false;
            String tmpInput = String.valueOf(tmpNum);
            String[] tmpProps = simpleOutput(tmpInput, tmpNum).split(", ");

            for (String  tmpProp: tmpProps) {
                for (String currentProp : userProps) {
                    if (currentProp.equalsIgnoreCase(tmpProp) || currentProp.isEmpty()) {
                        res++;
                    }
                    for (String unProp : userUnProps) {
                        if (unProp.equalsIgnoreCase(tmpProp)) {
                            containUnProp = true;
                            break;
                        }
                    }
                }
            }

            if (res >= userProps.length && !containUnProp) {
                System.out.println(tmpInput + " is " + simpleOutput(tmpInput, tmpNum));
                count--;
            }

            tmpNum++;
        }
    }

    static boolean isCorrectProperty(String props, boolean isUnProps) {
        String[] myProps = props.split(" ");
        StringBuilder badProps = new StringBuilder();

        for (String prop : myProps) {
            int count = 0;
            for (Property el : Property.values()) {
                if (el.name().equalsIgnoreCase(prop)) {
                    count++;
                    break;
                }
            }
            if (count == 0) {
                badProps.append(prop.toUpperCase()).append(", ");
            }
        }

        if (badProps.length() > 0) {
            String[] countErrors = badProps.toString().split(", ");
            String myBadProps = badProps.substring(0, badProps.length() - 2);

            System.out.printf("The %s [%s] %s wrong.%nAvailable properties: [BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, EVEN, ODD, SUNNY, SQUARE, JUMPING, HAPPY, SAD]%n",
                    countErrors.length > 1 ? "properties" : "property", myBadProps, countErrors.length > 1 ? "are" : "is");
            return false;
        }

        return checkUserProps(props, isUnProps);
    }

    static boolean isBadUserReq(String[] props) {
        String[] userProps = props[0].split(" ");
        String[] userUnProps = props[1].split(" ");

        if (userProps.length == 0 || userUnProps.length == 0) {
            return true;
        }

        for (String prop : userProps) {
            for (String unProp : userUnProps) {
                if (prop.equalsIgnoreCase(unProp)) {
                    printError(prop, "-" + unProp, false);
                    return false;
                }
            }
        }

        return true;
    }

    static boolean testUserProps(String[] props) {
        for (String prop : props) {
            boolean isUnProps = (prop.equalsIgnoreCase(props[1]));

            if (!prop.isBlank() || !prop.isEmpty()) {
                if (!isCorrectProperty(prop, isUnProps)) {
                    return false;
                }
            }
        }

        return isBadUserReq(props);
    }

    static String formattedOutput(String input) {
        char[] tmp = new StringBuilder(input).reverse().toString().toCharArray();
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < tmp.length; i++) {
            if (i != 0 && i % 3 == 0) {
                res.append(',');
            }
            res.append(tmp[i]);
        }
        return res.reverse().toString();
    }
    static boolean isEvenNumber(long input) {
        return input % 2 == 0;
    }
    static boolean isNaturalNumber(long input) {
        if (input < 1) {
            return false;
        }
        return true;
    }
    static boolean isBuzzNumber(String input, long num) {
        if (input.endsWith("7") && num % 7 == 0) {
            return true;
        } else if (num % 7 == 0) {
            return true;
        } else
            return input.endsWith("7");
    }
    static boolean isDuckNumber(String input) {
        return input.lastIndexOf("0") > 0;
    }
    static boolean isPalindromeNumber(String input) {
        String tmp = new StringBuilder(input).reverse().toString();
        return tmp.equals(input);
    }
    static boolean isGapfulNumber(String input) {
        if (input.length() >= 3) {
            String tmp = String.valueOf(input.charAt(0)) + input.charAt(input.length() - 1);
            return Long.parseLong(input) % Long.parseLong(tmp) == 0;
        }
        return false;
    }
    static boolean isSpyNumber(String input) {
        int product = 1;
        int sum = 0;
        String[] tmp = input.split("");
        for (int i = 0; i < tmp.length; i++) {
            product *= Integer.parseInt(tmp[i]);
            sum += Integer.parseInt(tmp[i]);
        }
        return product == sum;
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
    static boolean isJumpingNumber(String input) {
        long[] tmp = fillLongArr(input);
        int counter = 0;

        if (tmp.length == 1) {
            return true;
        }

        for (int i = 0; i < tmp.length; i++) {
            if (i != tmp.length - 1) {
                long diff = tmp[i] - tmp[i + 1];
                if (diff == 1 || diff == -1) {
                    counter++;
                }
            }
        }

        return counter == tmp.length - 1;
    }

    static long[] fillLongArr(String input) {
        long[] tmp = new long[input.length()];
        String[] inputArr = input.split("");

        for (int i = 0; i < inputArr.length; i++) {
            tmp[i] = Long.parseLong(inputArr[i]);
        }

        return tmp;
    }
    static  boolean isHappyNumber(String input) {
        String tmpStr = input;
        boolean isHappy = false;

        while (true) {
            long[] tmp = fillLongArr(tmpStr);
            long res = 0;

            for (long num : tmp) {
                res += Math.pow(num, 2);
            }

            if (String.valueOf(res).length() != 1) {
                tmpStr = String.valueOf(res);
                continue;
            }

            if (res == 1) {
                isHappy = true;
            }
            break;
        }
        return isHappy;
    }
    static void printInstructions() {
        System.out.println("""
                Supported requests:
                - enter a natural number to know its properties;
                - enter two natural numbers to obtain the properties of the list:
                  * the first parameter represents a starting number;
                  * the second parameter shows how many consecutive numbers are to be processed;
                - two natural numbers and properties to search for;
                - a property preceded by minus must not be present in numbers;
                - separate the parameters with one space;
                - enter 0 to exit.
                """);
    }
    static void formatOutput (String input, long num) {
        System.out.printf("Properties of %s%n\t\teven: %s%n\t\todd: %s%n\t\tbuzz: %s%n\t\tduck: %s%n\t\s" +
                        "palindromic: %s%n\t\tgapful: %s%n\t\tspy: %s%n\t\tsunny: %s%n\t\tsquare: %s%n\t\tjumping: %s%n," +
                        "\t\thappy: %s%n\t\tsad: %s%n",
                formattedOutput(input), isEvenNumber(num), !isEvenNumber(num), isBuzzNumber(input, num),
                isDuckNumber(input), isPalindromeNumber(input), isGapfulNumber(input), isSpyNumber(input),
                isSunnyNumber(input), isSquareNumber(input), isJumpingNumber(input), isHappyNumber(input),
                !isHappyNumber(input));
    }
    static String simpleOutput(String input, long num) {
        String even = isEvenNumber(num) ? "even" : "odd";
        String buzz = isBuzzNumber(input, num) ? "buzz" : "";
        String duck = isDuckNumber(input) ? "duck" : "";
        String pal = isPalindromeNumber(input) ? "palindromic" : "";
        String gap = isGapfulNumber(input) ? "gapful" : "";
        String spy = isSpyNumber(input) ? "spy" : "";
        String sunny = isSunnyNumber(input) ? "sunny" : "";
        String square = isSquareNumber(input) ? "square" : "";
        String jumping = isJumpingNumber(input) ? "jumping" : "";
        String happyOrSad = isHappyNumber(input) ? "happy" : "sad";

        String[] tmp = { even, buzz, duck, pal, gap, spy, square, sunny, jumping, happyOrSad };
        StringBuffer res = new StringBuffer();

        for (String el : tmp) {
            if (!el.isEmpty() && !el.isBlank()) {
                res.append(el).append(", ");
            }
        }

        return res.substring(0, res.length() - 2);

    }
}

enum Property {
    EVEN("ODD"), ODD("EVEN"),
    BUZZ, DUCK("SPY"),
    PALINDROMIC, GAPFUL,
    SPY("DUCK"), SQUARE("SUNNY"),
    SUNNY("SQUARE"), JUMPING,
    HAPPY("SAD"), SAD("HAPPY");

    private String mistake;

    Property() {
        this.mistake = "NOPE";
    }
    Property(String mistake) {
        this.mistake = mistake;
    }

    String getMistake() {
        return mistake;
    }

}
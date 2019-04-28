import java.util.*;

public class RouletteV4 {
   public static void main(String[] args) {
      int[][] lastBet = new int[2][];
      int[] data = new int[3];
      lastBet[0] = data;
      Random r = new Random();
      Scanner input = new Scanner(System.in);
      giveIntro();
      play(r, input, lastBet);
   }
   
   // explains how to play the game and lists bet types
   public static void giveIntro() {
      System.out.println("Roulette is a gambling game where the player can make several different");
      System.out.println("types of bets involving choosing number(s) between 0 and 36. In this computerized");
      System.out.println("version, a number between 0 and 36 is randomly chosen and this determines if");
      System.out.println("the player wins or loses. There are two categories of bets a player can make, inside");
      System.out.println("and outside bets. Within each of these categories, there are several sub categories.");
      System.out.println("In inside bets, the player can make a single, split, trio, corner, five line, or six");
      System.out.println("line bet. A single bet is where the player places a bet on one number and one number");
      System.out.println("only and the payout is 35 to 1. A split bet is a bet on two numbers and the payout is");
      System.out.println("17 to 1. A trio bet is a bet on three numbers and the payout is 11 to 1. A corner bet");
      System.out.println("is a bet on four numbers and the payout is 8 to 1. A five line bet is a bet on five");
      System.out.println("numbers and the payout is 6 to 1. A six line bet is a bet on six numbers and the payout");
      System.out.println("is 5 to 1. In outside bets, the player can make a red/black, even/odd, high/low, dozen,");
      System.out.println("column, or snake bet. A red/black bet is where the player bets on the color of the number");
      System.out.println("and the payout is 1 to 1. Red numbers are 1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25,");
      System.out.println("27, 30, 32, 34, and 36. Black numbers are 2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26,");
      System.out.println("28, 29, 31, 33, and 35. An even/odd bet is where the player bets on whether the number");
      System.out.println("will be even or odd and the payout is 1 to 1. A high/low bet is where the player bets on");
      System.out.println("whether the number will be in the range 1-18 or 19-36 and the payout is 1 to 1. A dozen bet");
      System.out.println("is where the player bets on either the first(1-12), second(13-24), or third(25-36) dozen");
      System.out.println("numbers and the payout is 2 to 1. A column bet is where the player bets on either the");
      System.out.println("first(1, 4, 7, etc.), second(2, 5, 8, etc.), or third(3, 6, 9, etc.) column and the payout is");
      System.out.println("2 to 1. A snake bet is a special dozen bet consisting of the numbers 1, 5, 9, 12, 14, 16, 19,");
      System.out.println("23, 27, 30, 32, and 34. If any of the number(s) you bet on is selected, you win that bet.");
      System.out.println("After you have made one bet, you may enter \"same\" as a bet type to make the same bet again");
      System.out.println("If you enter same as a bet type and it rejects the input, it is because you do not have enough");
      System.out.println("money to bet the amount you bet last time. Enter \"exit\" when prompted for bet type to exit.");
      System.out.println();
      System.out.println("bet types:");
      System.out.println("    red        low        split");
      System.out.println("    black      dozen      trio");
      System.out.println("    even       column     corner");
      System.out.println("    odd        snake      five line");
      System.out.println("    high       single     six line");
      System.out.println();
   }
   
   // Asks the player for a value for starting money and checks for input error. The starting money
   // value must be greater than 0 and less than the max value an int can hold. The program
   // checks to make sure an int and only an int is entered before the player can proceed.
   // The user is next prompted for a bet type and the program will only accept one of the listed
   // bet types. The program continues to ask for bets until the player has no money left, at which
   // point it asks the player if they want to play again. This method takes a random, and scanner 
   // object as paramenters, as well as an int[][] that contains information about the last bet
   // the player made.
   public static void play(Random r, Scanner input, int[][] lastBet) {
      boolean hasBetAtLeastOnce = false;
      String strMoney = "";
      int money;
      boolean isInt = false;
      boolean isPositive = false;
      while ((!isInt) || (!isPositive)) { // checks to make sure the input is an integer and greater than zero
         System.out.print("starting money? ");
         strMoney = input.nextLine();
         isInt = isInt(strMoney);
         if (isInt) {
            isPositive = isPositive(strMoney);
            if (!isPositive) {
               System.out.println("please enter a positive number!");
            }
         } else { // input cannot be interpreted as an int
            System.out.println("invald input! please enter a positive number(< 2147483647)!");
         }
      }
      money = Integer.parseInt(strMoney); // at this point the string should contain only an int
      while (money > 0) { // continues to prompt user for bets until they have 0 dollars
         int[] numbers;
         boolean isValidBetType = false;
         String betType = "";
         while (!isValidBetType) {
            System.out.print("bet type? ");
            betType = input.nextLine();         
            isValidBetType = isValidBetType(betType, hasBetAtLeastOnce, lastBet);
            if (!isValidBetType) {
               System.out.println("invalid bet type!");
            }
         }
         if (betType.equalsIgnoreCase("same")) {
            money = playLast(r, lastBet);
            lastBet[0][0] = money;
            System.out.println();
         } else if (betType.equalsIgnoreCase("exit")) {
            System.out.println();
            System.out.println("You ended with $" + money + "!");          
            System.out.println();
            break;
         } else { // the player did not select "same" as their bet type or want to exit the program
            numbers = getNumbers(input, betType);
            lastBet[1] = numbers;
            int bet = getBet(input, money);
            lastBet[0][1] = bet;
            int modifier = getModifier(betType);
            lastBet[0][2] = modifier;
            money = placeBet(r, money, bet, numbers, modifier);
            lastBet[0][0] = money;
            hasBetAtLeastOnce = true;
            System.out.println();
         }
      }
      playAgain(r, input, lastBet);
   }
   
   
   // checks to make sure the input for bet type is valid and reprompts the player
   // for a bet type until a valid type is entered. Switches list of valid types to include
   // "same" if the player has bet at least once. Takes the bet type string, a boolean that contains
   // whether or not the player has bet at least once before, and an int[][] that contains information
   // about the players last bet as parameters. It returns whether or not the input the player
   // enter to select their bet type is valid or not via boolean.
   public static boolean isValidBetType(String betType, boolean hasBetAtLeastOnce, int[][] lastBet) {
      String[] betTypes;
      String[] a1 = {"exit", "red", "black", "even", "odd", "high", "low", "dozen", "column", "snake", "single", "split", "trio", "corner", "five line", "six line"};
      String[] a2 = {"exit", "same", "red", "black", "even", "odd", "high", "low", "dozen", "column", "snake", "single", "split", "trio", "corner", "five line", "six line"};
      if (hasBetAtLeastOnce && lastBet[0][1] <= lastBet[0][0]) { // second part of if makes sure you are not trying to bet more than the player has
         betTypes = a2;
      } else { // the player has not bet at least once or, if they have bet before, they don't have enough money to bet the same amount they bet last time
         betTypes = a1;
      }
      for (int i = 0; i < betTypes.length; i++) {
         if (betType.equalsIgnoreCase(betTypes[i])) {
            return true;
         }
      }
      return false;
   }
   
   // prompts the player for a bet amount and makes sure the number is an integer
   // and less than or equal to the current amount of money the player has. Continues
   // to reprompt the player until the input is valid. Takes a scanner object and
   // the current amount of money the player has as parameters. Returns the value
   // of the bet the player has entered.
   public static int getBet(Scanner input, int money) {
      String strBet = "";
      int bet = 0;
      boolean isInt = false;
      boolean isInRange = false;
      while ((!isInt) || (!isInRange)) {
         System.out.print("How much would you like to bet? ");
         strBet = input.nextLine();
         isInt = isInt(strBet);
         if (isInt) {
            isInRange = isInRange(strBet, 1, money);
            if (!isInRange) {
               System.out.println("invalid input! please enter a number great than zero and less than or equal to " + money + "!");
            }
         } else { // input cannot be interpreted as an int
            System.out.println("invalid input! please enter a number great than zero and less than or equal to " + money + "!");
         }
      }
      bet = Integer.parseInt(strBet);
      return bet;
   }
   
   // based on the bet type, returns the numbers as an integer array the numbers player has selected. 
   // If the player must select their numbers, it calls a method that prompts them to do so. Takes a 
   // scanner object and the bet type string as input.
   public static int[] getNumbers(Scanner input, String betType) {
      int[] redNums = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
      int[] blackNums = {2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35};
      int[] evenNums = {2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36};
      int[] oddNums = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35};
      int[] highNums = {19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36};
      int[] lowNums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
      int[] dozen1Nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
      int[] dozen2Nums = {13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
      int[] dozen3Nums = {25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36};
      int[] column1Nums = {1, 4, 7, 10, 13, 16, 19, 22, 25, 28, 31, 34};
      int[] column2Nums = {2, 5, 8, 11, 14, 17, 20, 23, 26, 29, 32, 35};
      int[] column3Nums = {3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36};
      int[] snakeNums = {1, 5, 9, 12, 14, 16, 19, 23, 27, 30, 32, 34};
      int[] inside;
      if (betType.equalsIgnoreCase("red")) {
         return redNums;
      } else if (betType.equalsIgnoreCase("black")) {
         return blackNums;
      } else if (betType.equalsIgnoreCase("even")) {
         return evenNums;
      } else if (betType.equalsIgnoreCase("odd")) {
         return oddNums;
      } else if (betType.equalsIgnoreCase("high")) {
         return highNums;
      } else if (betType.equalsIgnoreCase("low")) {
         return lowNums;
      } else if (betType.equalsIgnoreCase("snake")) {
         return snakeNums;
      } else if (betType.equalsIgnoreCase("column")) {
         int columnNum = getNum(input, "column");
         if (columnNum == 1) {
            return column1Nums;
         } else if (columnNum == 2) {
            return column2Nums;
         } else { // columnNum == 3
            return column3Nums;
         }
      } else if (betType.equalsIgnoreCase("dozen")) {
         int dozenNum = getNum(input, "dozen");
         if (dozenNum == 1) {
            return dozen1Nums;
         } else if (dozenNum == 2) {
            return dozen2Nums;
         } else { // dozenNum == 3
            return dozen3Nums;
         }
      } else { // bet type is single or split or trio or corner or five line or six line
         inside = insideBet(input, betType);
         return inside;
      }      
   }
   
   // prompts the player for the number of row or column if the player has selected that type of bet.
   // Also makes sure these numbers are valid before allowing the player to continue. Takes a scanner
   // object and the bet type string as a parameter and returns the number of the row or column that
   // the player wants to bet on.
   public static int getNum(Scanner input, String type) {
      String prompt = "";
      if (type.equals("column")) {
         prompt = "column number? ";
      } else { // type.equals("dozen")
         prompt = "dozen number? ";
      }
      String strNum = "";
      int num;
      boolean isInt = false;
      boolean isInRange = false;
      while ((!isInt) || (!isInRange)) {
         System.out.print(prompt);
         strNum = input.nextLine();
         isInt = isInt(strNum);
         if (isInt) {
            isInRange = isInRange(strNum, 1, 3);
            if (!isInRange) {
               System.out.println("invald input! please enter 1, 2, or 3!");
            }
         } else { // input cannot be interpretted as an int
            System.out.println("invald input! please enter 1, 2, or 3!");
         }
      }
      num = Integer.parseInt(strNum);
      return num;
   }
   
   // if the player selects a bet type that requires them to choose their numbers
   // this method prompts them to do so. Before continuing, makes sure the numbers
   // are betwen 0 and 36 and that they have not choosen the same number in the same bet. 
   // Takes a scanner object and the bet type string as a parameter and returns an integer 
   // array of the numbers the player has selected to bet on.
   public static int[] insideBet(Scanner input, String betType) {
      int max;
      if (betType.equalsIgnoreCase("single")) {
         max = 1;
      } else if (betType.equalsIgnoreCase("split")) {
         max = 2;
      } else if (betType.equalsIgnoreCase("trio")) {
         max = 3;
      } else if (betType.equalsIgnoreCase("corner")) {
         max = 4;
      } else if (betType.equalsIgnoreCase("five line")) {
         max = 5;
      } else { // betType.equalsIgnoreCase("six line")
         max = 6;
      }
      int[] numbers = new int[max];
      ArrayList<Integer> currentNums = new ArrayList<Integer>();
      String[] words = {"1st", "2nd", "3rd", "4th", "5th", "6th"};
      for (int i = 0; i < max; i++) {
         String strNum = "";
         int num = 0;
         boolean isInt = false;
         boolean isInRange = false;
         boolean isUnused = false;
         while ((!isInt) || (!isInRange) || (!isUnused)) {
            System.out.print(words[i] + " of " + max + " number(s)? ");
            strNum = input.nextLine();
            isInt = isInt(strNum);
            if (isInt) {
               isInRange = isInRange(strNum, 0, 36);
               if (!isInRange) {
                  System.out.println("invald input! please enter a number between 0 and 36!");
               }
               isUnused = isUnused(strNum, currentNums);              
               if (!isUnused) {
                  System.out.println("please choose a number you have not chosen before!");
               }
            } else { // input cannot be interpretted as an int
               System.out.println("invald input! please enter a number between 0 and 36!");
            }
         }
         num = Integer.parseInt(strNum);
         currentNums.add(num);
         numbers[i] = num;
      }
      return numbers;
   }
   
   // uses the numbers the player selected and compares them to a randomly drawn number.
   // Also computes the new amount of money the player will have. Takes a random object,
   // the current amount of money the player has, the amount of money the player has bet,
   // the integer array of numbers the player has selected to bet on, and the modifier number
   // as parameters. Returns the new amount of money the player has.
   public static int placeBet(Random r, int money, int bet, int[] numbers, int modifier) {
      int rand = r.nextInt(37);
      System.out.println("the number was: " + rand);
      boolean win = outcome(rand, numbers);
      if (win) {
         money = money + (bet * modifier);
         System.out.println("You won!");
      } else { // the player lost
         money = money - bet;
         System.out.println("You lost!");
      }
      System.out.println("You now have $" + money);
      return money; 
   }
   
   // returns true if one of the numbers the player selected was the number
   // for that round. Takes the random number between 0 and 36, and the array
   // of integers the player has bet on as parameters. 
   public static boolean outcome(int rand, int[] numbers) {
      for (int i = 0; i < numbers.length; i++) {
         if (numbers[i] == rand) {
            return true;
         }
      }
      return false;
   }
   
   // returns the modfier int to be applied to the bet if the player wins based
   // on the bet type. Takes the bet type string as a parameter to determine
   // the modifier.
   public static int getModifier(String betType) {
      int modifier = 0;
      if (betType.equalsIgnoreCase("red") || betType.equalsIgnoreCase("black") || betType.equalsIgnoreCase("even") || betType.equalsIgnoreCase("odd") || betType.equalsIgnoreCase("high") || betType.equalsIgnoreCase("low")) {
         modifier = 1;
      } else if (betType.equalsIgnoreCase("dozen") || betType.equalsIgnoreCase("column") || betType.equalsIgnoreCase("snake")) {
         modifier = 2;
      } else if (betType.equalsIgnoreCase("six line")) {
         modifier = 5;
      } else if (betType.equalsIgnoreCase("five line")) {
         modifier = 6;
      } else if (betType.equalsIgnoreCase("corner")) {
         modifier = 8;
      } else if (betType.equalsIgnoreCase("trio")) {
         modifier = 11;
      } else if (betType.equalsIgnoreCase("split")) {
         modifier = 17;
      } else { // betType.equalsIgnoreCase("single")
         modifier = 35;
      }
      return modifier;
   }
   
   // prompts the player to play again and makes sure the player enters either
   // "yes" or "no". Takes a scanner and random object as parameters as well as
   // a int[][] that contains information about the players last bet.
   public static void playAgain(Random r, Scanner input, int[][] lastBet) {
      String answer = "";
      boolean isValid = false;
      while (!isValid) {
         System.out.print("play again? ");
         answer = input.nextLine();
         answer = answer.toLowerCase();
         Scanner data = new Scanner(answer);
         if (!answer.contains(" ") && (answer.equals("yes") || (answer.equals("no")))) {
            isValid = true;
         } else { // input is not "yes" or "no"
            System.out.println("invald input! please enter yes or no!");
         }
      }
      if (answer.equals("yes")) {
         System.out.println();
         play(r, input, lastBet);
      }
   }
   
   // uses data from an array of integer arrays to play the last bet the player made if they select
   // that type of bet. Takes that int[][] and a random object as parameters. Returns the new amount
   // of money the player has after the bet.
   public static int playLast(Random r, int[][] lastBet) {
      int money = lastBet[0][0];
      int bet = lastBet[0][1];
      int[] numbers = lastBet[1];
      int modifier = lastBet[0][2];
      money = placeBet(r, money, bet, numbers, modifier);
      return money;
   }
   
   // returns via boolean whether or not if the player has already inputted the number they
   // are currently trying to input. Takes the current number the player is trying
   // to enter and an integer arraylist of numbers the player has already entered
   // as parameters.
   public static boolean isUnused(String strNum, ArrayList<Integer> currentNums) {
      int num = Integer.parseInt(strNum);
      for (int i = 0; i < currentNums.size(); i++) {
         if (currentNums.get(i) == num) {
            return false;
         }
      }
      return true;
   }
   
   // takes the player input as a string and determines if it is an int and returns
   // a boolean
   public static boolean isInt(String strInput) {
      Scanner data = new Scanner(strInput);
      if (!strInput.contains(" ") && data.hasNextInt()) {
         return true;
      } else { // input cannot be interpreted as an int
         return false;
      }
   }
   
   // takes the player input as a string and determines if it is positive
   // and returns a boolean
   public static boolean isPositive(String strInput) {
      int input = Integer.parseInt(strInput);
      if (input > 0) {
         return true;
      } else { // input is less than or equal to zero
         return false;
      }
   }
   
   // takes the player input as a string and determines if it is within the specified range
   // returns a boolean
   public static boolean isInRange(String strInput, int min, int max) {
      int input = Integer.parseInt(strInput);
      if (input < min || input > max) {
         return false;
      } else { // input is in range
         return true;
      }
   }
}

// todo
//    ...
//    numbers selected based on real roulete table
//    simulation
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private final Random rand = new Random();
    private final Scanner scanner = new Scanner(System.in);
    private Player friend;
    private Player you;
    private final Dice dice = new Dice(2, 6);

    public static class Dice {
        int numDice;
        int sides;
    

        public Dice(int numDice, int sides) {
            this.numDice = numDice;
            this.sides = sides + 1;
        }

        public record diceRoll(int roll, int d1, int d2){
            @Override
            public String toString() {
                return "%d + %d = %d".formatted(d1, d2, roll);
            }
        }
/*/
        public record diceRoll(
            boolean comeOut,
            boolean pass,
            boolean crap,
            boolean toPoint,
            int point
        ) {
            @Override
            public String toString() {
                if (comeOut == true) {
                    if (pass == true) {
                        return "passed and won";
                    } else if (crap == true) {
                        return "crapped and lost";
                    } else {
                        return "rolled %d for point".formatted(point);
                    }
                } else {
                    if (crap == true) {
                        return "crapped and lost";
                    } else if (pass == true) {
                        return "rolled point and won";
                    } else {
                        return "rolled %d.".formatted(point);
                    }
                }
                }
            }
            */
    }


    public static class Player {
        private int cash;
        public final String name;

        public Player(String name, Random rand) {
            cash = rand.nextInt(50, 101);
            this.name = name;
        }

        public boolean isBroke() {
            return cash < 1;
        }

        public Dice.diceRoll rollDice(Random rand, Dice dice) {
            int d1 = rand.nextInt(1,dice.sides);
            int d2 = rand.nextInt(1,dice.sides);
            int roll = d1 + d2;
            return new Dice.diceRoll(roll, d1, d2);
           // return new Dice.diceRoll(comeOut, pass, crap, toPoint, point);
        }

        public static void printStats(Player... plrs) {
            System.out.print(" ".repeat(9));
            for (Player col : plrs) {
                System.out.printf("%8s ", col);
            }
            System.out.println();

            System.out.print("-".repeat(26));
            //for (Player col : plrs) {
            //    System.out.printf("-".repeat(9));
            //}
            System.out.println();

            System.out.printf("%8s ", "Cash");
            for (Player col : plrs) {
                System.out.printf("%8d ", col.cash);
            }
            System.out.printf("%n%n");
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public Game() {
        System.out.println("While walking to your CS class, your degenerate friend entices you to a game of dice. What do you do?");
        System.out.println();
        System.out.println("1. Continue to class like aresponsible adult.");
        System.out.println("2. Play like the degenerate you are.");
    }
    
   

    public Optional<Player> rollForShooter(Player  you, Player friend, Dice dice) {
         
        Dice.diceRoll you_roll = you.rollDice(rand, dice);
        Dice.diceRoll friend_roll = friend.rollDice(rand, dice);
        System.out.printf("%n%s rolled %s%n", you, you_roll);
        System.out.printf("%n%s rolled %s%n", friend, friend_roll);
        if (you_roll.roll > friend_roll.roll) {
            return Optional.of(you);
        } else if (you_roll.roll < friend_roll.roll) {
            return Optional.of(friend);
        } else {
            return Optional.empty();
        }
        
        //return Optional.empty();
    }

    public int placeBets(Player shooter) {
        boolean shooterIsYou = (you == shooter) ? true : false;
        int bet = 0;
        while (shooterIsYou) {
            System.out.print("\nPlace your bet: $");
            bet = Integer.valueOf(scanner.nextLine());
            if (bet > you.cash) {
                System.out.println("You don't have that much dough.");
            } else if (bet > friend.cash) {
                System.out.printf("%n%s, doesn't have that much dough.", friend);
            } else {
                bet += bet;
                break;
            }
        }

        if (!shooterIsYou) {
            bet = rand.nextInt(1, 11);
            System.out.printf("%n%s bet $%d, will you match it? (Y/N)%n", friend, bet);
            switch (scanner.nextLine().toUpperCase()) {
                case "Y":
                    bet += bet;
                    break;
                case "N":
                    System.out.printf("%nFine, %s lowered the bet to $%d. You'd better win this or you'll be out of lunch money.%n", friend, you.cash);
                    bet = you.cash * 2;
                    break;
                default:
                    System.err.println("Invalid option");
            }
        }
        return bet;
    }

    public void playRound() {
        Player shooter = you;
        Player other = friend;
        boolean rollingForShooter = true;
        //boolean crapped = false;
        //boolean passed = false;
        boolean goesToPoint = false;
        //boolean shooterIsYou;
        int point = 0;
        int pool = 0;

        while (rollingForShooter) {
            Optional<Player> isShooter = rollForShooter(you, friend, dice);
            if (isShooter.isPresent()) {
                shooter = isShooter.get();
                System.out.printf("%n%s is the shooter!%n%n", shooter);
                other = (you == shooter) ? friend : you;
                rollingForShooter = false;
            } else {
                System.out.printf("%nTIE! Roll again...");
            }
        }

        pool += placeBets(shooter);

        Dice.diceRoll comeOutRoll = shooter.rollDice(rand, dice);
        if (comeOutRoll.roll == 7 || comeOutRoll.roll == 11) {
            System.out.printf("%n%s passed this round with %s and won.%n", shooter, comeOutRoll);
            shooter.cash += pool / 2;
            other.cash -= pool / 2;
        } else if (comeOutRoll.roll == 2 || comeOutRoll.roll == 3 || comeOutRoll.roll == 12) {
            System.out.printf("%n%s crapped out with %s and lost to %s.%n", shooter, comeOutRoll, other);
            shooter.cash -= pool / 2;
            other.cash += pool / 2;
        } else {
            System.out.printf("%n%s rolled %s and the game goes to point.%n", shooter, comeOutRoll);
            goesToPoint = true;
            point = comeOutRoll.roll;
        }

        while (goesToPoint){
            System.out.printf("%nPoint: %d%n", point);
            pool += placeBets(shooter);
            Dice.diceRoll pointRoll = shooter.rollDice(rand, dice);
            if (pointRoll.roll == 7) {
                System.out.printf("%n%s crapped out with %s and lost to %s.%n", shooter, pointRoll, other);
                shooter.cash -= pool / 2;
                other.cash += pool / 2;
                goesToPoint = false;
            } else if (pointRoll.roll == point) {
                System.out.printf("%n%s rolled point %s and won.%n", shooter, pointRoll);
                shooter.cash += pool / 2;
                other.cash -= pool / 2;
                goesToPoint = false;
            } else {
                System.out.printf("%n%s rolled %s. Place your bets and roll again.%n", shooter, pointRoll);
            }
        }
    }

    public void start() {
	    boolean stop = true;
	    while (stop) {
            System.out.print("\nSelect 1 or 2: ");
		    switch (scanner.nextLine()) {
                case "1":
            	    System.out.println("NERD!!!");
            	    System.exit(0);
                case "2":
            	    System.out.print("Enter your name: ");
            	    you = new Player(scanner.nextLine(), rand);
                    System.out.print("Enter your friend's name: ");
                    friend = new Player(scanner.nextLine(), rand);
				    stop = false;
			        break;
                default:
                    System.err.println("Invalid option");
            }
	    }
	run();
    }
    
    public void run() {
        do {
            System.out.println();
            Player.printStats(you, friend);

            if (you.isBroke()) {
                System.out.printf("%s ran out of money and lost, %s gloats as they fill their pockets with your cash.%n", you, friend);
                break;
            } else if (friend.isBroke()) {
                System.out.printf("%s ran out of money and lost, %s quickly gathers their winnings and moves away swiftly.%n", friend, you);
                break;
            }
            
        } while (startRound());
    }

    public boolean startRound() {
        System.out.println("Starting new round...");
        System.out.println();
        System.out.println("Roll for shooter? (Y/N)");
        switch (scanner.nextLine().toUpperCase()) {
            case "Y":
                playRound();
                return true;
            case "N":
                System.out.printf("%n%s took their money and ran...%n", you);
                return false;
            default:
                System.err.println("Invalid option");
        }
        return false;
    }

    public static void main(String[] args) {
        new Game().start();
    }
}

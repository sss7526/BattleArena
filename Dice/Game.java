import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private final Random rand = new Random();
    private final Scanner scanner = new Scanner(System.in);
    private Player friend;
    private Player you;
    private final Dice dice = new Dice(2, rand);

    public static class Dice {
        int numDice;
    

        public Dice(int numDice, Random rand) {
            this.numDice = numDice;
        }

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

        public Dice.diceRoll rollDice(Player shooter, Random rand) {
            int d1 = rand.nextInt(1,7);
            int d2 = rand.nextInt(1,7);
            int roll = d1 + d2;

            return new Dice.diceRoll(comeOut, pass, crap, toPoint, point);
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
    
   

    public Optional<Player> rollForShooter() {
         
        int you_roll = diceRoll(rand);
        int friend_roll = diceRoll(rand);
        if (you_roll > friend_roll) {
            return Optional.of(you);
        } else if (you_roll < friend_roll) {
            return Optional.of(friend);
        } else {
            return Optional.empty();
        }
        
        //return Optional.empty();
    }

    public void playRound() {
        boolean go = true;
        Optional<Player> isShooter = rollForShooter();
        while (go) {
            if (isShooter.isPresent()) {
                Player shooter = isShooter.get();
                System.out.printf("%n%s is the shooter!%n%n", shooter);
                Player other = (you == shooter) ? friend : you;
                rollDice(shooter, other);
                
            } else {
                System.out.printf("%nTIE! Roll again...");
            }
        }
        //friend.cash = 0;
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

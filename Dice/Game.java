import java.util.Random;
import java.util.Scanner;

public class Game {
    private final Random rand = new Random();
    private final Scanner scanner = new Scanner(System.in);
    private Player friend;
    private Player you;

    public static class Player {
        private int cash;
        public final String name;

        public Player(String name, Random rand) {
            cash = rand.nextInt(50, 101);
            this.name = name;
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
       // System.out.print("\nSelect 1 or 2: ");
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
                		System.out.print("Enter your freind's name: ");
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
        System.out.println("We got this far");
        System.out.printf("%s%n", you);
        System.out.printf("%s%n", friend);
    }

    public static void main(String[] args) {
        new Game().start();
    }
}

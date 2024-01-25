import java.util.Scanner;
import java.util.*;

class Dice {
    static int score;
    
    static void setScore(int pts) {
        Dice.score = pts;
    }

    static int getScore() {
        return Dice.score;
    }

    static int roll() {
        int d1 = Game.getRandom(6, 1);
        int d2 = Game.getRandom(6, 1);
        Dice.setScore(d1 + d2);
        System.out.println(d1 + "+" + d2);
        System.out.println(Dice.getScore() + "\n");
        return Dice.getScore();
    }
}

class Player {
    int cash;
    String name;
    
    public Player(String cname) {
        cash = Game.getRandom(100, 50);
        name = cname;
    }
}

public class Game {

    static void printStats(Player pl, Player pc) {
        System.out.println(pl.name + "\'s cash: $" + pl.cash);
        System.out.println(pc.name + "\'s cash: $" + pc.cash);
    }

    static Player getShooter(Player pl, Player pc, Dice d) {
        System.out.println("\nRolling for shooter...\n");
        System.out.println(pl.name + " rolled:");
        int plroll = d.roll();
        System.out.println(pc.name + " rolled:");
        int pcroll = d.roll();
        if (plroll > pcroll) {
            return pl;
        } else if (pcroll > plroll) {
            return pc;
        } else {
            System.out.println("Tie! Rolling again...\n");
            return getShooter(pl, pc, d);
        }
    }
    static int getRandom(int max, int min) {
        int num = min + (int)(Math.random() * ((max - min) + 1));
        return num;
    }

    static int getIntInput(String prompt) {
        System.out.println("\n" + prompt);
        Scanner scanObj = new Scanner(System.in);
        int response = scanObj.nextInt();
        return response;
    }

    static String getStrInput(String prompt) {
        System.out.println("\n" + prompt);
        Scanner scanObj = new Scanner(System.in);
        String response = scanObj.nextLine();
        return response;
    }

    static void printWelcome() {
        System.out.println("You\'re walking to your CS class when suddenly your degenerate friend entices you to a game of dice.\nWhat do you do?");
        System.out.println("1. Go to class like a responsible adult.");
        System.out.println("2. Skip class and play like the degenerate you are.");
        String option = getStrInput("");
        if (option.contentEquals("1")){
            System.out.println("NERD!!!");
            System.exit(0);
        } else {

        }
    }

    static int plBet(Player shooter, Player other, boolean shooterIsYou) {
        int bet;
        if (shooterIsYou == true) {
            bet = getIntInput("Place your bet:");
            System.out.println(other.name + " matched your bet.");
        } else {
            bet = getRandom(10, 1);
            System.out.println(shooter.name + " bet $" + bet);
            if (other.cash >= bet) {
                String match = "Match it? (Y/N)";
                boolean option = getStrInput(match).toUpperCase().contentEquals("Y") ? true : false;
                if (option == false) {
                    System.out.println("Later loser");
                    System.exit(0);
                } else {
                }
            } else {
                System.out.println("You don\'t have enough money.");
                System.exit(0);
            }
        }
        return bet * 2;
    }

    static void pointRound(Player shooter, Player other, int pool, int point, Dice d, boolean shooterIsYou) {
        boolean roundOver = false;
        while (roundOver == false) {
            System.out.println(shooter.name + " rolled:");
            int roll = d.roll();
            if (roll == point) {
                shooter.cash += pool;
                System.out.println(shooter.name + " rolled a pass and gained $" + pool);
                roundOver = true;
            } else if (roll == 7) {
                other.cash += pool;
                System.out.println(shooter.name + " rolled a 7 and " + other.name + "gained $" + pool);
                roundOver = true;
            } else {
                System.out.println(shooter.name + " rolled a " + roll);
                pool += plBet(shooter, other, shooterIsYou);
            }
        }
    }

    static void startRound(Player pl, Player pc, Dice d) {
        int pool = 0;
        int bet;
        boolean shooterIsYou;
        Player shooter = getShooter(pl, pc, d);
        System.out.println(shooter.name + " is shooter");
        Player other;
        boolean roundOver = false;

        if (pl.name.contentEquals(shooter.name)) {
            other = pc;
            shooterIsYou = true;
        } else {
            other = pl;
            shooterIsYou = false;
        }

        pool += plBet(shooter, other, shooterIsYou);
        System.out.println("Stakes: " + pool);

        while (roundOver == false) {
            System.out.println(shooter.name + " rolled:");
            int roll = d.roll();
            if (roll > 3 && roll < 12) {
                if (roll == 7 || roll == 11) {
                    shooter.cash += pool;
                    System.out.println(shooter.name + " rolled a pass and gained $" + pool);
                    roundOver = true;
                } else {
                     System.out.println(shooter.name + " rolled point");
                     pointRound(shooter, other, pool, roll, d, shooterIsYou);
                
                }
            } else {
                other.cash += pool;
                System.out.println(shooter.name +  " crapped out and " + other.name + " won $" + pool);
                roundOver = true;
            }
        }
    }

    public static void main(String[] args) {
        printWelcome();
        Dice dice = new Dice();
        Player player = new Player(getStrInput("Enter player\'s name:"));
        Player pc = new Player(getStrInput("Enter friend\'s name:"));
        printStats(player, pc);
        startRound(player, pc, dice);

    }
}
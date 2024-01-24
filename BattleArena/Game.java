import java.util.Scanner;
import java.io.*;

public class Game {
    public class Character {
        int health;
        int defense;
        int strength;
        String cname;
        int init;
    
    public Character(String name) {
        health = getRandom();
        defense = getRandom();
        strength = getRandom();
        cname = name;
        init = 0;
    }
}
    static int getRandom() {
        int num = 1 + (int)(Math.random() * ((100 - 1) + 1));
        return num;
    }

    static void cls() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    static String printWelcome() {
        cls();
        System.out.println("Welcome to the arena!");
        Scanner scanObj = new Scanner(System.in);
        System.out.println("Enter your hero\'s name:");
        String heroName = scanObj.nextLine();
        cls();
        return heroName;
    }

    static void printStats(Character c) {
        Console cnsl = System.console();
        String fmt = "%1$-10s %2$-1s%n";
        System.out.println("\n" + c.cname + "\'s Stats:\n---------------");
        cnsl.format(fmt, "Health:", c.health);
        cnsl.format(fmt, "Defense:", c.defense);
        cnsl.format(fmt, "Strength:", c.strength);
    }

    static void clash(Character h, Character e) {
        System.out.println("\n" + e.cname + " took a cheapshot!\n(Croud gasps)\nBut " + h.cname + " blocked it in the nick of time!\n(Croud Chears)\n");
        doBattle(h, e);
    }

    static Character roll(Character h, Character e) {
        h.init = getRandom();
        e.init = getRandom();
        if (h.init > e.init) {
            return h;
        } else if (h.init < e.init) {
            return e;
        } else {
            clash(h, e);
            return e;
        }
    }
    static void doBattle(Character h, Character e) {
        System.out.println("test");
        Character goesFirst = roll(h, e);
        System.out.println(goesFirst.cname + " takes initiative!\n");
        Character defender;
        if (h.cname == goesFirst.cname) {
            defender = e;
        } else {
            defender = h;
        }
        System.out.println(defender.cname);
    }

    static int getOption() {
        Scanner scanObj = new Scanner(System.in);
        System.out.println("\nEnter option: (1 to battle, 2 to escape!)");
        int option = scanObj.nextInt();
        return option;
    }

    public static void main(String[] args) {
        Game myGame = new Game();
        Game.Character hero = myGame.new Character(printWelcome());
        Game.Character enemy = myGame.new Character("Spock");
        System.out.println("\nAvast, " + hero.cname + "! Go forth!");
       // System.out.println("Health: \t" + hero.health + "\nDefense: \t" + hero.defense + "\nStrength: \t" + hero.strength);
       printStats(hero);
       printStats(enemy);
       while (hero.health > 0 && enemy.health != 0) {
            int option = getOption();
            if (option == 1) {
                doBattle(hero, enemy);
            } else if (option == 2) {
                System.out.println("byb");
                System.exit(0);
            } else {
                System.out.println("Invalid Option");
            }
       }
    }
}
import java.util.Scanner;
import java.io.*;
//new comment
public class Game {
    public class Character {
        int health;
        int defense;
        int strength;
        String cname;
        int init;
    
    public Character(String name) {
        health = getRandom(100, 50);
        defense = getRandom(100, 50);
        strength = getRandom(100, 50);
        cname = name;
        init = 0;
    }
}
    static int getRandom(int max, int min) {
        int num = min + (int)(Math.random() * ((max - min) + 1));
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
        h.init = getRandom(6, 1);
        e.init = getRandom(6, 1);
        if (h.init > e.init) {
            return h;
        } else if (h.init < e.init) {
            return e;
        } else {
            clash(h, e);
            return h;
        }
    }

    static void attack(Character a, Character d) {
       int apts;
       String aname = a.cname;
       String dname = d.cname;

       if (d.defense > a.strength) {
        apts = 1;
        d.defense = d.defense - ((d.defense % a.strength) + 1);
        System.out.println("\n" + dname + " blocked " + aname + "\'s attack and only got a scratch!\n(Croud chears)\n");
       } else {
        apts = a.strength - d.defense;
        d.health = d.health - apts;
        d.defense = d.defense - ((d.defense % apts) + 1);
        System.out.println("\n" + aname + " strikes " + dname + " for " + apts + " points of damage!\n(Croud gasps!)\n");
       }
       if (d.health < 1) {
        d.health = 0;
       }
       if (d.defense < 1) {
        d.defense = 0;
       }
    }

    static void doBattle(Character h, Character e) {
        Character goesFirst = roll(h, e);
        System.out.println(goesFirst.cname + " takes initiative!\n");
        Character defender;
        if (h.cname == goesFirst.cname) {
            defender = e;
        } else {
            defender = h;
        }
        attack(goesFirst, defender);
       // System.out.println(defender.cname);
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

      // printStats(hero);
      // printStats(enemy);
       while (hero.health > 0 && enemy.health > 0) {
            printStats(hero);
            printStats(enemy);
            int option = getOption();
            cls();
            if (option == 1) {
                doBattle(hero, enemy);
            } else if (option == 2) {
                System.out.println("YOU COWARD!");
                System.exit(0);
            } else {
                System.out.println("Invalid Option");
            }
       }
       printStats(hero);
       printStats(enemy);
       if (hero.health < 1) {
        System.out.println(enemy.cname + " defeated " + hero.cname + "!\n(Cround boos aggressively)\nSomeone from the croud yelled \"YOU SUCK!\"\n");
       } else {
        System.out.println(hero.cname + " utterly smote " + enemy.cname + "!\n(Croud ROARS)\n");
       }
    }
}

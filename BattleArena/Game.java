import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

public class Game {
    public record Hit(
        boolean blocked,
        int attack_points
    ) {
        @Override
        public String toString() {
            if (attack_points == 0) {
                return "no damage";
            } else if (attack_points == 1) {
                return "1 point of damage";
            } else {
                return "%d points of damage".formatted(attack_points);
            }
        }
    }

    public static class Character {
        private int health;
        private int defense;
        private int strength;
        public final String name;

        public Character(String name, Random rand) {
            health = rand.nextInt(50, 101);
            defense = rand.nextInt(50, 101);
            strength = rand.nextInt(50, 101);
            this.name = name;
        }

        public boolean isAlive() {
            return health > 0;
        }

        public Hit receiveHit(Character attacker) {
            int attack_points = Integer.max(0, attacker.strength - defense);
            boolean blocked = attack_points == 0;
            int modifier;

            if (blocked) {
                modifier = (defense % attacker.strength) + 1;
                defense -= modifier;
                attacker.strength -= modifier / 2;
            } else {
                modifier = (defense % attack_points) + 1;
                defense -= modifier;
                attacker.strength -= modifier / 2;
            }
            health = Integer.max(0, health - attack_points);
            return new Hit(blocked, attack_points);
        }

        private void printStats() {
            System.out.printf("%s's Stats:%n", name);
            System.out.println("---------------");
            final String fmt = "%-10s %-2d%n";
            System.out.printf(fmt, "Health:", health);
            System.out.printf(fmt, "Defense:", defense);
            System.out.printf(fmt, "Strength:", strength);
            System.out.println();
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private final Random rand = new Random();
    private final Scanner scanner = new Scanner(System.in);
    private final Character hero;
    private final Character enemy = new Character("Spock", rand);

    public Game() {
        System.out.println("Welcome to the arena!");
        System.out.print("Enter your hero's name: ");
        hero = new Character(scanner.nextLine(), rand);
        System.out.printf("Avast, %s! Go forth!%n", hero);
    }

    public void clash() {
        System.out.printf("%s takes a cheap shot!%n", enemy);
        System.out.println("(Crowd gasps)");
        System.out.printf("But %s blocks it in the nick of time!%n", hero);
        System.out.println("(Crowd cheers)");
    }

    public Optional<Character> rollInitiative() {
        int hero_initiative = rand.nextInt(1, 7),
            enemy_initiative = rand.nextInt(1, 7);
        if (hero_initiative > enemy_initiative) {
            return Optional.of(hero);
        } else if (hero_initiative < enemy_initiative) {
            return Optional.of(enemy);
        } else {
            return Optional.empty();
        }
    }

    public void attack(Character attacker, Character defender) {
        Hit hit = defender.receiveHit(attacker);
        if (hit.blocked) {
            System.out.printf("%s blocks %s's attack and takes %s!%n", defender, attacker, hit);
            System.out.println("(Crowd cheers)");
        } else {
            System.out.printf("%s strikes %s for %s!%n", attacker, defender, hit);
            System.out.println("(Crowd boos)");
        }
    }

    public void doBattle() {
        boolean go = true;
        while (go) {
            Optional<Character> goesFirst = rollInitiative();
            if (goesFirst.isPresent()) {
                Character attacker= goesFirst.get();
                System.out.printf("%s takes initiative!%n", attacker);
                Character defender = (hero == attacker) ? enemy : hero;
                attack(attacker, defender);
                go = false;
            } else {
                clash();
            }
        } 
    }

    public void run() {
        do {
            System.out.println();
            hero.printStats();
            enemy.printStats();

            if (!hero.isAlive()) {
                System.out.printf("%s defeats %s!%n", enemy, hero);
                System.out.println("(Crowd boos aggressively)");
                System.out.println("Someone frome the crowd yells \"YOU SUCK!\"");
                break;
            } else if (!enemy.isAlive()) {
                System.out.printf("%s utterly smites %s!%n", hero, enemy);
                System.out.println("(Crowd ROARS)");
                break;
            }
        } while (doRound());
    }

    private boolean doRound() {
        while (true) {
            System.out.print("Enter option (1 to battle, 2 to escape)! ");

            switch (scanner.nextLine()) {
                case "1":
                    doBattle();
                    return true;
                case "2":
                    System.out.println("YOU COWARD!");
                    return false;
                default:
                    System.err.println("Invalid option");
            }
        }
    }

    public static void main(String[] args) {
        new Game().run();
    }
}
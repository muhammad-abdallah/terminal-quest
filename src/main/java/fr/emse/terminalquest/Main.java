package fr.emse.terminalquest;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

/** Entry point for the executable JAR. */
public final class Main {
    private Main() { }

    public static void main(String[] args) {
        if (args.length == 1 && "--version".equals(args[0])) {
            System.out.println("Terminal Quest v" + VersionInfo.version());
            return;
        }
        GameEngine game = new GameEngine(new Random(), SaveRepository.defaultRepository());
        System.out.println("=== Terminal Quest v" + VersionInfo.version() + " ===");
        System.out.println("Clear five rooms and defeat the Ancient Dragon to win.");
        System.out.println(GameEngine.help());
        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            while (true) {
                System.out.print("\n> ");
                if (!scanner.hasNextLine()) break;
                String command = scanner.nextLine();
                System.out.println(game.execute(command));
                if (command.trim().equalsIgnoreCase("quit")
                        || command.trim().equalsIgnoreCase("exit")) break;
            }
        }
    }
}

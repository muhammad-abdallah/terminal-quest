package fr.emse.terminalquest;

import java.io.IOException;
import java.util.Locale;
import java.util.Random;

/** Game rules independent from terminal input/output, so they are testable. */
public final class GameEngine {
    private final Random random;
    private final SaveRepository saves;
    private GameState state = new GameState();

    public GameEngine(Random random, SaveRepository saves) {
        this.random = random;
        this.saves = saves;
    }

    public GameState getState() { return state; }

    public String execute(String input) {
        String command = input == null ? "" : input.trim().toLowerCase(Locale.ROOT);
        return switch (command) {
            case "help", "?" -> help();
            case "status" -> status();
            case "explore" -> explore();
            case "attack" -> attack();
            case "heal" -> heal();
            case "save" -> save();
            case "load" -> load();
            case "new" -> newGame();
            case "version" -> "Terminal Quest v" + VersionInfo.version();
            case "quit", "exit" -> "Goodbye!";
            default -> "Unknown command. Type 'help' to see available commands.";
        };
    }

    public static String help() {
        return "Commands: explore, attack, heal, status, save, load, new, version, help, quit";
    }

    private String status() {
        String info = "HP: " + state.getHealth() + "/50 | Potions: " + state.getPotions()
                + " | Gold: " + state.getGold() + " | Rooms cleared: " + state.getRoomsCleared() + "/5";
        if (state.getMonster() != null) {
            info += "\nEnemy: " + state.getMonster().getName() + " (HP " + state.getMonster().getHealth() + ")";
        }
        if (state.isWon()) info += "\nVICTORY!";
        if (state.isDead()) info += "\nYou have fallen. Type 'new' to restart.";
        return info;
    }

    private String explore() {
        if (state.isWon()) return "You already won. Type 'new' to play again.";
        if (state.isDead()) return "You have fallen. Type 'new' to restart.";
        if (state.getMonster() != null) return "Defeat the current enemy before exploring again.";
        Monster monster = EncounterFactory.create(state.getRoomsCleared(), random);
        state.setMonster(monster);
        return "You encounter " + monster.getName() + " (HP " + monster.getHealth()
                + "). Type 'attack' to fight, or 'heal' to use a potion.";
    }

    private String attack() {
        if (state.isDead() || state.isWon()) return "Start a new game with 'new'.";
        Monster enemy = state.getMonster();
        if (enemy == null) return "No enemy here. Type 'explore' first.";

        int damage = 8 + random.nextInt(7);
        enemy.setHealth(enemy.getHealth() - damage);
        String result = "You hit " + enemy.getName() + " for " + damage + " damage.";
        if (enemy.getHealth() <= 0) {
            state.setGold(state.getGold() + enemy.getGoldReward());
            state.setRoomsCleared(state.getRoomsCleared() + 1);
            result += "\nYou defeated " + enemy.getName() + "! +" + enemy.getGoldReward() + " gold.";
            if (state.getRoomsCleared() % 2 == 0 && !enemy.isBoss()) {
                state.setPotions(state.getPotions() + 1);
                result += " You found a healing potion.";
            }
            if (enemy.isBoss()) {
                state.setWon(true);
                result += "\nVICTORY! You defeated the Ancient Dragon!";
            } else {
                result += "\nType 'explore' to enter the next room.";
            }
            state.setMonster(null);
            return result;
        }
        return result + "\n" + counterattack(enemy);
    }

    private String heal() {
        if (state.isDead() || state.isWon()) return "Start a new game with 'new'.";
        if (state.getPotions() <= 0) return "No potions left.";
        if (state.getHealth() >= 50) return "Your health is already full.";
        state.setPotions(state.getPotions() - 1);
        int recovered = Math.min(20, 50 - state.getHealth());
        state.setHealth(state.getHealth() + recovered);
        String result = "You recover " + recovered + " HP.";
        if (state.getMonster() != null) result += "\n" + counterattack(state.getMonster());
        return result;
    }

    private String counterattack(Monster enemy) {
        int damage = enemy.getAttackPower() + random.nextInt(4);
        state.setHealth(Math.max(0, state.getHealth() - damage));
        String result = enemy.getName() + " strikes back for " + damage + " damage.";
        if (state.isDead()) result += "\nGAME OVER. Type 'new' to try again.";
        return result;
    }

    private String newGame() {
        state = new GameState();
        return "New adventure started. " + status();
    }

    private String save() {
        try {
            saves.save(state);
            return "Game saved successfully.";
        } catch (IOException e) {
            return "Could not save game: " + e.getMessage();
        }
    }

    private String load() {
        try {
            state = saves.load();
            return "Game loaded successfully.\n" + status();
        } catch (IOException e) {
            return "Could not load game: " + e.getMessage();
        }
    }
}

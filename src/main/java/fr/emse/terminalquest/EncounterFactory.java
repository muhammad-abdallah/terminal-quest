package fr.emse.terminalquest;

import java.util.Random;

/** Creates encounters with increasing difficulty. */
public final class EncounterFactory {
    private static final String[] MONSTERS = {"Goblin", "Skeleton", "Cave Spider", "Orc"};

    private EncounterFactory() { }

    public static Monster create(int roomsCleared, Random random) {
        if (roomsCleared >= 4) {
            return new Monster("Ancient Dragon", 54, 12, 100, true);
        }
        String name = MONSTERS[random.nextInt(MONSTERS.length)];
        int health = 16 + roomsCleared * 6 + random.nextInt(6);
        int attack = 4 + roomsCleared * 2;
        int gold = 10 + roomsCleared * 5;
        return new Monster(name, health, attack, gold, false);
    }
}

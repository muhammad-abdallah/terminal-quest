package fr.emse.terminalquest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Saves and loads JSON data using the external Jackson library. */
public final class SaveRepository {
    private static final long MAX_SAVE_SIZE = 65_536;
    private final Path file;
    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public SaveRepository(Path file) {
        this.file = file;
    }

    public static SaveRepository defaultRepository() {
        String override = System.getenv("TERMINAL_QUEST_SAVE");
        Path path = (override != null && !override.isBlank())
                ? Path.of(override)
                : Path.of(System.getProperty("user.home"), ".terminal-quest", "save.json");
        return new SaveRepository(path);
    }

    public void save(GameState state) throws IOException {
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        mapper.writeValue(file.toFile(), state);
    }

    public GameState load() throws IOException {
        if (!Files.isRegularFile(file)) {
            throw new IOException("No saved game found at " + file);
        }
        if (Files.size(file) > MAX_SAVE_SIZE) {
            throw new IOException("Save file is unexpectedly large");
        }
        GameState state = mapper.readValue(file.toFile(), GameState.class);
        validate(state);
        return state;
    }

    private static void validate(GameState state) throws IOException {
        if (state == null || state.getHealth() < 0 || state.getHealth() > 50
                || state.getPotions() < 0 || state.getPotions() > 99
                || state.getGold() < 0 || state.getRoomsCleared() < 0
                || state.getRoomsCleared() > 5 || (state.isWon() && state.getRoomsCleared() != 5)) {
            throw new IOException("Saved game contains invalid player values");
        }
        Monster monster = state.getMonster();
        if (monster != null && (monster.getName() == null || monster.getName().isBlank()
                || monster.getHealth() <= 0 || monster.getHealth() > 100
                || monster.getAttackPower() < 0 || monster.getAttackPower() > 20
                || monster.getGoldReward() < 0 || monster.getGoldReward() > 100)) {
            throw new IOException("Saved game contains invalid monster values");
        }
    }
}

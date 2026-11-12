package fr.emse.terminalquest;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Path;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GameEngineTest {
    @TempDir Path tempDir;

    private GameEngine game() {
        return new GameEngine(new Random(42), new SaveRepository(tempDir.resolve("save.json")));
    }

    @Test
    void cannotAttackBeforeExploring() {
        assertTrue(game().execute("attack").contains("explore"));
    }

    @Test
    void exploreCreatesAnEnemyAndCannotSkipIt() {
        GameEngine game = game();
        assertTrue(game.execute("explore").contains("encounter"));
        assertNotNull(game.getState().getMonster());
        assertTrue(game.execute("explore").contains("Defeat"));
    }

    @Test
    void aHealingPotionCannotExceedMaximumHealth() {
        GameEngine game = game();
        game.getState().setHealth(45);
        assertTrue(game.execute("heal").contains("recover 5 HP"));
        assertEquals(50, game.getState().getHealth());
        assertEquals(2, game.getState().getPotions());
    }

    @Test
    void saveAndLoadRestoreProgress() {
        GameEngine game = game();
        game.execute("explore");
        String enemy = game.getState().getMonster().getName();
        assertTrue(game.execute("save").contains("saved successfully"));
        game.execute("new");
        assertNull(game.getState().getMonster());
        assertTrue(game.execute("load").contains("loaded successfully"));
        assertEquals(enemy, game.getState().getMonster().getName());
    }

    @Test
    void malformedSaveIsRejected() throws Exception {
        Path saveFile = tempDir.resolve("save.json");
        java.nio.file.Files.writeString(saveFile, "not JSON");
        assertTrue(game().execute("load").contains("Could not load"));
    }
}

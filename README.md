# Terminal Quest — Java CLI Dungeon Game

A small **turn-based dungeon adventure** playable entirely in the terminal. Built as a TFSD Course 4 demonstration of **Apache Maven** for dependency management, compilation, project versioning, testing, and packaging.

## Gameplay

Clear four rooms and defeat the Ancient Dragon in the fifth room. Monsters fight back, so manage your potions. You can save and restore progress.

| Command | Purpose |
| --- | --- |
| `help` | List commands |
| `explore` | Enter the next room and encounter an enemy |
| `attack` | Attack the current enemy |
| `heal` | Consume a potion and regain up to 20 HP |
| `status` | Show health, inventory, progress, and opponent |
| `save` / `load` | Save or restore a JSON game file |
| `new` | Start a new adventure |
| `version` | Print the version managed by Maven |
| `quit` | Exit (save first if you want to keep progress) |

## Requirements

- **Java JDK 17 or later** (JDK 25 is compatible through Maven's Java 17 release setting).
- **Apache Maven 3.9+**.
- Internet access for the first Maven build to download dependencies from Maven Central.

No separate dependency installation is needed: Maven resolves all declared libraries.

## Build, test, and run

From the repository root:

```bash
mvn clean package
java -jar target/terminal-quest-1.0.0.jar
```

The single `mvn clean package` command downloads dependencies, compiles Java, runs the JUnit tests, and packages the application and its runtime dependencies into an executable JAR.

Test only:

```bash
mvn test
```

Optional end-to-end smoke test:

```bash
bash scripts/smoke-test.sh
```

View the version embedded from `pom.xml`:

```bash
java -jar target/terminal-quest-1.0.0.jar --version
```

The default save path is `~/.terminal-quest/save.json`. For an alternative location:

```bash
TERMINAL_QUEST_SAVE=./save.json java -jar target/terminal-quest-1.0.0.jar
```

## Maven build automation — TFSD 4 requirements

| Requirement | Implemented by | Verification |
| --- | --- | --- |
| Dependency management | `pom.xml` declares Jackson Databind 2.22.4 and JUnit Jupiter 5.12.2 | `mvn dependency:tree` |
| Compilation | Maven Compiler Plugin, Java 17 `release` | `mvn clean compile` |
| Version management | `<version>1.0.0</version>` in `pom.xml`, filtered into `game.properties` | `java -jar target/terminal-quest-1.0.0.jar --version` |
| Packaging | Maven Shade Plugin creates an executable, self-contained JAR | `mvn package` and `java -jar ...` |
| Reproducible build commands | Pinned versions for application dependencies and explicitly configured Maven plugins | `mvn clean package` from a fresh clone |
| Testing | Maven Surefire and JUnit tests for game rules and JSON persistence | `mvn test` |

### External libraries

- **Jackson Databind**: actually used in `SaveRepository.java` for serializing and deserializing the game's `GameState` and `Monster` to/from JSON. Maven also pulls Jackson's required transitive dependencies.
- **JUnit Jupiter** (test scope): used in `GameEngineTest.java` to verify gameplay and persistence.

### Source structure

```text
pom.xml
src/main/java/fr/emse/terminalquest/
    Main.java            Terminal input/output
    GameEngine.java      Game commands and rules
    GameState.java       Player state
    Monster.java         Enemy model
    EncounterFactory.java  Enemy generation and boss logic
    SaveRepository.java  JSON saving/loading via Jackson
    VersionInfo.java     Version from Maven-filtered resources
src/main/resources/game.properties
src/test/java/fr/emse/terminalquest/GameEngineTest.java
scripts/smoke-test.sh
```

### Version changes

Update `<version>` in `pom.xml` when releasing a new version. Maven uses that version in the JAR filename and filtered `game.properties` file. For example, change it to `1.1.0`, rebuild, then run `java -jar target/terminal-quest-1.1.0.jar --version`.

### Reproducing the build

On any machine with JDK 17+ and Maven 3.9+:

```bash
git clone https://github.com/muhammad-abdallah/terminal-quest.git
cd terminal-quest
mvn clean package
java -jar target/terminal-quest-1.0.0.jar
```



## Future TFSD 5 improvements

Extend the JUnit suite with battle simulations and coverage reporting; add Checkstyle or Spotless for automated code quality, Javadoc generation, and a more complete canonical README.

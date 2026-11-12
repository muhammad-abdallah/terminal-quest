package fr.emse.terminalquest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Reads the application version automatically filtered from pom.xml. */
public final class VersionInfo {
    private VersionInfo() { }

    public static String version() {
        try (InputStream stream = VersionInfo.class.getResourceAsStream("/game.properties")) {
            if (stream == null) {
                return "development";
            }
            Properties properties = new Properties();
            properties.load(stream);
            return properties.getProperty("version", "development");
        } catch (IOException e) {
            return "development";
        }
    }
}

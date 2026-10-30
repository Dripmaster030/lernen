package de.example.clientgui;

import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class AuroraConfig {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("aurora-client.properties");
    public static int red = 139, green = 92, blue = 246;
    private AuroraConfig() {}
    public static int accent() { return 0xFF000000 | (red << 16) | (green << 8) | blue; }
    public static void load() {
        try {
            if (!Files.exists(FILE)) return;
            Properties p = new Properties();
            try (var in = Files.newInputStream(FILE)) { p.load(in); }
            red = clamp(Integer.parseInt(p.getProperty("red", "139")));
            green = clamp(Integer.parseInt(p.getProperty("green", "92")));
            blue = clamp(Integer.parseInt(p.getProperty("blue", "246")));
        } catch (Exception ignored) { }
    }
    public static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Properties p = new Properties();
            p.setProperty("red", Integer.toString(red));
            p.setProperty("green", Integer.toString(green));
            p.setProperty("blue", Integer.toString(blue));
            try (var out = Files.newOutputStream(FILE)) { p.store(out, "Aurora Client colors"); }
        } catch (Exception ignored) { }
    }
    public static int clamp(int n) { return Math.max(0, Math.min(255, n)); }
    public static void set(int r, int g, int b) {
        red=clamp(r); green=clamp(g); blue=clamp(b); save();
    }
}

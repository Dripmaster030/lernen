package de.example.clientgui;

import net.fabricmc.loader.api.FabricLoader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Installs optional resource packs into the instance folder, without activating them. */
public final class AuroraTexturePacks {
    private static final String[] PACKS = {
        "Aurora_Glint", "Aurora_Low_Fire", "Aurora_Short_Swords",
        "Aurora_Custom_Tools", "Aurora_Transparent_Inventory"
    };
    private AuroraTexturePacks() {}
    public static void install() {
        Path folder = FabricLoader.getInstance().getGameDir().resolve("resourcepacks");
        try {
            Files.createDirectories(folder);
            for (String name : PACKS) {
                String bundled = "/aurora/packs/" + name + ".zip";
                Path target = folder.resolve(name + ".zip");
                if (Files.exists(target)) continue;
                try (InputStream in = AuroraTexturePacks.class.getResourceAsStream(bundled)) {
                    if (in == null) {
                        System.err.println("Aurora missing pack: " + bundled);
                        continue;
                    }
                    Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (Exception e) {
            System.err.println("Aurora pack installation: " + e.getMessage());
        }
    }
}

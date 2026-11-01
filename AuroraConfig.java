package de.example.clientgui;

import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.*;
import java.util.Properties;

public final class AuroraConfig {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("aurora-client.properties");
    public static boolean fullbright = false, inspect = false, glint = false;
    public static int fullbrightKey = -1, inspectKey = 86, glintKey = -1;
    public static int[] px = {18, 148, 278}, py = {32, 32, 32};
    public static boolean[] expanded = {true, true, true};
    public static boolean[] moduleDetails = {false, false, false};
    private AuroraConfig() {}
    public static void load() {
        try {
            if (!Files.exists(FILE)) return;
            Properties props = new Properties();
            try (var in = Files.newInputStream(FILE)) { props.load(in); }
            fullbright=Boolean.parseBoolean(props.getProperty("fullbright","false"));
            glint=Boolean.parseBoolean(props.getProperty("glint","false"));
            inspect=Boolean.parseBoolean(props.getProperty("inspect","false"));
            fullbrightKey=Integer.parseInt(props.getProperty("fullbrightKey","-1"));
            glintKey=Integer.parseInt(props.getProperty("glintKey","-1"));
            inspectKey=Integer.parseInt(props.getProperty("inspectKey","86"));
            for(int i=0;i<3;i++){
                px[i]=Integer.parseInt(props.getProperty("px"+i,Integer.toString(px[i])));
                py[i]=Integer.parseInt(props.getProperty("py"+i,Integer.toString(py[i])));
                expanded[i]=Boolean.parseBoolean(props.getProperty("expanded"+i,"true"));
                moduleDetails[i]=Boolean.parseBoolean(props.getProperty("details"+i,"false"));
            }
        } catch (Exception e) { System.err.println("Aurora config: " + e.getMessage()); }
    }
    public static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Properties props = new Properties();
            props.setProperty("fullbright",""+fullbright);
            props.setProperty("glint",""+glint);
            props.setProperty("inspect",""+inspect);
            props.setProperty("fullbrightKey",""+fullbrightKey);
            props.setProperty("glintKey",""+glintKey);
            props.setProperty("inspectKey",""+inspectKey);
            for(int i=0;i<3;i++){
                props.setProperty("px"+i,""+px[i]);props.setProperty("py"+i,""+py[i]);
                props.setProperty("expanded"+i,""+expanded[i]);
                props.setProperty("details"+i,""+moduleDetails[i]);
            }
            try(var stream=Files.newOutputStream(FILE)){ props.store(stream,"Aurora Client v2.1"); }
        } catch(Exception e) { System.err.println("Aurora config save: " + e.getMessage()); }
    }
    public static boolean get(int id){return switch(id){case 0->glint;case 1->fullbright;default->inspect;};}
    public static int key(int id){return switch(id){case 0->glintKey;case 1->fullbrightKey;default->inspectKey;};}
    public static void key(int id,int key){switch(id){case 0->glintKey=key;case 1->fullbrightKey=key;default->inspectKey=key;}save();}
    public static void toggle(int id){switch(id){case 0->glint=!glint;case 1->fullbright=!fullbright;default->inspect=!inspect;}save();}
}

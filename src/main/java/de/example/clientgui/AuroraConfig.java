package de.example.clientgui;

import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.*;
import java.util.Properties;

public final class AuroraConfig {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("aurora-client.properties");
    public static int red = 139, green = 92, blue = 246;
    public static boolean fullbright = false, inspect = false, glint = false;
    public static int fullbrightKey = -1, inspectKey = 86, glintKey = -1;
    public static int[] px = {24,174,324}, py = {48,48,48};
    public static boolean[] expanded = {true,true,true};
    private AuroraConfig() {}
    public static int accent() { return 0xFF000000 | (red << 16) | (green << 8) | blue; }
    public static void load() {
        try {
            if (!Files.exists(FILE)) return;
            Properties p = new Properties();
            try(var in=Files.newInputStream(FILE)){ p.load(in); }
            red=clamp(Integer.parseInt(p.getProperty("red","139")));
            green=clamp(Integer.parseInt(p.getProperty("green","92")));
            blue=clamp(Integer.parseInt(p.getProperty("blue","246")));
            fullbright=Boolean.parseBoolean(p.getProperty("fullbright","false"));
            glint=Boolean.parseBoolean(p.getProperty("glint","false"));
            inspect=Boolean.parseBoolean(p.getProperty("inspect","false"));
            fullbrightKey=Integer.parseInt(p.getProperty("fullbrightKey","-1"));
            glintKey=Integer.parseInt(p.getProperty("glintKey","-1"));
            inspectKey=Integer.parseInt(p.getProperty("inspectKey","86"));
            for(int i=0;i<3;i++){
                px[i]=Integer.parseInt(p.getProperty("px"+i,Integer.toString(px[i])));
                py[i]=Integer.parseInt(p.getProperty("py"+i,Integer.toString(py[i])));
                expanded[i]=Boolean.parseBoolean(p.getProperty("expanded"+i,"true"));
            }
        } catch(Exception e){ System.err.println("Aurora config: "+e.getMessage()); }
    }
    public static void save(){
        try {
            Files.createDirectories(FILE.getParent()); Properties p=new Properties();
            p.setProperty("red",""+red);p.setProperty("green",""+green);p.setProperty("blue",""+blue);
            p.setProperty("fullbright",""+fullbright);p.setProperty("glint",""+glint);p.setProperty("inspect",""+inspect);
            p.setProperty("fullbrightKey",""+fullbrightKey);p.setProperty("glintKey",""+glintKey);p.setProperty("inspectKey",""+inspectKey);
            for(int i=0;i<3;i++){p.setProperty("px"+i,""+px[i]);p.setProperty("py"+i,""+py[i]);p.setProperty("expanded"+i,""+expanded[i]);}
            try(var out=Files.newOutputStream(FILE)){p.store(out,"Aurora Client v2");}
        }catch(Exception e){System.err.println("Aurora config save: "+e.getMessage());}
    }
    public static int clamp(int x){return Math.max(0,Math.min(255,x));}
    public static void set(int r,int g,int b){red=clamp(r);green=clamp(g);blue=clamp(b);save();}
    public static boolean get(int id){return switch(id){case 0->glint;case 1->fullbright;default->inspect;};}
    public static int key(int id){return switch(id){case 0->glintKey;case 1->fullbrightKey;default->inspectKey;};}
    public static void key(int id,int key){switch(id){case 0->glintKey=key;case 1->fullbrightKey=key;default->inspectKey=key;}save();}
    public static void toggle(int id){switch(id){case 0->glint=!glint;case 1->fullbright=!fullbright;default->inspect=!inspect;}save();}
}

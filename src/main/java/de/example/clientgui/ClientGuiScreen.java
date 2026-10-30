package de.example.clientgui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClientGuiScreen extends Screen {
    private static final String[] TABS = {"Visuals", "HUD", "Movement", "Settings"};
    private static final String[][] MODULES = {
        {"Custom Glint", "Particles", "Fullbright"},
        {"FPS Counter", "Coordinates", "Keystrokes"},
        {"Zoom", "FOV Display", "Sprint Indicator"}
    };
    private final boolean[][] enabled = new boolean[3][3];
    private int tab = 0;
    private int x, y;
    private static final int W = 460, H = 290;

    public ClientGuiScreen() { super(Component.literal("Aurora Client")); }

    @Override
    protected void init() {
        x = (width - W) / 2;
        y = (height - H) / 2;
        refresh();
    }

    private void add(String name, int bx, int by, int bw, int bh, Runnable action) {
        addRenderableWidget(Button.builder(Component.literal(name), b -> action.run())
                .bounds(bx, by, bw, bh).build());
    }

    private void refresh() {
        clearWidgets();
        for (int i = 0; i < TABS.length; i++) {
            final int next = i;
            add((tab == i ? "◆  " : "   ") + TABS[i], x + 17, y + 66 + i * 41, 118, 29,
                    () -> { tab = next; refresh(); });
        }
        add("X", x + W - 29, y + 13, 17, 17, this::onClose);
        if (tab == 3) {
            String[] names = {"Purple", "Blue", "Mint", "Pink", "Orange"};
            int[][] presets = {{139,92,246},{56,142,255},{45,220,173},{255,91,160},{255,165,55}};
            for (int i=0; i<5; i++) {
                final int n=i;
                add(names[i], x + 153 + (i%3)*93, y + 95 + (i/3)*30, 87, 24,
                        () -> { AuroraConfig.set(presets[n][0], presets[n][1], presets[n][2]); refresh(); });
            }
            String[] channels = {"R", "G", "B"};
            for (int i=0; i<3; i++) {
                final int channel=i;
                int cy=y+182+i*26;
                add("-",x+280,cy,23,20,()->change(channel,-10));
                add("+",x+353,cy,23,20,()->change(channel,10));
            }
            add("Reset", x + W - 90, y + H - 31, 74, 20,
                    () -> { AuroraConfig.set(139,92,246); refresh(); });
        } else {
            for (int i=0; i<MODULES[tab].length; i++) {
                final int index=i;
                add(enabled[tab][i] ? "ON" : "OFF", x + 351, y + 101 + i*53, 74, 25,
                        () -> { enabled[tab][index] = !enabled[tab][index]; refresh(); });
            }
        }
    }

    private void change(int channel,int delta) {
        int r=AuroraConfig.red, g=AuroraConfig.green, b=AuroraConfig.blue;
        if(channel==0) r+=delta;
        if(channel==1) g+=delta;
        if(channel==2) b+=delta;
        AuroraConfig.set(r,g,b);
        refresh();
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        int accent=AuroraConfig.accent();
        g.fill(0,0,width,height,0xBC050913);
        g.fill(x-2,y-2,x+W+2,y+H+2,0xFF0B0E18);
        g.fill(x,y,x+W,y+H,0xFF121725);
        g.fill(x,y,x+W,y+44,0xFF1B2234);
        g.fill(x,y,x+4,y+44,accent);
        g.fill(x+148,y+55,x+149,y+H-20,0xFF30394A);
        g.drawString(font,"A U R O R A",x+18,y+12,0xFFFFFFFF,true);
        g.drawString(font,"CLIENT  /  1.21.11",x+18,y+28,0xFF929DB2,false);
        g.drawString(font,TABS[tab].toUpperCase(),x+165,y+61,accent,false);
        g.drawString(font,"Appearance & utilities",x+165,y+77,0xFF8B96AA,false);
        if(tab==3) {
            g.drawString(font,"Theme color",x+165,y+103-22,0xFFFFFFFF,false);
            g.fill(x+390,y+60,x+426,y+78,accent);
            g.drawString(font,"Custom RGB",x+165,y+161,0xFFFFFFFF,false);
            int[] rgb={AuroraConfig.red,AuroraConfig.green,AuroraConfig.blue};
            String[] labels={"RED", "GREEN", "BLUE"};
            for(int i=0;i<3;i++) {
                int cy=y+182+i*26;
                g.drawString(font,labels[i],x+165,cy+6,0xFFB8C1D3,false);
                g.drawString(font,Integer.toString(rgb[i]),x+315,cy+6,0xFFFFFFFF,false);
            }
            g.drawString(font,"Saved automatically",x+165,y+H-22,0xFF8490A4,false);
        } else {
            for(int i=0;i<MODULES[tab].length;i++) {
                int cy=y+95+i*53;
                g.fill(x+163,cy-5,x+436,cy+36,0xFF20283A);
                g.fill(x+163,cy-5,x+166,cy+36,accent);
                g.drawString(font,MODULES[tab][i],x+179,cy+4,0xFFF1F4FA,false);
                g.drawString(font,"Preview only",x+179,cy+20,0xFF8693A8,false);
            }
            g.drawString(font,"Modules are placeholders for now",x+164,y+H-19,0xFF8490A4,false);
        }
        super.render(g, mouseX, mouseY, delta);
    }

    @Override public boolean isPauseScreen() { return false; }
}

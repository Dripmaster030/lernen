package de.example.clientgui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class ClientGuiScreen extends Screen {
    private static final String[] HEADERS = {"VISUAL", "PLAYER", "MISC"};
    private static final String[] MODULES = {"Custom Glint", "Fullbright", "Inspect"};
    private static final int[] PANELS = {0, 0, 2};
    private static final int WIDTH = 123, TOP = 16, LINE = 16, DETAILS = 31;
    private int dragging = -1, offsetX, offsetY, listening = -1;
    private boolean settingsOpen;

    public ClientGuiScreen() { super(Component.literal("Aurora Client")); }
    private boolean hit(double mx,double my,int x,int y,int w,int h){return mx>=x&&mx<x+w&&my>=y&&my<y+h;}
    private int rowHeight(int id){return LINE+(AuroraConfig.moduleDetails[id]?DETAILS:0);}
    private int totalHeight(int panel){int h=TOP;if(AuroraConfig.expanded[panel])for(int i=0;i<MODULES.length;i++)if(PANELS[i]==panel)h+=rowHeight(i);return h;}
    private int rowY(int id){int panel=PANELS[id];int y=AuroraConfig.py[panel]+TOP;for(int i=0;i<id;i++)if(PANELS[i]==panel)y+=rowHeight(i);return y;}
    private String keyName(int code){if(code<0)return "NONE";String name=GLFW.glfwGetKeyName(code,0);return name==null?"KEY "+code:name.toUpperCase();}
    @Override public void render(GuiGraphics g,int mouseX,int mouseY,float partialTick){
        // No fullscreen tint: the world remains clearly visible behind thin panels.
        for(int panel=0;panel<HEADERS.length;panel++){
            int x=AuroraConfig.px[panel], y=AuroraConfig.py[panel], h=totalHeight(panel);
            g.fill(x,y,x+WIDTH,y+h,0xA8090C11);
            g.fill(x,y,x+WIDTH,y+TOP,0xCE15191E);
            g.fill(x,y+TOP-1,x+WIDTH,y+TOP,0x774D5259);
            g.drawString(font,HEADERS[panel],x+5,y+4,0xFFE8E8EA,false);
            g.drawString(font,AuroraConfig.expanded[panel]?"-":"+",x+WIDTH-11,y+4,0xFFC5C8CB,false);
            if(!AuroraConfig.expanded[panel])continue;
            for(int i=0;i<MODULES.length;i++){
                if(PANELS[i]!=panel)continue;
                int ry=rowY(i);
                if(AuroraConfig.get(i))g.fill(x+2,ry,x+WIDTH-2,ry+LINE,0x704F555B);
                g.drawString(font,MODULES[i],x+6,ry+4,AuroraConfig.get(i)?0xFFFFFFFF:0xFFC1C6CD,false);
                // Neutral square toggle at right; name expands per-module settings.
                g.fill(x+WIDTH-16,ry+4,x+WIDTH-6,ry+13,AuroraConfig.get(i)?0xFFE5E5E5:0x99565B62);
                if(AuroraConfig.moduleDetails[i]){
                    g.fill(x+3,ry+LINE,x+WIDTH-3,ry+LINE+DETAILS,0x9B161A21);
                    g.drawString(font,"Key: "+(listening==i?"PRESS...":keyName(AuroraConfig.key(i))),x+8,ry+LINE+5,0xFFE0E0E2,false);
                    g.drawString(font,i==0?"Shimmer for durable items":i==1?"Extra brightness":"Press bind to spin item",x+8,ry+LINE+18,0xFF9DA3AA,false);
                }
            }
        }
        int bw=84,bx=(width-bw)/2,by=height-24;
        g.fill(bx,by,bx+bw,by+16,0xB612151A);
        g.drawCenteredString(font,"SETTINGS",width/2,by+4,0xFFD7D9DB);
        if(settingsOpen){
            int sx=width/2-105,sy=height-101;
            g.fill(sx,sy,sx+210,sy+70,0xDC101318);
            g.drawString(font,"AURORA  /  SETTINGS",sx+9,sy+8,0xFFF0F0F0,false);
            g.drawString(font,"Theme: neutral dark",sx+9,sy+27,0xFFBDC1C5,false);
            g.drawString(font,"Opacity: translucent",sx+9,sy+42,0xFFBDC1C5,false);
            g.drawString(font,"Right Shift: open GUI",sx+9,sy+57,0xFF8D9298,false);
        }
        super.render(g,mouseX,mouseY,partialTick);
    }
    @Override public boolean mouseClicked(MouseButtonEvent event,boolean doubleClick){
        double mx=event.x(),my=event.y();int button=event.button();
        if(button!=0)return super.mouseClicked(event,doubleClick);
        int bx=(width-84)/2;
        if(hit(mx,my,bx,height-24,84,16)){settingsOpen=!settingsOpen;return true;}
        for(int panel=HEADERS.length-1;panel>=0;panel--){
            int x=AuroraConfig.px[panel],y=AuroraConfig.py[panel];
            if(hit(mx,my,x,y,WIDTH,TOP)){
                if(mx>x+WIDTH-20){AuroraConfig.expanded[panel]=!AuroraConfig.expanded[panel];AuroraConfig.save();}
                else{dragging=panel;offsetX=(int)mx-x;offsetY=(int)my-y;}
                return true;
            }
            if(AuroraConfig.expanded[panel])for(int i=0;i<MODULES.length;i++){
                if(PANELS[i]!=panel)continue;
                int ry=rowY(i);
                if(hit(mx,my,x+WIDTH-20,ry,20,LINE)){AuroraConfig.toggle(i);return true;}
                if(hit(mx,my,x,ry,WIDTH-20,LINE)){AuroraConfig.moduleDetails[i]=!AuroraConfig.moduleDetails[i];AuroraConfig.save();return true;}
                if(AuroraConfig.moduleDetails[i]&&hit(mx,my,x+4,ry+LINE,WIDTH-8,16)){listening=i;return true;}
            }
        }
        return super.mouseClicked(event,doubleClick);
    }
    @Override public boolean mouseDragged(MouseButtonEvent event,double dx,double dy){
        if(dragging>=0){AuroraConfig.px[dragging]=Math.max(0,Math.min(width-WIDTH,(int)event.x()-offsetX));AuroraConfig.py[dragging]=Math.max(0,Math.min(height-TOP-25,(int)event.y()-offsetY));return true;}
        return super.mouseDragged(event,dx,dy);
    }
    @Override public boolean mouseReleased(MouseButtonEvent event){
        if(dragging>=0){dragging=-1;AuroraConfig.save();return true;}
        return super.mouseReleased(event);
    }
    @Override public boolean keyPressed(KeyEvent event){
        if(listening>=0){int key=event.key();AuroraConfig.key(listening,key==GLFW.GLFW_KEY_DELETE||key==GLFW.GLFW_KEY_BACKSPACE?-1:key);listening=-1;return true;}
        return super.keyPressed(event);
    }
    @Override public boolean isPauseScreen(){return false;}
}

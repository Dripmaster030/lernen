package de.example.clientgui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class ClientGuiScreen extends Screen {
    private static final String[] HEADERS={"VISUALS","DISPLAY","MISC"};
    private static final int[] MODULE_PANEL={0,0,2};
    private static final String[] MODULE_NAMES={"Custom Glint","Fullbright","Inspect Item"};
    private static final String[] MODULE_DESC={"Glint renderer in development","Brightness option (experimental)","Item details on screen"};
    private static final int PANEL_W=145, HEADER_H=24, ROW_H=48;
    private int dragging=-1, dragX, dragY, listening=-1;
    private boolean settings=false;
    public ClientGuiScreen(){super(Component.literal("Aurora Client v2"));}
    private int panelHeight(int i){return HEADER_H+(AuroraConfig.expanded[i]?12+rows(i)*ROW_H:0);}
    private int rows(int i){int n=0;for(int p:MODULE_PANEL)if(p==i)n++;return n;}
    private boolean in(double mx,double my,int x,int y,int w,int h){return mx>=x&&my>=y&&mx<x+w&&my<y+h;}
    private int moduleRowY(int id){int p=MODULE_PANEL[id],n=0;for(int j=0;j<id;j++)if(MODULE_PANEL[j]==p)n++;return AuroraConfig.py[p]+HEADER_H+6+n*ROW_H;}
    @Override public void render(GuiGraphics g,int mouseX,int mouseY,float delta){
        g.fill(0,0,width,height,0x45030913);
        g.drawString(font,"A U R O R A  |  CLIENT V2",12,12,0xFFE4E7F7,true);
        g.drawString(font,"RSHIFT  to close",width-110,12,0xFFACB4C9,false);
        for(int p=0;p<3;p++){
            int x=AuroraConfig.px[p],y=AuroraConfig.py[p],h=panelHeight(p);
            g.fill(x+2,y+3,x+PANEL_W+3,y+h+4,0x60000000);
            g.fill(x,y,x+PANEL_W,y+h,0xB40D1425);
            g.fill(x,y,x+PANEL_W,y+HEADER_H,0xD41D2638);
            g.fill(x,y,x+3,y+HEADER_H,AuroraConfig.accent());
            g.drawString(font,HEADERS[p],x+10,y+8,0xFFFFFFFF,true);
            g.drawString(font,AuroraConfig.expanded[p]?"−":"+",x+PANEL_W-15,y+8,0xFFE1E6F4,false);
            if(!AuroraConfig.expanded[p])continue;
            for(int i=0;i<MODULE_NAMES.length;i++){
                if(MODULE_PANEL[i]!=p)continue;
                int ry=moduleRowY(i);
                g.fill(x+5,ry,x+PANEL_W-5,ry+ROW_H-5,0x70374658);
                int accent=AuroraConfig.accent();
                g.fill(x+7,ry+5,x+9,ry+17,AuroraConfig.get(i)?accent:0xFF56647A);
                g.drawString(font,MODULE_NAMES[i],x+14,ry+6,0xFFEAF1FF,false);
                String description=MODULE_DESC[i];
                g.drawString(font,description.substring(0,Math.min(20,description.length())),x+14,ry+20,0xFF9CA8BB,false);
                g.drawString(font,AuroraConfig.get(i)?"ON":"OFF",x+PANEL_W-29,ry+6,AuroraConfig.get(i)?accent:0xFFACB1BB,false);
                String key=listening==i?"PRESS KEY...":(AuroraConfig.key(i)<0?"KEY: NONE":"KEY: "+GLFW.glfwGetKeyName(AuroraConfig.key(i),0));
                if(listening!=i&&AuroraConfig.key(i)>=0&&GLFW.glfwGetKeyName(AuroraConfig.key(i),0)==null)key="KEY: "+AuroraConfig.key(i);
                g.drawString(font,key,x+14,ry+32,0xFFCED7EB,false);
            }
        }
        int bw=114,bh=24,bx=(width-bw)/2,by=height-bh-12;
        g.fill(bx,by,bx+bw,by+bh,0xD21B2639);
        g.fill(bx,by,bx+3,by+bh,AuroraConfig.accent());
        g.drawCenteredString(font,settings?"CLOSE SETTINGS":"SETTINGS",width/2,by+8,0xFFE8EAF3);
        if(settings){
            int sx=width/2-135,sy=Math.max(36,height-184),sw=270;
            g.fill(sx,sy,sx+sw,sy+118,0xEB0D1424);
            g.fill(sx,sy,sx+sw,sy+23,0xE72B3446);
            g.drawString(font,"THEME  •  RGB",sx+12,sy+8,0xFFFFFFFF,false);
            int[] rgb={AuroraConfig.red,AuroraConfig.green,AuroraConfig.blue};
            String[] labels={"RED","GREEN","BLUE"};
            for(int i=0;i<3;i++){
                int yy=sy+32+i*26;
                g.drawString(font,labels[i],sx+13,yy+5,0xFFC7CFE1,false);
                g.fill(sx+91,yy,sx+109,yy+18,0x8039465C);
                g.fill(sx+159,yy,sx+177,yy+18,0x8039465C);
                g.drawString(font,"−",sx+97,yy+5,0xFFFFFFFF,false);
                g.drawString(font,"+",sx+165,yy+5,0xFFFFFFFF,false);
                g.drawString(font,""+rgb[i],sx+122,yy+5,0xFFFFFFFF,false);
            }
            g.fill(sx+211,sy+36,sx+253,sy+96,AuroraConfig.accent());
        }
        super.render(g,mouseX,mouseY,delta);
    }
    @Override public boolean mouseClicked(double mx,double my,int button){
        if(button!=0)return super.mouseClicked(mx,my,button);
        int bx=(width-114)/2,by=height-36;
        if(in(mx,my,bx,by,114,24)){settings=!settings;return true;}
        if(settings){
            int sx=width/2-135,sy=Math.max(36,height-184);
            for(int i=0;i<3;i++){
                int yy=sy+32+i*26,delta=0;
                if(in(mx,my,sx+91,yy,18,18))delta=-15;
                if(in(mx,my,sx+159,yy,18,18))delta=15;
                if(delta!=0){int[] vals={AuroraConfig.red,AuroraConfig.green,AuroraConfig.blue};vals[i]+=delta;AuroraConfig.set(vals[0],vals[1],vals[2]);return true;}
            }
        }
        for(int p=2;p>=0;p--){
            int x=AuroraConfig.px[p],y=AuroraConfig.py[p];
            if(in(mx,my,x,y,PANEL_W,HEADER_H)){
                if(mx>x+PANEL_W-26){AuroraConfig.expanded[p]=!AuroraConfig.expanded[p];AuroraConfig.save();}
                else {dragging=p;dragX=(int)mx-x;dragY=(int)my-y;}
                return true;
            }
            if(AuroraConfig.expanded[p])for(int i=0;i<MODULE_NAMES.length;i++){
                if(MODULE_PANEL[i]!=p)continue;
                int ry=moduleRowY(i);
                if(in(mx,my,x+5,ry,PANEL_W-10,27)){AuroraConfig.toggle(i);return true;}
                if(in(mx,my,x+5,ry+27,PANEL_W-10,17)){listening=i;return true;}
            }
        }
        return super.mouseClicked(mx,my,button);
    }
    @Override public boolean mouseDragged(double x,double y,int button,double dx,double dy){
        if(dragging>=0){AuroraConfig.px[dragging]=Math.max(0,Math.min(width-PANEL_W,(int)x-dragX));AuroraConfig.py[dragging]=Math.max(25,Math.min(height-HEADER_H-40,(int)y-dragY));return true;}
        return super.mouseDragged(x,y,button,dx,dy);
    }
    @Override public boolean mouseReleased(double x,double y,int button){
        if(dragging>=0){dragging=-1;AuroraConfig.save();return true;}
        return super.mouseReleased(x,y,button);
    }
    @Override public boolean keyPressed(int key,int scan,int mods){
        if(listening>=0){AuroraConfig.key(listening,key==GLFW.GLFW_KEY_DELETE||key==GLFW.GLFW_KEY_BACKSPACE?-1:key);listening=-1;return true;}
        return super.keyPressed(key,scan,mods);
    }
    @Override public boolean isPauseScreen(){return false;}
}

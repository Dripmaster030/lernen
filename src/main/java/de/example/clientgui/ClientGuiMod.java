package de.example.clientgui;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import java.lang.reflect.Method;

public class ClientGuiMod implements ClientModInitializer {
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("clientgui", "general"));
    private final boolean[] wasDown = new boolean[3];
    private Object gammaOption;
    private Object gammaOriginal;
    private boolean gammaApplied;

    @Override public void onInitializeClient() {
        AuroraConfig.load();
        KeyMapping openGui=KeyBindingHelper.registerKeyBinding(new KeyMapping("key.clientgui.open",InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_RIGHT_SHIFT,CATEGORY));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(openGui.consumeClick()) client.setScreen(new ClientGuiScreen());
            for(int i=0;i<3;i++){
                int key=AuroraConfig.key(i);
                boolean down=key>=0 && client.getWindow()!=null && GLFW.glfwGetKey(client.getWindow().handle(),key)==GLFW.GLFW_PRESS;
                if(down&&!wasDown[i] && !(client.screen instanceof ClientGuiScreen) && client.screen==null) AuroraConfig.toggle(i);
                wasDown[i]=down;
            }
            applyFullbright(client);
        });
    }
    // Reflection avoids hardcoding a version-specific option accessor. If absent, the feature fails safely.
    private void applyFullbright(Minecraft client){
        try {
            if(client.options==null)return;
            if(gammaOption==null){
                Method getter=client.options.getClass().getMethod("gamma");
                gammaOption=getter.invoke(client.options);
            }
            Method get=gammaOption.getClass().getMethod("get");
            Method set=gammaOption.getClass().getMethod("set",Object.class);
            if(AuroraConfig.fullbright&&!gammaApplied){
                gammaOriginal=get.invoke(gammaOption);
                set.invoke(gammaOption,100.0D);
                gammaApplied=true;
            } else if(!AuroraConfig.fullbright&&gammaApplied){
                set.invoke(gammaOption,gammaOriginal);gammaApplied=false;
            }
        }catch(Exception ignored) { // Some Minecraft versions no longer expose gamma as a configurable option.
        }
    }
}

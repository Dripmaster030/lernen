package de.example.clientgui.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import de.example.clientgui.InspectAnimation;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import com.mojang.math.Axis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class AuroraInspectMixin {
    @Inject(method = "renderArmWithItem", at = @At("HEAD"))
    private void aurora$inspectBegin(AbstractClientPlayer player, float partialTick, float pitch,
          InteractionHand hand, float swingProgress, ItemStack item, float equipProgress,
          PoseStack matrices, SubmitNodeCollector collector, int light, CallbackInfo ci) {
        matrices.pushPose();
        if (hand == InteractionHand.MAIN_HAND && !item.isEmpty()) {
            float p = InspectAnimation.progress();
            if (p >= 0f) {
                // Keep the held item near its regular first person position.
                // The toss goes up and settles back smoothly; the flip happens
                // around the X axis (not the sideways Y rotation from v2.2).
                float lift = (float) Math.sin(Math.PI * p);
                float flip = smoothStep(p);
                matrices.translate(0.0f, 0.17f * lift, -0.045f * lift);
                matrices.mulPose(Axis.XP.rotationDegrees(360.0f * flip));
                matrices.mulPose(Axis.ZP.rotationDegrees(9.0f * lift));
            }
        }
    }

    private static float smoothStep(float t) {
        return t * t * (3.0f - 2.0f * t);
    }

    @Inject(method = "renderArmWithItem", at = @At("RETURN"))
    private void aurora$inspectEnd(AbstractClientPlayer player, float partialTick, float pitch,
          InteractionHand hand, float swingProgress, ItemStack item, float equipProgress,
          PoseStack matrices, SubmitNodeCollector collector, int light, CallbackInfo ci) {
        matrices.popPose();
    }
}

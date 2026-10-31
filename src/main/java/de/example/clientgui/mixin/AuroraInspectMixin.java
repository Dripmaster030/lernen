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
                float envelope = (float) Math.sin(Math.PI * p);
                matrices.translate(0.0f, 0.12f * envelope, -0.12f * envelope);
                matrices.mulPose(Axis.YP.rotationDegrees(360f * p));
                matrices.mulPose(Axis.ZP.rotationDegrees(22f * envelope));
            }
        }
    }

    @Inject(method = "renderArmWithItem", at = @At("RETURN"))
    private void aurora$inspectEnd(AbstractClientPlayer player, float partialTick, float pitch,
          InteractionHand hand, float swingProgress, ItemStack item, float equipProgress,
          PoseStack matrices, SubmitNodeCollector collector, int light, CallbackInfo ci) {
        matrices.popPose();
    }
}

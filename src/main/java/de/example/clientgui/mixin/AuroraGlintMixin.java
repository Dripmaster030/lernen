package de.example.clientgui.mixin;

import de.example.clientgui.AuroraConfig;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class AuroraGlintMixin {
    @Inject(method = "hasFoil", at = @At("RETURN"), cancellable = true)
    private void aurora$extraGlint(CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (AuroraConfig.glint && !stack.isEmpty() && stack.isDamageableItem()) {
            cir.setReturnValue(true);
        }
    }
}

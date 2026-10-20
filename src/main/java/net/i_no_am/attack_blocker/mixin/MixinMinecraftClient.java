package net.i_no_am.attack_blocker.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.i_no_am.attack_blocker.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraftClient {

    @Shadow
    public Entity crosshairPickEntity;

    @ModifyReturnValue(method = "shouldEntityAppearGlowing", at = @At("RETURN"))
    private boolean showGlowingOutline(boolean original, Entity entity) {
        if (entity instanceof Player player && Config.isEnabled() && Config.isPlayerBlocked(player.getGameProfile().name())) {
            return !Config.getPlayerColor(player.getGameProfile().name()).equals("none");
        }
        return original;
    }

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void blockAttack(CallbackInfoReturnable<Boolean> cir) {
        if (!Config.isEnabled()) return;

        if (crosshairPickEntity instanceof Player player && Config.isPlayerBlocked(player.getGameProfile().name())) {
            cir.setReturnValue(false);
        }
    }
}

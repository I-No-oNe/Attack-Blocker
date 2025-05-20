package net.i_no_am.attack_blocker.mixin;

import net.i_no_am.attack_blocker.config.Config;
import net.i_no_am.attack_blocker.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        Config.getInstance();
    }

    @Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
    private void outlineEntities(Entity entity, CallbackInfoReturnable<Boolean> ci) {
        if (entity instanceof PlayerEntity player) {
            String playerName = player.getGameProfile().getName();
            if (!Config.getPlayerColor(playerName).equals("none") && Config.isEnabled() && Config.isPlayerBlocked(playerName)) {
                ci.setReturnValue(true);
            }
        }
    }

    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void onDoAttack(CallbackInfoReturnable<Boolean> cir) {
        if (MinecraftClient.getInstance().targetedEntity instanceof PlayerEntity targetPlayer) {
            if (targetPlayer != null && Config.isEnabled() && Utils.cannotAttack(targetPlayer)) {
                cir.setReturnValue(false);
            }
        }
    }
}
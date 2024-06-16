package net.i_no_am.attackblocker.mixin;

import net.i_no_am.attackblocker.config.Configuration;
import net.i_no_am.attackblocker.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {

    @Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
    private void outlineEntities(Entity entity, CallbackInfoReturnable<Boolean> ci) {
        if (entity.getType() == EntityType.PLAYER) {
            PlayerEntity player = (PlayerEntity) entity;
            String playerName = player.getGameProfile().getName();
            String color = Configuration.getPlayerColor(playerName);

            if (!color.equals("none") && Configuration.isEnabled() && Configuration.isPlayerBlocked(playerName)) {
                ci.setReturnValue(true);
            }
        }
    }

    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void onDoAttack(CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient mc = (MinecraftClient) (Object) this;
        if (mc.targetedEntity instanceof PlayerEntity targetPlayer) {
            if (targetPlayer != null && Configuration.isEnabled() && Utils.cannotAttack(targetPlayer)) {
                cir.cancel();
            }
        }
    }
}

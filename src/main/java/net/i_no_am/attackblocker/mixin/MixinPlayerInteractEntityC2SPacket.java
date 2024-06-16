package net.i_no_am.attackblocker.mixin;

import net.i_no_am.attackblocker.config.Configuration;
import net.i_no_am.attackblocker.utils.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInteractEntityC2SPacket.class)
public abstract class MixinPlayerInteractEntityC2SPacket {


    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private static void onAttack(Entity entity, boolean playerSneaking, CallbackInfoReturnable<PlayerInteractEntityC2SPacket> cir) {

        ClientWorld clientWorld = MinecraftClient.getInstance().world;
        if (clientWorld != null) {
            if (entity instanceof PlayerEntity targetPlayer) {
                if (Configuration.isEnabled() && !Utils.canAttack(Utils.attacker, targetPlayer)) {
                    Utils.setMixinEnabled(true);
                    cir.cancel();
                }
            }
        }
    }
}

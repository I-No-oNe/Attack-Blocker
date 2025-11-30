package net.i_no_am.attack_blocker.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.i_no_am.attack_blocker.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {


    @Shadow
    @Nullable
    public Entity targetedEntity;

    @ModifyReturnValue(method = "hasOutline", at = @At("RETURN"))
    private boolean hasOutline(boolean original, @NotNull Entity entity) {
        if (entity instanceof PlayerEntity player) {

            String name = player.getGameProfile().name();

            return (!Config.getPlayerColor(name).equals("none") && Config.isEnabled() && Config.isPlayerBlocked(name));
        }
        return original;
    }


    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void doAttack(CallbackInfoReturnable<Boolean> cir) {
        if (!Config.isEnabled()) return;

        if (targetedEntity instanceof PlayerEntity player && Config.isPlayerBlocked(player.getGameProfile().name())) {
            cir.setReturnValue(false);
        }
    }
}
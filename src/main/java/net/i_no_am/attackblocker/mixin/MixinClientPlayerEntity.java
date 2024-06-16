package net.i_no_am.attackblocker.mixin;

import net.i_no_am.attackblocker.utils.Utils;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity {

    @Inject(method = "swingHand(Lnet/minecraft/util/Hand;)V", at = @At("HEAD"), cancellable = true)
    private void disableSwingAnimation(CallbackInfo ci) {
        if (Utils.isMixinEnabled()) {
            ci.cancel();
            Utils.setMixinEnabled(false);
        }
    }
}
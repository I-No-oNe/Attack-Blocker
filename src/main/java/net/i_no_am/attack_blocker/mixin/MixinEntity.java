package net.i_no_am.attack_blocker.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.i_no_am.attack_blocker.config.Config;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class MixinEntity {

    @ModifyReturnValue(method = "getTeamColorValue", at = @At("RETURN"))
    private int getTeamColorValue(int original) {

        if (!Config.isEnabled()) return original;

        var entity = (((Entity) (Object) this));

        if (!(entity instanceof PlayerEntity player)) return original;

        String name = player.getGameProfile().name();

        if (!Config.isPlayerBlocked(name)) return original;

        String colorName = Config.getPlayerColor(name);

        if (colorName == null) {
            return original;
        }

        return Config.getColor(colorName);
    }
}

package net.i_no_am.attack_blocker.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.i_no_am.attack_blocker.config.Config;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class MixinEntity {

    @ModifyReturnValue(method = "getTeamColor", at = @At("RETURN"))
    private int adjustTeamColor(int original) {
        if (!Config.isEnabled()) return original;

        Entity entity = (Entity) (Object) this;
        if (!(entity instanceof Player player)) return original;

        String name = player.getGameProfile().name();
        if (!Config.isPlayerBlocked(name)) return original;

        String colorName = Config.getPlayerColor(name);
        if (colorName == null) return original;

        return Config.getColor(colorName);
    }
}

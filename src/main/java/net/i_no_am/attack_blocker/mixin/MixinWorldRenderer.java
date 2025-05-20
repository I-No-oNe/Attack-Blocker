package net.i_no_am.attack_blocker.mixin;

import net.i_no_am.attack_blocker.config.Config;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Inject(method = "renderEntity", at = @At("HEAD"))
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if (vertexConsumers instanceof OutlineVertexConsumerProvider outlineVertexConsumers && entity instanceof PlayerEntity player) {
            String playerName = player.getGameProfile().getName();
            if (Config.isPlayerBlocked(playerName)) {
                String input = Config.getPlayerColor(playerName);
                if (input != null) {
                    Color color = Config.getRGBA(input);
                    outlineVertexConsumers.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
                }
            }
        }
    }
}

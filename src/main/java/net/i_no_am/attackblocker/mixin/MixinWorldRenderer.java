// MixinWorldRenderer.java

package net.i_no_am.attackblocker.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.i_no_am.attackblocker.config.Configuration;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Inject(method = "renderEntity", at = @At("HEAD"))
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if (vertexConsumers instanceof OutlineVertexConsumerProvider && entity.getType() == EntityType.PLAYER) {
            PlayerEntity player = (PlayerEntity) entity;
            String playerName = player.getGameProfile().getName();

            if (Configuration.isPlayerBlocked(playerName)) {
                String color = Configuration.getPlayerColor(playerName);
                if (color != null) {
                    int[] rgb = Configuration.getRGB(color);
                    OutlineVertexConsumerProvider outlineVertexConsumers = (OutlineVertexConsumerProvider) vertexConsumers;
                    outlineVertexConsumers.setColor(rgb[0], rgb[1], rgb[2], rgb[3]);
                }
            }
        }
    }
}

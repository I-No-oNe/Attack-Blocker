package net.i_no_am.attackblocker.mixin;

import net.i_no_am.attackblocker.config.Configuration;
import net.i_no_am.attackblocker.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {

	@SuppressWarnings("DataFlowIssue")
	protected MixinPlayerEntity() {
		super(null, null);
	}

	@Inject(method = "attack", at = @At("HEAD"), cancellable = true)
	private void onAttack(Entity target, CallbackInfo ci) {
		PlayerEntity attacker = (PlayerEntity) (Object) this;
		if (target instanceof PlayerEntity targetPlayer && Configuration.isEnabled() && !Utils.canAttack(attacker, targetPlayer)) {
					ci.cancel();
				}
			}
		}
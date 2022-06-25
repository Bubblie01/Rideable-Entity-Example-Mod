package com.example.example_mod;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class RhinoEntity extends RavagerEntity implements JumpingMount {

	private boolean jumping;
	private float jumpStrength;

	public static final EntityType<RhinoEntity> RHINO_ENTITY = Registry.register(Registry.ENTITY_TYPE, new Identifier("modid", "rhino_entity"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, RhinoEntity::new).dimensions(EntityDimensions.fixed(1.95F, 2.2F)).build());

	protected RhinoEntity(EntityType<? extends RhinoEntity> entityType, World world) {
		super(entityType, world);
	}


	public static void createRhinoAttributes() {
		FabricDefaultAttributeRegistry.register(RHINO_ENTITY, RhinoEntity.createRavagerAttributes());
	}



	public boolean getJumping() {
		return jumping;
	}

	public void setJumping(boolean bool) {
		jumping = bool;
	}

	public boolean isJumping() {
		if(jumping) {
			return true;
		}
		return false;
	}

	@Override
	public void travel(Vec3d movementInput) {
		LivingEntity livingEntity = (LivingEntity) this.getPrimaryPassenger();
		if (livingEntity != null && this.isAlive()) {
			this.setYaw(livingEntity.getYaw());
			this.prevYaw = this.getYaw();
			this.setPitch(livingEntity.getPitch() * 0.5F);
			this.setRotation(this.getYaw(), this.getPitch());
			this.bodyYaw = this.getYaw();
			this.headYaw = this.bodyYaw;
			float f = livingEntity.sidewaysSpeed * 0.5f;
			float g = livingEntity.forwardSpeed;


			if(this.jumpStrength > 0.0F && this.onGround) {
				double d = (double)this.jumpStrength * (double)this.getJumpVelocityMultiplier();
				double e = d + this.getJumpBoostVelocityModifier();
				Vec3d vec3d = this.getVelocity();
				this.setVelocity(vec3d.x, e, vec3d.z);
				if (g > 0.0F) {
					float h = MathHelper.sin(this.getYaw() * (float) (Math.PI / 180.0));
					float i = MathHelper.cos(this.getYaw() * (float) (Math.PI / 180.0));
					this.setVelocity(this.getVelocity().add((double) (-0.4F * h * jumpStrength), 0.0, (double) (0.4F * i * jumpStrength)));
				}
				this.jumpStrength = 0.0F;
			}
			if (this.isLogicalSideForUpdatingMovement()) {
				this.setMovementSpeed((float)this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
				super.travel(new Vec3d((double)f, movementInput.y, (double)g));
			}
			else if(livingEntity instanceof PlayerEntity) {
				this.setVelocity(Vec3d.ZERO);
			}
		}
		super.travel(movementInput);

	}

	protected void putPlayerOnRhinoBack(PlayerEntity player) {
		if (!this.world.isClient) {
			player.setYaw(this.getYaw());
			player.setPitch(this.getPitch());
			player.startRiding(this);
		}

	}

	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		putPlayerOnRhinoBack(player);
		return super.interactMob(player, hand);
	}

	@Override
	public void setJumpStrength(int strength) {
		if (strength >= 90) {
			this.jumpStrength = 1.0F;
		} else {
			this.jumpStrength = 0.4F + 0.4F * (float)strength / 90.0F;
		}
	}

	@Override
	public boolean canJump() {
		return true;
	}

	@Override
	public void startJumping(int height) {
		this.jumping = true;
	}

	@Override
	public void stopJumping() {

	}
}

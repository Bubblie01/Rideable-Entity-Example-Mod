package com.example.example_mod;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.SheepEntityRenderer;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.tag.EntityTypeTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RideableSheepEntity extends SheepEntity implements JumpingMount {

	private boolean jumping;
	private float jumpStrength;

	public static final EntityType<RideableSheepEntity> RIDEABLE_SHEEP_ENTITY_ENTITY = Registry.register(Registry.ENTITY_TYPE, new Identifier("modid", "rideable_sheep_entity"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, RideableSheepEntity::new).dimensions(EntityDimensions.fixed(0.9f, 1.3f)).build());

	public RideableSheepEntity(EntityType<? extends SheepEntity> entityType, World world) {
		super(entityType, world);
	}


	public static void createRideableSheepAttributes() {
		FabricDefaultAttributeRegistry.register(RIDEABLE_SHEEP_ENTITY_ENTITY, createMobAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0f).add(EntityAttributes.GENERIC_MAX_HEALTH, 15f).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.0f));
	}


	@Override
	protected void initGoals() {
		super.initGoals();
	}

	@Override
	protected void updateGoalControls() {
		if(this.getPrimaryPassenger() instanceof PlayerEntity) {
			this.goalSelector.setControlEnabled(Goal.Control.MOVE, false);
			this.goalSelector.setControlEnabled(Goal.Control.JUMP, false);
			this.goalSelector.setControlEnabled(Goal.Control.LOOK, false);
			this.goalSelector.setControlEnabled(Goal.Control.TARGET, false);
		}
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
		float f;
		float g;
		LivingEntity livingEntity = (LivingEntity)this.getPrimaryPassenger();
		if (this.hasPassengers() && livingEntity != null) {
			this.setYaw(livingEntity.getYaw());
			this.prevYaw = this.getYaw();
			this.setPitch(livingEntity.getPitch() * 0.5F);
			this.setRotation(this.getYaw(), this.getPitch());
			this.bodyYaw = this.getYaw();
			this.headYaw = this.bodyYaw;
			f = livingEntity.sidewaysSpeed * 0.5F;
			g = livingEntity.forwardSpeed;
			if (g <= 0.0F) {
				g *= 0.25F;
			}
			if (this.onGround && jumpStrength > 0.0F) {
				double d = (double) this.jumpStrength * (double) this.getJumpVelocityMultiplier();
				double e = d + this.getJumpBoostVelocityModifier();
				Vec3d vec3d = this.getVelocity();
				this.setVelocity(vec3d.x, e, vec3d.z);
				if (g > 0.0) {
					float h = MathHelper.sin(this.getYaw() * (float) (Math.PI / 180.0));
					float i = MathHelper.cos(this.getYaw() * (float) (Math.PI / 180.0));
					this.setVelocity(this.getVelocity().add((double) (-0.4F * h * jumpStrength), 0.0, (double) (0.4F * i * jumpStrength)));
				}
			}
			if (isLogicalSideForUpdatingMovement()) {
				this.setMovementSpeed((float) this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
				super.travel(new Vec3d((double) f, movementInput.y, (double) g));
			}
			this.setMovementSpeed(0f);
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

	@Nullable
	@Override
	public Entity getPrimaryPassenger() {
		Entity entity = this.getFirstPassenger();
		return entity != null ? entity : null;
	}

	@Override
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		putPlayerOnRhinoBack(player);
		return super.interactMob(player, hand);
	}

	@Override
	public void setJumpStrength(int strength) {
		jumpStrength = strength;
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

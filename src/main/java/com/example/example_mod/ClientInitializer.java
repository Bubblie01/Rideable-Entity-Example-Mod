package com.example.example_mod;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.RavagerEntity;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class ClientInitializer implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		EntityRendererRegistry.register(RhinoEntity.RHINO_ENTITY, (context) -> {
			return new RhinoEntityRenderer(context);
		});

	}
}

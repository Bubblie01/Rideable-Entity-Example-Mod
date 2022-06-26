package com.example.example_mod;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class ClientInitializer implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		EntityRendererRegistry.register(RideableSheepEntity.RIDEABLE_SHEEP_ENTITY_ENTITY, (context) -> {
			return new RideableSheepEntityRenderer(context);
		});

	}
}

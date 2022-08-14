package com.example.example_mod;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SheepEntity;

public class RideableSheepEntityRenderer extends SheepEntityRenderer {

	public RideableSheepEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public void render(SheepEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		matrixStack.scale(0.5f,0.5f,0.5f);
		super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}
}

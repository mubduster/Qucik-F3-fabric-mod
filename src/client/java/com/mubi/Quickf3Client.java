package com.mubi;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

public class Quickf3Client implements ClientModInitializer {
    private static final Identifier EXAMPLE_LAYER = Identifier.of(Quickf3.MOD_ID, "hud-example-layer");
    
    private static void render(DrawContext context, RenderTickCounter tickCounter) {
            TextRenderer textRenderer= MinecraftClient.getInstance().textRenderer;
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();

            String coords = String.format("X: %.0f, Y: %.0f, Z: %.0f", x, y, z);

            context.drawText(textRenderer, coords, 2, 5, 0x0000FF00, false);
        }

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        HudLayerRegistrationCallback.EVENT.register(layeredDrawer ->
        layeredDrawer.attachLayerBefore(IdentifiedLayer.CHAT, EXAMPLE_LAYER, Quickf3Client::render));
    }
}
package com.mubi.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mubi.Quickf3;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;


public class Quickf3Client implements ClientModInitializer {

    private static final Identifier Overlay = Identifier.fromNamespaceAndPath(Quickf3.MOD_ID, "hud");

    private static boolean showCords = true; // variable to toggle the overlay

    private static boolean f3condition = true; // variable to make sure that the overlay doesn't overlap with the F3 menu

    private static int mode = 1;

    private static boolean prevF3Down = false; // variable to check if F3 was pressed before, so that it only toggles once per press

    private static void render() {
        boolean f3Down = keyBindingF3.isDown();

        //Checks for F3 keypress to toggle the overlay
        if (f3Down && !prevF3Down) { // Only on initial press
            if (f3condition) {
                showCords = false;
                f3condition = false;
            } else {
                showCords = true;
                f3condition = true;
            }
        }
        prevF3Down = f3Down;
        if (keyBinding.consumeClick()) { // checks for keypress F4 to toggle the overlay
            showCords = !showCords;
        } else if (showCords) {
            if (keyBindingmode.consumeClick()) {
                mode += 1;
            }
            if (mode > 3) { // if mode is greater than 3, reset it to 1
                mode = 1;
            }
        }
    }

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("quickf3", "client"));

    private static final KeyMapping keyBinding = new KeyMapping(
            "key.QuickF3.toggle_overlay",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F4,
            CATEGORY
    );

    private static final KeyMapping keyBindingF3 = new KeyMapping(
            "key.QuickF3.toggle_after_F3",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F3,
            CATEGORY
    );

    private static final KeyMapping keyBindingmode = new KeyMapping(
            "key.QuickF3.toggle_modes",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_ALT,
            CATEGORY
    );

    @Override
    public void onInitializeClient() {

        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        KeyMappingHelper.registerKeyMapping(keyBinding);
        KeyMappingHelper.registerKeyMapping(keyBindingF3);
        KeyMappingHelper.registerKeyMapping(keyBindingmode);


        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            render();
        });

        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                Overlay,
                (GuiGraphicsExtractor context, DeltaTracker tickDelta) -> {

                    if (showCords) { // rendering code for the overlay if overlay toggled
                        Font textRenderer = Minecraft.getInstance().font; // variable for initializing text rendering
                        LocalPlayer player = Minecraft.getInstance().player; // variable for initializing player information function
                        if (player == null) return;
                        double x = player.getX(); // x coordinate 64 bit float
                        double y = player.getY(); // y coordinate 64 bit float
                        double z = player.getZ(); // z coordinate 64 bit float

                        int fps = Minecraft.getInstance().getFps(); // initialize FPS variable


                        if (mode == 1) {
                            String coords = String.format("X: %.0f, Y: %.0f, Z: %.0f", x, y, z); // coordinates render format

                            String FPS = String.format("FPS: " + fps); // FPS render format

                            int width = textRenderer.width(coords + FPS);

                            context.fill(0, 0, width + 25, 17, 0x77333333); // This renders the background. Remember x and y graph is flipped for some reason. (x1, y1, x2, y2, colour).

                            context.outline(0, 0, width + 25, 17, 0x88444444); // This renders the outline for background.These lengths correspond to the lengths of fill, and x, y are is position. (x, y, width, height, colour)                   }

                            context.text(textRenderer, FPS, 3, 5, 0xFFFFFFFF, true); // renders FPS

                            context.text(textRenderer, coords, textRenderer.width(FPS) + 15, 5, 0xFFFFFFFF, true); // renders coordinates


                        } else if (mode == 2) {
                            String FPS = String.format("FPS: " + fps); // FPS render format

                            int width = textRenderer.width(FPS);
                            context.fill(0, 0, width + 10, 17, 0x77333333); // This renders the background. Remember x and y graph is flipped for some reason. (x1, x2, y1, y2, colour).

                            context.outline(0, 0, width + 10, 17, 0x88444444); // This renders the outline for background.These lengths correspond to the lengths of fill, and x, y are is position. (x, y, width, height, colour)

                            context.text(textRenderer, FPS, 3, 5, 0xFFFFFFFF, true); // renders FPS


                        } else if (mode == 3) {
                            String coords = String.format("X: %.0f, Y: %.0f, Z: %.0f", x, y, z); // coordinates render format

                            int width = textRenderer.width(coords);

                            context.fill(0, 0, width + 14, 17, 0x77333333); // This renders the background. Remember x and y graph is flipped for some reason. (x1, x2, y1, y2, colour).

                            context.outline(0, 0, width + 14, 17, 0x88444444); // This renders the outline for background.These lengths correspond to the lengths of fill, and x, y are is position. (x, y, width, height, colour)

                            context.text(textRenderer, coords, 3, 5, 0xFFFFFFFF, true); // renders coordinates

                        }
                    }
                }
        );
    }
}
package com.mubi;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.impl.client.rendering.hud.HudElementRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class QuickF3Client implements ClientModInitializer {

    private static final Identifier Overlay = Identifier.of(QuickF3.MOD_ID, "hud");

    private static boolean showCords = true; // variable to toggle the overlay

    private static boolean f3condition = true; // variable to make sure that the overlay doesn't overlap with the F3 menu

    private static int mode = 1;

    private static boolean prevF3Down = false; // variable to check if F3 was pressed before, so that it only toggles once per press

    private static void render() {
        boolean f3Down = keyBindingF3.isPressed();

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
        if (keyBinding.wasPressed()) { // checks for keypress F4 to toggle the overlay
            showCords = !showCords;
        }

        else if (showCords){
            if (keyBindingmode.wasPressed()){
                mode+=1;
            }
            if (mode > 3) { // if mode is greater than 3, reset it to 1
                mode = 1;
            }
        }
	}

    private static final KeyBinding.Category CATEGORY = KeyBinding.Category.create(Identifier.of("quickf3","client"));

    private static final KeyBinding keyBinding = new KeyBinding(
            "key.QuickF3.toggle_overlay",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F4,
            CATEGORY
    );

    private static final KeyBinding keyBindingF3 = new KeyBinding(
            "key.QuickF3.toggle_after_F3",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F3,
            CATEGORY
    );

    private static final KeyBinding keyBindingmode = new KeyBinding(
            "key.QuickF3.toggle_modes",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_ALT,
            CATEGORY
    );

    @Override
    public void onInitializeClient() {

        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        KeyBindingHelper.registerKeyBinding(keyBinding);
        KeyBindingHelper.registerKeyBinding(keyBindingF3);
        KeyBindingHelper.registerKeyBinding(keyBindingmode);


        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            render();
        });

        HudElementRegistry.addLast(
                Overlay,
                (DrawContext context, RenderTickCounter tickDelta) -> {

                    if (showCords) { // rendering code for the overlay if overlay toggled
                        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer; // variable for initializing text rendering
                        ClientPlayerEntity player = MinecraftClient.getInstance().player; // variable for initializing player information function
                        if (player == null) return;
                        double x = player.getX(); // x coordinate 64 bit float
                        double y = player.getY(); // y coordinate 64 bit float
                        double z = player.getZ(); // z coordinate 64 bit float

                        int fps = MinecraftClient.getInstance().getCurrentFps(); // initialize FPS variable


                        if (mode == 1) {
                            String coords = String.format("X: %.0f, Y: %.0f, Z: %.0f", x, y, z); // coordinates render format

                            String FPS = String.format("FPS: " + fps); // FPS render format

                            int width = textRenderer.getWidth(coords + FPS);

                            context.fill(0, 0, width + 25, 17, 0x77333333); // This renders the background. Remember x and y graph is flipped for some reason. (x1, y1, x2, y2, colour).

                            //context.drawStrokedRectangle(0, 0, width +25, 17, 0x88444444); // This renders the outline for background.These lengths correspond to the lengths of fill, and x, y are is position. (x, y, width, height, colour)                   }
                            // I don't know why this isn't working properly

                            context.drawText(textRenderer, FPS, 3, 5, 0xFFFFFFFF, true); // renders FPS

                            context.drawText(textRenderer, coords, textRenderer.getWidth(FPS) + 15, 5, 0xFFFFFFFF, true); // renders coordinates


                        } else if (mode == 2) {
                            String FPS = String.format("FPS: " + fps); // FPS render format

                            int width = textRenderer.getWidth(FPS);
                            context.fill(0, 0, width + 10, 17, 0x77333333); // This renders the background. Remember x and y graph is flipped for some reason. (x1, x2, y1, y2, colour).

                            //context.drawStrokedRectangle(0, 0, width + 10, 17, 0x88444444); // This renders the outline for background.These lengths correspond to the lengths of fill, and x, y are is position. (x, y, width, height, colour)
                            // I don't know why this isn't working properly

                            context.drawText(textRenderer, FPS, 3, 5, 0xFFFFFFFF, true); // renders FPS


                        } else if (mode == 3) {
                            String coords = String.format("X: %.0f, Y: %.0f, Z: %.0f", x, y, z); // coordinates render format

                            int width = textRenderer.getWidth(coords);

                            context.fill(0, 0, width + 14, 17, 0x77333333); // This renders the background. Remember x and y graph is flipped for some reason. (x1, x2, y1, y2, colour).

                            //context.drawStrokedRectangle(0, 0, width + 14, 17, 0x88444444); // This renders the outline for background.These lengths correspond to the lengths of fill, and x, y are is position. (x, y, width, height, colour)
                            // I don't know why this isn't working properly

                            context.drawText(textRenderer, coords, 3, 5, 0xFFFFFFFF, true); // renders coordinates

                        }
                    }
                }
        );
    }

}
package com.mubi;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

import org.lwjgl.glfw.GLFW;


public class Quickf3Client implements ClientModInitializer {

    private static final Identifier Overlay = Identifier.of(Quickf3.MOD_ID, "hud");

    private static KeyBinding keyBinding; // initializing variable for setting key bind F4
    private static KeyBinding keyBindingF3; // initializing variable for setting key bind F3
    private static KeyBinding keyBindingmode; // initializing the variable for setting the key for modes
    
    private static boolean showCords = true; // variable to toggle the overlay
    
    private static boolean f3condition = true; // variable to make sure that the overlay doesnt overlap with the F3 menu

    private static int mode = 1; 

    private static void render(DrawContext context, RenderTickCounter tickCounter) {
    
            if (keyBinding.wasPressed()) { // checks for keypress F4 to toggle the overlay
                showCords = !showCords;
            }
            else if (keyBindingF3.wasPressed()) { // checks for keypress F3 to toggle the overlay
                if (f3condition ) {  // to toggle off the overlay when the F3 menu is opened
                    showCords = false;
                    f3condition = false;
                }
                else {             // to toggle on the overlay when the F3 menu is closed
                    showCords = true;
                    f3condition = true;
                    
                    if (keyBindingmode.wasPressed()) {
                        mode+=1;
                            if (mode > 3){
                                mode=1;
                            }
                    }
                }
            }
            else if (showCords == true){
                if (keyBindingmode.wasPressed()){
                    mode+=1;
                    if (mode > 3){
                        mode=1;
                    }
                }
            }            

            if (showCords){ // rendering code for the overlay if overlay toggled
                TextRenderer textRenderer= MinecraftClient.getInstance().textRenderer; // variable for initializing text rendering
                ClientPlayerEntity player = MinecraftClient.getInstance().player; // variable for initializing player information function
                double x = player.getX(); // x coordinate 64 bit float
                double y = player.getY(); // y coordinate 64 bit float
                double z = player.getZ(); // z coordinate 64 bit float

                int fps = MinecraftClient.getInstance().getCurrentFps(); // initialize FPS variable
                //if (mode == 2){
                //    context.fill( 0, 0, 50, 17, 0 ,0x77333333); // This renders the background. Remember x and y graph is flipped for some reason. (x1, x2, y1, y2, z(which layer is this on), colour).
                //    context.drawBorder( 0, 0, 50, 17, 0x88444444); // This renders the outline for background.These lengths corrispond to the lenghts of fill, and x, y are is position. (x, y, weidth, height, colour)
                //}
                //else if (mode == 3){
                //    context.fill( 0, 0, 110, 17, 0 ,0x77333333); // This renders the background. Remember x and y graph is flipped for some reason. (x1, x2, y1, y2, z(which layer is this on), colour).
                //    context.drawBorder( 0, 0, 110, 17, 0x88444444); // This renders the outline for background.These lengths corrispond to the lenghts of fill, and x, y are is position. (x, y, weidth, height, colour)
                //}
                //else if (mode == 1){
                //    context.fill( 0, 0, 165, 17, 0 ,0x77333333); // This renders the background. Remember x and y graph is flipped for some reason. (x1, x2, y1, y2, z(which layer is this on), colour).
                //    context.drawBorder( 0, 0, 165, 17, 0x88444444); // This renders the outline for background.These lengths corrispond to the lenghts of fill, and x, y are is position. (x, y, weidth, height, colour)
                //}
                
                if (mode == 1){
                    String coords = String.format("X: %.0f, Y: %.0f, Z: %.0f", x, y, z); // coordinates render format

                    String FPS = String.format("FPS: "+fps); // FPS render format
                        
                    int weidth = textRenderer.getWidth(coords+FPS);

                    if (!(textRenderer.getWidth(FPS) >= 40)){
                        context.fill( 0, 0, weidth+25, 17, 0 ,0x77333333); // This renders the background. Remember x and y graph is flipped for some reason. (x1, x2, y1, y2, z(which layer is this on), colour).
                        context.drawBorder( 0, 0, weidth+25, 17, 0x88444444); // This renders the outline for background.These lengths corrispond to the lenghts of fill, and x, y are is position. (x, y, weidth, height, colour)                   }
                        
                        context.drawText(textRenderer, coords, 50, 5, 0x00FFFFFF, true); // renders coordinates
    
                        context.drawText(textRenderer, FPS, 3, 5, 0x00FFFFFF, true); // renders FPS
                    }
                    else{
                        context.fill( 0, 0, weidth+Math.round((float)((weidth-textRenderer.getWidth(coords))/1.5)), 17, 0 ,0x77333333); // This renders the background. Remember x and y graph is flipped for some reason. (x1, x2, y1, y2, z(which layer is this on), colour).
                        context.drawBorder( 0, 0, Math.round((float) (weidth+(((weidth-textRenderer.getWidth(coords)))/1.5))), 17, 0x88444444); // This renders the outline for background.These lengths corrispond to the lenghts of fill, and x, y are is position. (x, y, weidth, height, colour)                   }
                        
                        context.drawText(textRenderer, coords, 50+((weidth-textRenderer.getWidth(coords))/4), 5, 0x00FFFFFF, true); // renders coordinates
    
                        context.drawText(textRenderer, FPS, 3, 5, 0x00FFFFFF, true); // renders FPS
                    }
                }
                else if (mode == 2){
                    String FPS = String.format("FPS: "+fps); // FPS render format

                    int weidth= textRenderer.getWidth(FPS) ;

                    context.fill( 0, 0, weidth+10, 17, 0 ,0x77333333); // This renders the background. Remember x and y graph is flipped for some reason. (x1, x2, y1, y2, z(which layer is this on), colour).
                    context.drawBorder( 0, 0, weidth+10, 17, 0x88444444); // This renders the outline for background.These lengths corrispond to the lenghts of fill, and x, y are is position. (x, y, weidth, height, colour)

                    context.drawText(textRenderer, FPS, 3, 5, 0x00FFFFFF, true); // renders FPS
                }
                else if (mode == 3 ){
                    String coords = String.format("X: %.0f, Y: %.0f, Z: %.0f", x, y, z); // coordinates render format

                    int weidth= textRenderer.getWidth(coords) ;

                    context.fill( 0, 0, weidth+10, 17, 0 ,0x77333333); // This renders the background. Remember x and y graph is flipped for some reason. (x1, x2, y1, y2, z(which layer is this on), colour).
                    context.drawBorder( 0, 0, weidth+10, 17, 0x88444444); // This renders the outline for background.These lengths corrispond to the lenghts of fill, and x, y are is position. (x, y, weidth, height, colour)

                    context.drawText(textRenderer, coords, 3, 5, 0x00FFFFFF, true); // renders coordinates
                }
            }
            
        }

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        HudLayerRegistrationCallback.EVENT.register(layeredDrawer ->            //initializes all rendering
        layeredDrawer.attachLayerBefore(IdentifiedLayer.CHAT, Overlay, Quickf3Client::render));

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(  // initialized keybind F4
            "key.Quickf3.press",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F4,
            "category.Quickf3.show"
        ));

        keyBindingF3 = KeyBindingHelper.registerKeyBinding(new KeyBinding(  //initializes keybind F3
            "key.Quickf3.pressed",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F3,
            "category.Quickf3.unshow"
        ));

        keyBindingmode = KeyBindingHelper.registerKeyBinding(new KeyBinding(  //initialize keybind left Alt
            "key.Quickf3.toggled",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_ALT,
            "category.Quickf3.mode"
        ));
    }
}
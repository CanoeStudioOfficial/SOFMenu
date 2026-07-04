package com.canoestudios.sofmenu.client.resources;

import com.canoestudios.sofmenu.client.gui.SofMainMenuScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

@SideOnly(Side.CLIENT)
public final class MenuTextureCache {

    private static final Queue<ResourceLocation> PRELOAD_QUEUE = new ArrayDeque<ResourceLocation>();
    private static final Set<ResourceLocation> QUEUED_TEXTURES = new HashSet<ResourceLocation>();
    private static final Set<ResourceLocation> LOADED_TEXTURES = new HashSet<ResourceLocation>();

    private MenuTextureCache() {
    }

    public static void schedulePreload() {
        for (ResourceLocation texture : SofMainMenuScreen.getPreloadTextures()) {
            if (!LOADED_TEXTURES.contains(texture) && QUEUED_TEXTURES.add(texture)) {
                PRELOAD_QUEUE.offer(texture);
            }
        }
    }

    public static void tick(Minecraft minecraft, Logger logger) {
        if (minecraft == null || minecraft.getTextureManager() == null) {
            return;
        }

        schedulePreload();
        ResourceLocation texture = PRELOAD_QUEUE.poll();
        if (texture == null) {
            return;
        }

        QUEUED_TEXTURES.remove(texture);
        TextureManager textureManager = minecraft.getTextureManager();
        if (textureManager.getTexture(texture) != null) {
            LOADED_TEXTURES.add(texture);
            return;
        }

        try {
            if (textureManager.loadTexture(texture, new SimpleTexture(texture))) {
                LOADED_TEXTURES.add(texture);
            }
        } catch (RuntimeException exception) {
            logger.warn("Unable to preload SOF Menu texture {}.", texture, exception);
        }
    }
}

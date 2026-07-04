package com.canoestudios.sofmenu.client.window;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

@SideOnly(Side.CLIENT)
public final class WindowCustomizer {

    private static final String WINDOW_TITLE = "Survival - Origin & Future BY CanoeStudio";
    private static final ResourceLocation ICON_16 = new ResourceLocation("sofmenu", "textures/menu/window/icon16x16.png");
    private static final ResourceLocation ICON_32 = new ResourceLocation("sofmenu", "textures/menu/window/icon32x32.png");

    private WindowCustomizer() {
    }

    public static void apply(Minecraft minecraft, Logger logger) {
        try {
            Display.setTitle(WINDOW_TITLE);
            Display.setIcon(new ByteBuffer[] {
                    loadIcon(minecraft, ICON_16),
                    loadIcon(minecraft, ICON_32)
            });
        } catch (Exception exception) {
            logger.warn("Unable to apply SOF Menu window icon.", exception);
        }
    }

    private static ByteBuffer loadIcon(Minecraft minecraft, ResourceLocation location) throws IOException {
        IResource resource = minecraft.getResourceManager().getResource(location);
        try (InputStream inputStream = resource.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            int[] pixels = new int[image.getWidth() * image.getHeight()];
            image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

            ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length * 4);
            for (int pixel : pixels) {
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
            buffer.flip();
            return buffer;
        }
    }
}

package com.canoestudios.sofmenu.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class SofMainMenuScreen extends GuiScreen {

    private static final ResourceLocation[] BACKGROUNDS = new ResourceLocation[] {
            texture("menu/backgrounds/image_01.png"),
            texture("menu/backgrounds/image_02.png"),
            texture("menu/backgrounds/image_03.png"),
            texture("menu/backgrounds/image_04.png")
    };
    private static final ResourceLocation LOGO = texture("menu/sof_logo_full.png");
    private static final ResourceLocation INFO_BOOK = texture("menu/info_menu_book.png");
    private static final ResourceLocation BUTTON_LONG = texture("menu/buttons/longbutton_normal.png");
    private static final ResourceLocation BUTTON_LONG_HOVER = texture("menu/buttons/longbutton_hover.png");
    private static final ResourceLocation BUTTON_SHORT = texture("menu/buttons/shortbutton_normal.png");
    private static final ResourceLocation BUTTON_SHORT_HOVER = texture("menu/buttons/shortbutton_hover.png");
    private static final ResourceLocation STICKER_EARTH = texture("menu/stickers/earth.png");
    private static final ResourceLocation STICKER_COOKIE = texture("menu/stickers/cookie.png");
    private static final ResourceLocation STICKER_CAT = texture("menu/stickers/cat.png");
    private static final ResourceLocation STICKER_BOOK_PEN = texture("menu/stickers/book_pen.png");
    private static final ResourceLocation STICKER_SYNC_CHECK = texture("menu/stickers/sync_check.png");
    private static final ResourceLocation STICKER_WIKI = texture("menu/stickers/wiki.png");
    private static final ResourceLocation[] PRELOAD_TEXTURES = new ResourceLocation[] {
            LOGO,
            INFO_BOOK,
            BUTTON_LONG,
            BUTTON_LONG_HOVER,
            BUTTON_SHORT,
            BUTTON_SHORT_HOVER,
            STICKER_EARTH,
            STICKER_COOKIE,
            STICKER_CAT,
            STICKER_BOOK_PEN,
            STICKER_SYNC_CHECK,
            STICKER_WIKI,
            BACKGROUNDS[0],
            BACKGROUNDS[1],
            BACKGROUNDS[2],
            BACKGROUNDS[3]
    };

    private static final int BUTTON_SINGLEPLAYER = 1001;
    private static final int BUTTON_MULTIPLAYER = 1002;
    private static final int BUTTON_OPTIONS = 1003;
    private static final int BUTTON_QUIT = 1004;
    private static final int BUTTON_INFO = 1005;

    private static final String MOJANG_COPYRIGHT = "Copyright Mojang AB. Do not distribute!";
    private static final int SCREEN_TOP_SCRIM = 0x24000000;
    private static final int SCREEN_BOTTOM_SCRIM = 0x3A000000;
    private static final int LOGO_TEXTURE_SIZE = 2048;
    private static final int LOGO_CONTENT_X = 65;
    private static final int LOGO_CONTENT_Y = 467;
    private static final int LOGO_CONTENT_WIDTH = 1929;
    private static final int LOGO_CONTENT_HEIGHT = 1009;
    private static boolean playedFirstAppearance;

    private final long screenOpenedAt;
    private final long backgroundStartedAt;
    private final boolean animateIntro;

    private Layout layout;

    public SofMainMenuScreen() {
        this.screenOpenedAt = Minecraft.getSystemTime();
        this.backgroundStartedAt = this.screenOpenedAt;
        this.animateIntro = !playedFirstAppearance;
        playedFirstAppearance = true;
    }

    @Override
    public void initGui() {
        this.layout = Layout.create(this.width, this.height);
        this.buttonList.clear();

        addTexturedButton(BUTTON_SINGLEPLAYER, this.layout.buttonX, rowY(0), this.layout.longButtonWidth,
                this.layout.buttonHeight, I18n.format("dwm.fm.mp"), BUTTON_LONG, BUTTON_LONG_HOVER, 0.35F);
        addTexturedButton(BUTTON_MULTIPLAYER, this.layout.buttonX, rowY(1), this.layout.longButtonWidth,
                this.layout.buttonHeight, I18n.format("dwm.fm.sp"), BUTTON_LONG, BUTTON_LONG_HOVER, 0.65F);
        addTexturedButton(BUTTON_OPTIONS, this.layout.buttonX, rowY(2), this.layout.longButtonWidth,
                this.layout.buttonHeight, I18n.format("menu.options"), BUTTON_LONG, BUTTON_LONG_HOVER, 0.95F);
        addTexturedButton(BUTTON_QUIT, this.layout.buttonX, rowY(3), this.layout.longButtonWidth,
                this.layout.buttonHeight, I18n.format("dwm.fm.quitgame"), BUTTON_LONG, BUTTON_LONG_HOVER, 1.25F)
                .setHoverLabel(I18n.format("dwm.fm.quitgame.choose"));
        addTexturedButton(BUTTON_INFO, this.layout.infoButtonX, this.layout.infoButtonY, this.layout.infoButtonWidth,
                this.layout.buttonHeight, "i", BUTTON_SHORT, BUTTON_SHORT_HOVER, 1.55F)
                .setHoverLabel(I18n.format("dwm.fm.info.short"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (!button.enabled) {
            return;
        }

        switch (button.id) {
            case BUTTON_SINGLEPLAYER:
                this.mc.displayGuiScreen(new GuiWorldSelection(this));
                break;
            case BUTTON_MULTIPLAYER:
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case BUTTON_OPTIONS:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case BUTTON_QUIT:
                this.mc.shutdown();
                break;
            case BUTTON_INFO:
                this.mc.displayGuiScreen(new SofInfoBookScreen(this));
                break;
            default:
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.layout == null) {
            this.layout = Layout.create(this.width, this.height);
        }

        drawSlideshowBackground();
        drawReadableShading();
        drawLogo(getIntroAlpha(0.0F));

        super.drawScreen(mouseX, mouseY, partialTicks);
        drawRect(this.width - this.fontRenderer.getStringWidth(MOJANG_COPYRIGHT) - 4, this.height - 12,
                this.width, this.height, 0x30000000);
        drawString(this.fontRenderer, MOJANG_COPYRIGHT,
                this.width - this.fontRenderer.getStringWidth(MOJANG_COPYRIGHT) - 2, this.height - 10, -1);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private TexturedMenuButton addTexturedButton(int id, int x, int y, int width, int height, String label,
            ResourceLocation normalTexture, ResourceLocation hoverTexture, float delaySeconds) {
        TexturedMenuButton button = new TexturedMenuButton(id, x, y, width, height, label, normalTexture,
                hoverTexture, this.screenOpenedAt, this.animateIntro ? delaySeconds : 0.0F);
        this.buttonList.add(button);
        return button;
    }

    private int rowY(int row) {
        return this.layout.firstButtonY + row * (this.layout.buttonHeight + this.layout.buttonGap);
    }

    private float getIntroAlpha(float delaySeconds) {
        if (!this.animateIntro) {
            return 1.0F;
        }

        float seconds = (Minecraft.getSystemTime() - this.screenOpenedAt) / 1000.0F;
        return MathHelper.clamp((seconds - delaySeconds) * 1.35F, 0.0F, 1.0F);
    }

    private void drawSlideshowBackground() {
        long elapsed = Minecraft.getSystemTime() - this.backgroundStartedAt;
        float seconds = elapsed / 1000.0F;
        float slideDuration = 7.0F;
        int slide = MathHelper.floor(seconds / slideDuration) % BACKGROUNDS.length;
        float progress = (seconds % slideDuration) / slideDuration;
        float fade = MathHelper.clamp((progress - 0.82F) / 0.18F, 0.0F, 1.0F);

        drawTexturedQuad(BACKGROUNDS[slide], 0, 0, this.width, this.height, 1.0F);
        if (fade > 0.0F) {
            drawTexturedQuad(BACKGROUNDS[(slide + 1) % BACKGROUNDS.length], 0, 0, this.width, this.height, fade);
        }
    }

    private void drawReadableShading() {
        drawGradientRect(0, 0, this.width, Math.max(28, this.height / 5), SCREEN_TOP_SCRIM, 0x00000000);
        drawGradientRect(0, Math.max(0, this.height - this.height / 4), this.width, this.height, 0x00000000,
                SCREEN_BOTTOM_SCRIM);
    }

    private void drawLogo(float alpha) {
        drawTexturedRegion(LOGO, this.layout.logoX, this.layout.logoY, this.layout.logoWidth,
                this.layout.logoHeight, LOGO_CONTENT_X, LOGO_CONTENT_Y, LOGO_CONTENT_WIDTH, LOGO_CONTENT_HEIGHT,
                LOGO_TEXTURE_SIZE, LOGO_TEXTURE_SIZE, alpha);
    }

    static void drawTexturedQuad(ResourceLocation texture, int x, int y, int width, int height, float alpha) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GuiScreen.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, width, height);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private static void drawTexturedRegion(ResourceLocation texture, int x, int y, int width, int height, float u,
            float v, float regionWidth, float regionHeight, float textureWidth, float textureHeight, float alpha) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        float minU = u / textureWidth;
        float minV = v / textureHeight;
        float maxU = (u + regionWidth) / textureWidth;
        float maxV = (v + regionHeight) / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, 0.0D).tex(minU, maxV).endVertex();
        buffer.pos(x + width, y + height, 0.0D).tex(maxU, maxV).endVertex();
        buffer.pos(x + width, y, 0.0D).tex(maxU, minV).endVertex();
        buffer.pos(x, y, 0.0D).tex(minU, minV).endVertex();
        tessellator.draw();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
    }

    static ResourceLocation texture(String path) {
        return new ResourceLocation("sofmenu", "textures/" + path);
    }

    public static ResourceLocation[] getPreloadTextures() {
        return PRELOAD_TEXTURES.clone();
    }

    private static class Layout {
        private static final float LOGO_ASPECT = (float) LOGO_CONTENT_WIDTH / (float) LOGO_CONTENT_HEIGHT;

        final int logoX;
        final int logoY;
        final int logoWidth;
        final int logoHeight;
        final int buttonX;
        final int firstButtonY;
        final int longButtonWidth;
        final int buttonHeight;
        final int buttonGap;
        final int infoButtonX;
        final int infoButtonY;
        final int infoButtonWidth;

        private Layout(int logoX, int logoY, int logoWidth, int logoHeight, int buttonX, int firstButtonY,
                int longButtonWidth, int buttonHeight, int buttonGap, int infoButtonX, int infoButtonY,
                int infoButtonWidth) {
            this.logoX = logoX;
            this.logoY = logoY;
            this.logoWidth = logoWidth;
            this.logoHeight = logoHeight;
            this.buttonX = buttonX;
            this.firstButtonY = firstButtonY;
            this.longButtonWidth = longButtonWidth;
            this.buttonHeight = buttonHeight;
            this.buttonGap = buttonGap;
            this.infoButtonX = infoButtonX;
            this.infoButtonY = infoButtonY;
            this.infoButtonWidth = infoButtonWidth;
        }

        static Layout create(int screenWidth, int screenHeight) {
            int margin = 8;
            int longButtonWidth = MathHelper.clamp(Math.round(screenWidth * 0.30F), 108, 176);
            int buttonHeight = MathHelper.clamp(Math.round(longButtonWidth * 20.0F / 108.0F), 20, 30);
            int buttonGap = MathHelper.clamp(Math.round(buttonHeight * 0.22F), 4, 8);
            int logoWidth = MathHelper.clamp(Math.round(longButtonWidth * 1.10F), 128, 210);
            int logoHeight = MathHelper.clamp(Math.round(logoWidth / LOGO_ASPECT), 58, 108);
            int logoGap = Math.max(4, Math.round(buttonHeight * 0.20F));
            int buttonsHeight = buttonHeight * 4 + buttonGap * 3;
            int menuHeight = logoHeight + logoGap + buttonsHeight;
            int menuTop = MathHelper.clamp((screenHeight - menuHeight) / 2, margin,
                    Math.max(margin, screenHeight - menuHeight - margin));
            int buttonX = (screenWidth - longButtonWidth) / 2;
            int logoX = (screenWidth - logoWidth) / 2;
            int logoY = menuTop;
            int firstButtonY = logoY + logoHeight + logoGap;
            int infoButtonWidth = MathHelper.clamp(Math.round(longButtonWidth * 0.34F), 38, 56);
            int infoGap = Math.max(4, Math.round(longButtonWidth * 0.035F));
            int infoButtonX = buttonX + longButtonWidth + infoGap;
            if (infoButtonX + infoButtonWidth > screenWidth - margin) {
                infoButtonX = screenWidth - margin - infoButtonWidth;
            }
            int infoButtonY = firstButtonY + (buttonHeight + buttonGap) * 2;

            return new Layout(logoX, logoY, logoWidth, logoHeight, buttonX, firstButtonY, longButtonWidth,
                    buttonHeight, buttonGap, infoButtonX, infoButtonY, infoButtonWidth);
        }
    }
}

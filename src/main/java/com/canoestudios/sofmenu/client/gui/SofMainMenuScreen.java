package com.canoestudios.sofmenu.client.gui;

import com.canoestudios.sofmenu.client.session.LastSessionStore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

@SideOnly(Side.CLIENT)
public class SofMainMenuScreen extends GuiScreen {

    private static final ResourceLocation[] BACKGROUNDS = new ResourceLocation[] {
            texture("menu/backgrounds/image_01.png"),
            texture("menu/backgrounds/image_02.png"),
            texture("menu/backgrounds/image_03.png"),
            texture("menu/backgrounds/image_04.png")
    };
    private static final ResourceLocation BLUEPRINT = texture("menu/blueprint_texture.png");
    private static final ResourceLocation LOGO = texture("menu/logo.png");
    private static final ResourceLocation BUTTON_LARGE = texture("menu/buttons/button_large.png");
    private static final ResourceLocation BUTTON_LARGE_HOVER = texture("menu/buttons/button_large_hover.png");
    private static final ResourceLocation BUTTON_MEDIUM = texture("menu/buttons/button_medium.png");
    private static final ResourceLocation BUTTON_MEDIUM_HOVER = texture("menu/buttons/button_medium_hover.png");
    private static final ResourceLocation BUTTON_SMALL = texture("menu/buttons/button_small.png");
    private static final ResourceLocation BUTTON_SMALL_HOVER = texture("menu/buttons/button_small_hover.png");
    private static final ResourceLocation[] PRELOAD_TEXTURES = new ResourceLocation[] {
            BLUEPRINT,
            LOGO,
            BUTTON_LARGE,
            BUTTON_LARGE_HOVER,
            BUTTON_MEDIUM,
            BUTTON_MEDIUM_HOVER,
            BUTTON_SMALL,
            BUTTON_SMALL_HOVER,
            BACKGROUNDS[0],
            BACKGROUNDS[1],
            BACKGROUNDS[2],
            BACKGROUNDS[3]
    };

    private static final int BUTTON_LAST_WORLD = 1000;
    private static final int BUTTON_SINGLEPLAYER = 1001;
    private static final int BUTTON_MULTIPLAYER = 1002;
    private static final int BUTTON_OPTIONS = 1003;
    private static final int BUTTON_LANGUAGE = 1004;
    private static final int BUTTON_MODS = 1005;
    private static final int BUTTON_QUIT = 1006;
    private static final int BUTTON_WEB = 1007;
    private static final int CONFIRM_WEB = 1100;

    private static final String MOJANG_COPYRIGHT = "Copyright Mojang AB. Do not distribute!";
    private static final String WEB_URL = "https://github.com/CanoeStudioOfficial/Destiny-Winds-Drifter-s-Chronicle";
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

        addTexturedButton(BUTTON_LAST_WORLD, this.layout.logoX, this.layout.firstButtonY, this.layout.logoSize, 20,
                I18n.format("dwm.fm.lls"), BUTTON_LARGE, BUTTON_LARGE_HOVER, 0.5F);
        addTexturedButton(BUTTON_SINGLEPLAYER, this.layout.logoX, rowY(1.1F), this.layout.logoSize, 20,
                I18n.format("dwm.fm.mp"), BUTTON_LARGE, BUTTON_LARGE_HOVER, 1.0F);
        addTexturedButton(BUTTON_MULTIPLAYER, this.layout.logoX, rowY(2.2F), this.layout.logoSize, 20,
                I18n.format("dwm.fm.sp"), BUTTON_LARGE, BUTTON_LARGE_HOVER, 1.5F);
        addTexturedButton(BUTTON_OPTIONS, this.layout.logoX, rowY(3.3F), this.layout.logoSize - 25, 20,
                I18n.format("menu.options"), BUTTON_MEDIUM, BUTTON_MEDIUM_HOVER, 2.0F);
        addTexturedButton(BUTTON_LANGUAGE, this.layout.logoX + this.layout.logoSize - 21, rowY(3.3F), 20, 20,
                "", BUTTON_SMALL, BUTTON_SMALL_HOVER, 2.5F);
        addTexturedButton(BUTTON_MODS, this.layout.logoX, rowY(4.4F), this.layout.logoSize, 20,
                I18n.format("dwm.fm.mods"), BUTTON_LARGE, BUTTON_LARGE_HOVER, 3.0F);
        addTexturedButton(BUTTON_QUIT, this.layout.logoX, rowY(5.5F), this.layout.logoSize, 20,
                I18n.format("dwm.fm.quitgame"), BUTTON_LARGE, BUTTON_LARGE_HOVER, 3.5F)
                .setHoverLabel(I18n.format("dwm.fm.quitgame.choose"));
        addTexturedButton(BUTTON_WEB, this.layout.logoX, rowY(6.6F), this.layout.logoSize, 20,
                I18n.format("dwm.fm.web"), BUTTON_LARGE, BUTTON_LARGE_HOVER, 0.0F);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (!button.enabled) {
            return;
        }

        switch (button.id) {
            case BUTTON_LAST_WORLD:
                LastSessionStore.joinLastSession(this);
                break;
            case BUTTON_SINGLEPLAYER:
                this.mc.displayGuiScreen(new GuiWorldSelection(this));
                break;
            case BUTTON_MULTIPLAYER:
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case BUTTON_OPTIONS:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case BUTTON_LANGUAGE:
                this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
                break;
            case BUTTON_MODS:
                this.mc.displayGuiScreen(new GuiModList(this));
                break;
            case BUTTON_QUIT:
                this.mc.shutdown();
                break;
            case BUTTON_WEB:
                GuiConfirmOpenLink confirm = new GuiConfirmOpenLink(this, WEB_URL, CONFIRM_WEB, false);
                confirm.disableSecurityWarning();
                this.mc.displayGuiScreen(confirm);
                break;
            default:
                break;
        }
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        if (id == CONFIRM_WEB && result) {
            try {
                openWebLink(new URI(WEB_URL));
            } catch (Exception ignored) {
            }
        }

        this.mc.displayGuiScreen(this);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.layout == null) {
            this.layout = Layout.create(this.width, this.height);
        }

        drawSlideshowBackground();
        drawTexturedQuad(BLUEPRINT, this.layout.panelX, 0, this.layout.panelSize, this.layout.panelSize, 1.0F);
        drawTexturedQuad(LOGO, this.layout.logoX, this.layout.logoY, this.layout.logoSize, this.layout.logoSize, 1.0F);

        super.drawScreen(mouseX, mouseY, partialTicks);
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

    private int rowY(float multiplier) {
        return this.layout.firstButtonY + Math.round(20.0F * multiplier);
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

    private static void drawTexturedQuad(ResourceLocation texture, int x, int y, int width, int height, float alpha) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GuiScreen.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, width, height);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static ResourceLocation texture(String path) {
        return new ResourceLocation("sofmenu", "textures/" + path);
    }

    public static ResourceLocation[] getPreloadTextures() {
        return PRELOAD_TEXTURES.clone();
    }

    private static void openWebLink(URI uri) throws Exception {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(uri);
            return;
        }

        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            Runtime.getRuntime().exec(new String[] {"rundll32", "url.dll,FileProtocolHandler", uri.toString()});
        } else if (os.contains("mac")) {
            Runtime.getRuntime().exec(new String[] {"open", uri.toString()});
        } else {
            Runtime.getRuntime().exec(new String[] {"xdg-open", uri.toString()});
        }
    }

    private static class Layout {
        final int panelX;
        final int panelSize;
        final int logoX;
        final int logoY;
        final int logoSize;
        final int firstButtonY;

        private Layout(int panelX, int panelSize, int logoX, int logoY, int logoSize, int firstButtonY) {
            this.panelX = panelX;
            this.panelSize = panelSize;
            this.logoX = logoX;
            this.logoY = logoY;
            this.logoSize = logoSize;
            this.firstButtonY = firstButtonY;
        }

        static Layout create(int screenWidth, int screenHeight) {
            int panelSize = screenHeight;
            int panelX = Math.max(0, screenWidth - panelSize);
            int logoSize = MathHelper.clamp(screenHeight / 2, 118, Math.max(118, screenWidth - 16));
            int logoX = panelX + Math.round(panelSize * 0.6F) - logoSize / 2;
            logoX = MathHelper.clamp(logoX, 8, Math.max(8, screenWidth - logoSize - 8));
            int logoY = Math.round(logoSize * -0.17F);
            int firstButtonY = logoY + logoSize + 10;
            int bottom = firstButtonY + Math.round(20.0F * 6.6F) + 20;
            if (bottom > screenHeight - 6) {
                firstButtonY -= bottom - (screenHeight - 6);
            }
            return new Layout(panelX, panelSize, logoX, logoY, logoSize, firstButtonY);
        }
    }
}

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
    private static final ResourceLocation SOF_LOGO = texture("menu/sof_logo_full.png");
    private static final ResourceLocation INFO_BOOK = texture("menu/info_menu_book.png");
    private static final ResourceLocation BUTTON_LONG = texture("menu/buttons/longbutton_normal.png");
    private static final ResourceLocation BUTTON_LONG_HOVER = texture("menu/buttons/longbutton_hover.png");
    private static final ResourceLocation BUTTON_SHORT = texture("menu/buttons/shortbutton_normal.png");
    private static final ResourceLocation BUTTON_SHORT_HOVER = texture("menu/buttons/shortbutton_hover.png");
    private static final ResourceLocation[] PRELOAD_TEXTURES = new ResourceLocation[] {
            SOF_LOGO,
            INFO_BOOK,
            BUTTON_LONG,
            BUTTON_LONG_HOVER,
            BUTTON_SHORT,
            BUTTON_SHORT_HOVER,
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

        int longX = this.layout.buttonX + (this.layout.optionRowWidth - this.layout.longButtonWidth) / 2;
        addTexturedButton(BUTTON_LAST_WORLD, longX, rowY(0), this.layout.longButtonWidth, this.layout.buttonHeight,
                I18n.format("dwm.fm.lls"), BUTTON_LONG, BUTTON_LONG_HOVER, 0.35F);
        addTexturedButton(BUTTON_SINGLEPLAYER, longX, rowY(1), this.layout.longButtonWidth, this.layout.buttonHeight,
                I18n.format("dwm.fm.mp"), BUTTON_LONG, BUTTON_LONG_HOVER, 0.65F);
        addTexturedButton(BUTTON_MULTIPLAYER, longX, rowY(2), this.layout.longButtonWidth, this.layout.buttonHeight,
                I18n.format("dwm.fm.sp"), BUTTON_LONG, BUTTON_LONG_HOVER, 0.95F);
        addTexturedButton(BUTTON_MODS, longX, rowY(3), this.layout.longButtonWidth, this.layout.buttonHeight,
                I18n.format("dwm.fm.mods"), BUTTON_LONG, BUTTON_LONG_HOVER, 1.25F);
        addTexturedButton(BUTTON_OPTIONS, this.layout.buttonX, rowY(4), this.layout.longButtonWidth,
                this.layout.buttonHeight, I18n.format("menu.options"), BUTTON_LONG, BUTTON_LONG_HOVER, 1.55F);
        addTexturedButton(BUTTON_LANGUAGE, this.layout.buttonX + this.layout.longButtonWidth + this.layout.smallButtonGap,
                rowY(4), this.layout.shortButtonWidth, this.layout.buttonHeight, I18n.format("dwm.fm.lang.short"),
                BUTTON_SHORT, BUTTON_SHORT_HOVER, 1.85F);
        addTexturedButton(BUTTON_WEB, longX, rowY(5), this.layout.longButtonWidth, this.layout.buttonHeight,
                I18n.format("dwm.fm.web"), BUTTON_LONG, BUTTON_LONG_HOVER, 2.15F);
        addTexturedButton(BUTTON_QUIT, longX, rowY(6), this.layout.longButtonWidth, this.layout.buttonHeight,
                I18n.format("dwm.fm.quitgame"), BUTTON_LONG, BUTTON_LONG_HOVER, 2.45F)
                .setHoverLabel(I18n.format("dwm.fm.quitgame.choose"));
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
        drawRect(0, 0, this.width, this.height, 0xB8000000);
        drawTexturedQuad(SOF_LOGO, this.layout.logoX, this.layout.logoY, this.layout.logoSize, this.layout.logoSize,
                getIntroAlpha(0.0F));
        drawTexturedQuad(INFO_BOOK, this.layout.bookX, this.layout.bookY, this.layout.bookWidth,
                this.layout.bookHeight, getIntroAlpha(0.15F));

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
        private static final float BOOK_ASPECT = 1200.0F / 794.0F;

        final int logoX;
        final int logoY;
        final int logoSize;
        final int bookX;
        final int bookY;
        final int bookWidth;
        final int bookHeight;
        final int buttonX;
        final int firstButtonY;
        final int longButtonWidth;
        final int shortButtonWidth;
        final int buttonHeight;
        final int buttonGap;
        final int smallButtonGap;
        final int optionRowWidth;

        private Layout(int logoX, int logoY, int logoSize, int bookX, int bookY, int bookWidth, int bookHeight,
                int buttonX, int firstButtonY, int longButtonWidth, int shortButtonWidth, int buttonHeight,
                int buttonGap, int smallButtonGap, int optionRowWidth) {
            this.logoX = logoX;
            this.logoY = logoY;
            this.logoSize = logoSize;
            this.bookX = bookX;
            this.bookY = bookY;
            this.bookWidth = bookWidth;
            this.bookHeight = bookHeight;
            this.buttonX = buttonX;
            this.firstButtonY = firstButtonY;
            this.longButtonWidth = longButtonWidth;
            this.shortButtonWidth = shortButtonWidth;
            this.buttonHeight = buttonHeight;
            this.buttonGap = buttonGap;
            this.smallButtonGap = smallButtonGap;
            this.optionRowWidth = optionRowWidth;
        }

        static Layout create(int screenWidth, int screenHeight) {
            boolean wide = screenWidth >= 760;
            int margin = 14;
            int bookHeight;
            int bookWidth;
            int bookX;
            int bookY;
            int logoSize;
            int logoX;
            int logoY;

            if (wide) {
                bookHeight = MathHelper.clamp(Math.round(screenHeight * 0.72F), 245, 390);
                bookWidth = Math.round(bookHeight * BOOK_ASPECT);
                if (bookWidth > screenWidth - 260) {
                    bookWidth = MathHelper.clamp(screenWidth - 260, 360, screenWidth - margin * 2);
                    bookHeight = Math.round(bookWidth / BOOK_ASPECT);
                }
                bookX = screenWidth - bookWidth - margin;
                bookY = (screenHeight - bookHeight) / 2;

                int logoSpace = Math.max(140, bookX - margin * 2);
                logoSize = MathHelper.clamp(Math.min(Math.round(screenHeight * 0.56F), logoSpace), 145, 380);
                logoX = Math.max(margin, (bookX - logoSize) / 2);
                logoY = MathHelper.clamp((screenHeight - logoSize) / 2 - Math.round(screenHeight * 0.02F), 8,
                        Math.max(8, screenHeight - logoSize - 18));
            } else {
                logoSize = MathHelper.clamp(Math.round(screenWidth * 0.62F), 132, Math.max(132, screenHeight / 3));
                logoX = (screenWidth - logoSize) / 2;
                logoY = 8;

                bookWidth = MathHelper.clamp(screenWidth - margin * 2, 300, 560);
                bookHeight = Math.round(bookWidth / BOOK_ASPECT);
                int availableBookHeight = screenHeight - logoY - logoSize + 8;
                if (bookHeight > availableBookHeight) {
                    bookHeight = MathHelper.clamp(availableBookHeight, 205, Math.max(205, screenHeight - 28));
                    bookWidth = Math.round(bookHeight * BOOK_ASPECT);
                }
                bookX = (screenWidth - bookWidth) / 2;
                bookY = MathHelper.clamp(screenHeight - bookHeight - 12, logoY + logoSize - 10,
                        Math.max(logoY + logoSize - 10, screenHeight - bookHeight - 8));
            }

            int longButtonWidth = MathHelper.clamp(Math.round(bookWidth * 0.225F), 96, 132);
            int buttonHeight = MathHelper.clamp(Math.round(longButtonWidth * 20.0F / 108.0F), 18, 24);
            int shortButtonWidth = MathHelper.clamp(Math.round(longButtonWidth * 51.0F / 108.0F), 46, 62);
            int smallButtonGap = Math.max(4, Math.round(bookWidth * 0.012F));
            int optionRowWidth = longButtonWidth + smallButtonGap + shortButtonWidth;
            int buttonX = bookX + Math.round(bookWidth * 0.75F) - optionRowWidth / 2;
            int firstButtonY = bookY + Math.round(bookHeight * 0.225F);
            int pageBottom = bookY + Math.round(bookHeight * 0.845F);
            int available = Math.max(120, pageBottom - firstButtonY);
            int buttonGap = (available - buttonHeight * 7) / 6;

            if (buttonGap < 2) {
                buttonGap = 2;
                buttonHeight = Math.max(16, (available - buttonGap * 6) / 7);
            }

            int lastButtonBottom = firstButtonY + buttonHeight * 7 + buttonGap * 6;
            if (lastButtonBottom > pageBottom) {
                firstButtonY -= lastButtonBottom - pageBottom;
            }

            return new Layout(logoX, logoY, logoSize, bookX, bookY, bookWidth, bookHeight, buttonX, firstButtonY,
                    longButtonWidth, shortButtonWidth, buttonHeight, buttonGap, smallButtonGap, optionRowWidth);
        }
    }
}

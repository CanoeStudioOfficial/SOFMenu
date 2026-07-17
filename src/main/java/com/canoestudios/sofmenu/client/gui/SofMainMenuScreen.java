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

        int longX = this.layout.buttonX;
        addTexturedButton(BUTTON_LAST_WORLD, longX, rowY(0), this.layout.longButtonWidth, this.layout.buttonHeight,
                I18n.format("dwm.fm.lls"), BUTTON_LONG, BUTTON_LONG_HOVER, 0.35F);
        addTexturedButton(BUTTON_SINGLEPLAYER, longX, rowY(1), this.layout.longButtonWidth, this.layout.buttonHeight,
                I18n.format("dwm.fm.mp"), BUTTON_LONG, BUTTON_LONG_HOVER, 0.65F);
        addTexturedButton(BUTTON_MULTIPLAYER, longX, rowY(2), this.layout.longButtonWidth, this.layout.buttonHeight,
                I18n.format("dwm.fm.sp"), BUTTON_LONG, BUTTON_LONG_HOVER, 0.95F);
        addTexturedButton(BUTTON_MODS, longX, rowY(3), this.layout.longButtonWidth, this.layout.buttonHeight,
                I18n.format("dwm.fm.mods"), BUTTON_LONG, BUTTON_LONG_HOVER, 1.25F);
        addTexturedButton(BUTTON_OPTIONS, this.layout.buttonX, rowY(4), this.layout.optionButtonWidth,
                this.layout.buttonHeight, I18n.format("menu.options"), BUTTON_LONG, BUTTON_LONG_HOVER, 1.55F);
        addTexturedButton(BUTTON_LANGUAGE, this.layout.buttonX + this.layout.optionButtonWidth
                + this.layout.smallButtonGap, rowY(4), this.layout.shortButtonWidth, this.layout.buttonHeight,
                I18n.format("dwm.fm.lang.short"), BUTTON_SHORT, BUTTON_SHORT_HOVER, 1.85F);
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
        drawTexturedQuad(INFO_BOOK, this.layout.bookX, this.layout.bookY, this.layout.bookWidth,
                this.layout.bookHeight, getIntroAlpha(0.0F));
        drawDecorativeStickers(getIntroAlpha(0.2F));

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

    private void drawDecorativeStickers(float alpha) {
        drawSticker(STICKER_EARTH, 0.620F, 0.205F, 0.092F, 1.25F, alpha);
        drawSticker(STICKER_COOKIE, 0.805F, 0.200F, 0.090F, 1.0F, alpha);
        drawSticker(STICKER_CAT, 0.625F, 0.405F, 0.118F, 1.25F, alpha);
        drawSticker(STICKER_BOOK_PEN, 0.805F, 0.405F, 0.142F, 1.0F, alpha);
        drawSticker(STICKER_SYNC_CHECK, 0.625F, 0.625F, 0.082F, 1.0F, alpha);
        drawSticker(STICKER_WIKI, 0.805F, 0.625F, 0.118F, 1.0F, alpha);
    }

    private void drawSticker(ResourceLocation texture, float centerX, float centerY, float widthRatio,
            float textureAspect, float alpha) {
        int stickerWidth = Math.max(14, Math.round(this.layout.bookWidth * widthRatio));
        int stickerHeight = Math.max(14, Math.round(stickerWidth / textureAspect));
        int x = this.layout.bookX + Math.round(this.layout.bookWidth * centerX) - stickerWidth / 2;
        int y = this.layout.bookY + Math.round(this.layout.bookHeight * centerY) - stickerHeight / 2;
        drawTexturedQuad(texture, x, y, stickerWidth, stickerHeight, alpha);
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

        final int bookX;
        final int bookY;
        final int bookWidth;
        final int bookHeight;
        final int buttonX;
        final int firstButtonY;
        final int longButtonWidth;
        final int optionButtonWidth;
        final int shortButtonWidth;
        final int buttonHeight;
        final int buttonGap;
        final int smallButtonGap;

        private Layout(int bookX, int bookY, int bookWidth, int bookHeight, int buttonX, int firstButtonY,
                int longButtonWidth, int optionButtonWidth, int shortButtonWidth, int buttonHeight, int buttonGap,
                int smallButtonGap) {
            this.bookX = bookX;
            this.bookY = bookY;
            this.bookWidth = bookWidth;
            this.bookHeight = bookHeight;
            this.buttonX = buttonX;
            this.firstButtonY = firstButtonY;
            this.longButtonWidth = longButtonWidth;
            this.optionButtonWidth = optionButtonWidth;
            this.shortButtonWidth = shortButtonWidth;
            this.buttonHeight = buttonHeight;
            this.buttonGap = buttonGap;
            this.smallButtonGap = smallButtonGap;
        }

        static Layout create(int screenWidth, int screenHeight) {
            int margin = 8;
            int maxBookWidth = Math.max(220, screenWidth - margin * 2);
            int maxBookHeight = Math.max(165, screenHeight - 28);
            float heightFill = screenHeight < 380 ? 0.86F : 0.72F;
            float widthFill = screenWidth < 560 ? 0.96F : 0.82F;
            int bookHeight = MathHelper.clamp(Math.round(screenHeight * heightFill), Math.min(185, maxBookHeight),
                    maxBookHeight);
            int bookWidth = Math.round(bookHeight * BOOK_ASPECT);
            int widthCap = Math.min(maxBookWidth, Math.round(screenWidth * widthFill));
            if (bookWidth > widthCap) {
                bookWidth = widthCap;
                bookHeight = Math.round(bookWidth / BOOK_ASPECT);
            }
            bookWidth = MathHelper.clamp(bookWidth, Math.min(300, maxBookWidth), maxBookWidth);
            bookHeight = Math.round(bookWidth / BOOK_ASPECT);

            if (bookHeight > maxBookHeight) {
                bookHeight = maxBookHeight;
                bookWidth = Math.round(bookHeight * BOOK_ASPECT);
            }

            int bookX = (screenWidth - bookWidth) / 2;
            int bookY = Math.max(6, (screenHeight - bookHeight) / 2);

            int longButtonWidth = MathHelper.clamp(Math.round(bookWidth * 0.34F), 88, 210);
            int buttonHeight = MathHelper.clamp(Math.round(bookHeight * 0.062F), 18, 28);
            int smallButtonGap = Math.max(3, Math.round(bookWidth * 0.008F));
            int shortButtonWidth = MathHelper.clamp(Math.round(longButtonWidth * 0.34F), 38, 64);
            int optionButtonWidth = longButtonWidth - smallButtonGap - shortButtonWidth;
            if (optionButtonWidth < 52) {
                shortButtonWidth = Math.max(34, longButtonWidth - smallButtonGap - 52);
                optionButtonWidth = longButtonWidth - smallButtonGap - shortButtonWidth;
            }

            int buttonCenterX = bookX + Math.round(bookWidth * 0.252F);
            int pageLeft = bookX + Math.round(bookWidth * 0.080F);
            int pageRight = bookX + Math.round(bookWidth * 0.475F);
            int maxButtonX = Math.max(pageLeft, pageRight - longButtonWidth);
            int buttonX = MathHelper.clamp(buttonCenterX - longButtonWidth / 2, pageLeft, maxButtonX);

            int firstButtonY = bookY + Math.round(bookHeight * 0.108F);
            int pageBottom = bookY + Math.round(bookHeight * 0.820F);
            int available = Math.max(buttonHeight * 7 + 12, pageBottom - firstButtonY);
            int maxGap = Math.max(4, Math.round(bookHeight * 0.044F));
            int buttonGap = MathHelper.clamp((available - buttonHeight * 7) / 6, 4, Math.min(16, maxGap));
            int buttonsHeight = buttonHeight * 7 + buttonGap * 6;
            if (firstButtonY + buttonsHeight > pageBottom) {
                firstButtonY = Math.max(bookY + Math.round(bookHeight * 0.075F), pageBottom - buttonsHeight);
            }

            return new Layout(bookX, bookY, bookWidth, bookHeight, buttonX, firstButtonY, longButtonWidth,
                    optionButtonWidth, shortButtonWidth, buttonHeight, buttonGap, smallButtonGap);
        }
    }
}

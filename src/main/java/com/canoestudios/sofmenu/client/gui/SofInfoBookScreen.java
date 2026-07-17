package com.canoestudios.sofmenu.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

@SideOnly(Side.CLIENT)
public class SofInfoBookScreen extends GuiScreen {

    private static final ResourceLocation[] BACKGROUNDS = new ResourceLocation[] {
            SofMainMenuScreen.texture("menu/backgrounds/image_01.png"),
            SofMainMenuScreen.texture("menu/backgrounds/image_02.png"),
            SofMainMenuScreen.texture("menu/backgrounds/image_03.png"),
            SofMainMenuScreen.texture("menu/backgrounds/image_04.png")
    };
    private static final ResourceLocation INFO_BOOK = SofMainMenuScreen.texture("menu/info_menu_book.png");
    private static final ResourceLocation BUTTON_LONG = SofMainMenuScreen.texture("menu/buttons/longbutton_normal.png");
    private static final ResourceLocation BUTTON_SHORT = SofMainMenuScreen.texture("menu/buttons/shortbutton_normal.png");
    private static final ResourceLocation BUTTON_SHORT_HOVER = SofMainMenuScreen.texture("menu/buttons/shortbutton_hover.png");
    private static final ResourceLocation STICKER_EARTH = SofMainMenuScreen.texture("menu/stickers/earth.png");
    private static final ResourceLocation STICKER_COOKIE = SofMainMenuScreen.texture("menu/stickers/cookie.png");
    private static final ResourceLocation STICKER_CAT = SofMainMenuScreen.texture("menu/stickers/cat.png");
    private static final ResourceLocation STICKER_BOOK_PEN = SofMainMenuScreen.texture("menu/stickers/book_pen.png");
    private static final ResourceLocation STICKER_SYNC_CHECK = SofMainMenuScreen.texture("menu/stickers/sync_check.png");
    private static final ResourceLocation STICKER_WIKI = SofMainMenuScreen.texture("menu/stickers/wiki.png");

    private static final int BUTTON_BACK = 2000;
    private static final int BUTTON_WEBSITE = 2001;
    private static final int BUTTON_GITHUB = 2002;
    private static final int BUTTON_CREDITS = 2003;
    private static final int CONFIRM_WEBSITE = 2101;
    private static final int CONFIRM_GITHUB = 2102;
    private static final String WEBSITE_URL = "https://www.wecanoe.top/";
    private static final String GITHUB_URL = "https://github.com/CanoeStudioOfficial/dev_modpack";

    private final GuiScreen parent;
    private final long screenOpenedAt;
    private final long backgroundStartedAt;
    private Layout layout;

    public SofInfoBookScreen(GuiScreen parent) {
        this.parent = parent;
        this.screenOpenedAt = Minecraft.getSystemTime();
        this.backgroundStartedAt = this.screenOpenedAt;
    }

    @Override
    public void initGui() {
        this.layout = Layout.create(this.width, this.height);
        this.buttonList.clear();
        this.buttonList.add(new TexturedMenuButton(BUTTON_BACK, this.layout.backButtonX, this.layout.backButtonY,
                this.layout.backButtonWidth, this.layout.backButtonHeight, "<", BUTTON_SHORT, BUTTON_SHORT_HOVER,
                this.screenOpenedAt, 0.25F));
        this.buttonList.add(new InvisibleHoverButton(BUTTON_WEBSITE, this.layout.websiteButtonX,
                this.layout.websiteButtonY, this.layout.websiteButtonSize, this.layout.websiteButtonSize,
                "dwm.fm.website"));
        this.buttonList.add(new InvisibleHoverButton(BUTTON_GITHUB, this.layout.githubButtonX,
                this.layout.githubButtonY, this.layout.githubButtonSize, this.layout.githubButtonSize,
                "dwm.fm.web"));
        this.buttonList.add(new InvisibleHoverButton(BUTTON_CREDITS, this.layout.creditsButtonX,
                this.layout.creditsButtonY, this.layout.creditsButtonSize, this.layout.creditsButtonSize,
                "dwm.fm.credits"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (!button.enabled) {
            return;
        }

        switch (button.id) {
            case BUTTON_BACK:
                this.mc.displayGuiScreen(this.parent);
                break;
            case BUTTON_WEBSITE:
                openLinkConfirm(WEBSITE_URL, CONFIRM_WEBSITE);
                break;
            case BUTTON_GITHUB:
                openLinkConfirm(GITHUB_URL, CONFIRM_GITHUB);
                break;
            case BUTTON_CREDITS:
                this.mc.displayGuiScreen(new SofCreditsScreen(this));
                break;
            default:
                break;
        }
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        if (result && (id == CONFIRM_WEBSITE || id == CONFIRM_GITHUB)) {
            try {
                openWebLink(new URI(id == CONFIRM_WEBSITE ? WEBSITE_URL : GITHUB_URL));
            } catch (Exception ignored) {
            }
        }

        this.mc.displayGuiScreen(this);
    }

    private void openLinkConfirm(String url, int confirmId) {
        GuiConfirmOpenLink confirm = new GuiConfirmOpenLink(this, url, confirmId, false);
        confirm.disableSecurityWarning();
        this.mc.displayGuiScreen(confirm);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(this.parent);
            return;
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.layout == null) {
            this.layout = Layout.create(this.width, this.height);
        }

        drawSlideshowBackground();
        drawRect(0, 0, this.width, this.height, 0xB4000000);
        float alpha = getIntroAlpha(0.0F);
        SofMainMenuScreen.drawTexturedQuad(INFO_BOOK, this.layout.bookX, this.layout.bookY, this.layout.bookWidth,
                this.layout.bookHeight, alpha);
        drawLeftPageSlots(alpha);
        drawDecorativeStickers(getIntroAlpha(0.15F));

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private float getIntroAlpha(float delaySeconds) {
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

        SofMainMenuScreen.drawTexturedQuad(BACKGROUNDS[slide], 0, 0, this.width, this.height, 1.0F);
        if (fade > 0.0F) {
            SofMainMenuScreen.drawTexturedQuad(BACKGROUNDS[(slide + 1) % BACKGROUNDS.length], 0, 0, this.width,
                    this.height, fade);
        }
    }

    private void drawLeftPageSlots(float alpha) {
        for (int row = 0; row < 6; row++) {
            int y = this.layout.firstSlotY + row * (this.layout.slotHeight + this.layout.slotGap);
            SofMainMenuScreen.drawTexturedQuad(BUTTON_LONG, this.layout.slotX, y, this.layout.slotWidth,
                    this.layout.slotHeight, alpha * 0.72F);
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
        SofMainMenuScreen.drawTexturedQuad(texture, x, y, stickerWidth, stickerHeight, alpha);
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

    private static class InvisibleHoverButton extends GuiButton {

        private final String labelKey;

        InvisibleHoverButton(int buttonId, int x, int y, int widthIn, int heightIn, String labelKey) {
            super(buttonId, x, y, widthIn, heightIn, "");
            this.labelKey = labelKey;
        }

        @Override
        public void drawButton(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
            if (!this.visible) {
                return;
            }

            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
                    && mouseY < this.y + this.height;
            if (this.hovered) {
                drawRect(this.x - 2, this.y - 2, this.x + this.width + 2, this.y + this.height + 2, 0x46FFFFFF);
                drawCenteredString(minecraft.fontRenderer, I18n.format(this.labelKey), this.x + this.width / 2,
                        this.y + this.height + 4, 0xFFFFFFFF);
            }
        }
    }

    private static class Layout {
        private static final float BOOK_ASPECT = 1200.0F / 794.0F;

        final int bookX;
        final int bookY;
        final int bookWidth;
        final int bookHeight;
        final int slotX;
        final int firstSlotY;
        final int slotWidth;
        final int slotHeight;
        final int slotGap;
        final int backButtonX;
        final int backButtonY;
        final int backButtonWidth;
        final int backButtonHeight;
        final int websiteButtonX;
        final int websiteButtonY;
        final int websiteButtonSize;
        final int githubButtonX;
        final int githubButtonY;
        final int githubButtonSize;
        final int creditsButtonX;
        final int creditsButtonY;
        final int creditsButtonSize;

        private Layout(int bookX, int bookY, int bookWidth, int bookHeight, int slotX, int firstSlotY, int slotWidth,
                int slotHeight, int slotGap, int backButtonX, int backButtonY, int backButtonWidth,
                int backButtonHeight, int websiteButtonX, int websiteButtonY, int websiteButtonSize,
                int githubButtonX, int githubButtonY, int githubButtonSize, int creditsButtonX, int creditsButtonY,
                int creditsButtonSize) {
            this.bookX = bookX;
            this.bookY = bookY;
            this.bookWidth = bookWidth;
            this.bookHeight = bookHeight;
            this.slotX = slotX;
            this.firstSlotY = firstSlotY;
            this.slotWidth = slotWidth;
            this.slotHeight = slotHeight;
            this.slotGap = slotGap;
            this.backButtonX = backButtonX;
            this.backButtonY = backButtonY;
            this.backButtonWidth = backButtonWidth;
            this.backButtonHeight = backButtonHeight;
            this.websiteButtonX = websiteButtonX;
            this.websiteButtonY = websiteButtonY;
            this.websiteButtonSize = websiteButtonSize;
            this.githubButtonX = githubButtonX;
            this.githubButtonY = githubButtonY;
            this.githubButtonSize = githubButtonSize;
            this.creditsButtonX = creditsButtonX;
            this.creditsButtonY = creditsButtonY;
            this.creditsButtonSize = creditsButtonSize;
        }

        static Layout create(int screenWidth, int screenHeight) {
            int margin = 8;
            int maxBookWidth = Math.max(220, screenWidth - margin * 2);
            int maxBookHeight = Math.max(165, screenHeight - 24);
            int bookHeight = MathHelper.clamp(Math.round(screenHeight * 0.80F), Math.min(185, maxBookHeight),
                    maxBookHeight);
            int bookWidth = Math.round(bookHeight * BOOK_ASPECT);
            int widthCap = Math.min(maxBookWidth, Math.round(screenWidth * 0.92F));
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
            int slotWidth = MathHelper.clamp(Math.round(bookWidth * 0.32F), 94, 230);
            int slotHeight = MathHelper.clamp(Math.round(bookHeight * 0.054F), 15, 27);
            int slotX = bookX + Math.round(bookWidth * 0.112F);
            int firstSlotY = bookY + Math.round(bookHeight * 0.120F);
            int slotGap = MathHelper.clamp(Math.round(bookHeight * 0.033F), 5, 15);
            int backButtonWidth = MathHelper.clamp(Math.round(bookWidth * 0.070F), 35, 54);
            int backButtonHeight = MathHelper.clamp(Math.round(bookHeight * 0.047F), 17, 24);
            int backButtonX = bookX + Math.round(bookWidth * 0.085F);
            int backButtonY = bookY + Math.round(bookHeight * 0.838F);
            int websiteButtonSize = MathHelper.clamp(Math.round(bookWidth * 0.095F), 28, 64);
            int websiteButtonX = bookX + Math.round(bookWidth * 0.620F) - websiteButtonSize / 2;
            int websiteButtonY = bookY + Math.round(bookHeight * 0.205F) - websiteButtonSize / 2;
            int githubButtonSize = MathHelper.clamp(Math.round(bookWidth * 0.118F), 34, 78);
            int githubButtonX = bookX + Math.round(bookWidth * 0.625F) - githubButtonSize / 2;
            int githubButtonY = bookY + Math.round(bookHeight * 0.405F) - githubButtonSize / 2;
            int creditsButtonSize = MathHelper.clamp(Math.round(bookWidth * 0.135F), 38, 82);
            int creditsButtonX = bookX + Math.round(bookWidth * 0.805F) - creditsButtonSize / 2;
            int creditsButtonY = bookY + Math.round(bookHeight * 0.405F) - creditsButtonSize / 2;

            return new Layout(bookX, bookY, bookWidth, bookHeight, slotX, firstSlotY, slotWidth, slotHeight, slotGap,
                    backButtonX, backButtonY, backButtonWidth, backButtonHeight, websiteButtonX, websiteButtonY,
                    websiteButtonSize, githubButtonX, githubButtonY, githubButtonSize, creditsButtonX,
                    creditsButtonY, creditsButtonSize);
        }
    }
}

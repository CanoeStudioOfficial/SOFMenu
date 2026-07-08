package com.canoestudios.sofmenu.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TexturedMenuButton extends GuiButton {

    private final ResourceLocation normalTexture;
    private final ResourceLocation hoverTexture;
    private final long screenOpenedAt;
    private final float delaySeconds;
    private boolean wasHovered;
    private String hoverLabel;

    public TexturedMenuButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText,
            ResourceLocation normalTexture, ResourceLocation hoverTexture, long screenOpenedAt, float delaySeconds) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.normalTexture = normalTexture;
        this.hoverTexture = hoverTexture;
        this.screenOpenedAt = screenOpenedAt;
        this.delaySeconds = delaySeconds;
    }

    public TexturedMenuButton setHoverLabel(String hoverLabel) {
        this.hoverLabel = hoverLabel;
        return this;
    }

    @Override
    public void drawButton(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        if (!this.visible) {
            return;
        }

        float alpha = getAppearanceAlpha();
        this.enabled = alpha >= 1.0F;
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        if (this.hovered && !this.wasHovered && alpha >= 1.0F) {
            minecraft.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
        this.wasHovered = this.hovered;

        minecraft.getTextureManager().bindTexture(this.hovered ? this.hoverTexture : this.normalTexture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        drawModalRectWithCustomSizedTexture(this.x, this.y, 0.0F, 0.0F, this.width, this.height, this.width, this.height);

        String label = this.hovered && this.hoverLabel != null ? this.hoverLabel : this.displayString;
        if (!label.isEmpty()) {
            int color = this.hovered ? 0xFFFFA0 : 0xF2F2F2;
            int alphaBits = MathHelper.clamp((int) (alpha * 255.0F), 0, 255) << 24;
            this.drawCenteredString(minecraft.fontRenderer, label, this.x + this.width / 2,
                    this.y + (this.height - 8) / 2, alphaBits | color);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private float getAppearanceAlpha() {
        if (this.delaySeconds <= 0.0F) {
            return 1.0F;
        }

        float seconds = (Minecraft.getSystemTime() - this.screenOpenedAt) / 1000.0F;
        return MathHelper.clamp((seconds - this.delaySeconds) * 1.0F, 0.0F, 1.0F);
    }
}

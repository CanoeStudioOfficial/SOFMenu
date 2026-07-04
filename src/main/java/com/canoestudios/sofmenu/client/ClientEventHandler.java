package com.canoestudios.sofmenu.client;

import com.canoestudios.sofmenu.SOFMenu;
import com.canoestudios.sofmenu.client.gui.SofMainMenuScreen;
import com.canoestudios.sofmenu.client.session.LastSessionStore;
import com.canoestudios.sofmenu.client.window.WindowCustomizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {

    private boolean windowCustomized;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMainMenu && !(event.getGui() instanceof SofMainMenuScreen)) {
            event.setGui(new SofMainMenuScreen());
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft minecraft = Minecraft.getMinecraft();
        if (!this.windowCustomized) {
            this.windowCustomized = true;
            WindowCustomizer.apply(minecraft, SOFMenu.LOGGER);
        }

        LastSessionStore.recordCurrentSession(minecraft);
    }
}

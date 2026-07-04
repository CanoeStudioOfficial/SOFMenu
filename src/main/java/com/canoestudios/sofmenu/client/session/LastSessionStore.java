package com.canoestudios.sofmenu.client.session;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@SideOnly(Side.CLIENT)
public final class LastSessionStore {

    private static final String KEY_IS_SERVER = "is_server";
    private static final String KEY_TARGET = "target";
    private static String cachedTarget = "";
    private static boolean cachedServer;
    private static boolean loaded;

    private LastSessionStore() {
    }

    public static void recordCurrentSession(Minecraft minecraft) {
        if (minecraft.world == null) {
            return;
        }

        if (minecraft.isIntegratedServerRunning() && minecraft.getIntegratedServer() != null) {
            record(minecraft, minecraft.getIntegratedServer().getFolderName(), false);
        } else if (minecraft.getCurrentServerData() != null) {
            record(minecraft, minecraft.getCurrentServerData().serverIP, true);
        }
    }

    public static void joinLastSession(GuiScreen parent) {
        Minecraft minecraft = Minecraft.getMinecraft();
        load(minecraft);

        if (cachedTarget.isEmpty()) {
            minecraft.displayGuiScreen(new net.minecraft.client.gui.GuiWorldSelection(parent));
            return;
        }

        if (cachedServer) {
            ServerData serverData = new ServerData(cachedTarget, cachedTarget, false);
            minecraft.displayGuiScreen(new GuiConnecting(parent, minecraft, serverData));
        } else if (minecraft.getSaveLoader().canLoadWorld(cachedTarget)) {
            minecraft.launchIntegratedServer(cachedTarget, cachedTarget, (WorldSettings) null);
        } else {
            minecraft.displayGuiScreen(new net.minecraft.client.gui.GuiWorldSelection(parent));
        }
    }

    private static void record(Minecraft minecraft, String target, boolean server) {
        if (target == null || target.isEmpty()) {
            return;
        }

        load(minecraft);
        if (cachedServer == server && target.equals(cachedTarget)) {
            return;
        }

        cachedTarget = target;
        cachedServer = server;
        save(minecraft);
    }

    private static void load(Minecraft minecraft) {
        if (loaded) {
            return;
        }

        loaded = true;
        File file = getStoreFile(minecraft);
        if (!file.isFile()) {
            return;
        }

        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(file)) {
            properties.load(inputStream);
            cachedTarget = properties.getProperty(KEY_TARGET, "");
            cachedServer = Boolean.parseBoolean(properties.getProperty(KEY_IS_SERVER, "false"));
        } catch (IOException ignored) {
            cachedTarget = "";
            cachedServer = false;
        }
    }

    private static void save(Minecraft minecraft) {
        File file = getStoreFile(minecraft);
        File parent = file.getParentFile();
        if (parent != null && !parent.isDirectory()) {
            parent.mkdirs();
        }

        Properties properties = new Properties();
        properties.setProperty(KEY_TARGET, cachedTarget);
        properties.setProperty(KEY_IS_SERVER, Boolean.toString(cachedServer));
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            properties.store(outputStream, "SOF Menu last session");
        } catch (IOException ignored) {
        }
    }

    private static File getStoreFile(Minecraft minecraft) {
        return new File(new File(minecraft.gameDir, "mods/sofmenu"), "last_session.properties");
    }
}

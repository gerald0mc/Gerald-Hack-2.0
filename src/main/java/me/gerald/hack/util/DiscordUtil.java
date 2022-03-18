package me.gerald.hack.util;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;

public class DiscordUtil {
    public static final DiscordRPC discordRPC = DiscordRPC.INSTANCE;
    public static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();

    public static void start() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        eventHandlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));
        String discordID = "908941447355580457";
        discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);
        discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        String ip = Minecraft.getMinecraft().getCurrentServerData() != null ? Minecraft.getMinecraft().getCurrentServerData().serverIP : "SinglePlayer";
        discordRichPresence.details = "Playing: [" + ip + "] Name: [" + Minecraft.getMinecraft().player.getDisplayNameString() + "]";
        discordRichPresence.largeImageKey = "big_nigga";
        discordRichPresence.largeImageText = "Im the biggest nigga.";
        discordRichPresence.smallImageKey = "gerald_head";
        discordRichPresence.smallImageText = "Join the discord! https://discord.gg/5JJpxWznq7";
        discordRichPresence.state = null;
        discordRPC.Discord_UpdatePresence(discordRichPresence);
    }

    public static void stop() {
        discordRPC.Discord_Shutdown();
        discordRPC.Discord_ClearPresence();
    }
}

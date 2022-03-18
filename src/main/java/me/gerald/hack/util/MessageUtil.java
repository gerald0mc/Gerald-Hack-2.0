package me.gerald.hack.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.module.modules.client.ClickGui;
import me.gerald.hack.module.modules.client.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class MessageUtil {
    public static String clientPrefix = ChatFormatting.GRAY + "-" + ChatFormatting.AQUA + "Gerald" + ChatFormatting.GRAY + "(Hack)- " + ChatFormatting.RESET;
    public static String errorPrefix = ChatFormatting.GRAY + "-" + ChatFormatting.RED + "Gerald" + ChatFormatting.GRAY + "(Hack)- " + ChatFormatting.RED;

    public static void sendRawMessage(String message) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(message));
    }

    public static void sendClientMessage(String message) {
        if(GeraldHack.INSTANCE.moduleManager.getModule(Client.class).clientMessageWatermark.getValue())
            sendRawMessage(clientPrefix + message);
        else
            sendRawMessage(message);
    }

    public static void sendErrorMessage(String message) {sendRawMessage(errorPrefix + message);}

    public static void sendRemovableMessage(String message, int id, boolean watermark) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(watermark ? clientPrefix : "" + message), id);
    }
}

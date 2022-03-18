package me.gerald.hack.module.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.event.events.ModuleToggleEvent;
import me.gerald.hack.module.Module;
import me.gerald.hack.util.MessageUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Messages extends Module {
    public Messages() {
        super("Messages", Category.CLIENT, "Sends a message when you toggle a module.");
        setNeedsKeybind(false);
    }

    @SubscribeEvent
    public void onModuleEnable(ModuleToggleEvent.Pre event) {
        MessageUtil.sendClientMessage(ChatFormatting.BOLD + event.getModule().getName() + ChatFormatting.GREEN + " ENABLED!");
    }

    @SubscribeEvent
    public void onModuleDisable(ModuleToggleEvent.Post event) {
        MessageUtil.sendClientMessage(ChatFormatting.BOLD + event.getModule().getName() + ChatFormatting.RED + " DISABLED!");
    }
}

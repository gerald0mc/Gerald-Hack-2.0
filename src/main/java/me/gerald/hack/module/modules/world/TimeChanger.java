package me.gerald.hack.module.modules.world;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.event.events.PacketEvent;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.NumSetting;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TimeChanger extends Module {
    public TimeChanger() {
        super("TimeChanger", Category.WORLD, "Changes the world time.");
    }

    public NumSetting time = register(new NumSetting("Time", 12500, 0, 24000));

    @Override
    public String getMetaData() {
        return "[" + ChatFormatting.GRAY + (int) time.getValue() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        mc.world.setWorldTime((long) time.getValue());
    }

    @SubscribeEvent
    public void onTimeUpdate(PacketEvent.Receive event) {
        if(event.getPacket() instanceof SPacketTimeUpdate) {
            event.setCanceled(true);
        }
    }
}


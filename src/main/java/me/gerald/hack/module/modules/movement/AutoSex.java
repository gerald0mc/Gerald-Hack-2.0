package me.gerald.hack.module.modules.movement;

import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.TimerUtil;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoSex extends Module {
    public AutoSex() {
        super("AutoSex", Category.MOVEMENT, "Hello.");
    }

    public NumSetting delay = register(new NumSetting("Delay", 25, 0, 250));

    public TimerUtil timer = new TimerUtil();

    @SubscribeEvent
    public void onUpdate(TickEvent.WorldTickEvent event) {
        if(nullCheck()) return;
        Entity target = null;
        for(EntityPlayer player : mc.world.playerEntities) {
            if(player == mc.player) continue;
            target = player;
        }
        if(target != null) {
            if(target.getDistance(mc.player) > 2)  {
                return;
            }
            if(mc.player.getDistance(target) < 2) {
                if(timer.passedMs((long) delay.getValue())) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), !mc.player.isSneaking());
                    timer.reset();
                }
            }
        }else {
            if(mc.player.isSneaking() && timer.passedMs(500)) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
            }
        }
    }
}

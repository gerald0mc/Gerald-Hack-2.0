package me.gerald.hack.module.modules.movement;

import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.NumSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ReverseStep extends Module {
    public ReverseStep() {
        super("ReverseStep", Category.MOVEMENT, "Pulls you down.");
    }

    public NumSetting stepPower = register(new NumSetting("StepPower", 9, 0, 10));

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        if(mc.player.onGround) {
            mc.player.motionY -= stepPower.getValue();
        }
    }
}

package me.gerald.hack.module.modules.movement;

import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.NumSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Step extends Module {
    public Step() {
        super("Step", Category.MOVEMENT, "Vanilla step useful on cc.");
    }

    public NumSetting stepHeight = register(new NumSetting("StepHeight", 1.5f, 0.5f, 2));

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if(nullCheck()) return;
        if(mc.player.isInLava() || mc.player.isInWater() || mc.player.isSneaking()) return;
        mc.player.stepHeight = stepHeight.getValue();
    }

    @Override
    public void onDisable() {
        mc.player.stepHeight = 0.5f;
    }
}

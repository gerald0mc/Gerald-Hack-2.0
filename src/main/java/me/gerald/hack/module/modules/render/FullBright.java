package me.gerald.hack.module.modules.render;

import me.gerald.hack.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FullBright extends Module {
    public FullBright() {
        super("FullBright", Category.RENDER, "LET THERE BE LIGHT!!!");
        setNeedsKeybind(false);
    }

    float originalValue;

    @Override
    public void onEnable() {originalValue = mc.gameSettings.gammaSetting;}

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {mc.gameSettings.gammaSetting = 100f;}

    @Override
    public void onDisable() {mc.gameSettings.gammaSetting = originalValue;}
}

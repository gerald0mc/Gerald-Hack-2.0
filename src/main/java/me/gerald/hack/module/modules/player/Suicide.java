package me.gerald.hack.module.modules.player;

import me.gerald.hack.module.Module;

public class Suicide extends Module {
    public Suicide() {
        super("Suicide", Category.PLAYER, "Automatically kills the player.");
    }

    @Override
    public void onEnable() {
        mc.player.sendChatMessage("/kill");
        toggle();
    }
}

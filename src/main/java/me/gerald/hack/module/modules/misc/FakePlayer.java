package me.gerald.hack.module.modules.misc;

import com.mojang.authlib.GameProfile;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.StringSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FakePlayer extends Module {
    public FakePlayer() {
        super("FakePlayer", Category.MISC, "Spawns in a fake player for testing.");
    }

    public StringSetting name = register(new StringSetting("Name", "Gerald(Hack) v" + GeraldHack.VERSION));

    public EntityOtherPlayerMP fakePlayer = null;

    @Override
    public void onEnable() {
        if(nullCheck()) return;
        fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(mc.player.getUniqueID(), name.getString()));
        mc.world.spawnEntity(fakePlayer);
        fakePlayer.copyLocationAndAnglesFrom(mc.player);
    }

    @Override
    public void onDisable() {
        if(fakePlayer != null)
            mc.world.removeEntity(fakePlayer);
    }
}

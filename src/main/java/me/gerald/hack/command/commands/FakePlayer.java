package me.gerald.hack.command.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.command.Command;
import me.gerald.hack.util.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;

public class FakePlayer extends Command {
    public FakePlayer() {
        super("FakePlayer", "Spawns a fake entity into the world.", new String[]{"fakeplayer", "[name]"});
    }

    public EntityOtherPlayerMP fakePlayer = null;

    @Override
    public void onCommand(String[] args) {
        if(fakePlayer == null) {
            String fakePlayerName = "Gerald(Hack)";
            if(args.length == 2) {
                fakePlayerName = args[1];
            }else {
                MessageUtil.sendClientMessage("If you want to name your fake player next time do [prefix]fakeplayer [name].");
            }
            fakePlayer = new EntityOtherPlayerMP(Minecraft.getMinecraft().world, new GameProfile(Minecraft.getMinecraft().player.getUniqueID(), fakePlayerName));
            Minecraft.getMinecraft().world.spawnEntity(fakePlayer);
            fakePlayer.copyLocationAndAnglesFrom(Minecraft.getMinecraft().player);
            MessageUtil.sendClientMessage(ChatFormatting.GRAY + "Spawned fake player named " + ChatFormatting.AQUA + fakePlayerName + ChatFormatting.GRAY + ".");
        }else {
            Minecraft.getMinecraft().world.removeEntity(fakePlayer);
            MessageUtil.sendClientMessage(ChatFormatting.GRAY + "Removed fake player.");
            fakePlayer = null;
        }
    }
}

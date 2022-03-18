package me.gerald.hack.module.modules.world;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.module.Module;
import me.gerald.hack.util.MessageUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class ShulkerLogger extends Module {
    public ShulkerLogger() {
        super("ShulkerLogger", Category.WORLD, "Sends a name and location of shulker box.");
    }

    public List<TileEntityShulkerBox> shulkerBoxes = new ArrayList<>();

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        for(TileEntity entity : mc.world.loadedTileEntityList) {
            if(entity instanceof TileEntityShulkerBox) {
                TileEntityShulkerBox shulkerBox = (TileEntityShulkerBox) entity;
                if(!shulkerBoxes.contains(shulkerBox)) {
                    MessageUtil.sendClientMessage(ChatFormatting.GRAY + "Located shulker box named " + ChatFormatting.AQUA + shulkerBox.getName() + ChatFormatting.GRAY + " and is at [X:" + ChatFormatting.AQUA + shulkerBox.getPos().getX() + ChatFormatting.GRAY + " Y:" + ChatFormatting.AQUA + shulkerBox.getPos().getY() + ChatFormatting.GRAY + " Z:" + ChatFormatting.AQUA + shulkerBox.getPos().getZ() + ChatFormatting.GRAY + "]");
                    shulkerBoxes.add(shulkerBox);
                }
            }
        }
    }
}

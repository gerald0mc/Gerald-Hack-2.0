package me.gerald.hack.module.modules.world;

import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.InventoryUtil;
import me.gerald.hack.util.MessageUtil;
import me.gerald.hack.util.WorldUtil;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class AntiRegear extends Module {
    public AntiRegear() {
        super("AntiRegear", Category.WORLD, "Breaks shulkers for you.");
    }

    public NumSetting range = register(new NumSetting("Range", 5, 0, 6));
    public BoolSetting switchToPick = register(new BoolSetting("SwitchToPick", true));

    public int originalSlot = -1;
    public List<BlockPos> breakQueue = new ArrayList<>();

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        if(breakQueue.isEmpty()) {
            if(originalSlot != -1 && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_PICKAXE && switchToPick.getValue()) {
                MessageUtil.sendClientMessage("Switching back to original slot.");
                InventoryUtil.switchToSlot(originalSlot);
                originalSlot = -1;
            }
        }
        for(BlockPos pos : WorldUtil.getSphere(mc.player.getPosition(), range.getValue(), false)) {
            if(mc.world.getBlockState(pos).getBlock() instanceof BlockShulkerBox) {
                if(!breakQueue.contains(pos)) {
                    breakQueue.add(pos);
                }
            }
        }
        if(!breakQueue.isEmpty()) {
            if(mc.player.getHeldItemMainhand().getItem() != Items.DIAMOND_PICKAXE) {
                int pickSlot = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
                if (pickSlot != -1 && switchToPick.getValue()) {
                    originalSlot = mc.player.inventory.currentItem;
                    InventoryUtil.switchToSlot(pickSlot);
                }
            }
            for(BlockPos pos : breakQueue) {
                if(!(mc.world.getBlockState(pos).getBlock() instanceof BlockShulkerBox) || mc.player.getDistance(pos.getX(), pos.getY(), pos.getZ()) > range.getValue()) {
                    breakQueue.remove(pos);
                    return;
                }
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.getDirectionFromEntityLiving(pos, mc.player)));
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.getDirectionFromEntityLiving(pos, mc.player)));
            }
        }
    }
}

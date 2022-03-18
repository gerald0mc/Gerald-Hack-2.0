package me.gerald.hack.module.modules.combat;

import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.*;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class HoleFill extends Module {
    public HoleFill() {
        super("HoleFill", Category.COMBAT, "Fills holes.");
    }

    public NumSetting range = register(new NumSetting("Range", 4, 0, 6));
    public NumSetting delay = register(new NumSetting("Delay", 50, 0, 250));

    public TimerUtil timer = new TimerUtil();

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        int obsidianSlot = InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN));
        if (obsidianSlot == -1) {
            MessageUtil.sendErrorMessage("No obsidian in hotbar toggling.");
            toggle();
            return;
        }
        for (BlockPos pos : WorldUtil.getSphere(mc.player.getPosition(), range.getValue(), false)) {
            if (isHole(pos)) {
                if(mc.player.inventory.currentItem != obsidianSlot)
                    InventoryUtil.switchToSlot(obsidianSlot);
                EnumFacing[] enumFacings = EnumFacing.values();
                for (EnumFacing side : enumFacings) {
                    BlockPos neighbor = pos.offset(side);
                    EnumFacing side2 = side.getOpposite();
                    Vec3d hitVec = (new Vec3d(neighbor)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(side2.getDirectionVec())).scale(0.5D));
                    if(timer.passedMs((long) delay.getValue())) {
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        mc.playerController.processRightClickBlock(mc.player, mc.world, pos, side2, hitVec, EnumHand.MAIN_HAND);
                        timer.reset();
                    }
                }
            }
        }
    }

    public boolean isHole(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() == Blocks.AIR
                && (mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN)
                && mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR
                && mc.world.getBlockState(pos.up().up()).getBlock() == Blocks.AIR
                && (mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.north()).getBlock() == Blocks.OBSIDIAN)
                && (mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.south()).getBlock() == Blocks.OBSIDIAN)
                && (mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.west()).getBlock() == Blocks.OBSIDIAN)
                && (mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.east()).getBlock() == Blocks.OBSIDIAN);
    }
}

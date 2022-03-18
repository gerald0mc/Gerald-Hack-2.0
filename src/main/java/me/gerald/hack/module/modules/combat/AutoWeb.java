package me.gerald.hack.module.modules.combat;

import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.ColorSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.*;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

public class AutoWeb extends Module {
    public AutoWeb() {
        super("AutoWeb", Category.COMBAT, "Places webs on things.");
    }

    public NumSetting range = register(new NumSetting("Range", 4, 0, 6));
    public NumSetting delay = register(new NumSetting("Delay", 50, 0, 500));
    public BoolSetting silent = register(new BoolSetting("Silent", true));
    public ColorSetting color = register(new ColorSetting("Color", 255, 0, 0, 43));
    public NumSetting lineWidth = register(new NumSetting("LineWidth", 1, 0, 3));
    public BoolSetting autoDisable = register(new BoolSetting("AutoDisable", false));

    public TimerUtil timer = new TimerUtil();
    public BlockPos target = null;

    @SubscribeEvent
    public void onUpdate(TickEvent.WorldTickEvent event) {
        if(nullCheck()) return;
        int originalSlot = -1;
        Vec3d hitVec = null;
        EnumFacing side2 = null;
        EnumFacing[] enumFacings = EnumFacing.values();
        target = getTarget(range.getValue());
        if(target == null && autoDisable.getValue()) {
            MessageUtil.sendErrorMessage("Could not find a target for web aura toggling.");
            toggle();
        }
        if(target != null) {
            if(mc.world.getBlockState(target).getBlock() == Blocks.WEB || mc.player.getDistance(target.getX(), target.getY(), target.getZ()) > range.getValue()) {
                target = null;
                return;
            }
            int webSlot = InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.WEB));
            if(webSlot == -1 && autoDisable.getValue()) {
                MessageUtil.sendErrorMessage("No web's to use on target toggling.");
                toggle();
            }
            if(webSlot != -1) {
                originalSlot = mc.player.inventory.currentItem;
                if(silent.getValue())
                    InventoryUtil.silentSwitchToSlot(webSlot);
                else
                    InventoryUtil.switchToSlot(webSlot);
            }
            for (EnumFacing side : enumFacings) {
                BlockPos neighbor = target.offset(side);
                side2 = side.getOpposite();
                hitVec = (new Vec3d(neighbor)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(side2.getDirectionVec())).scale(0.5D));
            }
            if(timer.passedMs((long) delay.getValue()) && hitVec != null) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.playerController.processRightClickBlock(mc.player, mc.world, target, side2, hitVec, EnumHand.MAIN_HAND);
                if(autoDisable.getValue()) {
                    MessageUtil.sendErrorMessage("Done placing on entity toggling.");
                    toggle();
                }
                timer.reset();
                target = null;
            }
        }
        if(target == null && originalSlot != -1) {
            if(silent.getValue())
                InventoryUtil.silentSwitchToSlot(originalSlot);
            else
                InventoryUtil.switchToSlot(originalSlot);
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(target != null) {
            AxisAlignedBB box = new AxisAlignedBB(target).offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
            RenderUtil.prepare();
            GL11.glLineWidth(lineWidth.getValue());
            float r = (color.getR())  / 255f;
            float g = (color.getG())  / 255f;
            float b = (color.getB())  / 255f;
            RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b, 1f);
            RenderUtil.release();
        }
    }

    public BlockPos getTarget(float range) {
        for(EntityPlayer player : mc.world.playerEntities) {
            if(player == mc.player) continue;
            if(mc.player.getDistance(player) < range) {
                if(mc.world.getBlockState(mc.player.getPosition()).getBlock() == Blocks.WEB) continue;
                return player.getPosition();
            }
        }
        return null;
    }
}

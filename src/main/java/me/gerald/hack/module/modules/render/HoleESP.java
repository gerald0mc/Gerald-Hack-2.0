package me.gerald.hack.module.modules.render;

import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.ColorSetting;
import me.gerald.hack.setting.settings.ModeSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.RenderUtil;
import me.gerald.hack.util.WorldUtil;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class HoleESP extends Module {
    public HoleESP() {
        super("HoleESP", Category.RENDER, "Renders safe and unsafe holes.");
    }

    public NumSetting range = register(new NumSetting("Range", 12, 0, 30));
    public NumSetting yToAdd = register(new NumSetting("YToAdd", 0, -1, 4));
    public ModeSetting renderMode = register(new ModeSetting("RenderMode", RenderMode.Both));
    public enum RenderMode{Fill, Outline, Both}
    public ColorSetting safeColor = register(new ColorSetting("SafeColor", 0, 125, 255, 43));
    public ColorSetting unSafeColor = register(new ColorSetting("UnSafeColor", 255, 0, 0, 43));
    public BoolSetting rainbow = register(new BoolSetting("Rainbow", false));
    public NumSetting rainbowSpeed = register(new NumSetting("RainbowSpeed", 6, 1, 10));
    public NumSetting lineWidth = register(new NumSetting("LineWidth", 2f, 0f, 4f));

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        for(BlockPos pos : WorldUtil.getSphere(mc.player.getPosition(), range.getValue(), false)) {
            RenderUtil.prepare();
            AxisAlignedBB box = mc.world.getBlockState(pos).getSelectedBoundingBox(mc.world, pos).offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
            float r = (rainbow.getValue() ? RenderUtil.genRainbow((int) rainbowSpeed.getValue()).getRed() : (isSafeHole(pos) ?  safeColor.getR() : unSafeColor.getR())) / 255f;
            float g = (rainbow.getValue() ? RenderUtil.genRainbow((int) rainbowSpeed.getValue()).getGreen() : (isSafeHole(pos) ?  safeColor.getG() : unSafeColor.getG())) / 255f;
            float b = (rainbow.getValue() ? RenderUtil.genRainbow((int) rainbowSpeed.getValue()).getBlue() : (isSafeHole(pos) ?  safeColor.getB() : unSafeColor.getB())) / 255f;
            if(isSafeHole(pos)) {
                GL11.glLineWidth(lineWidth.getValue());
                if(renderMode.getValueEnum() == RenderMode.Fill || renderMode.getValueEnum() == RenderMode.Both)
                    RenderGlobal.renderFilledBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY + yToAdd.getValue(), box.maxZ, r, g, b, safeColor.getA() / 255f);
                if(renderMode.getValueEnum() == RenderMode.Outline || renderMode.getValueEnum() == RenderMode.Both)
                    RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY + yToAdd.getValue(), box.maxZ, r, g, b, 1f);
            }else if(isUnSafeHole(pos)) {
                GL11.glLineWidth(lineWidth.getValue());
                if(renderMode.getValueEnum() == RenderMode.Fill || renderMode.getValueEnum() == RenderMode.Both)
                    RenderGlobal.renderFilledBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY + yToAdd.getValue(), box.maxZ, r, g, b, safeColor.getA() / 255f);
                if(renderMode.getValueEnum() == RenderMode.Outline || renderMode.getValueEnum() == RenderMode.Both)
                    RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY + yToAdd.getValue(), box.maxZ, r, g, b, 1f);
            }
            RenderUtil.release();
        }
    }

    public boolean isSafeHole(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() == Blocks.AIR
                && mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK
                && mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR
                && mc.world.getBlockState(pos.up().up()).getBlock() == Blocks.AIR
                && mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK
                && mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK
                && mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK
                && mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK;
    }

    public boolean isUnSafeHole(BlockPos pos) {
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

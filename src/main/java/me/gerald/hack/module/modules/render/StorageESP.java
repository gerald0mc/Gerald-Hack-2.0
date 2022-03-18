package me.gerald.hack.module.modules.render;

import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.ColorSetting;
import me.gerald.hack.setting.settings.ModeSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class StorageESP extends Module {
    public StorageESP() {
        super("StorageESP", Category.RENDER, "Renders boxes around storage.");
    }

    public ModeSetting renderMode = register(new ModeSetting("RenderMode", RenderMode.Both));
    public enum RenderMode{Fill, Outline, Both}
    public ModeSetting colorMode = register(new ModeSetting("ColorMode", ColorMode.Static));
    public enum ColorMode{Static, Custom}
    public ColorSetting color = register(new ColorSetting("Color", 0, 125, 255, 43));
    public BoolSetting rainbow = register(new BoolSetting("Rainbow", false));
    public NumSetting rainbowSpeed = register(new NumSetting("RainbowSpeed", 6, 1, 10));
    public NumSetting lineWidth = register(new NumSetting("LineWidth", 2f, 0f, 4f));
    public BoolSetting chest = register(new BoolSetting("Chests", true));
    public BoolSetting enderChest = register(new BoolSetting("EnderChests", true));
    public BoolSetting shulker = register(new BoolSetting("Shulkers", true));
    public BoolSetting hopper = register(new BoolSetting("Hoppers", true));

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        for(TileEntity entity : mc.world.loadedTileEntityList) {
            RenderUtil.prepare();
            GlStateManager.glLineWidth(lineWidth.getValue());
            if(entity instanceof TileEntityChest && chest.getValue()) {
                render(entity, new Color(100, 58, 11));
            }
            if(entity instanceof TileEntityEnderChest && enderChest.getValue()) {
                render(entity, new Color(113, 37, 217));
            }
            if(entity instanceof TileEntityHopper && hopper.getValue()) {
                render(entity, new Color(72, 72, 72));
            }
            if(entity instanceof TileEntityShulkerBox && shulker.getValue()) {
                render(entity, new Color(227, 19, 182));
            }
            RenderUtil.release();
        }
    }

    public void render(TileEntity entity, Color staticColor) {
        AxisAlignedBB box = new AxisAlignedBB(entity.getPos());
        box = box.offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
        float r = (rainbow.getValue() ? RenderUtil.genRainbow((int) rainbowSpeed.getValue()).getRed() : (colorMode.getValueEnum() == ColorMode.Custom ? color.getR() : staticColor.getRed())) / 255f;
        float g = (rainbow.getValue() ? RenderUtil.genRainbow((int) rainbowSpeed.getValue()).getGreen() : (colorMode.getValueEnum() == ColorMode.Custom ? color.getG() : staticColor.getGreen())) / 255f;
        float b = (rainbow.getValue() ? RenderUtil.genRainbow((int) rainbowSpeed.getValue()).getBlue() : (colorMode.getValueEnum() == ColorMode.Custom ? color.getB() : staticColor.getBlue())) / 255f;
        if(renderMode.getValueEnum() == RenderMode.Fill || renderMode.getValueEnum() == RenderMode.Both)
            RenderGlobal.renderFilledBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b, color.getA() / 255f);
        if(renderMode.getValueEnum() == RenderMode.Outline || renderMode.getValueEnum() == RenderMode.Both)
            RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b, 1f);
    }
}

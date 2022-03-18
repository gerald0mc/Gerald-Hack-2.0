package me.gerald.hack.module.modules.render;

import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.ColorSetting;
import me.gerald.hack.setting.settings.ModeSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.MessageUtil;
import me.gerald.hack.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ESP extends Module {
    public ESP() {
        super("ESP", Category.RENDER, "Renders boxes around entities.");
    }

    public ModeSetting renderMode = register(new ModeSetting("RenderMode", RenderMode.Both));
    public enum RenderMode{Fill, Outline, Both}
    public ColorSetting color = register(new ColorSetting("Color", 0, 125, 255, 43));
    public BoolSetting rainbow = register(new BoolSetting("Rainbow", false));
    public NumSetting rainbowSpeed = register(new NumSetting("RainbowSpeed", 6, 1, 10));
    public NumSetting lineWidth = register(new NumSetting("LineWidth", 2f, 0f, 4f));
    public BoolSetting players = register(new BoolSetting("Players", true));
    public BoolSetting items = register(new BoolSetting("Items", true));
    public BoolSetting mobs = register(new BoolSetting("Mobs", true));
    public BoolSetting animals = register(new BoolSetting("Animals", true));

    public EntityPlayer player;

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        for(Entity e : mc.world.loadedEntityList) {
            if(e == mc.player) continue;
            RenderUtil.prepare();
            GlStateManager.glLineWidth(lineWidth.getValue());
            if(e instanceof EntityPlayer && players.getValue()) {
                render(e);
            }
            if(e instanceof EntityItem && items.getValue()) {
                render(e);
            }
            if(e instanceof EntityMob || e instanceof EntitySlime && mobs.getValue()) {
                render(e);
            }
            if(e instanceof EntityAnimal && animals.getValue()) {
                render(e);
            }
            RenderUtil.release();
        }
    }

    public void render(Entity entity) {
        AxisAlignedBB box = entity.getRenderBoundingBox();
        box = box.offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
        float r = (rainbow.getValue() ? RenderUtil.genRainbow((int) rainbowSpeed.getValue()).getRed() : color.getR()) / 255f;
        float g = (rainbow.getValue() ? RenderUtil.genRainbow((int) rainbowSpeed.getValue()).getGreen() : color.getG()) / 255f;
        float b = (rainbow.getValue() ? RenderUtil.genRainbow((int) rainbowSpeed.getValue()).getBlue() : color.getB()) / 255f;
        if(renderMode.getValueEnum() == RenderMode.Fill || renderMode.getValueEnum() == RenderMode.Both)
            RenderGlobal.renderFilledBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b, color.getA() / 255f);
        if(renderMode.getValueEnum() == RenderMode.Outline || renderMode.getValueEnum() == RenderMode.Both)
            RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b, 1f);
    }
}

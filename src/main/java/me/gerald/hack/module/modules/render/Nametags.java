package me.gerald.hack.module.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.hack.GeraldHack;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.ModeSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.ProjectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Objects;

public class Nametags extends Module {
    public Nametags() {
        super("Nametags", Category.RENDER, "Renders info about players.");
    }

    public NumSetting scale = register(new NumSetting("Scale", 2.5f, 0, 5));
    public BoolSetting ping = register(new BoolSetting("Ping", true));
    public BoolSetting totemPops = register(new BoolSetting("TotemPops", true));
    public BoolSetting health = register(new BoolSetting("Health", true));
    public ModeSetting healthType = register(new ModeSetting("HealthType", HealthType.TEXT));
    public enum HealthType {TEXT, BAR}

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if(nullCheck()) return;
        for(EntityPlayer player : mc.world.playerEntities) {
            if(player == mc.player) continue;
            double playerHealth = player.getHealth() + player.getAbsorptionAmount();
            String str = "";
            double yAdd = player.isSneaking() ? 1.75 : 2.25;
            double deltaX = MathHelper.clampedLerp(player.lastTickPosX, player.posX, event.getPartialTicks());
            double deltaY = MathHelper.clampedLerp(player.lastTickPosY, player.posY, event.getPartialTicks());
            double deltaZ = MathHelper.clampedLerp(player.lastTickPosZ, player.posZ, event.getPartialTicks());
            Vec3d projection = ProjectionUtil.toScaledScreenPos(new Vec3d(deltaX, deltaY, deltaZ).add(0, yAdd, 0));
            //Render stuff
            double healthSliderLength = ((playerHealth) / 20.0f) * 100.0f;
            GlStateManager.pushMatrix();
            GlStateManager.translate(projection.x, projection.y, 0);
            GlStateManager.scale(scale.getValue(), scale.getValue(), 0);
            str += GeraldHack.INSTANCE.friendManager.isFriend(player.getDisplayNameString()) ? ChatFormatting.AQUA + player.getDisplayNameString() + ChatFormatting.RESET : player.getDisplayNameString();
            if(ping.getValue())
                str += " " + getPingColor(player) + MathHelper.ceil(Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime()) + ChatFormatting.RESET + "ms";
            if(totemPops.getValue())
                str += " Pops: " + GeraldHack.INSTANCE.totemPopListener.getTotalPops(player);
            if(health.getValue()) {
                if(healthType.getValueEnum() == HealthType.TEXT)
                    str += " " + getHealthColor(player) + MathHelper.ceil(playerHealth) + ChatFormatting.RESET;
                else
                    Gui.drawRect(1, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, (int) healthSliderLength, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT - 1, new Color(0, 255, 0).getRGB());
            }
            Gui.drawRect((int) -((mc.fontRenderer.getStringWidth(str) + 2) / 2f), -(mc.fontRenderer.FONT_HEIGHT + 2), ((mc.fontRenderer.getStringWidth(str) + 2) / (int) 2f), 1, new Color(12, 12, 12, 100).getRGB());
            mc.fontRenderer.drawStringWithShadow(str, -(mc.fontRenderer.getStringWidth(str) / 2f), -(mc.fontRenderer.FONT_HEIGHT), -1);
            GlStateManager.popMatrix();
        }
    }

    public ChatFormatting getHealthColor(EntityPlayer entity) {
        if(entity.getHealth() + entity.getAbsorptionAmount() > 14) {
            return ChatFormatting.GREEN;
        }else if(entity.getHealth() + entity.getAbsorptionAmount() > 6){
            return ChatFormatting.YELLOW;
        }else {
            return ChatFormatting.RED;
        }
    }

    public ChatFormatting getPingColor(EntityPlayer entity) {
        if(Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(entity.getUniqueID()).getResponseTime() > 150) {
            return ChatFormatting.RED;
        }else if(Minecraft.getMinecraft().getConnection().getPlayerInfo(entity.getUniqueID()).getResponseTime() > 75) {
            return ChatFormatting.YELLOW;
        }else {
            return ChatFormatting.GREEN;
        }
    }
}

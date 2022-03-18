package me.gerald.hack.module.modules.combat;

import me.gerald.hack.GeraldHack;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.ColorSetting;
import me.gerald.hack.setting.settings.ModeSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.InventoryUtil;
import me.gerald.hack.util.RenderUtil;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

public class KillAura extends Module {
    public KillAura() {
        super("KillAura", Category.COMBAT, "Automatically attacks entities for you.");
    }

    public NumSetting range = register(new NumSetting("Range", 4, 1, 6));
    public ColorSetting color = register(new ColorSetting("Color", 255, 0, 0, 43));
    public NumSetting lineWidth = register(new NumSetting("LineWidth", 1, 0, 3));
    public BoolSetting players = register(new BoolSetting("Players", true));
    public BoolSetting animals = register(new BoolSetting("Animals", true));
    public BoolSetting mobs = register(new BoolSetting("Mobs", true));

    public EntityLivingBase target = null;

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if(nullCheck()) return;
        if(target != null) {
            if (target.getDistance(mc.player) >= range.getValue() || target.isDead || !target.isEntityAlive())
                target = null;
        }
        for(Entity entity : mc.world.loadedEntityList) {
            if(entity == mc.player) continue;
            if(entity.isDead || !entity.isEntityAlive()) continue;
            if(entity.getDistance(mc.player) <= range.getValue()) {
                if(mc.player.getCooledAttackStrength(0.0f) >= 1.0f) {
                    if (entity instanceof EntityPlayer && players.getValue()) {
                        if(GeraldHack.INSTANCE.friendManager.isFriend(entity.getName())) continue;
                        target = (EntityLivingBase) entity;
                        attack(entity);
                    }
                    if (entity instanceof EntityAnimal && animals.getValue()) {
                        target = (EntityLivingBase) entity;
                        attack(entity);
                    }
                    if ((entity instanceof EntityMob || entity instanceof EntitySlime) && mobs.getValue()) {
                        target = (EntityLivingBase) entity;
                        attack(entity);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(target != null) {
            AxisAlignedBB box = target.getRenderBoundingBox().offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
            RenderUtil.prepare();
            GL11.glLineWidth(lineWidth.getValue());
            float r = (target.hurtTime > 0 ? 255 : color.getR())  / 255f;
            float g = (target.hurtTime > 0 ? 0 : color.getG())  / 255f;
            float b = (target.hurtTime > 0 ? 0 : color.getB())  / 255f;
            RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b, 1f);
            RenderUtil.release();
        }
    }

    public void attack(Entity entity) {
        mc.playerController.attackEntity(mc.player, entity);
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    @Override
    public void onDisable() {
        if(target != null)
            target = null;
    }
}

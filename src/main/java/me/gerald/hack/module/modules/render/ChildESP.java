package me.gerald.hack.module.modules.render;

import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.ColorSetting;
import me.gerald.hack.setting.settings.ModeSetting;
import me.gerald.hack.util.MessageUtil;
import me.gerald.hack.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class ChildESP extends Module {
    public ChildESP() {
        super("ChildESP", Category.RENDER, "Renders children.");
    }

    public ModeSetting renderMode = register(new ModeSetting("RenderMode", RenderMode.GLOW));
    public enum RenderMode {GLOW, BOX}
    public ColorSetting color = register(new ColorSetting("Color", 0, 125, 255, 43));

    public List<Entity> children = new ArrayList<>();

    @SubscribeEvent
    public void onRenderChild(RenderWorldLastEvent event) {
        //by gerald0mc
        if(nullCheck()) return;
        for(Entity entity : mc.world.loadedEntityList) {
            if(entity instanceof EntityAnimal) {
                EntityAnimal animal = (EntityAnimal) entity;
                if(animal.isChild()) {
                    if(!children.contains(animal)) {
                        AxisAlignedBB box = animal.getEntityBoundingBox().offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
                        children.add(animal);
                        if(renderMode.getValueEnum() == RenderMode.BOX)
                            animal.setGlowing(true);
                        else
                            RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, color.getR(), color.getG(), color.getB(), 1f);
                        MessageUtil.sendClientMessage("Found a child at [X:" + animal.getPosition().getX() + " Y:" + animal.getPosition().getY() + " Z:" + animal.getPosition().getZ() + "]");
                    }
                }else if(!animal.isChild() && children.contains(animal)) {
                    children.remove(animal);
                    animal.setGlowing(false);
                    MessageUtil.sendClientMessage("Ew a child grew up at [X:" + animal.getPosition().getX() + " Y:" + animal.getPosition().getY() + " Z:" + animal.getPosition().getZ() + "]");
                }
            }else if(entity instanceof EntityMob) {
                EntityMob mob = (EntityMob) entity;
                if(mob.isChild()) {
                    if(!children.contains(mob)) {
                        AxisAlignedBB box = mob.getEntityBoundingBox().offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
                        children.add(mob);
                        if(renderMode.getValueEnum() == RenderMode.BOX)
                            mob.setGlowing(true);
                        else
                            RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, color.getR(), color.getG(), color.getB(), 1f);
                        MessageUtil.sendClientMessage("Found a child at [X:" + mob.getPosition().getX() + " Y:" + mob.getPosition().getY() + " Z:" + mob.getPosition().getZ() + "]");
                    }
                }else if(!mob.isChild() && children.contains(mob)) {
                    children.remove(mob);
                    mob.setGlowing(false);
                    MessageUtil.sendClientMessage("Ew a child grew up at [X:" + mob.getPosition().getX() + " Y:" + mob.getPosition().getY() + " Z:" + mob.getPosition().getZ() + "]");
                }
            }else if(entity instanceof EntityVillager) {
                EntityVillager villager = (EntityVillager) entity;
                if(villager.isChild()) {
                    if(!children.contains(villager)) {
                        AxisAlignedBB box = villager.getEntityBoundingBox().offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
                        children.add(villager);
                        if(renderMode.getValueEnum() == RenderMode.BOX)
                            villager.setGlowing(true);
                        else
                            RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, color.getR(), color.getG(), color.getB(), 1f);
                        MessageUtil.sendClientMessage("Found a child at [X:" + villager.getPosition().getX() + " Y:" + villager.getPosition().getY() + " Z:" + villager.getPosition().getZ() + "]");
                    }
                }else if(!villager.isChild() && children.contains(villager)) {
                    children.remove(villager);
                    villager.setGlowing(false);
                    MessageUtil.sendClientMessage("Ew a child grew up at [X:" + villager.getPosition().getX() + " Y:" + villager.getPosition().getY() + " Z:" + villager.getPosition().getZ() + "]");
                }
            }
        }
    }
}

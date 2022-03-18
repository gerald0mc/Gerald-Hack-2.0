package me.gerald.hack.module.modules.render;

import me.gerald.hack.event.events.PacketEvent;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.ColorSetting;
import me.gerald.hack.setting.settings.ModeSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.NumberUtil;
import me.gerald.hack.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public class ExplosionChams extends Module {
    public ExplosionChams() {
        super("ExplosionChams", Category.RENDER, "Renders explosion breaks.");
    }

    public NumSetting timeToRemove = register(new NumSetting("TimeToRemove", 1500, 0, 3000));
    public ModeSetting renderMode = register(new ModeSetting("RenderMode", RenderMode.Both));
    public enum RenderMode {Wireframe, Fill, Both}
    public ColorSetting color = register(new ColorSetting("Color", 0, 125, 255, 43));
    public BoolSetting rainbow = register(new BoolSetting("Rainbow", false));
    public NumSetting rainbowSpeed = register(new NumSetting("RainbowSpeed", 6, 1, 10));
    public NumSetting lineWidth = register(new NumSetting("LineWidth", 2, 1, 4));

    private static final HashMap<EntityEnderCrystal, Long> crystalExplosionMap = new HashMap<>();

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(nullCheck()) return;
        for (Map.Entry<EntityEnderCrystal, Long> entry : new HashMap<>(crystalExplosionMap).entrySet()) {
            if(System.currentTimeMillis() - entry.getValue() > (long) timeToRemove.getValue()) {
                crystalExplosionMap.remove(entry.getKey());
                continue;
            }
            float r = (rainbow.getValue() ? RenderUtil.genRainbow((int) rainbowSpeed.getValue()).getRed() : color.getR()) / 255f;
            float g = (rainbow.getValue() ? RenderUtil.genRainbow((int) rainbowSpeed.getValue()).getGreen() : color.getG()) / 255f;
            float b = (rainbow.getValue() ? RenderUtil.genRainbow((int) rainbowSpeed.getValue()).getBlue() : color.getB()) / 255f;
            GL11.glPushMatrix();
            GL11.glDepthRange(0, 0.01f);
            if(renderMode.getValueEnum() == RenderMode.Wireframe || renderMode.getValueEnum() == RenderMode.Both) {
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glLineWidth(lineWidth.getValue());
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
                GL11.glColor4f(r, g, b, 1f);
                renderEntityStatic(entry.getKey(), event.getPartialTicks(), true);
                GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glColor4f(1f, 1f, 1f, 1f);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
            if(renderMode.getValueEnum() == RenderMode.Fill || renderMode.getValueEnum() == RenderMode.Both) {
                GL11.glPushAttrib(GL11.GL_ALL_CLIENT_ATTRIB_BITS);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GL11.glLineWidth(1.5f);
                GL11.glColor4f(r, g, b, color.getA() / 255f);
                renderEntityStatic(entry.getKey(), event.getPartialTicks(), true);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1f, 1f, 1f, 1f);
                GL11.glPopAttrib();
            }
            GL11.glDepthRange(0.0, 1.0f);
            GL11.glPopMatrix();
        }
    }

    @SubscribeEvent
    public void onBreak(PacketEvent.Receive event) {
        if(event.getPacket() instanceof SPacketDestroyEntities) {
            SPacketDestroyEntities packet = (SPacketDestroyEntities) event.getPacket();
            for(Entity entity : mc.world.loadedEntityList) {
                if(entity instanceof EntityEnderCrystal) {
                    for (int array : packet.getEntityIDs()) {
                        if (array == entity.getEntityId()) {
                            final EntityEnderCrystal fakeEntity = new EntityEnderCrystal(mc.world, entity.posX, entity.posY, entity.posZ);
                            fakeEntity.innerRotation = 0;
                            fakeEntity.setShowBottom(false);
                            crystalExplosionMap.put(fakeEntity, System.currentTimeMillis());
                        }
                    }
                }
            }
        }
    }

    public void renderEntityStatic(Entity entityIn, float partialTicks, boolean p_188388_3_) {
        if (entityIn.ticksExisted == 0)
        {
            entityIn.lastTickPosX = entityIn.posX;
            entityIn.lastTickPosY = entityIn.posY;
            entityIn.lastTickPosZ = entityIn.posZ;
        }

        double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
        double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
        double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
        float f = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
        int i = entityIn.getBrightnessForRender();

        if (entityIn.isBurning())
        {
            i = 15728880;
        }

        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        mc.getRenderManager().renderEntity(entityIn, d0 - mc.getRenderManager().viewerPosX, d1 - mc.getRenderManager().viewerPosY, d2 - mc.getRenderManager().viewerPosZ, f, partialTicks, p_188388_3_);
    }
}

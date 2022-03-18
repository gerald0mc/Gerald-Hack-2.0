package me.gerald.hack.module.modules.render;

import io.netty.util.internal.MathUtil;
import me.gerald.hack.event.events.PlayerDeathEvent;
import me.gerald.hack.event.events.TotemPopEvent;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.ColorSetting;
import me.gerald.hack.setting.settings.ModeSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.RenderUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public class PopChams extends Module {
    public PopChams() {
        super("PopChams", Category.RENDER, "Renders player pops.");
    }

    public NumSetting timeToRemove = register(new NumSetting("TimeToRemove", 1500, 0, 3000));
    public BoolSetting self = register(new BoolSetting("Self", true));
    public ModeSetting renderMode = register(new ModeSetting("RenderMode", RenderMode.Both));
    public enum RenderMode {Wireframe, Fill, Both}
    public ColorSetting color = register(new ColorSetting("Color", 0, 125, 255, 45));
    public BoolSetting fade = register(new BoolSetting("Fade", true));
    public NumSetting fadeAlpha = register(new NumSetting("FadeAlpha", 50, 0, 255));
    public BoolSetting rainbow = register(new BoolSetting("Rainbow", false));
    public NumSetting rainbowSpeed = register(new NumSetting("RainbowSpeed", 6, 1, 10));
    public NumSetting lineWidth = register(new NumSetting("LineWidth", 2, 1, 4));

    private static final HashMap<EntityOtherPlayerMP, Long> popFakePlayerMap = new HashMap<>();

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(nullCheck()) return;
        for (Map.Entry<EntityOtherPlayerMP, Long> entry : new HashMap<>(popFakePlayerMap).entrySet()) {
            float factor = (timeToRemove.getValue() - entry.getValue()) / timeToRemove.getValue();
            float r = (rainbow.getValue() ? RenderUtil.genRainbow((int) rainbowSpeed.getValue()).getRed() : color.getR()) / 255f;
            float g = (rainbow.getValue() ? RenderUtil.genRainbow((int) rainbowSpeed.getValue()).getGreen() : color.getG()) / 255f;
            float b = (rainbow.getValue() ? RenderUtil.genRainbow((int) rainbowSpeed.getValue()).getBlue() : color.getB()) / 255f;
            float a = (clamp((int)((fade.getValue() ? factor : 255) * fadeAlpha.getValue()),  0,  255)) / 255f;
            if(System.currentTimeMillis() - entry.getValue() > (long) timeToRemove.getValue()) {
                popFakePlayerMap.remove(entry.getKey());
                continue;
            }
            GL11.glPushMatrix();
            GL11.glDepthRange(0.01, 1.0f);
            if(renderMode.getValueEnum() == RenderMode.Wireframe || renderMode.getValueEnum() == RenderMode.Both) {
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glLineWidth(lineWidth.getValue());
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
                GL11.glColor4f(r, g, b, 1f);
                renderEntityStatic(entry.getKey(), event.getPartialTicks(), true);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
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
    public void onPop(TotemPopEvent event) {
        if (mc.world.getEntityByID(event.getEntity().getEntityId()) != null) {
            final Entity entity = mc.world.getEntityByID(event.getEntity().getEntityId());
            if (entity instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer) entity;
                if(entity == mc.player && !self.getValue()) return;
                final EntityOtherPlayerMP fakeEntity = new EntityOtherPlayerMP(mc.world, player.getGameProfile());
                fakeEntity.copyLocationAndAnglesFrom(player);
                fakeEntity.rotationYawHead = player.rotationYawHead;
                fakeEntity.prevRotationYawHead = player.rotationYawHead;
                fakeEntity.rotationYaw = player.rotationYaw;
                fakeEntity.prevRotationYaw = player.prevRotationYaw;
                fakeEntity.rotationPitch = player.rotationPitch;
                fakeEntity.prevRotationPitch = player.prevRotationPitch;
                fakeEntity.cameraYaw = fakeEntity.rotationYaw;
                fakeEntity.cameraPitch = fakeEntity.rotationPitch;
                fakeEntity.setSneaking(player.isSneaking());
                popFakePlayerMap.put(fakeEntity, System.currentTimeMillis());
            }
        }
    }

    @SubscribeEvent
    public void onDeath(PlayerDeathEvent event) {
        if (mc.world.getEntityByID(event.getEntity().getEntityId()) != null) {
            final Entity entity = mc.world.getEntityByID(event.getEntity().getEntityId());
            if (entity instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer) entity;
                if(entity == mc.player) return;
                final EntityOtherPlayerMP fakeEntity = new EntityOtherPlayerMP(mc.world, player.getGameProfile());
                fakeEntity.copyLocationAndAnglesFrom(player);
                fakeEntity.rotationYawHead = player.rotationYawHead;
                fakeEntity.prevRotationYawHead = player.rotationYawHead;
                fakeEntity.rotationYaw = player.rotationYaw;
                fakeEntity.prevRotationYaw = player.rotationYaw;
                fakeEntity.rotationPitch = player.rotationPitch;
                fakeEntity.prevRotationPitch = player.rotationPitch;
                fakeEntity.cameraYaw = fakeEntity.rotationYaw;
                fakeEntity.cameraPitch = fakeEntity.rotationPitch;
                fakeEntity.setSneaking(player.isSneaking());
                popFakePlayerMap.put(fakeEntity, System.currentTimeMillis());
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

    public int clamp(final int num,  final int min,  final int max) {
        return (num < min) ? min : Math.min(num,  max);
    }
}

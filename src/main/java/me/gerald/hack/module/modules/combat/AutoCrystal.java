package me.gerald.hack.module.modules.combat;

import me.gerald.hack.GeraldHack;
import me.gerald.hack.module.Module;
import me.gerald.hack.setting.settings.BoolSetting;
import me.gerald.hack.setting.settings.NumSetting;
import me.gerald.hack.util.RenderUtil;
import me.gerald.hack.util.TimerUtil;
import me.gerald.hack.util.WorldUtil;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AutoCrystal extends Module {
    public AutoCrystal() {
        super("AutoCrystal", Category.COMBAT, "Fucks people.");
    }

    public NumSetting delay = register(new NumSetting("Delay", 25, 0, 250));
    public NumSetting range = register(new NumSetting("Range", 4, 1, 6));
    public NumSetting maxPlayerDamage = register(new NumSetting("MaxPlayerDamage", 10, 1, 20));
    public BoolSetting packetBreak = register(new BoolSetting("PacketBreak", true));
    public NumSetting hitAttempts = register(new NumSetting("HitAttempts", 2, 1, 5));

    public BlockPos targetPos = null;
    public TimerUtil timer = new TimerUtil();

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        Entity target = getTarget((int) range.getValue());
        if(target != null) {
            if(mc.player.getDistance(target) > range.getValue() || !target.isEntityAlive() || target.isDead ) {
                return;
            }
            if(timer.passedMs((long) delay.getValue())) {
                placeLogic(target);
                breakLogic();
            }
        }
    }

    public void placeLogic(Entity target) {
        List<BlockPos> blocks = findCrystalBlocks();
        if(mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            for (BlockPos pos : blocks) {
                double playerDamage = calculateDamage(pos.getX(), pos.getY(), pos.getZ(), target);
                double selfDamage = calculateDamage(pos.getX(), pos.getY(), pos.getZ(), mc.player);
                if (playerDamage > .5) {
                    if (selfDamage > maxPlayerDamage.getValue()) return;
                    RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX() + .5, pos.getY() - .5d, pos.getZ() + .5));
                    EnumFacing face;
                    if (result == null || result.sideHit == null) {
                        face = EnumFacing.UP;
                    } else {
                        face = result.sideHit;
                    }
                    boolean offhand = mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, face, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0, 0, 0));
                    targetPos = pos;
                    return;
                }
            }
        }
    }

    public void breakLogic() {
        EntityEnderCrystal crystal = mc.world.loadedEntityList.stream()
                .filter(entity -> entity instanceof EntityEnderCrystal)
                .map(entity -> (EntityEnderCrystal) entity)
                .min(Comparator.comparing(c -> mc.player.getDistance(c)))
                .orElse(null);
        for(int i = 0; i < hitAttempts.getValue(); i++) {
            if(crystal != null) {
                if (packetBreak.getValue())
                    Objects.requireNonNull(mc.getConnection()).sendPacket(new CPacketUseEntity(crystal));
                else
                    mc.playerController.attackEntity(mc.player, crystal);
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }else {
                return;
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(targetPos != null) {
            AxisAlignedBB box = mc.world.getBlockState(targetPos).getSelectedBoundingBox(mc.world, targetPos).offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
            RenderUtil.prepare();
            RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, 1f, 1f, 1f, 1f);
            RenderGlobal.renderFilledBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, 1f, 1f, 1f, 0.5f);
            RenderUtil.release();
        }
    }

    @Override
    public void onDisable() {
        targetPos = null;
    }

    public Entity getTarget(int range) {
        for(Entity entity : mc.world.loadedEntityList) {
            if(entity == mc.player) continue;
            if(GeraldHack.INSTANCE.friendManager.isFriend(entity.getDisplayName().getUnformattedText())) continue;
            if(mc.player.getDistance(entity) < range) return entity;
        }
        return null;
    }

    //kami calcs
    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 6.0F * 2.0F;
        double distancedSize = entity.getDistance(posX, posY, posZ) / (double) doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        double v = (1.0D - distancedSize) * blockDensity;
        float damage = (float) ((int) ((v * v + v) / 2.0D * 7.0D * (double) doubleExplosionSize + 1.0D));
        double finalD = 1;
        if (entity instanceof EntityLivingBase) {
            finalD = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion(mc.world, null, posX, posY, posZ, 6F, false, true));
        }
        return (float) finalD;
    }

    public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer) entity;
            DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

            int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            float f = MathHelper.clamp(k, 0.0F, 20.0F);
            damage = damage * (1.0F - f / 25.0F);

            if (entity.isPotionActive(Objects.requireNonNull(Potion.getPotionById(11)))) {
                damage = damage - (damage / 5);
            }

            damage = Math.max(damage, 0.0F);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    private static float getDamageMultiplied(float damage) {
        int diff = mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0 : (diff == 2 ? 1 : (diff == 1 ? 0.5f : 1.5f)));
    }

    private List<BlockPos> findCrystalBlocks() {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(WorldUtil.getSphere(mc.player.getPosition(), range.getValue(), false).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
        return positions;
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    private boolean canPlaceCrystal(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        return (mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK
                || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN)
                && mc.world.getBlockState(boost).getBlock() == Blocks.AIR
                && mc.world.getBlockState(boost2).getBlock() == Blocks.AIR
                && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty()
                && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
}

package cofh.test.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import static cofh.test.CoFHTest.LIGHTNING_ARROW_ENTITY;

public class LightningArrowEntity extends AbstractArrowEntity {

    public LightningArrowEntity(EntityType<? extends LightningArrowEntity> entityIn, World worldIn) {

        super(entityIn, worldIn);
    }

    public LightningArrowEntity(World worldIn, LivingEntity shooter) {

        super(LIGHTNING_ARROW_ENTITY.get(), shooter, worldIn);
    }

    public LightningArrowEntity(World worldIn, double x, double y, double z) {

        super(LIGHTNING_ARROW_ENTITY.get(), x, y, z, worldIn);
    }

    @Override
    protected ItemStack getArrowStack() {

        return new ItemStack(Items.ARROW);
    }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {

        super.onHit(raytraceResultIn);
        if (raytraceResultIn.getType() != RayTraceResult.Type.MISS) {
            if (!isInWater() && !isInLava() && this.world instanceof ServerWorld) {
                BlockPos pos = this.getPosition();
                if (this.world.isSkyLightMax(pos)) {
                    LightningBoltEntity lightningboltentity = new LightningBoltEntity(this.world, (double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, false);
                    lightningboltentity.setCaster(getShooter() instanceof ServerPlayerEntity ? (ServerPlayerEntity) getShooter() : null);
                    ((ServerWorld) this.world).addLightningBolt(lightningboltentity);
                }
            }
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {

        return NetworkHooks.getEntitySpawningPacket(this);
    }

}

package cofh.lib.util;

import cofh.lib.util.helpers.MathHelper;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.file.Path;
import java.util.List;

import static cofh.lib.util.constants.Tags.TAG_ENCHANTMENTS;
import static cofh.lib.util.references.CoreReferences.GLOSSED_MAGMA;
import static net.minecraft.block.Blocks.*;
import static net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel;
import static net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND;
import static net.minecraftforge.common.util.Constants.NBT.TAG_LIST;

public class Utils {

    private Utils() {

    }

    public static boolean isClientWorld(World world) {

        return world.isRemote;
    }

    public static boolean isServerWorld(World world) {

        return !world.isRemote;
    }

    public static boolean isFakePlayer(Entity entity) {

        return entity instanceof FakePlayer;
    }

    public static String createPrettyJSON(String jsonString) {

        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(json);
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {

        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }

    // region PARTICLE UTILS
    public static void spawnParticles(World world, IParticleData particle, double posX, double posY, double posZ, int particleCount, double xOffset, double yOffset, double zOffset, double speed) {

        if (isServerWorld(world)) {
            ((ServerWorld) world).spawnParticle(particle, posX, posY + 1.0D, posZ, particleCount, xOffset, yOffset, zOffset, speed);
        } else {
            world.addParticle(particle, posX + xOffset, posY + yOffset, posZ + zOffset, 0.0D, 0.0D, 0.0D);
        }
    }
    // endregion

    // region CHAT UTILS

    // endregion

    // region ENTITY UTILS
    public static boolean addToPlayerInventory(PlayerEntity player, ItemStack stack) {

        if (stack.isEmpty() || player == null) {
            return false;
        }
        if (stack.getItem() instanceof ArmorItem) {
            int index = ((ArmorItem) stack.getItem()).getEquipmentSlot().getIndex();
            if (player.inventory.armorInventory.get(index).isEmpty()) {
                player.inventory.armorInventory.set(index, stack);
                return true;
            }
        }
        PlayerInventory inv = player.inventory;
        for (int i = 0; i < inv.mainInventory.size(); ++i) {
            if (inv.mainInventory.get(i).isEmpty()) {
                inv.mainInventory.set(i, stack.copy());
                return true;
            }
        }
        return false;
    }

    public static boolean dropItemStackIntoWorld(ItemStack stack, World world, Vec3d pos) {

        return dropItemStackIntoWorld(stack, world, pos, false);
    }

    public static boolean dropItemStackIntoWorldWithVelocity(ItemStack stack, World world, BlockPos pos) {

        return dropItemStackIntoWorld(stack, world, new Vec3d(pos), true);
    }

    public static boolean dropItemStackIntoWorldWithVelocity(ItemStack stack, World world, Vec3d pos) {

        return dropItemStackIntoWorld(stack, world, pos, true);
    }

    public static boolean dropItemStackIntoWorld(ItemStack stack, World world, Vec3d pos, boolean velocity) {

        if (stack.isEmpty()) {
            return false;
        }
        float x2 = 0.5F;
        float y2 = 0.0F;
        float z2 = 0.5F;

        if (velocity) {
            x2 = world.rand.nextFloat() * 0.8F + 0.1F;
            y2 = world.rand.nextFloat() * 0.8F + 0.1F;
            z2 = world.rand.nextFloat() * 0.8F + 0.1F;
        }
        ItemEntity entity = new ItemEntity(world, pos.x + x2, pos.y + y2, pos.z + z2, stack.copy());

        if (velocity) {
            entity.setMotion(world.rand.nextGaussian() * 0.05F, world.rand.nextGaussian() * 0.05F + 0.2F, world.rand.nextGaussian() * 0.05F);
        } else {
            entity.setMotion(-0.05, 0, 0);
        }
        world.addEntity(entity);

        return true;
    }

    public static boolean dropDismantleStackIntoWorld(ItemStack stack, World world, BlockPos pos) {

        if (stack.isEmpty()) {
            return false;
        }
        float f = 0.3F;
        double x2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
        double y2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
        double z2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
        ItemEntity dropEntity = new ItemEntity(world, pos.getX() + x2, pos.getY() + y2, pos.getZ() + z2, stack);
        dropEntity.setPickupDelay(10);
        world.addEntity(dropEntity);

        return true;
    }

    public static boolean teleportEntityTo(Entity entity, BlockPos pos) {

        return teleportEntityTo(entity, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
    }

    public static boolean teleportEntityTo(Entity entity, double x, double y, double z) {

        if (entity instanceof LivingEntity) {
            return teleportEntityTo((LivingEntity) entity, x, y, z);
        } else {
            entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch);
            entity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
        }
        return true;
    }

    public static boolean teleportEntityTo(LivingEntity entity, double x, double y, double z) {

        EnderTeleportEvent event = new EnderTeleportEvent(entity, x, y, z, 0);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }
        if (entity instanceof ServerPlayerEntity && !isFakePlayer(entity)) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            if (player.connection.getNetworkManager().isChannelOpen() && !player.isSleeping()) {
                if (entity.isPassenger()) {
                    entity.stopRiding();
                }
                entity.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                entity.fallDistance = 0.0F;
            }
        } else {
            if (entity.isPassenger()) {
                entity.stopRiding();
            }
            entity.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
            entity.fallDistance = 0.0F;
        }
        entity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
        return true;
    }
    // endregion

    // region ENCHANT UTILS
    public static int getEnchantedCapacity(int amount, int holding) {

        return amount + amount * holding / 2;
    }

    public static int getHeldEnchantmentLevel(LivingEntity living, Enchantment ench) {

        return Math.max(getEnchantmentLevel(ench, living.getHeldItemMainhand()), getEnchantmentLevel(ench, living.getHeldItemOffhand()));
    }

    public static void addEnchantment(ItemStack stack, Enchantment ench, int level) {

        stack.addEnchantment(ench, level);
    }

    public static void removeEnchantment(ItemStack stack, Enchantment ench) {

        if (stack.getTag() == null || !stack.getTag().contains(TAG_ENCHANTMENTS, TAG_LIST)) {
            return;
        }
        ListNBT list = stack.getTag().getList(TAG_ENCHANTMENTS, TAG_COMPOUND);
        String encId = String.valueOf(ForgeRegistries.ENCHANTMENTS.getKey(ench));

        for (int i = 0; i < list.size(); ++i) {
            CompoundNBT tag = list.getCompound(i);
            String id = tag.getString("id");
            if (encId.equals(id)) {
                list.remove(i);
                break;
            }
        }
        if (list.isEmpty()) {
            stack.removeChildTag(TAG_ENCHANTMENTS);
        }
    }
    // endregion

    // region BURNING
    public static void igniteNearbyEntities(Entity entity, World worldIn, BlockPos pos, int radius, int duration) {

        AxisAlignedBB area = new AxisAlignedBB(pos.add(-radius, -radius, -radius), pos.add(1 + radius, 1 + radius, 1 + radius));
        List<LivingEntity> mobs = worldIn.getEntitiesWithinAABB(LivingEntity.class, area, EntityPredicates.IS_ALIVE);
        mobs.removeIf(Entity::isInWater);
        mobs.removeIf(Entity::isImmuneToFire);
        mobs.removeIf(mob -> mob instanceof EndermanEntity);
        for (LivingEntity mob : mobs) {
            mob.setFire(duration);
        }
    }

    public static void igniteNearbyGround(Entity entity, World worldIn, BlockPos pos, int radius) {

        BlockState blockstate = FIRE.getDefaultState();
        float f = (float) Math.min(16, radius);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-f, -f, -f), pos.add(f, f, f))) {
            if (blockpos.withinDistance(entity.getPositionVec(), f)) {
                blockpos$mutableblockpos.setPos(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
                BlockState blockstate1 = worldIn.getBlockState(blockpos$mutableblockpos);
                if (blockstate1.isAir(worldIn, blockpos$mutableblockpos)) {
                    if (isValidFirePosition(worldIn, blockpos$mutableblockpos)) {
                        worldIn.setBlockState(blockpos$mutableblockpos, blockstate);
                    }
                }
            }
        }
    }

    private static boolean isValidFirePosition(World worldIn, BlockPos pos) {

        BlockState state = worldIn.getBlockState(pos.down());
        if (Block.doesSideFillSquare(state.getCollisionShape(worldIn, pos.down()), Direction.UP)) {
            return state.getMaterial().isFlammable() || worldIn.rand.nextInt(3) == 0;
        }
        return false;
    }
    // endregion

    // region FUNGUS
    public static void transformMycelium(Entity entity, World worldIn, BlockPos pos, int radius) {

        BlockState state = MYCELIUM.getDefaultState();
        BlockState dirt = DIRT.getDefaultState();

        float f = (float) Math.min(16, radius);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-f, -f, -f), pos.add(f, f, f))) {
            if (blockpos.withinDistance(entity.getPositionVec(), f)) {
                blockpos$mutableblockpos.setPos(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
                BlockState blockstate1 = worldIn.getBlockState(blockpos$mutableblockpos);
                if (blockstate1.isAir(worldIn, blockpos$mutableblockpos)) {
                    if (worldIn.getBlockState(blockpos) == dirt) {
                        worldIn.setBlockState(blockpos, state);
                    }
                }
            }
        }
    }
    // endregion

    // region FREEZING
    public static void freezeNearbyGround(Entity entity, World worldIn, BlockPos pos, int radius) {

        BlockState state = SNOW.getDefaultState();
        float f = (float) Math.min(16, radius);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-f, -f, -f), pos.add(f, f, f))) {
            if (blockpos.withinDistance(entity.getPositionVec(), f)) {
                blockpos$mutableblockpos.setPos(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
                BlockState blockstate1 = worldIn.getBlockState(blockpos$mutableblockpos);
                if (blockstate1.isAir(worldIn, blockpos$mutableblockpos)) {
                    if (worldIn.getBiome(blockpos$mutableblockpos).func_225486_c(blockpos) < 0.8F && isValidSnowPosition(worldIn, blockpos$mutableblockpos)) {
                        worldIn.setBlockState(blockpos$mutableblockpos, state);
                    }
                }
                // TODO: Snow layering?
                //                else if (blockstate1.getBlock() == SNOW && blockstate1.get(SnowBlock.LAYERS) < 3) {
                //                    worldIn.setBlockState(blockpos$mutableblockpos, blockstate1.with(SnowBlock.LAYERS, Math.min(blockstate1.get(SnowBlock.LAYERS) + 1, 3)));
                //                }
            }
        }
    }

    public static void freezeNearbyWater(Entity entity, World worldIn, BlockPos pos, int radius, boolean permanent) {

        BlockState state = permanent ? ICE.getDefaultState() : FROSTED_ICE.getDefaultState();
        float f = (float) Math.min(16, radius);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-f, -f, -f), pos.add(f, f, f))) {
            if (blockpos.withinDistance(entity.getPositionVec(), f)) {
                blockpos$mutableblockpos.setPos(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
                BlockState blockstate1 = worldIn.getBlockState(blockpos$mutableblockpos);
                if (blockstate1.isAir(worldIn, blockpos$mutableblockpos)) {
                    BlockState blockstate2 = worldIn.getBlockState(blockpos);
                    boolean isFull = blockstate2.getBlock() == WATER && blockstate2.get(FlowingFluidBlock.LEVEL) == 0;
                    if (blockstate2.getMaterial() == Material.WATER && isFull && state.isValidPosition(worldIn, blockpos) && worldIn.func_217350_a(state, blockpos, ISelectionContext.dummy())) {
                        worldIn.setBlockState(blockpos, state);
                        worldIn.getPendingBlockTicks().scheduleTick(blockpos, FROSTED_ICE, MathHelper.nextInt(worldIn.rand, 60, 120));
                    }
                }
            }
        }
    }

    public static void freezeNearbyLava(Entity entity, World worldIn, BlockPos pos, int radius, boolean permanent) {

        if (GLOSSED_MAGMA == null && !permanent) {
            return;
        }
        BlockState state = permanent ? OBSIDIAN.getDefaultState() : GLOSSED_MAGMA.getDefaultState();
        float f = (float) Math.min(16, radius);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-f, -f, -f), pos.add(f, f, f))) {
            if (blockpos.withinDistance(entity.getPositionVec(), f)) {
                blockpos$mutableblockpos.setPos(blockpos.getX(), blockpos.getY() + 1, blockpos.getZ());
                BlockState blockstate1 = worldIn.getBlockState(blockpos$mutableblockpos);
                if (blockstate1.isAir(worldIn, blockpos$mutableblockpos)) {
                    BlockState blockstate2 = worldIn.getBlockState(blockpos);
                    boolean isFull = blockstate2.getBlock() == LAVA && blockstate2.get(FlowingFluidBlock.LEVEL) == 0;
                    if (blockstate2.getMaterial() == Material.LAVA && isFull && state.isValidPosition(worldIn, blockpos) && worldIn.func_217350_a(state, blockpos, ISelectionContext.dummy())) {
                        worldIn.setBlockState(blockpos, state);
                        worldIn.getPendingBlockTicks().scheduleTick(blockpos, GLOSSED_MAGMA, MathHelper.nextInt(worldIn.rand, 60, 120));
                    }
                }
            }
        }
    }

    public static boolean isValidSnowPosition(World worldIn, BlockPos pos) {

        BlockState state = worldIn.getBlockState(pos.down());
        Block block = state.getBlock();
        if (block == ICE || block == PACKED_ICE || block == BARRIER || block == FROSTED_ICE || block == GLOSSED_MAGMA) {
            return false;
        }
        return Block.doesSideFillSquare(state.getCollisionShape(worldIn, pos.down()), Direction.UP) || block == SNOW && state.get(SnowBlock.LAYERS) == 8;
    }
    // endregion
}

package cofh.thermal.core.init;

import cofh.lib.block.storage.MetalStorageBlock;
import cofh.lib.block.storage.ResourceStorageBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import static cofh.lib.util.modhelpers.ThermalHelper.*;

public class BlocksTSeries {

    private BlocksTSeries() {

    }

    public static void registerBlocks(RegistryEvent.Register<Block> event) {

        IForgeRegistry<Block> registry = event.getRegistry();

        registry.register(new ResourceStorageBlock(Block.Properties.from(Blocks.COAL_BLOCK)) {

            @Override
            public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {

                return 5;
            }

            @Override
            public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {

                return 5;
            }
        }.setRegistryName(ID_CHARCOAL_BLOCK));

        registry.register(new ResourceStorageBlock(Block.Properties.from(Blocks.COAL_BLOCK)) {

            @Override
            public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {

                return 5;
            }

            @Override
            public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {

                return 5;
            }
        }.setRegistryName(ID_COAL_COKE_BLOCK));

        registry.register(new MetalStorageBlock(Block.Properties.create(Material.IRON, MaterialColor.RED).hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL).harvestLevel(2).harvestTool(ToolType.PICKAXE).lightValue(7)) {

            @Override
            public boolean canProvidePower(BlockState state) {

                return true;
            }

            @Override
            public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {

                return 15;
            }
        }.setRegistryName(ID_SIGNALUM_BLOCK));

        registry.register(new MetalStorageBlock(Block.Properties.create(Material.IRON, MaterialColor.YELLOW).hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL).harvestLevel(2).harvestTool(ToolType.PICKAXE).lightValue(15)).setRegistryName(ID_LUMIUM_BLOCK));
        registry.register(new MetalStorageBlock(Block.Properties.create(Material.IRON, MaterialColor.CYAN).hardnessAndResistance(25.0F, 30.0F).sound(SoundType.METAL).harvestLevel(3).harvestTool(ToolType.PICKAXE).lightValue(3)).setRegistryName(ID_ENDERIUM_BLOCK));
    }

    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {

        IForgeRegistry<Item> registry = event.getRegistry();


    }

}

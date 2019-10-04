package cofh.lib.block.ores;

import cofh.lib.block.BlockCoFH;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;

public class OreBlockFluid extends BlockCoFH {

    public OreBlockFluid() {

        this(Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(5.0F, 6.0F).sound(SoundType.STONE).harvestLevel(2).harvestTool(ToolType.PICKAXE));
    }

    public OreBlockFluid(Properties properties) {

        super(properties);
    }

}
package cofh.thermal.expansion.init;

import cofh.thermal.expansion.tileentity.device.DeviceRockGenTile;
import cofh.thermal.expansion.tileentity.device.DeviceWaterGenTile;
import cofh.thermal.expansion.tileentity.dynamo.*;
import cofh.thermal.expansion.tileentity.machine.*;
import net.minecraft.tileentity.TileEntityType;

import static cofh.thermal.core.ThermalCore.TILE_ENTITIES;
import static cofh.thermal.expansion.init.TExpReferences.*;

public class TExpTileEntities {

    private TExpTileEntities() {

    }

    public static void register() {

        TILE_ENTITIES.register(ID_MACHINE_FURNACE, () -> TileEntityType.Builder.create(MachineFurnaceTile::new, MACHINE_FURNACE_BLOCK).build(null));
        TILE_ENTITIES.register(ID_MACHINE_SAWMILL, () -> TileEntityType.Builder.create(MachineSawmillTile::new, MACHINE_SAWMILL_BLOCK).build(null));
        TILE_ENTITIES.register(ID_MACHINE_PULVERIZER, () -> TileEntityType.Builder.create(MachinePulverizerTile::new, MACHINE_PULVERIZER_BLOCK).build(null));
        TILE_ENTITIES.register(ID_MACHINE_INSOLATOR, () -> TileEntityType.Builder.create(MachineInsolatorTile::new, MACHINE_INSOLATOR_BLOCK).build(null));
        TILE_ENTITIES.register(ID_MACHINE_CENTRIFUGE, () -> TileEntityType.Builder.create(MachineCentrifugeTile::new, MACHINE_CENTRIFUGE_BLOCK).build(null));
        TILE_ENTITIES.register(ID_MACHINE_PRESS, () -> TileEntityType.Builder.create(MachinePressTile::new, MACHINE_PRESS_BLOCK).build(null));
        TILE_ENTITIES.register(ID_MACHINE_CRUCIBLE, () -> TileEntityType.Builder.create(MachineCrucibleTile::new, MACHINE_CRUCIBLE_BLOCK).build(null));
        TILE_ENTITIES.register(ID_MACHINE_CHILLER, () -> TileEntityType.Builder.create(MachineChillerTile::new, MACHINE_CHILLER_BLOCK).build(null));
        TILE_ENTITIES.register(ID_MACHINE_REFINERY, () -> TileEntityType.Builder.create(MachineRefineryTile::new, MACHINE_REFINERY_BLOCK).build(null));
        TILE_ENTITIES.register(ID_MACHINE_BREWER, () -> TileEntityType.Builder.create(MachineBrewerTile::new, MACHINE_BREWER_BLOCK).build(null));
        TILE_ENTITIES.register(ID_MACHINE_BOTTLER, () -> TileEntityType.Builder.create(MachineBottlerTile::new, MACHINE_BOTTLER_BLOCK).build(null));

        TILE_ENTITIES.register(ID_DYNAMO_STIRLING, () -> TileEntityType.Builder.create(DynamoStirlingTile::new, DYNAMO_STIRLING_BLOCK).build(null));
        TILE_ENTITIES.register(ID_DYNAMO_COMPRESSION, () -> TileEntityType.Builder.create(DynamoCompressionTile::new, DYNAMO_COMPRESSION_BLOCK).build(null));
        TILE_ENTITIES.register(ID_DYNAMO_MAGMATIC, () -> TileEntityType.Builder.create(DynamoMagmaticTile::new, DYNAMO_MAGMATIC_BLOCK).build(null));
        TILE_ENTITIES.register(ID_DYNAMO_NUMISMATIC, () -> TileEntityType.Builder.create(DynamoNumismaticTile::new, DYNAMO_NUMISMATIC_BLOCK).build(null));
        TILE_ENTITIES.register(ID_DYNAMO_LAPIDARY, () -> TileEntityType.Builder.create(DynamoLapidaryTile::new, DYNAMO_LAPIDARY_BLOCK).build(null));

        TILE_ENTITIES.register(ID_DEVICE_ROCK_GEN, () -> TileEntityType.Builder.create(DeviceRockGenTile::new, DEVICE_ROCK_GEN_BLOCK).build(null));
        TILE_ENTITIES.register(ID_DEVICE_WATER_GEN, () -> TileEntityType.Builder.create(DeviceWaterGenTile::new, DEVICE_WATER_GEN_BLOCK).build(null));
    }

}

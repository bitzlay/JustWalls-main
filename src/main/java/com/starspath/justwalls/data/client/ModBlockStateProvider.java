package com.starspath.justwalls.data.client;

import com.starspath.justwalls.JustWalls;
import com.starspath.justwalls.init.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, JustWalls.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        simpleMultiBlock(ModBlocks.THATCH_WALL, "thatch_wall");
        simpleMultiBlock(ModBlocks.THATCH_WALL_FLOOR, "thatch_wall");
        simpleMultiBlock(ModBlocks.THATCH_WALL_DOOR_FRAME, "thatch_wall");
        simpleMultiBlock(ModBlocks.THATCH_WALL_WINDOW_FRAME, "thatch_wall");
        simpleMultiBlock(ModBlocks.THATCH_WALL_HATCH, "thatch_wall");

        simpleMultiBlock(ModBlocks.WOODEN_WALL, "wooden_wall");
        simpleMultiBlock(ModBlocks.WOODEN_WALL_FLOOR, "wooden_wall");
        simpleMultiBlock(ModBlocks.WOODEN_WALL_DOOR_FRAME, "wooden_wall");
        simpleMultiBlock(ModBlocks.WOODEN_WALL_WINDOW_FRAME, "wooden_wall");
        simpleMultiBlock(ModBlocks.WOODEN_WALL_HATCH, "wooden_wall");

        simpleMultiBlock(ModBlocks.STONE_WALL, "stone_wall");
        simpleMultiBlock(ModBlocks.STONE_WALL_FLOOR, "stone_wall");
        simpleMultiBlock(ModBlocks.STONE_WALL_DOOR_FRAME, "stone_wall");
        simpleMultiBlock(ModBlocks.STONE_WALL_WINDOW_FRAME, "stone_wall");
        simpleMultiBlock(ModBlocks.STONE_WALL_HATCH, "stone_wall");

        simpleMultiBlock(ModBlocks.METAL_WALL, "metal_wall");
        simpleMultiBlock(ModBlocks.METAL_WALL_FLOOR, "metal_wall");
        simpleMultiBlock(ModBlocks.METAL_WALL_DOOR_FRAME, "metal_wall");
        simpleMultiBlock(ModBlocks.METAL_WALL_WINDOW_FRAME, "metal_wall");
        simpleMultiBlock(ModBlocks.METAL_WALL_HATCH, "metal_wall");

        simpleMultiBlock(ModBlocks.ARMORED_WALL, "armored_wall");
        simpleMultiBlock(ModBlocks.ARMORED_WALL_FLOOR, "armored_wall");
        simpleMultiBlock(ModBlocks.ARMORED_WALL_DOOR_FRAME, "armored_wall");
        simpleMultiBlock(ModBlocks.ARMORED_WALL_WINDOW_FRAME, "armored_wall");
        simpleMultiBlock(ModBlocks.ARMORED_WALL_HATCH, "armored_wall");

        directionalBlock(ModBlocks.THATCH_WALL_PILLAR.get(), models().getExistingFile(modLoc("thatch_wall_pillar")));
        directionalBlock(ModBlocks.WOODEN_WALL_PILLAR.get(), models().getExistingFile(modLoc("wooden_wall_pillar")));
        directionalBlock(ModBlocks.STONE_WALL_PILLAR.get(), models().getExistingFile(modLoc("stone_wall_pillar")));
        directionalBlock(ModBlocks.METAL_WALL_PILLAR.get(), models().getExistingFile(modLoc("metal_wall_pillar")));
        directionalBlock(ModBlocks.ARMORED_WALL_PILLAR.get(), models().getExistingFile(modLoc("armored_wall_pillar")));
    }

    private void blockWithItem(RegistryObject<Block> registryObject){
        simpleBlockWithItem(registryObject.get(), cubeAll(registryObject.get()));
    }

    private void simpleMultiBlock(RegistryObject<Block> registryObject, String name){
        simpleBlock(registryObject.get(), ConfiguredModel.builder()
                .modelFile(
                        models().getExistingFile(
                                modLoc(name)
                        )
                ).build());
    }
}

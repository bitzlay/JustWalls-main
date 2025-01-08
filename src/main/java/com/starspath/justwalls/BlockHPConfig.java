package com.starspath.justwalls;

import com.starspath.justwalls.world.DamageBlockSaveData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = JustWalls.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockHPConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<String>> BLOCK_HP_VALUES;
    private static final Map<Block, Integer> resolvedBlockHP = new HashMap<>();

    static {
        BUILDER.push("Block HP Configuration");

        BLOCK_HP_VALUES = BUILDER
                .comment("Define HP values for blocks in format 'modid:blockid=hp'")
                .define("blockHPValues", List.of(
                        "minecraft:obsidian=1000"
                ));

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        resolvedBlockHP.clear();
        for(String entry : BLOCK_HP_VALUES.get()) {
            String[] parts = entry.split("=");
            if(parts.length == 2) {
                ResourceLocation resourceLocation = ResourceLocation.tryParse(parts[0]);
                if(resourceLocation != null) {
                    Block block = ForgeRegistries.BLOCKS.getValue(resourceLocation);
                    if(block != null) {
                        try {
                            resolvedBlockHP.put(block, Integer.parseInt(parts[1]));
                        } catch(NumberFormatException e) {
                            // Skip invalid numbers
                        }
                    }
                }
            }
        }
    }

    public static boolean hasCustomHP(Block block) {
        return resolvedBlockHP.containsKey(block);
    }

    public static int getBlockHP(Block block) {
        return resolvedBlockHP.getOrDefault(block, 0);
    }

    public static void init() {
        MinecraftForge.EVENT_BUS.register(BlockHPConfig.class);
    }
}
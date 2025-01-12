package com.starspath.justwalls;

import com.starspath.justwalls.JustWalls;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = JustWalls.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockHPConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> BLOCK_HP_VALUES;
    private static final Map<Block, Integer> maxBlockHP = new HashMap<>();
    private static List<String> DEFAULT_CONFIG = List.of("minecraft:obsidian=1000");

    static {
        BUILDER.push("Block HP Configuration");

        BLOCK_HP_VALUES = BUILDER
                .comment("Define HP values for blocks in format 'modid:blockid=hp'")
                .defineList("blockHPValues",
                        () -> DEFAULT_CONFIG,
                        entry -> entry instanceof String && validateEntry((String) entry));

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC,"justwalls-block-hp.toml");
    }

    private static boolean validateEntry(String entry) {
        String[] parts = entry.split("=");
        if (parts.length != 2) return false;

        ResourceLocation resourceLocation = ResourceLocation.tryParse(parts[0].trim());
        if (resourceLocation == null) return false;

        try {
            Integer.parseInt(parts[1].trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        maxBlockHP.clear();
        List<? extends String> configValues = BLOCK_HP_VALUES.get();

        for (String entry : configValues) {
            try {
                String[] parts = entry.split("=");
                ResourceLocation resourceLocation = ResourceLocation.tryParse(parts[0].trim());
                if (resourceLocation != null) {
                    Block block = ForgeRegistries.BLOCKS.getValue(resourceLocation);
                    if (block != null) {
                        int hp = Integer.parseInt(parts[1].trim());
                        maxBlockHP.put(block, hp);
                    }
                }
            } catch (Exception e) {
                // Log or handle parsing errors
            }
        }
    }

    public static boolean hasCustomHP(Block block) {
        return maxBlockHP.containsKey(block);
    }

    public static int getBlockHP(Block block) {
        return maxBlockHP.getOrDefault(block, 100);
    }

    public static void init() {
        MinecraftForge.EVENT_BUS.register(BlockHPConfig.class);
    }
}
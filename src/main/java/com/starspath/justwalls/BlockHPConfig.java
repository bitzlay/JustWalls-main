package com.starspath.justwalls;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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

    static {
        BUILDER.push("Block HP Configuration");

        BLOCK_HP_VALUES = BUILDER
                .comment("Define HP values for blocks in format 'modid:blockid=hp'")
                .defineList("blockHPValues",
                        () -> new ArrayList<>(List.of("minecraft:obsidian=1000")),
                        entry -> entry instanceof String && validateEntry((String) entry));

        BUILDER.pop();
        SPEC = BUILDER.build();
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
    public static void onLoad(final ModConfigEvent.Loading event) {
        loadConfig();
    }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading event) {
        loadConfig();
    }

    private static void loadConfig() {
        maxBlockHP.clear();
        for (String entry : BLOCK_HP_VALUES.get()) {
            try {
                String[] parts = entry.split("=");
                ResourceLocation resourceLocation = ResourceLocation.tryParse(parts[0].trim());
                if (resourceLocation != null) {
                    Block block = ForgeRegistries.BLOCKS.getValue(resourceLocation);
                    if (block != null) {
                        int hp = Integer.parseInt(parts[1].trim());
                        maxBlockHP.put(block, hp);
                    } else {
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public static boolean hasCustomHP(Block block) {
        return maxBlockHP.containsKey(block);
    }

    public static int getBlockHP(Block block) {
        return maxBlockHP.getOrDefault(block, 0);
    }

    public static void init() {
        MinecraftForge.EVENT_BUS.register(BlockHPConfig.class);
    }
}
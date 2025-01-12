package com.starspath.justwalls;

import com.mojang.logging.LogUtils;
//import com.starspath.justwalls.events.TACZEventHandler;
import com.starspath.justwalls.init.ModBlockEntity;
import com.starspath.justwalls.init.ModBlocks;
import com.starspath.justwalls.init.ModCreativeModeTab;
import com.starspath.justwalls.init.ModItems;
import com.starspath.justwalls.world.DamageBlockSaveData;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import static com.starspath.justwalls.BlockHPConfig.getBlockHP;
import static com.starspath.justwalls.BlockHPConfig.hasCustomHP;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(JustWalls.MODID)
public class JustWalls
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "justwalls";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();



    public JustWalls()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BlockHPConfig.register();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        ModBlocks.BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ModItems.ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        ModCreativeModeTab.CREATIVE_MODE_TABS.register(modEventBus);

        ModBlockEntity.BLOCK_ENTITIES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM COMMON SETUP");
        if (ModList.get().isLoaded("tacz")) {
            try{
                Class<?> ammoHitBlockEventClass = Class.forName("com.tacz.guns.api.event.server.AmmoHitBlockEvent");
                Class<?> entityKineticBulletClass = Class.forName("com.tacz.guns.entity.EntityKineticBullet");

            }
            catch (ClassNotFoundException e){
                LOGGER.info("TACZ mod not found, skipping TACZEventHandler registration");
            }
      }
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == ModCreativeModeTab.JUSTWALLS_TAB.getKey()){
            event.accept(ModItems.THATCH_WALL_ITEM);
            event.accept(ModItems.WOODEN_WALL_ITEM);
            event.accept(ModItems.STONE_WALL_ITEM);
            event.accept(ModItems.METAL_WALL_ITEM);
            event.accept(ModItems.ARMORED_WALL_ITEM);

            event.accept(ModItems.THATCH_WALL_FLOOR_ITEM);
            event.accept(ModItems.WOODEN_WALL_FLOOR_ITEM);
            event.accept(ModItems.STONE_WALL_FLOOR_ITEM);
            event.accept(ModItems.METAL_WALL_FLOOR_ITEM);
            event.accept(ModItems.ARMORED_WALL_FLOOR_ITEM);

            event.accept(ModItems.THATCH_WALL_WINDOW_ITEM);
            event.accept(ModItems.WOODEN_WALL_WINDOW_ITEM);
            event.accept(ModItems.STONE_WALL_WINDOW_ITEM);
            event.accept(ModItems.METAL_WALL_WINDOW_ITEM);
            event.accept(ModItems.ARMORED_WALL_WINDOW_ITEM);

            event.accept(ModItems.THATCH_WALL_DOOR_ITEM);
            event.accept(ModItems.WOODEN_WALL_DOOR_ITEM);
            event.accept(ModItems.STONE_WALL_DOOR_ITEM);
            event.accept(ModItems.METAL_WALL_DOOR_ITEM);
            event.accept(ModItems.ARMORED_WALL_DOOR_ITEM);

            event.accept(ModItems.THATCH_WALL_WINDOW_FRAME_ITEM);
            event.accept(ModItems.WOODEN_WALL_WINDOW_FRAME_ITEM);
            event.accept(ModItems.STONE_WALL_WINDOW_FRAME_ITEM);
            event.accept(ModItems.METAL_WALL_WINDOW_FRAME_ITEM);
            event.accept(ModItems.ARMORED_WALL_WINDOW_FRAME_ITEM);

            event.accept(ModItems.THATCH_WALL_DOOR_FRAME_ITEM);
            event.accept(ModItems.WOODEN_WALL_DOOR_FRAME_ITEM);
            event.accept(ModItems.STONE_WALL_DOOR_FRAME_ITEM);
            event.accept(ModItems.METAL_WALL_DOOR_FRAME_ITEM);
            event.accept(ModItems.ARMORED_WALL_DOOR_FRAME_ITEM);

            event.accept(ModItems.THATCH_WALL_HATCH_ITEM);
            event.accept(ModItems.WOODEN_WALL_HATCH_ITEM);
            event.accept(ModItems.STONE_WALL_HATCH_ITEM);
            event.accept(ModItems.METAL_WALL_HATCH_ITEM);
            event.accept(ModItems.ARMORED_WALL_HATCH_ITEM);

            event.accept(ModItems.THATCH_WALL_PILLAR_ITEM_2);
            event.accept(ModItems.THATCH_WALL_PILLAR_ITEM_3);
            event.accept(ModItems.THATCH_WALL_PILLAR_ITEM_4);
            event.accept(ModItems.THATCH_WALL_PILLAR_ITEM_5);

            event.accept(ModItems.STRAW_SCRAP);
            event.accept(ModItems.WOOD_SCRAP);
            event.accept(ModItems.STONE_SCRAP);
            event.accept(ModItems.METAL_SCRAP);
            event.accept(ModItems.ARMORED_SCRAP);

            event.accept(ModItems.LOOT_CRATE_ITEM);

            event.accept(ModItems.SUPER_HAMMER);
            event.accept(ModItems.DEBUGGER);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        DamageBlockSaveData.get(event.getServer().overworld());
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
            if(!event.getLevel().isClientSide() && hasCustomHP(event.getPlacedBlock().getBlock())) {
                DamageBlockSaveData data = DamageBlockSaveData.get(event.getLevel());
                data.setBlockHP(event.getPos(), getBlockHP(event.getPlacedBlock().getBlock()));
            }
        }
    }
}

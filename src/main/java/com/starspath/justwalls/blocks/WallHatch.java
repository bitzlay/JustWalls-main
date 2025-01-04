package com.starspath.justwalls.blocks;

import com.starspath.justwalls.blocks.abstracts.StructureBlock;
import com.starspath.justwalls.init.ModBlocks;
import com.starspath.justwalls.utils.Tiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.ArrayList;

public class WallHatch extends StructureBlock {

    public WallHatch(Tiers.TIER tier){
        super(tier);
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.FACING, Direction.NORTH).setValue(MASTER, false).setValue(TIER, tier));
    }

    public WallHatch(Properties properties, Tiers.TIER tier) {
        super(properties, tier);
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.FACING, Direction.NORTH).setValue(MASTER, false).setValue(TIER, tier));
    }

    protected Boolean isMaster(BlockState blockState, BlockState self){
        return blockState.getBlock() == self.getBlock() && blockState.getValue(BlockStateProperties.FACING) == self.getValue(BlockStateProperties.FACING) && blockState.getValue(MASTER);
    }

    @Override
    public BlockPos getMasterPos(LevelAccessor level, BlockPos blockPos, BlockState blockState) {
        Direction direction = blockState.getValue(BlockStateProperties.FACING);
        for(int i = -1; i <= 1; i++){
            for(int j = 0; j <= 2; j++){
                BlockPos checkPos = blockPos.relative(direction.getClockWise(), j).relative(direction.getOpposite(), i);
                if(isMaster(level.getBlockState(checkPos), blockState)){
                    return checkPos;
                }
            }
        }
        return null;
    }

    @Override
    protected ArrayList<BlockPos> getChildPos(LevelAccessor level, BlockPos blockPos, BlockState blockState){
        Direction direction = blockState.getValue(BlockStateProperties.FACING);
        ArrayList<BlockPos> childPos = new ArrayList<>();
        blockPos = blockPos.relative(direction.getCounterClockWise());
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                BlockPos checkPos = blockPos.relative(Direction.NORTH, i).relative(Direction.EAST, j);
                BlockState checkBlockState = level.getBlockState(checkPos);
                if(checkBlockState.getBlock() == this && checkBlockState.getValue(BlockStateProperties.FACING) == direction){
                    childPos.add(checkPos);
                }
            }
        }
        return childPos;
    }

    @Override
    protected Block getNextTierBlock(Tiers.TIER tier){
        switch (tier){
            case THATCH -> {
                return ModBlocks.THATCH_WALL_HATCH.get();
            }
            case WOOD -> {
                return ModBlocks.WOODEN_WALL_HATCH.get();
            }
            case STONE -> {
                return ModBlocks.STONE_WALL_HATCH.get();
            }
            case METAL -> {
                return ModBlocks.METAL_WALL_HATCH.get();
            }
            case ARMOR -> {
                return ModBlocks.ARMORED_WALL_HATCH.get();
            }
        }
        return ModBlocks.THATCH_WALL_HATCH.get();
    }


    @Override
    public ItemStack getRequiredItemForUpgrade(BlockState blockState) {
        return super.getRequiredItemForUpgrade(blockState, 8);
    }
}

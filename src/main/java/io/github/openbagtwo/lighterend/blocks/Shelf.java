package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.ShelfBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ShelfBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class Shelf extends ShelfBlock {

  public Shelf(Properties settings) {
    super(settings
    );
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new Entity(pos, state);
  }

  public static class Entity extends ShelfBlockEntity {

    public Entity(BlockPos pos, BlockState state) {
      super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
      return LighterEndBlockEntities.SHELF;
    }

    @Override
    public boolean isValidBlockState(BlockState blockState) {
      return this.getType().isValid(blockState);
    }
  }
}

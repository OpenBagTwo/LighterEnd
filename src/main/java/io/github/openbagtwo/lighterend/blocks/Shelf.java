package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShelfBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ShelfBlockEntity;
import net.minecraft.util.math.BlockPos;

public class Shelf extends ShelfBlock {

  public Shelf(Settings settings) {
    super(settings
    );
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
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
    public boolean supports(BlockState blockState) {
      return this.getType().supports(blockState);
    }
  }
}

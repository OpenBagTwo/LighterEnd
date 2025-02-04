package io.github.openbagtwo.lighterend.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AuroraCrystalBlock extends TransparentBlock {

  public AuroraCrystalBlock(Properties properties) {
    super(
      properties
        .instrument(NoteBlockInstrument.HAT)
        .sound(SoundType.GLASS)
        .noOcclusion()
        .isValidSpawn(Blocks::never)
        .isRedstoneConductor(Blocks::never)
        .isSuffocating(Blocks::never)
        .isViewBlocking(Blocks::never)
        .strength(1F)
        .lightLevel((bs) -> 15)
    );
  }

  @Override
  public VoxelShape getVisualShape(
    BlockState blockState,
    BlockGetter blockGetter,
    BlockPos blockPos,
    CollisionContext collisionContext
  ) {
    return this.getCollisionShape(blockState, blockGetter, blockPos, collisionContext);
  }
}

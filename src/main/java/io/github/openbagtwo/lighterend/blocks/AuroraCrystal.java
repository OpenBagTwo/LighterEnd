package io.github.openbagtwo.lighterend.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AuroraCrystal extends TransparentBlock {

  public AuroraCrystal(Properties properties) {
    super(
        properties
            .instrument(NoteBlockInstrument.HAT)
            .sound(SoundType.GLASS)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .strength(0.5F)
            .lightLevel((bs) -> 15)
            .mapColor(MapColor.COLOR_MAGENTA)
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

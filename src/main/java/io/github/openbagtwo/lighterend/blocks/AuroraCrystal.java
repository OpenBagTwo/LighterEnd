package io.github.openbagtwo.lighterend.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.TransparentBlock;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class AuroraCrystal extends TransparentBlock {

  public AuroraCrystal(Settings properties) {
    super(
        properties
            .instrument(NoteBlockInstrument.HAT)
            .sounds(BlockSoundGroup.GLASS)
            .nonOpaque()
            .allowsSpawning(Blocks::never)
            .solidBlock(Blocks::never)
            .suffocates(Blocks::never)
            .blockVision(Blocks::never)
            .strength(0.5F)
            .luminance((bs) -> 15)
            .mapColor(MapColor.MAGENTA)
    );
  }

  @Override
  public VoxelShape getCameraCollisionShape(
      BlockState blockState,
      BlockView blockGetter,
      BlockPos blockPos,
      ShapeContext collisionContext
  ) {
    return this.getCollisionShape(blockState, blockGetter, blockPos, collisionContext);
  }
}

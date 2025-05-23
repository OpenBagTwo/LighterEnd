package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.HangingSignBlock;
import net.minecraft.block.SignBlock;
import net.minecraft.block.WallHangingSignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.util.math.BlockPos;

public class Signs {

  public static class LighterEndStandingSignBlock extends SignBlock {

    public static final MapCodec<SignBlock> CODEC = createCodec(LighterEndStandingSignBlock::new);

    public LighterEndStandingSignBlock(Settings properties) {
      super(
          WoodType.CHERRY,
          properties
              .solid()
              .instrument(NoteBlockInstrument.BASS)
              .noCollision()
              .strength(1.0F)
              .burnable()
      );
    }

    @Override
    public MapCodec<SignBlock> getCodec() {
      return CODEC;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
      return new LighterEndSignBlockEntity(pos, state);
    }
  }

  public static class LighterEndWallSignBlock extends WallSignBlock {

    public static final MapCodec<WallSignBlock> CODEC = createCodec(LighterEndWallSignBlock::new);

    public LighterEndWallSignBlock(Settings properties) {
      super(
          WoodType.CHERRY,
          properties
              .solid()
              .instrument(NoteBlockInstrument.BASS)
              .noCollision()
              .strength(1.0F)
              .burnable()
      );
    }

    @Override
    public MapCodec<WallSignBlock> getCodec() {
      return CODEC;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
      return new LighterEndSignBlockEntity(pos, state);
    }
  }

  public static class LighterEndCeilingHangingSignBlock extends HangingSignBlock {

    public static final MapCodec<HangingSignBlock> CODEC = createCodec(
        LighterEndCeilingHangingSignBlock::new);

    public LighterEndCeilingHangingSignBlock(Settings properties) {
      super(
          WoodType.CHERRY,
          properties
              .solid()
              .instrument(NoteBlockInstrument.BASS)
              .noCollision()
              .strength(1.0F)
              .burnable()
      );
    }

    @Override
    public MapCodec<HangingSignBlock> getCodec() {
      return CODEC;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
      return new LighterEndHangingSignBlockEntity(pos, state);
    }
  }

  public static class LighterEndWallHangingSignBlock extends WallHangingSignBlock {

    public static final MapCodec<WallHangingSignBlock> CODEC = createCodec(
        LighterEndWallHangingSignBlock::new);

    public LighterEndWallHangingSignBlock(Settings properties) {
      super(
          WoodType.CHERRY,
          properties
              .solid()
              .instrument(NoteBlockInstrument.BASS)
              .noCollision()
              .strength(1.0F)
              .burnable()
      );
    }

    @Override
    public MapCodec<WallHangingSignBlock> getCodec() {
      return CODEC;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
      return new LighterEndHangingSignBlockEntity(pos, state);
    }
  }

  public static class LighterEndSignBlockEntity extends SignBlockEntity {

    public LighterEndSignBlockEntity(BlockPos pos, BlockState state) {
      super(LighterEndBlockEntities.SIGN, pos, state);
    }
  }

  public static class LighterEndHangingSignBlockEntity extends SignBlockEntity {

    public LighterEndHangingSignBlockEntity(BlockPos pos, BlockState state) {
      super(LighterEndBlockEntities.HANGING_SIGN, pos, state);
    }
  }
}

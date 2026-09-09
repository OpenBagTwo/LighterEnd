package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;

public class Signs {

  public static class LighterEndStandingSignBlock extends StandingSignBlock {

    public LighterEndStandingSignBlock(WoodType woodType, Properties properties) {
      super(
          woodType,
          properties
              .forceSolidOn()
              .instrument(NoteBlockInstrument.BASS)
              .noCollision()
              .strength(1.0F)
              .ignitedByLava()
      );
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new LighterEndSignBlockEntity(pos, state);
    }
  }

  public static class LighterEndWallSignBlock extends WallSignBlock {

    public LighterEndWallSignBlock(WoodType woodType, Properties properties) {
      super(
          woodType,
          properties
              .forceSolidOn()
              .instrument(NoteBlockInstrument.BASS)
              .noCollision()
              .strength(1.0F)
              .ignitedByLava()
      );
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new LighterEndSignBlockEntity(pos, state);
    }
  }

  public static class LighterEndCeilingHangingSignBlock extends CeilingHangingSignBlock {

    public LighterEndCeilingHangingSignBlock(WoodType woodType, Properties properties) {
      super(
          woodType,
          properties
              .forceSolidOn()
              .instrument(NoteBlockInstrument.BASS)
              .noCollision()
              .strength(1.0F)
              .ignitedByLava()
      );
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new LighterEndHangingSignBlockEntity(pos, state);
    }
  }

  public static class LighterEndWallHangingSignBlock extends WallHangingSignBlock {

    public LighterEndWallHangingSignBlock(WoodType woodType, Properties properties) {
      super(
          woodType,
          properties
              .forceSolidOn()
              .instrument(NoteBlockInstrument.BASS)
              .noCollision()
              .strength(1.0F)
              .ignitedByLava()
      );
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new LighterEndHangingSignBlockEntity(pos, state);
    }
  }

  public static class LighterEndSignBlockEntity extends SignBlockEntity {

    public LighterEndSignBlockEntity(BlockPos pos, BlockState state) {
      super(LighterEndBlockEntities.SIGN, pos, state);
    }
  }

  public static class LighterEndHangingSignBlockEntity extends HangingSignBlockEntity {

    public LighterEndHangingSignBlockEntity(BlockPos pos, BlockState state) {
      super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
      return LighterEndBlockEntities.HANGING_SIGN;
    }

    @Override
    public boolean isValidBlockState(BlockState blockState) {
      return this.getType().isValid(blockState);
    }
  }
}

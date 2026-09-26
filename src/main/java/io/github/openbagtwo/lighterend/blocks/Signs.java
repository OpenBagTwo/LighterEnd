package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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

    public static final MapCodec<StandingSignBlock> CODEC =
        RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    WoodType.CODEC.fieldOf("wood_type")
                        .forGetter((obj) -> obj.type()),
                    propertiesCodec())
                .apply(instance, LighterEndStandingSignBlock::new));

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
    public MapCodec<StandingSignBlock> codec() {
      return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new LighterEndSignBlockEntity(pos, state);
    }
  }

  public static class LighterEndWallSignBlock extends WallSignBlock {

    public static final MapCodec<WallSignBlock> CODEC =
        RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    WoodType.CODEC.fieldOf("wood_type")
                        .forGetter((obj) -> obj.type()),
                    propertiesCodec())
                .apply(instance, LighterEndWallSignBlock::new));

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
    public MapCodec<WallSignBlock> codec() {
      return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new LighterEndSignBlockEntity(pos, state);
    }
  }

  public static class LighterEndCeilingHangingSignBlock extends CeilingHangingSignBlock {

    public static final MapCodec<CeilingHangingSignBlock> CODEC =
        RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    WoodType.CODEC.fieldOf("wood_type")
                        .forGetter((obj) -> obj.type()),
                    propertiesCodec())
                .apply(instance, LighterEndCeilingHangingSignBlock::new));

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
    public MapCodec<CeilingHangingSignBlock> codec() {
      return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new LighterEndHangingSignBlockEntity(pos, state);
    }
  }

  public static class LighterEndWallHangingSignBlock extends WallHangingSignBlock {

    public static final MapCodec<WallHangingSignBlock> CODEC =
        RecordCodecBuilder.mapCodec((instance) ->
            instance.group(
                    WoodType.CODEC.fieldOf("wood_type")
                        .forGetter((obj) -> obj.type()),
                    propertiesCodec())
                .apply(instance, LighterEndWallHangingSignBlock::new));

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
    public MapCodec<WallHangingSignBlock> codec() {
      return CODEC;
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

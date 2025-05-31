package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.world.LighterEndConfiguredFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.MossBlock;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class EndMoss extends MossBlock {
  /* One of the biggest differences between LighterEnd and BetterEnd in terms of design is that
     there will only be *one* EndTerrainBlock (onto which all LighterEnd plants can be planted).
     Different patternings / colorations / bonemealing outputs, if implemented, will be determined
     by the biome in which the blocks are placed.
   */

  private final RegistryKey<ConfiguredFeature<?, ?>> feature;

  public EndMoss(Settings settings) {
    super(
        LighterEndConfiguredFeatures.END_MOSS_PATCH_BONEMEAL,
        settings
            .mapColor(MapColor.CYAN)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresTool()
            .strength(1.5F, 4.5F)
            .sounds(BlockSoundGroup.NYLIUM)
            .burnable()
            .ticksRandomly()
    );
    this.feature = LighterEndConfiguredFeatures.END_MOSS_PATCH_BONEMEAL;
  }

  @Override
  protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    if (!stayAlive(state, world, pos)) {
      world.setBlockState(pos, Blocks.END_STONE.getDefaultState());
    }
  }

  private static boolean stayAlive(BlockState state, WorldView world, BlockPos pos) {
    BlockPos blockPos = pos.up();
    BlockState blockState = world.getBlockState(blockPos);

    int i = ChunkLightProvider.getRealisticOpacity(
        state,
        blockState,
        Direction.UP,
        blockState.getOpacity()
    );
    return i < 15;
  }
}

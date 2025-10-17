package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.MapColor;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class Needlegrass extends PlantBlock implements Fertilizable {

  public static final MapCodec<Needlegrass> CODEC = createCodec(Needlegrass::new);

  public Needlegrass(Settings settings) {
    super(
        settings
            .mapColor(MapColor.BLACK)
            .replaceable()
            .noCollision()
            .breakInstantly()
            .nonOpaque()
            .sounds(BlockSoundGroup.GRASS)
            .pistonBehavior(PistonBehavior.DESTROY)
            .offset(OffsetType.XZ)
            .burnable()
    );
  }

  @Override
  protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
    return floor.isIn(LighterEndTags.END_SOIL);
  }

  @Override
  protected MapCodec<? extends PlantBlock> getCodec() {
    return CODEC;
  }

  @Override
  public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
    return true;
  }

  @Override
  public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
    return true;
  }

  @Override
  public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
    dropStack(world, pos, new ItemStack(this));
  }

  @Override
  protected boolean canPathfindThrough(BlockState state, NavigationType nav) {
    return false;
  }

}

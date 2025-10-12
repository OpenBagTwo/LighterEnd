package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.utils.Flags;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class VentBubbleColumn extends Block implements FluidDrainable, FluidFillable {

  public VentBubbleColumn(Settings settings) {
    super(
        settings
            .mapColor(MapColor.WATER_BLUE)
            .replaceable()
            .noCollision()
            .dropsNothing()
            .pistonBehavior(PistonBehavior.DESTROY)
            .liquid()
            .sounds(BlockSoundGroup.INTENTIONALLY_EMPTY)
            .nonOpaque()
    );
  }

  @Override
  public ItemStack tryDrainFluid(
      @Nullable LivingEntity drainer,
      WorldAccess world,
      BlockPos pos,
      BlockState state
  ) {
    world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
    return new ItemStack(Items.WATER_BUCKET);
  }

  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.INVISIBLE;
  }

  @Override
  public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    BlockState blockState = world.getBlockState(pos.down());
    return blockState.isOf(this) || blockState.isOf(LighterEndBlocks.HYDROTHERMAL_VENT);
  }

  @Override
  public VoxelShape getOutlineShape(
      BlockState state,
      BlockView world,
      BlockPos pos,
      ShapeContext context
  ) {
    return VoxelShapes.empty();
  }

  @Override
  protected BlockState getStateForNeighborUpdate(
      BlockState state,
      WorldView world,
      ScheduledTickView tickView,
      BlockPos pos,
      Direction direction,
      BlockPos neighborPos,
      BlockState neighborState,
      Random random
  ) {
    if (!state.canPlaceAt(world, pos)) {
      return Blocks.WATER.getDefaultState();
    } else {
      BlockPos up = pos.up();
      if (world.getBlockState(up).isOf(Blocks.WATER)) {
        if (world instanceof ServerWorld serverWorld) {
          serverWorld.setBlockState(up, this.getDefaultState(), Flags.SILENT);
          serverWorld.createOrderedTick(up, this, 5);
        }
      }
    }
    return state;
  }

  @Environment(EnvType.CLIENT)
  public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    if (random.nextInt(4) == 0) {
      double px = pos.getX() + random.nextDouble();
      double py = pos.getY() + random.nextDouble();
      double pz = pos.getZ() + random.nextDouble();
      world.addImportantParticleClient(ParticleTypes.BUBBLE_COLUMN_UP, px, py, pz, 0, 0.04, 0);
    }
    if (random.nextInt(200) == 0) {
      world.playSoundClient(
          pos.getX(),
          pos.getY(),
          pos.getZ(),
          SoundEvents.BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT,
          SoundCategory.BLOCKS,
          0.2F + random.nextFloat() * 0.2F,
          0.9F + random.nextFloat() * 0.15F,
          false
      );
    }
  }

  //  @Environment(EnvType.CLIENT)
  @Override
  protected void onEntityCollision(
      BlockState state,
      World world,
      BlockPos pos,
      Entity entity,
      EntityCollisionHandler handler,
      boolean bl
  ) {
    BlockState blockState = world.getBlockState(pos.up());
    if (blockState.isAir()) {
      entity.onBubbleColumnSurfaceCollision(false, pos.up());
      if (!world.isClient()) {
        ServerWorld serverWorld = (ServerWorld) world;

        for (int i = 0; i < 2; ++i) {
          serverWorld.spawnParticles(
              ParticleTypes.SPLASH,
              (double) pos.getX() + world.random.nextDouble(),
              pos.getY() + 1,
              (double) pos.getZ() + world.random.nextDouble(),
              1,
              0.0D,
              0.0D,
              0.0D,
              1.0D
          );
          serverWorld.spawnParticles(
              ParticleTypes.BUBBLE,
              (double) pos.getX() + world.random.nextDouble(),
              pos.getY() + 1,
              (double) pos.getZ() + world.random.nextDouble(),
              1,
              0.0D,
              0.01D,
              0.0D,
              0.2D
          );
        }
      }
    } else {
      entity.onBubbleColumnCollision(false);
    }
  }

  @Override
  public boolean canFillWithFluid(
      @Nullable LivingEntity filler,
      BlockView world,
      BlockPos pos,
      BlockState state,
      Fluid fluid
  ) {
    return false;
  }

  @Override
  public boolean tryFillWithFluid(
      WorldAccess world,
      BlockPos pos,
      BlockState state,
      FluidState fluidState
  ) {
    return false;
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return Fluids.WATER.getStill(false);
  }


  @Override
  public Optional<SoundEvent> getBucketFillSound() {
    return Fluids.WATER.getBucketFillSound();
  }
}

package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.Flags;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class VentBubbleColumn extends Block implements BucketPickup, LiquidBlockContainer {

  public VentBubbleColumn(Properties settings) {
    super(
        settings
            .mapColor(MapColor.WATER)
            .replaceable()
            .noCollision()
            .noLootTable()
            .pushReaction(PushReaction.DESTROY)
            .liquid()
            .sound(SoundType.EMPTY)
            .noOcclusion()
    );
  }

  @Override
  public ItemStack pickupBlock(
      @Nullable LivingEntity drainer,
      LevelAccessor world,
      BlockPos pos,
      BlockState state
  ) {
    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
    return new ItemStack(Items.WATER_BUCKET);
  }

  @Override
  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.INVISIBLE;
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
    BlockState blockState = world.getBlockState(pos.below());
    return blockState.is(this) || blockState.is(LighterEndBlocks.HYDROTHERMAL_VENT);
  }

  @Override
  public VoxelShape getShape(
      BlockState state,
      BlockGetter world,
      BlockPos pos,
      CollisionContext context
  ) {
    return Shapes.empty();
  }

  @Override
  protected BlockState updateShape(
      BlockState state,
      LevelReader world,
      ScheduledTickAccess tickView,
      BlockPos pos,
      Direction direction,
      BlockPos neighborPos,
      BlockState neighborState,
      RandomSource random
  ) {
    if (!state.canSurvive(world, pos)) {
      return Blocks.WATER.defaultBlockState();
    } else {
      BlockPos up = pos.above();
      if (world.getBlockState(up).is(Blocks.WATER)) {
        if (world instanceof ServerLevel serverWorld) {
          serverWorld.setBlock(up, this.defaultBlockState(), Flags.SILENT);
          serverWorld.createTick(up, this, 5);
        }
      }
    }
    return state;
  }

  public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
    if (random.nextInt(4) == 0) {
      double px = pos.getX() + random.nextDouble();
      double py = pos.getY() + random.nextDouble();
      double pz = pos.getZ() + random.nextDouble();
      world.addAlwaysVisibleParticle(ParticleTypes.BUBBLE_COLUMN_UP, px, py, pz, 0, 0.04, 0);
    }
    if (random.nextInt(200) == 0) {
      world.playLocalSound(
          pos.getX(),
          pos.getY(),
          pos.getZ(),
          SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT,
          SoundSource.BLOCKS,
          0.2F + random.nextFloat() * 0.2F,
          0.9F + random.nextFloat() * 0.15F,
          false
      );
    }
  }

  @Override
  protected void entityInside(
      BlockState state,
      Level world,
      BlockPos pos,
      Entity entity,
      InsideBlockEffectApplier handler,
      boolean bl
  ) {
    if (entity.getType().is(LighterEndTags.IGNORES_GEYSER_BUBBLES)) {
      return;
    }
    BlockState blockState = world.getBlockState(pos.above());
    if (blockState.isAir()) {
      entity.onAboveBubbleColumn(false, pos.above());
      if (!world.isClientSide()) {
        ServerLevel serverWorld = (ServerLevel) world;

        for (int i = 0; i < 2; ++i) {
          serverWorld.sendParticles(
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
          serverWorld.sendParticles(
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
      entity.onInsideBubbleColumn(false);
    }
  }

  @Override
  protected ItemStack getCloneItemStack(
      LevelReader world,
      BlockPos pos,
      BlockState state,
      boolean includeData
  ) {
    return new ItemStack(Blocks.WATER);
  }

  @Override
  public boolean canPlaceLiquid(
      @Nullable LivingEntity filler,
      BlockGetter world,
      BlockPos pos,
      BlockState state,
      Fluid fluid
  ) {
    return false;
  }

  @Override
  public boolean placeLiquid(
      LevelAccessor world,
      BlockPos pos,
      BlockState state,
      FluidState fluidState
  ) {
    return false;
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return Fluids.WATER.getSource(false);
  }


  @Override
  public Optional<SoundEvent> getPickupSound() {
    return Fluids.WATER.getPickupSound();
  }
}

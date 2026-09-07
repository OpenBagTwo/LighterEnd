package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;

public class Needlegrass extends VegetationBlock implements BonemealableBlock {

  public Needlegrass(Properties settings) {
    super(
        settings
            .mapColor(MapColor.COLOR_BLACK)
            .replaceable()
            .noCollision()
            .instabreak()
            .noOcclusion()
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.POPPED)
            .offsetType(OffsetType.XZ)
            .ignitedByLava()
    );
  }

  @Override
  protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
    return floor.is(LighterEndTags.END_SOIL);
  }

  @Override
  public boolean isValidBonemealTarget(
      LevelReader world,
      BlockPos pos,
      BlockState state,
      BonemealSource source
  ) {
    return true;
  }

  @Override
  public boolean isBonemealSuccess(
      Level world,
      RandomSource random,
      BlockPos pos,
      BlockState state,
      BonemealSource source
  ) {
    return true;
  }

  @Override
  public void performBonemeal(
      ServerLevel world,
      RandomSource random,
      BlockPos pos,
      BlockState state,
      BonemealSource source
  ) {
    popResource(world, pos, new ItemStack(this));
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
    if (
        entity instanceof LivingEntity
            && !entity.is(LighterEndTags.IMMUNE_TO_NEEDLEGRASS)
    ) {
      entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75, 0.8F));
      if (world instanceof ServerLevel serverWorld) {
        Vec3 vec3d = entity.isClientAuthoritative() ? entity.getKnownMovement()
            : entity.oldPosition().subtract(entity.position());
        if (vec3d.horizontalDistanceSqr() > 0.0) {
          if (Math.abs(vec3d.x()) >= 0.003F || Math.abs(vec3d.z()) >= 0.003F) {
            entity.hurtServer(serverWorld, world.damageSources().sweetBerryBush(), 1.0F);
          }
        }
      }
    }
  }

  @Override
  protected boolean isPathfindable(BlockState state, PathComputationType nav) {
    return false;
  }

}

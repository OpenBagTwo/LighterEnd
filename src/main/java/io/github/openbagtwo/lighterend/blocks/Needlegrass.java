package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.MapColor;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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
  protected void onEntityCollision(
      BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler
  ) {
    if (
        entity instanceof LivingEntity
            && !entity.getType().isIn(LighterEndTags.IMMUNE_TO_NEEDLEGRASS)
    ) {
      entity.slowMovement(state, new Vec3d(0.8F, 0.75, 0.8F));
      if (world instanceof ServerWorld serverWorld) {
        Vec3d vec3d = entity.isControlledByPlayer() ? entity.getMovement()
            : entity.getLastRenderPos().subtract(entity.getPos());
        if (vec3d.horizontalLengthSquared() > 0.0) {
          if (Math.abs(vec3d.getX()) >= 0.003F || Math.abs(vec3d.getZ()) >= 0.003F) {
            entity.damage(serverWorld, world.getDamageSources().sweetBerryBush(), 1.0F);
          }
        }
      }
    }
  }

  @Override
  protected boolean canPathfindThrough(BlockState state, NavigationType nav) {
    return false;
  }

}

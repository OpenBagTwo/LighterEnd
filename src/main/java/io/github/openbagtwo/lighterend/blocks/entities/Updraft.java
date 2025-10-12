package io.github.openbagtwo.lighterend.blocks.entities;

import io.github.openbagtwo.lighterend.blocks.HydrothermalVent;
import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndParticles;
import io.github.openbagtwo.lighterend.utils.GlobalState;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Updraft extends BlockEntity {

  private final static Vec3d POSITIVE_Y = new Vec3d(0.0f, 1.0f, 0.0f);

  public Updraft(BlockPos blockPos, BlockState blockState) {
    super(LighterEndBlockEntities.UPDRAFT, blockPos, blockState);
  }

  public static <T extends BlockEntity> void tick(
      World level,
      BlockPos worldPosition,
      BlockState state,
      T uncastedEntity
  ) {
    if (level != null && uncastedEntity instanceof Updraft updraft && state.isOf(
        LighterEndBlocks.HYDROTHERMAL_VENT)) {
      if (level.isClient()) {
        clientTick(level, worldPosition, state, updraft);
      }
      serverTick(level, worldPosition, state, updraft);
    }
  }

  private static void clientTick(
      World level,
      BlockPos worldPosition,
      BlockState state,
      Updraft updraft
  ) {
    boolean active = state.get(HydrothermalVent.ACTIVATED);
    if (active && level.random.nextInt(20) == 0 && state.get(HydrothermalVent.WATERLOGGED)) {
      double x = worldPosition.getX() + level.random.nextDouble();
      double y = worldPosition.getY() + 0.9 + level.random.nextDouble() * 0.3;
      double z = worldPosition.getZ() + level.random.nextDouble();
      level.addParticleClient(LighterEndParticles.GEYSER, x, y, z, 0, 0, 0);
    }
  }

  private static void serverTick(
      World level,
      BlockPos worldPosition,
      BlockState state,
      Updraft updraft
  ) {
    final Mutable POS = GlobalState.stateForThread().POS;
    boolean active = state.get(HydrothermalVent.ACTIVATED);
    POS.set(worldPosition).move(Direction.UP);
    int height = active ? 85 : 25;
    Box box = new Box(POS.add(-1, 0, -1).toCenterPos(), POS.add(1, height, 1).toCenterPos());
    List<LivingEntity> entities = level.getNonSpectatingEntities(LivingEntity.class, box);
    if (entities.size() > 0) {
      while (POS.getY() < box.maxY) {
        BlockState blockState = level.getBlockState(POS);
        if (blockState.isOpaqueFullCube()) {
          break;
        }
        if (blockState.isAir()) {
          double mult = active ? 3.0 : 5.0;
          float force = (float) ((1.0 - (POS.getY() / box.maxY)) / mult);
          entities.stream()
              .filter(entity -> (int) entity.getY() == POS.getY() && hasElytra(entity)
                  && entity
                  .isGliding())
              .forEach(entity -> entity.updateVelocity(force, POSITIVE_Y));
        }
        POS.move(Direction.UP);
      }
    }
  }

  private static boolean hasElytra(LivingEntity entity) {
    return entity.getEquippedStack(EquipmentSlot.CHEST).contains(DataComponentTypes.GLIDER);
  }
}

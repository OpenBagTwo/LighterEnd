package io.github.openbagtwo.lighterend.blocks.entities;

import io.github.openbagtwo.lighterend.blocks.HydrothermalVent;
import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndParticles;
import io.github.openbagtwo.lighterend.utils.GlobalState;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Updraft extends BlockEntity {

  private final static Vec3 POSITIVE_Y = new Vec3(0.0f, 1.0f, 0.0f);

  public Updraft(BlockPos blockPos, BlockState blockState) {
    super(LighterEndBlockEntities.UPDRAFT, blockPos, blockState);
  }

  public static <T extends BlockEntity> void tick(
      Level level,
      BlockPos worldPosition,
      BlockState state,
      T uncastedEntity
  ) {

    if (
        level != null
            && uncastedEntity instanceof Updraft updraft
            && state.is(LighterEndBlocks.HYDROTHERMAL_VENT)
    ) {
      particleTick(level, worldPosition, state, updraft);
      serverTick(level, worldPosition, state, updraft);
    }
  }

  private static void particleTick(
      Level level,
      BlockPos worldPosition,
      BlockState state,
      Updraft updraft
  ) {
    boolean active = state.getValue(HydrothermalVent.ACTIVATED);
    if (active && level.random.nextInt(20) == 0) {
      double x = worldPosition.getX() + 0.5 * level.random.nextDouble();
      double y = worldPosition.getY() + 0.9 + level.random.nextDouble() * 0.3;
      double z = worldPosition.getZ() + 0.5 * level.random.nextDouble();
      level.addParticle(LighterEndParticles.GEYSER, x, y, z, 0, 0.125, 0);
    }
  }

  private static void serverTick(
      Level level,
      BlockPos worldPosition,
      BlockState state,
      Updraft updraft
  ) {
    final MutableBlockPos POS = GlobalState.stateForThread().POS;
    boolean active = state.getValue(HydrothermalVent.ACTIVATED);
    POS.set(worldPosition).move(Direction.UP);
    int height = active ? 85 : 25;
    AABB box = new AABB(POS.offset(-1, 0, -1).getCenter(), POS.offset(1, height, 1).getCenter());
    List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, box);
    if (entities.size() > 0) {
      while (POS.getY() < box.maxY) {
        BlockState blockState = level.getBlockState(POS);
        if (blockState.isSolidRender()) {
          break;
        }
        if (blockState.isAir()) {
          double mult = active ? 3.0 : 5.0;
          float force = (float) ((1.0 - (POS.getY() / box.maxY)) / mult);
          entities.stream()
              .filter(entity -> (int) entity.getY() == POS.getY() && hasElytra(entity)
                  && entity
                  .isFallFlying())
              .forEach(entity -> entity.moveRelative(force, POSITIVE_Y));
        }
        POS.move(Direction.UP);
      }
    }
  }

  private static boolean hasElytra(LivingEntity entity) {
    return entity.getItemBySlot(EquipmentSlot.CHEST).has(DataComponents.GLIDER);
  }
}

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
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.TintedParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class Murkweed extends PlantBlock implements Fertilizable {

  public static final MapCodec<Murkweed> CODEC = createCodec(Murkweed::new);

  public Murkweed(Settings settings) {
    super(
        settings
            .mapColor(MapColor.BLACK)
            .replaceable()
            .noCollision()
            .breakInstantly()
            .nonOpaque()
            .sounds(BlockSoundGroup.GRASS)
            .pistonBehavior(PistonBehavior.DESTROY)
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
  public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    double x = pos.getX() + random.nextDouble();
    double y = pos.getY() + random.nextDouble() * 0.5 + 0.5;
    double z = pos.getZ() + random.nextDouble();
    double v = random.nextDouble() * 0.1;
    world.addParticleClient(
        TintedParticleEffect.create(ParticleTypes.ENTITY_EFFECT, 0xFFFFFFFF),
        x,
        y,
        z,
        v,
        v,
        v
    );
  }

  @Override
  protected void onEntityCollision(
      BlockState state,
      World world,
      BlockPos pos,
      Entity entity,
      EntityCollisionHandler handler,
      boolean bl
  ) {
    if (
        entity instanceof LivingEntity livingEntity
            && !entity.getType().isIn(LighterEndTags.IMMUNE_TO_MURKWEED)
    ) {
      if (!livingEntity.hasStatusEffect(StatusEffects.BLINDNESS)) {
        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50));
      }
    }
  }
}

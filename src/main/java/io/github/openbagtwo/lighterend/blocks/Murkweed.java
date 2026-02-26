package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class Murkweed extends VegetationBlock {

  public static final MapCodec<Murkweed> CODEC = simpleCodec(Murkweed::new);

  public Murkweed(Properties settings) {
    super(
        settings
            .mapColor(MapColor.COLOR_BLACK)
            .replaceable()
            .noCollision()
            .instabreak()
            .noOcclusion()
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.DESTROY)
            .ignitedByLava()
    );
  }

  @Override
  protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
    return floor.is(LighterEndTags.END_SOIL);
  }

  @Override
  protected MapCodec<? extends VegetationBlock> codec() {
    return CODEC;
  }

  @Override
  public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
    double x = pos.getX() + random.nextDouble();
    double y = pos.getY() + random.nextDouble() * 0.5 + 0.5;
    double z = pos.getZ() + random.nextDouble();
    double v = random.nextDouble() * 0.1;
    world.addParticle(
        ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0xFFFFFFFF),
        x,
        y,
        z,
        v,
        v,
        v
    );
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
        entity instanceof LivingEntity livingEntity
            && !entity.getType().is(LighterEndTags.IMMUNE_TO_MURKWEED)
    ) {
      if (!livingEntity.hasEffect(MobEffects.BLINDNESS)) {
        livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 50));
      }
    }
  }
}

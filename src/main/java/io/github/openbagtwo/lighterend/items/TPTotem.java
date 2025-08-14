package io.github.openbagtwo.lighterend.items;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.openbagtwo.lighterend.registries.LighterEndData;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import java.util.List;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DeathProtectionComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.item.consume.ClearAllEffectsConsumeEffect;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class TPTotem extends Item {

  public TPTotem(Settings settings) {
    super(
        settings
            .maxCount(1)
            .rarity(Rarity.UNCOMMON)
            .component(DataComponentTypes.DEATH_PROTECTION, TOTEM_EFFECTS)
    );
  }

  public static final DeathProtectionComponent TOTEM_EFFECTS = new DeathProtectionComponent(
      List.of(
          new ClearAllEffectsConsumeEffect(),
          new ApplyEffectsConsumeEffect(
              List.of(
                  new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1),
                  new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1),
                  new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0)
              )
          ),
          new FixedTeleport(new BlockPos(3000, 100, 8))
      )
  );

  public record FixedTeleport(BlockPos pos) implements ConsumeEffect {

    private static final float DIAMETER = 4F;
    public static final MapCodec<FixedTeleport> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
                BlockPos.CODEC.fieldOf("pos").forGetter(
                    FixedTeleport::pos))
            .apply(instance, FixedTeleport::new)
    );
    public static final PacketCodec<RegistryByteBuf, FixedTeleport> PACKET_CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, FixedTeleport::pos,
        FixedTeleport::new
    );

    @Override
    public ConsumeEffect.Type<FixedTeleport> getType() {
      return LighterEndData.TOTEM_TELEPORT;
    }

    @Override
    public boolean onConsume(World world, ItemStack stack, LivingEntity user) {
      boolean bl = false;
      world.getWorldChunk(this.pos).setLoadedToWorld(true);
      for (int i = 0; i < 16; i++) {
        double d = this.pos.getX() + (user.getRandom().nextDouble() - 0.5) * DIAMETER;
        double e = MathHelper.clamp(
            this.pos.getY() + (user.getRandom().nextDouble() - 0.5) * DIAMETER,
            world.getBottomY(),
            (world.getBottomY() + ((ServerWorld) world).getLogicalHeight() - 1)
        );
        double f = this.pos.getZ() + (user.getRandom().nextDouble() - 0.5) * DIAMETER;
        if (user.hasVehicle()) {
          user.stopRiding();
        }

        Vec3d vec3d = user.getPos();
        if (user.teleport(d, e, f, true)) {
          world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(user));
          SoundCategory soundCategory;
          SoundEvent soundEvent;
          if (user instanceof FoxEntity) {
            soundEvent = SoundEvents.ENTITY_FOX_TELEPORT;
            soundCategory = SoundCategory.NEUTRAL;
          } else {
            soundEvent = LighterEndSounds.TOTEM_TELEPORT;
            soundCategory = SoundCategory.PLAYERS;
          }

          world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent, soundCategory);
          user.onLanding();
          bl = true;
          break;
        } else {
          System.out.println("TP Failed :(");
        }
      }

      if (bl && user instanceof PlayerEntity playerEntity) {
        playerEntity.clearCurrentExplosion();
      }

      return bl;
    }
  }
}

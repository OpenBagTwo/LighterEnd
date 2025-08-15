package io.github.openbagtwo.lighterend.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.Obelisk;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndData;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DeathProtectionComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.item.consume.ClearAllEffectsConsumeEffect;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
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

  @Override
  public boolean hasGlint(ItemStack stack) {
    return stack.contains(LighterEndData.TOTEM_TARGET) || super.hasGlint(stack);
  }

  @Override
  public ActionResult useOnBlock(ItemUsageContext context) {
    BlockPos blockPos = context.getBlockPos();
    World world = context.getWorld();
    BlockState state = world.getBlockState(blockPos);

    if (!state.isOf(LighterEndBlocks.OBELISK)) {
      return super.useOnBlock(context);
    }

    switch (state.get(Obelisk.SHAPE, null)) {
      case Obelisk.Shape.TOP:
        blockPos = blockPos.down();
        break;
      case Obelisk.Shape.BOTTOM:
        blockPos = blockPos.up();
        break;
      case Obelisk.Shape.MIDDLE:
        break;
      default:
        return super.useOnBlock(context);
    }

    world.playSound(null, blockPos, LighterEndSounds.TP_TOTEM_TARGET_SET, SoundCategory.PLAYERS,
        1.0F, 1.0F);
    PlayerEntity playerEntity = context.getPlayer();
    ItemStack itemStack = context.getStack();
    Target target = new Target(
        Optional.of(GlobalPos.create(world.getRegistryKey(), blockPos))
    );
    if (!playerEntity.isInCreativeMode() && itemStack.getCount() == 1) {
      itemStack.set(LighterEndData.TOTEM_TARGET, target);
    } else {
      ItemStack newStack = itemStack.copyComponentsToNewStack(
          LighterEndItems.TOTEM_OF_TELEPORTATION, 1);
      itemStack.decrementUnlessCreative(1, playerEntity);
      newStack.set(LighterEndData.TOTEM_TARGET, target);
      if (!playerEntity.getInventory().insertStack(newStack)) {
        playerEntity.dropItem(newStack, false);
      }
    }
    return ActionResult.SUCCESS;

  }


  public static final DeathProtectionComponent TOTEM_EFFECTS = new DeathProtectionComponent(
      List.of(
          new ClearAllEffectsConsumeEffect(),
          new ApplyEffectsConsumeEffect(
              List.of(
                  new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1),
                  new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1)
              )
          ),
          new FixedTeleport()
      )
  );

  public record FixedTeleport(float diameter) implements ConsumeEffect {

    private static final float DEFAULT_DIAMETER = 0F;
    public static final MapCodec<FixedTeleport> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
            Codecs.POSITIVE_FLOAT.optionalFieldOf("diameter", DEFAULT_DIAMETER)
                .forGetter(FixedTeleport::diameter)
        ).apply(instance, FixedTeleport::new)
    );
    public static final PacketCodec<RegistryByteBuf, FixedTeleport> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.FLOAT, FixedTeleport::diameter,
        FixedTeleport::new
    );

    public FixedTeleport() {
      this(DEFAULT_DIAMETER);
    }

    @Override
    public ConsumeEffect.Type<FixedTeleport> getType() {
      return LighterEndData.TOTEM_TELEPORT;
    }

    @Override
    public boolean onConsume(World world, ItemStack stack, LivingEntity user) {

      Target target = stack.getComponents().get(LighterEndData.TOTEM_TARGET);
      if (target == null) {
        return false;
      }
      GlobalPos tpTarget = target.target().orElse(null);
      if (tpTarget == null) {
        return false;
      }

      ServerWorld destinationDimension = world.getServer().getWorld(tpTarget.dimension());
      if (destinationDimension == null) {
        return false;
      }

      destinationDimension.getWorldChunk(tpTarget.pos()).setLoadedToWorld(true);  // Unnecessary?

      user.teleportTo(
          new TeleportTarget(
              destinationDimension,
              tpTarget.pos().toCenterPos(),
              Vec3d.ZERO,
              user.getYaw(),
              user.getPitch(),
              TeleportTarget.NO_OP)
      );

      world.emitGameEvent(GameEvent.TELEPORT, tpTarget.pos(), GameEvent.Emitter.of(user));

      if (user instanceof FoxEntity) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(),
            SoundEvents.ENTITY_FOX_TELEPORT, SoundCategory.NEUTRAL);
      } else if (user instanceof PlayerEntity) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(),
            LighterEndSounds.TOTEM_TELEPORT, SoundCategory.PLAYERS);
      }

      user.onLanding();

      if (user instanceof PlayerEntity player) {
        player.clearCurrentExplosion();
      }

      return true;
    }
  }

  public record Target(Optional<GlobalPos> target) implements TooltipAppender {

    public static final Codec<Target> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
                GlobalPos.CODEC.optionalFieldOf("target").forGetter(
                    Target::target)
            )
            .apply(instance, Target::new)
    );
    public static final PacketCodec<ByteBuf, Target> PACKET_CODEC = PacketCodec.tuple(
        GlobalPos.PACKET_CODEC.collect(PacketCodecs::optional),
        Target::target,
        Target::new
    );

    @Override
    public void appendTooltip(
        Item.TooltipContext context,
        Consumer<Text> textConsumer,
        TooltipType type,
        ComponentsAccess components
    ) {

      if (this.target.isEmpty()) {
        textConsumer.accept(
            Text.translatable(
                "item." + LighterEnd.MOD_ID + ".totem_of_teleportation.no_target"
            ).formatted(Formatting.GRAY)
        );
      } else {
        textConsumer.accept(
            Text.translatable(
                "item." + LighterEnd.MOD_ID + ".totem_of_teleportation.targeting",
                this.target.get().pos().toShortString(),
                Text.translatable(this.target.get().dimension().getValue().toTranslationKey())
            ).formatted(Formatting.GRAY, Formatting.ITALIC)
        );
      }
    }
  }
}

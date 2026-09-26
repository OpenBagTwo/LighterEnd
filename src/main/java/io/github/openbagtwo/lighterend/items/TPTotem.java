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
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

public class TPTotem extends Item {

  public TPTotem(Properties settings) {
    super(
        settings
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON)
            .component(DataComponents.DEATH_PROTECTION, TOTEM_EFFECTS)
    );
  }

  @Override
  public boolean isFoil(ItemStack stack) {
    return stack.has(LighterEndData.TOTEM_TARGET) || super.isFoil(stack);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    BlockPos blockPos = context.getClickedPos();
    Level world = context.getLevel();
    BlockState state = world.getBlockState(blockPos);

    if (!state.is(LighterEndBlocks.OBELISK)) {
      return super.useOn(context);
    }

    switch (state.getValueOrElse(Obelisk.SHAPE, null)) {
      case Obelisk.Shape.TOP:
        blockPos = blockPos.below();
        break;
      case Obelisk.Shape.BOTTOM:
        blockPos = blockPos.above();
        break;
      case Obelisk.Shape.MIDDLE:
        break;
      default:
        return super.useOn(context);
    }

    world.playSound(null, blockPos, LighterEndSounds.TP_TOTEM_TARGET_SET, SoundSource.PLAYERS,
        1.0F, 1.0F);
    Player playerEntity = context.getPlayer();
    ItemStack itemStack = context.getItemInHand();
    Target target = new Target(
        Optional.of(GlobalPos.of(world.dimension(), blockPos))
    );
    if (!playerEntity.hasInfiniteMaterials() && itemStack.getCount() == 1) {
      itemStack.set(LighterEndData.TOTEM_TARGET, target);
    } else {
      ItemStack newStack = itemStack.transmuteCopy(
          LighterEndItems.TOTEM_OF_TELEPORTATION, 1);
      itemStack.consume(1, playerEntity);
      newStack.set(LighterEndData.TOTEM_TARGET, target);
      if (!playerEntity.getInventory().add(newStack)) {
        playerEntity.drop(newStack, false);
      }
    }
    return InteractionResult.SUCCESS;

  }


  public static final DeathProtection TOTEM_EFFECTS = new DeathProtection(
      List.of(
          new ClearAllStatusEffectsConsumeEffect(),
          new ApplyStatusEffectsConsumeEffect(
              List.of(
                  new MobEffectInstance(MobEffects.REGENERATION, 900, 1)
              )
          ),
          new FixedTeleport()
      )
  );

  public record FixedTeleport(float diameter) implements ConsumeEffect {

    private static final float DEFAULT_DIAMETER = 0F;
    public static final MapCodec<FixedTeleport> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
            ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("diameter", DEFAULT_DIAMETER)
                .forGetter(FixedTeleport::diameter)
        ).apply(instance, FixedTeleport::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, FixedTeleport> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT,
        FixedTeleport::diameter,
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
    public boolean apply(Level world, ItemStack stack, LivingEntity user) {

      Target target = stack.getComponents().get(LighterEndData.TOTEM_TARGET);
      if (target == null) {
        return false;
      }
      GlobalPos tpTarget = target.target().orElse(null);
      if (tpTarget == null) {
        return false;
      }

      ServerLevel destinationDimension = world.getServer().getLevel(tpTarget.dimension());
      if (destinationDimension == null) {
        return false;
      }

      destinationDimension.getChunkAt(tpTarget.pos()).setLoaded(true);  // Unnecessary?

      user.teleport(
          new TeleportTransition(
              destinationDimension,
              tpTarget.pos().getCenter(),
              Vec3.ZERO,
              user.getYRot(),
              user.getXRot(),
              TeleportTransition.DO_NOTHING)
      );

      world.gameEvent(GameEvent.TELEPORT, tpTarget.pos(), GameEvent.Context.of(user));

      if (user instanceof Fox) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(),
            SoundEvents.FOX_TELEPORT, SoundSource.NEUTRAL);
      } else if (user instanceof Player) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(),
            LighterEndSounds.TOTEM_TELEPORT, SoundSource.PLAYERS);
      }

      user.resetFallDistance();

      if (user instanceof Player player) {
        player.resetCurrentImpulseContext();
      }

      return true;
    }
  }

  public record Target(Optional<GlobalPos> target) implements TooltipProvider {

    public static final Codec<Target> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
                GlobalPos.CODEC.optionalFieldOf("target").forGetter(
                    Target::target)
            )
            .apply(instance, Target::new)
    );
    public static final StreamCodec<ByteBuf, Target> PACKET_CODEC = StreamCodec.composite(
        GlobalPos.STREAM_CODEC.apply(ByteBufCodecs::optional),
        Target::target,
        Target::new
    );

    @Override
    public void addToTooltip(
        Item.TooltipContext context,
        Consumer<Component> textConsumer,
        TooltipFlag type,
        DataComponentGetter components
    ) {

      if (this.target.isEmpty()) {
        textConsumer.accept(
            Component.translatable(
                "item." + LighterEnd.MOD_ID + ".totem_of_teleportation.no_target"
            ).withStyle(ChatFormatting.GRAY)
        );
      } else {
        textConsumer.accept(
            Component.translatable(
                "item." + LighterEnd.MOD_ID + ".totem_of_teleportation.targeting",
                this.target.get().pos().toShortString(),
                Component.translatable(this.target.get().dimension().identifier().toLanguageKey())
            ).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)
        );
      }
    }
  }
}

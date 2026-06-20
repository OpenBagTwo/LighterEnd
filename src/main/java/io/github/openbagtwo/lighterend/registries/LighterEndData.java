package io.github.openbagtwo.lighterend.registries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.SilkMothNest;
import io.github.openbagtwo.lighterend.blocks.entities.SilkMothNestEntity;
import io.github.openbagtwo.lighterend.blocks.entities.SilkMothNestEntity.MothData;
import io.github.openbagtwo.lighterend.items.TPTotem;
import io.github.openbagtwo.lighterend.items.TPTotem.Target;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponentType.Builder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

public class LighterEndData {

  public static final DataComponentType<MothsComponent> MOTHS = registerDataComponent(
      "moths",
      builder -> builder.persistent(MothsComponent.CODEC)
          .networkSynchronized(MothsComponent.STREAM_CODEC)
          .cacheEncoding()
  );

  public static final DataComponentType<SilkLevelComponent> SILK_LEVEL = registerDataComponent(
      "silk_level",
      builder -> builder.persistent(SilkLevelComponent.CODEC)
          .networkSynchronized(SilkLevelComponent.PACKET_CODEC)
          .cacheEncoding()
  );

  public static final DataComponentType<Variant> VARIANT = registerDataComponent(
      "variant",
      builder -> builder.persistent(Variant.CODEC)
          .networkSynchronized(Variant.PACKET_CODEC)
          .cacheEncoding()
  );

  public record MothsComponent(List<MothData> moths) implements TooltipProvider {

    public static final Codec<MothsComponent> CODEC = MothData.LIST_CODEC.xmap(
        MothsComponent::new, MothsComponent::moths);
    public static final StreamCodec<RegistryFriendlyByteBuf, MothsComponent> STREAM_CODEC = MothData.STREAM_CODEC
        .apply(ByteBufCodecs.list())
        .map(MothsComponent::new, MothsComponent::moths);
    public static final MothsComponent DEFAULT = new MothsComponent(List.of());

    @Override
    public void addToTooltip(
        Item.TooltipContext context,
        Consumer<Component> textConsumer,
        TooltipFlag type,
        DataComponentGetter components
    ) {
      textConsumer.accept(
          Component.translatable(
              "container." + LighterEnd.MOD_ID + ".silk_moth_nest.moths",
              this.moths.size(),
              SilkMothNestEntity.MAX_MOTH_COUNT
          ).withStyle(ChatFormatting.GRAY)
      );
    }
  }

  public record SilkLevelComponent(int silk_level) implements TooltipProvider {

    public static final Codec<SilkLevelComponent> CODEC = Codec.INT.xmap(SilkLevelComponent::new,
        SilkLevelComponent::silk_level);
    public static final StreamCodec<ByteBuf, SilkLevelComponent> PACKET_CODEC = ByteBufCodecs.VAR_INT.map(
        SilkLevelComponent::new, SilkLevelComponent::silk_level);

    @Override
    public void addToTooltip(
        Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type,
        DataComponentGetter components) {
      textConsumer.accept(
          Component.translatable(
              "container." + LighterEnd.MOD_ID + ".silk_moth_nest.fullness",
              this.silk_level(),
              SilkMothNest.MAX_FULLNESS
          ).withStyle(ChatFormatting.GRAY)
      );
    }
  }

  public record Variant(int variant) {

    public static final Codec<Variant> CODEC = Codec.INT.xmap(Variant::new, Variant::variant);
    public static final StreamCodec<ByteBuf, Variant> PACKET_CODEC = ByteBufCodecs.VAR_INT.map(
        Variant::new, Variant::variant
    );
  }

  public static final ConsumeEffect.Type<TPTotem.FixedTeleport> TOTEM_TELEPORT = registerConsumeComponent(
      "totem_teleport", TPTotem.FixedTeleport.CODEC, TPTotem.FixedTeleport.STREAM_CODEC
  );

  public static final DataComponentType<Target> TOTEM_TARGET = registerDataComponent(
      "teleportation_target",
      builder -> builder
          .persistent(Target.CODEC)
          .networkSynchronized(Target.PACKET_CODEC)
          .cacheEncoding()
  );

  private static <T> DataComponentType<T> registerDataComponent(String name,
      UnaryOperator<Builder<T>> builderOperator) {
    return Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        LighterEnd.of(name),
        builderOperator.apply(DataComponentType.builder()).build()
    );
  }

  private static <T extends ConsumeEffect> ConsumeEffect.Type<T> registerConsumeComponent(
      String id,
      MapCodec<T> codec,
      StreamCodec<RegistryFriendlyByteBuf, T> packetCodec
  ) {
    return Registry.register(BuiltInRegistries.CONSUME_EFFECT_TYPE, id,
        new ConsumeEffect.Type<>(codec, packetCodec));
  }

  public static void initialize() {
  }
}

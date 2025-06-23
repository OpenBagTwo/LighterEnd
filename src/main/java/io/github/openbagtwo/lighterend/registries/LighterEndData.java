package io.github.openbagtwo.lighterend.registries;

import com.mojang.serialization.Codec;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.SilkMothNest;
import io.github.openbagtwo.lighterend.blocks.entities.SilkMothNestEntity;
import io.github.openbagtwo.lighterend.blocks.entities.SilkMothNestEntity.MothData;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.minecraft.component.ComponentType;
import net.minecraft.component.ComponentType.Builder;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class LighterEndData {

  public static final ComponentType<MothsComponent> MOTHS = registerDataComponent(
      "moths",
      builder -> builder.codec(MothsComponent.CODEC).packetCodec(MothsComponent.PACKET_CODEC)
          .cache()
  );

  public static final ComponentType<SilkLevelComponent> SILK_LEVEL = registerDataComponent(
      "silk_level",
      builder -> builder.codec(SilkLevelComponent.CODEC)
          .packetCodec(SilkLevelComponent.PACKET_CODEC)
          .cache()
  );

  public static final ComponentType<Variant> VARIANT = registerDataComponent(
      "variant",
      builder -> builder.codec(Variant.CODEC)
          .packetCodec(Variant.PACKET_CODEC)
          .cache()
  );

  public record MothsComponent(List<MothData> moths) implements TooltipAppender {

    public static final Codec<MothsComponent> CODEC = MothData.LIST_CODEC.xmap(
        MothsComponent::new, MothsComponent::moths);
    public static final PacketCodec<ByteBuf, MothsComponent> PACKET_CODEC = MothData.PACKET_CODEC
        .collect(PacketCodecs.toList())
        .xmap(
            MothsComponent::new, MothsComponent::moths);
    public static final MothsComponent DEFAULT = new MothsComponent(List.of());

    @Override
    public void appendTooltip(
        Item.TooltipContext context,
        Consumer<Text> textConsumer,
        TooltipType type,
        ComponentsAccess components
    ) {
      textConsumer.accept(
          Text.translatable(
              "container." + LighterEnd.MOD_ID + ".silk_moth_nest.moths",
              this.moths.size(),
              SilkMothNestEntity.MAX_MOTH_COUNT
          ).formatted(Formatting.GRAY)
      );
    }
  }

  public record SilkLevelComponent(int silk_level) implements TooltipAppender {

    public static final Codec<SilkLevelComponent> CODEC = Codec.INT.xmap(SilkLevelComponent::new,
        SilkLevelComponent::silk_level);
    public static final PacketCodec<ByteBuf, SilkLevelComponent> PACKET_CODEC = PacketCodecs.VAR_INT.xmap(
        SilkLevelComponent::new, SilkLevelComponent::silk_level);

    @Override
    public void appendTooltip(
        Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type,
        ComponentsAccess components) {
      textConsumer.accept(
          Text.translatable(
              "container." + LighterEnd.MOD_ID + ".silk_moth_nest.fullness",
              this.silk_level(),
              SilkMothNest.MAX_FULLNESS
          ).formatted(Formatting.GRAY)
      );
    }
  }

  public record Variant(int variant) {

    public static final Codec<Variant> CODEC = Codec.INT.xmap(Variant::new, Variant::variant);
    public static final PacketCodec<ByteBuf, Variant> PACKET_CODEC = PacketCodecs.VAR_INT.xmap(
        Variant::new, Variant::variant
    );
  }

  private static <T> ComponentType<T> registerDataComponent(String name,
      UnaryOperator<Builder<T>> builderOperator) {
    return Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        LighterEnd.of(name),
        builderOperator.apply(ComponentType.builder()).build()
    );
  }

  public static void initialize() {
  }
}

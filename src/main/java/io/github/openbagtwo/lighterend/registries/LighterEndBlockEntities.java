package io.github.openbagtwo.lighterend.registries;

import com.mojang.serialization.Codec;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.Signs;
import io.github.openbagtwo.lighterend.blocks.Signs.LighterEndHangingSignBlockEntity;
import io.github.openbagtwo.lighterend.blocks.Signs.LighterEndSignBlockEntity;
import io.github.openbagtwo.lighterend.blocks.entities.SilkMothNestEntity;
import io.github.openbagtwo.lighterend.blocks.entities.SilkMothNestEntity.MothData;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
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
import net.minecraft.util.Identifier;

public class LighterEndBlockEntities {

  public static final BlockEntityType<LighterEndSignBlockEntity> SIGN = Registry.register(
      Registries.BLOCK_ENTITY_TYPE, Identifier.of(LighterEnd.MOD_ID, "sign"),
      FabricBlockEntityTypeBuilder.create(Signs.LighterEndSignBlockEntity::new,
          LighterEndBlocks.TENANEA.sign, LighterEndBlocks.TENANEA.wallSign).build(null));

  public static final BlockEntityType<LighterEndHangingSignBlockEntity> HANGING_SIGN = Registry.register(
      Registries.BLOCK_ENTITY_TYPE, Identifier.of(LighterEnd.MOD_ID, "hanging_sign"),
      FabricBlockEntityTypeBuilder.create(Signs.LighterEndHangingSignBlockEntity::new,
              LighterEndBlocks.TENANEA.hangingSign, LighterEndBlocks.TENANEA.wallHangingSign)
          .build(null));

  public static final BlockEntityType<SilkMothNestEntity> SILK_MOTH_NEST = Registry.register(
      Registries.BLOCK_ENTITY_TYPE, Identifier.of(LighterEnd.MOD_ID, "silk_moth_nest"),
      FabricBlockEntityTypeBuilder.create(SilkMothNestEntity::new, LighterEndBlocks.SILK_MOTH_NEST)
          .build(null)
  );

  public static final ComponentType<MothsComponent> MOTHS = registerDataComponent(
      "moths",
      builder -> builder.codec(MothsComponent.CODEC).packetCodec(MothsComponent.PACKET_CODEC)
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
        Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type,
        ComponentsAccess components) {
      textConsumer.accept(
          Text.translatable(LighterEnd.MOD_ID, "container.silk_moth_nest.moths", this.moths.size(),
              3).formatted(
              Formatting.GRAY));
    }
  }

  private static <T> ComponentType<T> registerDataComponent(String id,
      UnaryOperator<Builder<T>> builderOperator) {
    return Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        id,
        builderOperator.apply(ComponentType.builder()).build()
    );
  }


  public static void initialize() {
  }

}

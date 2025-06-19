package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.Signs;
import io.github.openbagtwo.lighterend.blocks.Signs.LighterEndHangingSignBlockEntity;
import io.github.openbagtwo.lighterend.blocks.Signs.LighterEndSignBlockEntity;
import io.github.openbagtwo.lighterend.blocks.entities.SilkMothNestEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class LighterEndBlockEntities {

  public static final BlockEntityType<LighterEndSignBlockEntity> SIGN = Registry.register(
      Registries.BLOCK_ENTITY_TYPE, LighterEnd.of("sign"),
      FabricBlockEntityTypeBuilder.create(
          Signs.LighterEndSignBlockEntity::new,
          LighterEndBlocks.TENANEA.sign,
          LighterEndBlocks.TENANEA.wallSign,
          LighterEndBlocks.UMBRELLA.sign,
          LighterEndBlocks.UMBRELLA.wallSign,
          LighterEndBlocks.LOTUS.sign,
          LighterEndBlocks.LOTUS.wallSign
      ).build(null));

  public static final BlockEntityType<LighterEndHangingSignBlockEntity> HANGING_SIGN = Registry.register(
      Registries.BLOCK_ENTITY_TYPE, LighterEnd.of("hanging_sign"),
      FabricBlockEntityTypeBuilder.create(
          Signs.LighterEndHangingSignBlockEntity::new,
          LighterEndBlocks.TENANEA.hangingSign,
          LighterEndBlocks.TENANEA.wallHangingSign,
          LighterEndBlocks.UMBRELLA.hangingSign,
          LighterEndBlocks.UMBRELLA.wallHangingSign,
          LighterEndBlocks.LOTUS.hangingSign,
          LighterEndBlocks.LOTUS.wallHangingSign
      ).build(null));

  public static final BlockEntityType<SilkMothNestEntity> SILK_MOTH_NEST = Registry.register(
      Registries.BLOCK_ENTITY_TYPE, LighterEnd.of("silk_moth_nest"),
      FabricBlockEntityTypeBuilder.create(SilkMothNestEntity::new, LighterEndBlocks.SILK_MOTH_NEST)
          .build(null)
  );

  public static void initialize() {
  }

}

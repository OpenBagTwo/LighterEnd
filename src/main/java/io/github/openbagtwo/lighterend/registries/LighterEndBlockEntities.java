package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.Furnaces;
import io.github.openbagtwo.lighterend.blocks.Signs.LighterEndHangingSignBlockEntity;
import io.github.openbagtwo.lighterend.blocks.Signs.LighterEndSignBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class LighterEndBlockEntities {

  public static final BlockEntityType<LighterEndSignBlockEntity> SIGN = Registry.register(
      Registries.BLOCK_ENTITY_TYPE,
      LighterEnd.of("sign"),
      FabricBlockEntityTypeBuilder.create(
          LighterEndSignBlockEntity::new,
          LighterEndBlocks.TENANEA.sign,
          LighterEndBlocks.TENANEA.wallSign,
          LighterEndBlocks.UMBRELLA.sign,
          LighterEndBlocks.UMBRELLA.wallSign,
          LighterEndBlocks.LOTUS.sign,
          LighterEndBlocks.LOTUS.wallSign,
          LighterEndBlocks.GLOWSHROOM.sign,
          LighterEndBlocks.GLOWSHROOM.wallSign,
          LighterEndBlocks.DRAGON.sign,
          LighterEndBlocks.DRAGON.wallSign
      ).build(null));

  public static final BlockEntityType<LighterEndHangingSignBlockEntity> HANGING_SIGN = Registry.register(
      Registries.BLOCK_ENTITY_TYPE,
      LighterEnd.of("hanging_sign"),
      FabricBlockEntityTypeBuilder.create(
          LighterEndHangingSignBlockEntity::new,
          LighterEndBlocks.TENANEA.hangingSign,
          LighterEndBlocks.TENANEA.wallHangingSign,
          LighterEndBlocks.UMBRELLA.hangingSign,
          LighterEndBlocks.UMBRELLA.wallHangingSign,
          LighterEndBlocks.LOTUS.hangingSign,
          LighterEndBlocks.LOTUS.wallHangingSign,
          LighterEndBlocks.GLOWSHROOM.hangingSign,
          LighterEndBlocks.GLOWSHROOM.wallHangingSign,
          LighterEndBlocks.DRAGON.hangingSign,
          LighterEndBlocks.DRAGON.wallHangingSign
      ).build(null));


  public static final BlockEntityType<Furnaces.EndFurnaceEntity> END_FURNACE = Registry.register(
      Registries.BLOCK_ENTITY_TYPE,
      LighterEnd.of("end_stone_furnace"),
      FabricBlockEntityTypeBuilder.create(
          Furnaces.EndFurnaceEntity::new, LighterEndBlocks.END_FURNACE
      ).build(null));

  public static final BlockEntityType<Furnaces.EndSmokerEntity> END_SMOKER = Registry.register(
      Registries.BLOCK_ENTITY_TYPE,
      LighterEnd.of("end_stone_smoker"),
      FabricBlockEntityTypeBuilder.create(
          Furnaces.EndSmokerEntity::new, LighterEndBlocks.END_SMOKER
      ).build(null));

  public static void initialize() {
  }

}

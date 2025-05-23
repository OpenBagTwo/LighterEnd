package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.Signs;
import io.github.openbagtwo.lighterend.blocks.Signs.LighterEndHangingSignBlockEntity;
import io.github.openbagtwo.lighterend.blocks.Signs.LighterEndSignBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
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


  public static void initialize() {
  }

}

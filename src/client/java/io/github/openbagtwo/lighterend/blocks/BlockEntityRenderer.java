package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.misc.Wood.WoodSet;
import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import java.util.Arrays;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.ShelfRenderer;
import net.minecraft.client.renderer.blockentity.StandingSignRenderer;

public class BlockEntityRenderer {

  public static void initialize() {
    BlockEntityRenderers.register(
        LighterEndBlockEntities.SIGN,
        StandingSignRenderer::new
    );
    BlockEntityRenderers.register(
        LighterEndBlockEntities.HANGING_SIGN,
        HangingSignRenderer::new
    );
    BlockEntityRenderers.register(
        LighterEndBlockEntities.SHELF,
        ShelfRenderer::new
    );
    BlockEntityRenderers.register(
        LighterEndBlockEntities.PEDESTAL,
        PedestalRenderer::new
    );

    for (WoodSet wood : Arrays.asList(
        LighterEndBlocks.TENANEA,
        LighterEndBlocks.UMBRELLA,
        LighterEndBlocks.LOTUS,
        LighterEndBlocks.GLOWSHROOM,
        LighterEndBlocks.DRAGON
    )) {
      Sheets.SIGN_SPRITES.put(wood.woodType,
          Sheets.getSignSprite(wood.woodType));
      Sheets.HANGING_SIGN_SPRITES.put(wood.woodType,
          Sheets.getHangingSignSprite(wood.woodType));
    }
  }

}

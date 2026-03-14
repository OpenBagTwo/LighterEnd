package io.github.openbagtwo.lighterend.mobs.states;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class GlossyMooshroomRenderState extends LivingEntityRenderState {

  public int variant = 0;
  public boolean sheared = false;
  public final BlockModelRenderState rightShroom = new BlockModelRenderState();
  public final BlockModelRenderState leftShroom = new BlockModelRenderState();
}

package io.github.openbagtwo.lighterend.blocks;


import io.github.openbagtwo.lighterend.world.gen.noise.OpenSimplexNoise;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlimeBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

public class UmbrellaMembrane extends SlimeBlock {

  public static final IntProperty COLOR = IntProperty.of("color", 0, 7);
  private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(0);

  public UmbrellaMembrane(Settings settings) {
    super(settings.slipperiness(0.8F).sounds(BlockSoundGroup.SLIME).nonOpaque());
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    double px = ctx.getBlockPos().getX() * 0.1;
    double py = ctx.getBlockPos().getY() * 0.1;
    double pz = ctx.getBlockPos().getZ() * 0.1;
    return this.getDefaultState().with(COLOR, MathHelper.floor(NOISE.eval(px, py, pz) * 3.5 + 4));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
    stateManager.add(COLOR);
  }

  @Override
  public boolean isTransparent(BlockState state) {
    return state.get(COLOR) > 0;
  }

  @Override
  public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
    if (state.get(COLOR) > 0) {
      return super.isSideInvisible(state, stateFrom, direction);
    } else {
      return false;
    }
  }
}

package io.github.openbagtwo.lighterend.blocks;


import io.github.openbagtwo.lighterend.world.gen.noise.OpenSimplexNoise;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class UmbrellaMembrane extends SlimeBlock {

  public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, 7);
  private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(0);

  public UmbrellaMembrane(Properties settings) {
    super(settings.friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion());
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    double px = ctx.getClickedPos().getX() * 0.1;
    double py = ctx.getClickedPos().getY() * 0.1;
    double pz = ctx.getClickedPos().getZ() * 0.1;
    return this.defaultBlockState().setValue(COLOR, Mth.floor(NOISE.eval(px, py, pz) * 3.5 + 4));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
    stateManager.add(COLOR);
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state) {
    return state.getValue(COLOR) > 0;
  }

  @Override
  public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
    if (state.getValue(COLOR) > 0) {
      return super.skipRendering(state, stateFrom, direction);
    } else {
      return false;
    }
  }
}

package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.VineLogic;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;

public class TenaneaFlowerBlock extends AbstractPlantStemBlock {

  public static final MapCodec<TenaneaFlowerBlock> CODEC = createCodec(TenaneaFlowerBlock::new);
  private static final VoxelShape SHAPE = Block.createCuboidShape(2, 0, 2, 14, 16, 14);
  public static final Vec3i[] COLORS;

  public TenaneaFlowerBlock(AbstractBlock.Settings settings) {
    super(
        settings
            .mapColor(MapColor.MAGENTA)
            .replaceable()
            .noCollision()
            .breakInstantly()
            .nonOpaque()
            .sounds(BlockSoundGroup.GRASS)
            .pistonBehavior(PistonBehavior.DESTROY)
            .offset(OffsetType.NONE)
            .burnable()
            .requiresTool()
            .ticksRandomly()
            .luminance((bs) -> 15),
        Direction.DOWN,
        SHAPE,
        false,
        0.1
    );
  }

  @Override
  protected int getGrowthLength(Random random) {
    return VineLogic.getGrowthLength(random);
  }

  @Override
  protected boolean chooseStemState(BlockState state) {
    return VineLogic.isValidForWeepingStem(state);
  }

  @Override
  protected Block getPlant() {
    return LighterEndBlocks.TENANEA_FLOWER;
  }

  @Override
  public MapCodec<TenaneaFlowerBlock> getCodec() {
    return CODEC;
  }


  static {
    COLORS = new Vec3i[]{
        new Vec3i(250, 111, 222),
        new Vec3i(167, 89, 255),
        new Vec3i(120, 207, 239),
        new Vec3i(255, 87, 182)
    };
  }


}

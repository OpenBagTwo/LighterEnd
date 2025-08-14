package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.SmokerBlock;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SmokerScreenHandler;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Furnaces {

  public static class EndFurnace extends FurnaceBlock {

    public EndFurnace(Settings settings) {
      super(
          settings
              .mapColor(MapColor.PALE_YELLOW)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .requiresTool()
              .strength(7F)
              .luminance(Blocks.createLightLevelFromLitBlockState(13))
      );
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
      return new EndFurnaceEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        World world, BlockState state, BlockEntityType<T> type
    ) {
      return validateTicker(world, type, LighterEndBlockEntities.END_FURNACE);
    }

    @Override
    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
      BlockEntity blockEntity = world.getBlockEntity(pos);
      if (blockEntity instanceof EndFurnaceEntity) {
        player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
        player.incrementStat(Stats.INTERACT_WITH_FURNACE);
      }
    }
  }

  public static class EndFurnaceEntity extends AbstractFurnaceBlockEntity {

    public EndFurnaceEntity(BlockPos pos, BlockState state) {
      super(LighterEndBlockEntities.END_FURNACE, pos, state, RecipeType.SMELTING);
    }

    @Override
    protected Text getContainerName() {
      return Text.translatable("container.furnace");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
      return new FurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
  }

  public static class EndSmoker extends SmokerBlock {

    public EndSmoker(Settings settings) {
      super(
          settings
              .mapColor(MapColor.PALE_YELLOW)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .requiresTool()
              .strength(7F)
              .luminance(Blocks.createLightLevelFromLitBlockState(13))
      );
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
      return new EndSmokerEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        World world, BlockState state, BlockEntityType<T> type
    ) {
      return validateTicker(world, type, LighterEndBlockEntities.END_SMOKER);
    }

    @Override
    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
      BlockEntity blockEntity = world.getBlockEntity(pos);
      if (blockEntity instanceof EndSmokerEntity) {
        player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
        player.incrementStat(Stats.INTERACT_WITH_SMOKER);
      }
    }
  }

  public static class EndSmokerEntity extends AbstractFurnaceBlockEntity {

    public EndSmokerEntity(BlockPos pos, BlockState state) {
      super(LighterEndBlockEntities.END_SMOKER, pos, state, RecipeType.SMOKING);
    }

    @Override
    protected Text getContainerName() {
      return Text.translatable("container.smoker");
    }

    @Override
    protected int getFuelTime(FuelRegistry fuelRegistry, ItemStack stack) {
      return super.getFuelTime(fuelRegistry, stack) / 2;
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
      return new SmokerScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
  }

}

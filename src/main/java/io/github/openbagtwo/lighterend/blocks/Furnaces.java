package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.inventory.SmokerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.SmokerBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class Furnaces {

  public static class EndFurnace extends FurnaceBlock {

    public EndFurnace(Properties settings) {
      super(
          settings
              .mapColor(MapColor.SAND)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .requiresCorrectToolForDrops()
              .strength(7F)
              .lightLevel(Blocks.litBlockEmission(13))
      );
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new EndFurnaceEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        Level world, BlockState state, BlockEntityType<T> type
    ) {
      return createFurnaceTicker(world, type, LighterEndBlockEntities.END_FURNACE);
    }

    @Override
    protected void openContainer(Level world, BlockPos pos, Player player) {
      BlockEntity blockEntity = world.getBlockEntity(pos);
      if (blockEntity instanceof EndFurnaceEntity) {
        player.openMenu((MenuProvider) blockEntity);
        player.awardStat(Stats.INTERACT_WITH_FURNACE);
      }
    }
  }

  public static class EndFurnaceEntity extends AbstractFurnaceBlockEntity {

    public EndFurnaceEntity(BlockPos pos, BlockState state) {
      super(LighterEndBlockEntities.END_FURNACE, pos, state, RecipeType.SMELTING);
    }

    @Override
    protected Component getDefaultName() {
      return Component.translatable("container.furnace");
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
      return new FurnaceMenu(syncId, playerInventory, this, this.dataAccess);
    }
  }

  public static class EndSmoker extends SmokerBlock {

    public EndSmoker(Properties settings) {
      super(
          settings
              .mapColor(MapColor.SAND)
              .instrument(NoteBlockInstrument.BASEDRUM)
              .requiresCorrectToolForDrops()
              .strength(7F)
              .lightLevel(Blocks.litBlockEmission(13))
      );
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new EndSmokerEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        Level world, BlockState state, BlockEntityType<T> type
    ) {
      return createFurnaceTicker(world, type, LighterEndBlockEntities.END_SMOKER);
    }

    @Override
    protected void openContainer(Level world, BlockPos pos, Player player) {
      BlockEntity blockEntity = world.getBlockEntity(pos);
      if (blockEntity instanceof EndSmokerEntity) {
        player.openMenu((MenuProvider) blockEntity);
        player.awardStat(Stats.INTERACT_WITH_SMOKER);
      }
    }
  }

  public static class EndSmokerEntity extends AbstractFurnaceBlockEntity {

    public EndSmokerEntity(BlockPos pos, BlockState state) {
      super(LighterEndBlockEntities.END_SMOKER, pos, state, RecipeType.SMOKING);
    }

    @Override
    protected Component getDefaultName() {
      return Component.translatable("container.smoker");
    }

    @Override
    protected int getBurnDuration(FuelValues fuelRegistry, ItemStack stack) {
      return super.getBurnDuration(fuelRegistry, stack) / 2;
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
      return new SmokerMenu(syncId, playerInventory, this, this.dataAccess);
    }
  }

}

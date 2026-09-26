package io.github.openbagtwo.lighterend.blocks.entities;

import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ListBackedContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class PedestalDisplay extends BlockEntity implements ItemOwner, ListBackedContainer {

  private final NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
  private float rotation = 0;

  public PedestalDisplay(BlockPos pos, BlockState state) {
    super(LighterEndBlockEntities.PEDESTAL, pos, state);
  }

  public float getRenderingRotation() {
    rotation += 0.5f;
    if (rotation >= 360) {
      rotation = 0;
    }
    return rotation;
  }

  @Override
  protected void loadAdditional(ValueInput view) {
    super.loadAdditional(view);
    this.inventory.clear();
    ContainerHelper.loadAllItems(view, this.inventory);
  }

  @Override
  protected void saveAdditional(ValueOutput view) {
    super.saveAdditional(view);
    ContainerHelper.saveAllItems(view, this.inventory, true);
  }

  @Override
  public void preRemoveSideEffects(BlockPos pos, BlockState oldState) {
    Containers.dropContents(level, pos, this);
    super.preRemoveSideEffects(pos, oldState);
  }

  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
    return saveWithoutMetadata(registryLookup);
  }

  @Override
  public NonNullList<ItemStack> getItems() {
    return this.inventory;
  }

  @Override
  public int getContainerSize() {
    return inventory.size();
  }

  @Override
  public boolean isEmpty() {
    return inventory.getFirst().isEmpty();
  }

  @Override
  public ItemStack getItem(int slot) {
    return inventory.getFirst();
  }

  @Override
  public ItemStack removeItem(int slot, int amount) {
    return this.removeItemNoUpdate(slot);
  }

  @Override
  public ItemStack removeItemNoUpdate(int slot) {
    if (slot == 0) {
      ItemStack stack = inventory.getFirst();
      inventory.set(0, ItemStack.EMPTY);
      return stack;
    }
    return null;
  }

  @Override
  public void setItem(int slot, ItemStack stack) {
    if (slot == 0) {
      inventory.set(0, stack);
    }
  }

  @Override
  public boolean stillValid(Player player) {
    return true;
  }

  @Override
  public void clearContent() {
    this.removeItemNoUpdate(0);
  }

  @Override
  public Level level() {
    return this.level;
  }

  @Override
  public Vec3 position() {
    return this.getBlockPos().getCenter();
  }

  @Override
  public float getVisualRotationYInDegrees() {
    return 0;
  }
}

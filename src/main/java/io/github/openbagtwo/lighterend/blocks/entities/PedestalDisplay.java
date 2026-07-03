package io.github.openbagtwo.lighterend.blocks.entities;

import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.ListInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.HeldItemContext;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PedestalDisplay extends BlockEntity implements HeldItemContext, ListInventory {

  private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
  private float rotation = 0;

  public PedestalDisplay(BlockPos pos, BlockState state) {
    super(LighterEndBlockEntities.PEDESTAL, pos, state);
  }

  public DefaultedList<ItemStack> getItems() {
    return inventory;
  }

  public float getRenderingRotation() {
    rotation += 0.5f;
    if (rotation >= 360) {
      rotation = 0;
    }
    return rotation;
  }

  @Override
  protected void readData(ReadView view) {
    super.readData(view);
    this.inventory.clear();
    Inventories.readData(view, this.inventory);
  }

  @Override
  protected void writeData(WriteView view) {
    super.writeData(view);
    Inventories.writeData(view, this.inventory, true);
  }

  @Override
  public void onBlockReplaced(BlockPos pos, BlockState oldState) {
    ItemScatterer.spawn(world, pos, this);
    super.onBlockReplaced(pos, oldState);
  }

  @Override
  public Packet<ClientPlayPacketListener> toUpdatePacket() {
    return BlockEntityUpdateS2CPacket.create(this);
  }

  @Override
  public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
    return createNbt(registryLookup);
  }

  @Override
  public DefaultedList<ItemStack> getHeldStacks() {
    return this.inventory;
  }

  @Override
  public int size() {
    return inventory.size();
  }

  @Override
  public boolean isEmpty() {
    return inventory.getFirst().isEmpty();
  }

  @Override
  public ItemStack getStack(int slot) {
    return inventory.getFirst();
  }

  @Override
  public ItemStack removeStack(int slot, int amount) {
    return this.removeStack(slot);
  }

  @Override
  public ItemStack removeStack(int slot) {
    if (slot == 0) {
      ItemStack stack = inventory.getFirst();
      inventory.set(0, ItemStack.EMPTY);
      return stack;
    }
    return null;
  }

  @Override
  public void setStack(int slot, ItemStack stack) {
    if (slot == 0) {
      inventory.set(0, stack);
    }
  }

  @Override
  public boolean canPlayerUse(PlayerEntity player) {
    return true;
  }

  @Override
  public void clear() {
    this.removeStack(0);
  }

  @Override
  public World getEntityWorld() {
    return this.world;
  }

  @Override
  public Vec3d getEntityPos() {
    return this.getPos().toCenterPos();
  }

  @Override
  public float getBodyYaw() {
    return 0;
  }
}

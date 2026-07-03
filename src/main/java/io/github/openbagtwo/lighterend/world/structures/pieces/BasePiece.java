package io.github.openbagtwo.lighterend.world.structures.pieces;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.math.BlockBox;

public abstract class BasePiece extends StructurePiece {

  protected BasePiece(StructurePieceType type, int i, BlockBox boundingBox) {
    super(type, i, boundingBox);
  }

  protected BasePiece(StructurePieceType type, NbtCompound tag) {
    super(type, tag);
    fromNbt(tag);
  }

  protected abstract void fromNbt(NbtCompound tag);

  protected void addAdditionalSaveData(NbtCompound tag) {
  }

  @Override
  protected void writeNbt(
      StructureContext structurePieceSerializationContext,
      NbtCompound compoundTag
  ) {
    addAdditionalSaveData(compoundTag);
  }
}

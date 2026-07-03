package io.github.openbagtwo.lighterend.registries;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.world.structures.EndLake;
import io.github.openbagtwo.lighterend.world.structures.Megalake;
import io.github.openbagtwo.lighterend.world.structures.pieces.LakePiece;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public class LighterEndStructures {

  public static final StructurePieceType LAKE_PIECE = register("lake_piece", LakePiece::new);
  public static final StructureType<Megalake> MEGALAKE = register("megalake", Megalake.CODEC);
  public static final StructureType<EndLake> END_LAKE = register("end_lake", EndLake.CODEC);

  public static StructurePieceType register(String name, StructurePieceType piece) {
    return Registry.register(
        Registries.STRUCTURE_PIECE,
        LighterEnd.of(name),
        piece
    );
  }

  public static <S extends Structure> StructureType<S> register(String name, MapCodec<S> codec) {
    return Registry.register(Registries.STRUCTURE_TYPE, LighterEnd.of(name),
        () -> codec);
  }

  public static void initialize() {
  }

}

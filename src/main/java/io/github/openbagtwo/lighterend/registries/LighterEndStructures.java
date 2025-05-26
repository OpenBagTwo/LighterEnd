package io.github.openbagtwo.lighterend.registries;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.world.structures.Megalake;
import io.github.openbagtwo.lighterend.world.structures.pieces.LakePiece;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public class LighterEndStructures {

  public static final StructurePieceType LAKE_PIECE = register("lake_piece", LakePiece::new);
  public static final StructureType<Megalake> MEGALAKE = register("megalake", Megalake.CODEC);

  public static StructurePieceType register(String name, StructurePieceType piece) {
    return Registry.register(
        Registries.STRUCTURE_PIECE,
        Identifier.of(LighterEnd.MOD_ID, name),
        piece
    );
  }

  public static <S extends Structure> StructureType<S> register(String name, MapCodec<S> codec) {
    return Registry.register(Registries.STRUCTURE_TYPE, Identifier.of(LighterEnd.MOD_ID, name),
        () -> codec);
  }

//  public static void bootstrap(Registerable<Structure> context) {
//    RegistryEntryLookup<Biome> biomeLookup = context.getRegistryLookup(RegistryKeys.BIOME);
//    context.register(
//        of("megalake"),
//        new Megalake(new Config(biomeLookup.getOrThrow(BiomeTags.IS_END)))
//    );
//  }

//  public static RegistryKey<Structure> of(String id) {
//    return RegistryKey.of(RegistryKeys.STRUCTURE, Identifier.of(LighterEnd.MOD_ID, id));
//  }

  public static void initialize() {
  }

}

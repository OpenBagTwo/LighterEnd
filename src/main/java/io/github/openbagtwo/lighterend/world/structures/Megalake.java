package io.github.openbagtwo.lighterend.world.structures;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.registries.LighterEndStructures;
import io.github.openbagtwo.lighterend.world.structures.pieces.LakePiece;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class Megalake extends Structure {

  public static final MapCodec<Megalake> CODEC = simpleCodec(Megalake::new);


  public Megalake(Structure.StructureSettings structureSettings) {
    super(structureSettings);
  }

  @Override
  public StructureType<Megalake> type() {
    return LighterEndStructures.MEGALAKE;
  }

  protected void generatePieces(StructurePiecesBuilder structurePiecesBuilder,
      Structure.GenerationContext context) {
    final RandomSource random = context.random();
    final ChunkPos chunkPos = context.chunkPos();
    final ChunkGenerator chunkGenerator = context.chunkGenerator();
    final RandomState rState = context.randomState();

    final LevelHeightAccessor levelHeightAccessor = context.heightAccessor();

    int x = chunkPos.getBlockX(Mth.nextInt(random, 4, 12));
    int z = chunkPos.getBlockZ(Mth.nextInt(random, 4, 12));
    int y = chunkGenerator.getBaseHeight(x, z, Types.WORLD_SURFACE_WG, levelHeightAccessor, rState);

    if (y > 5) {
      Holder<Biome> biome = getNoiseBiome(chunkGenerator, rState, x >> 2, y >> 2, z >> 2);

      float radius = Mth.nextFloat(random, 32F, 64F);
      float depth = Mth.nextFloat(random, 7F, 15F);
      LakePiece piece = new LakePiece(new BlockPos(x, y, z), radius, depth, random, biome);
      structurePiecesBuilder.addPiece(piece);
    }
  }

  @Override
  public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
    BlockPos pos = getGenerationHeight(
        context.chunkPos(),
        context.chunkGenerator(),
        context.heightAccessor(),
        context.randomState()
    );
    if (pos.getY() >= 10) {
      return Optional.of(
          new Structure.GenerationStub(
              pos,
              (structurePiecesBuilder) -> {
                generatePieces(structurePiecesBuilder, context);
              }
          )
      );
    }
    return Optional.empty();
  }

  protected Holder<Biome> getNoiseBiome(
      ChunkGenerator cg, RandomState rState, int i, int j, int k
  ) {
    return cg.getBiomeSource().createCachingResolver(rState).getNoiseBiome(i, j, k);
  }

  private static BlockPos getGenerationHeight(
      ChunkPos chunkPos,
      ChunkGenerator chunkGenerator,
      LevelHeightAccessor levelHeightAccessor,
      RandomState rState
  ) {
    LegacyRandomSource random = new LegacyRandomSource(chunkPos.x() + chunkPos.z() * 10387313);
    Rotation blockRotation = Rotation.getRandom(random);

    int offsetX = 5;
    int offsetZ = 5;
    if (blockRotation == Rotation.CLOCKWISE_90) {
      offsetX = -5;
    } else if (blockRotation == Rotation.CLOCKWISE_180) {
      offsetX = -5;
      offsetZ = -5;
    } else if (blockRotation == Rotation.COUNTERCLOCKWISE_90) {
      offsetZ = -5;
    }

    int blockX = chunkPos.getBlockX(7);
    int blockZ = chunkPos.getBlockZ(7);
    int minZ = Integer.MAX_VALUE;
    BlockPos.MutableBlockPos result = new BlockPos.MutableBlockPos(blockX, Integer.MIN_VALUE,
        blockZ);
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        int z = chunkGenerator.getFirstOccupiedHeight(
            blockX + i * offsetX,
            blockZ + j * offsetZ,
            Heightmap.Types.WORLD_SURFACE_WG,
            levelHeightAccessor,
            rState
        );
        if (z < minZ) {
          result.set(blockX + i * offsetX, z, blockZ + j * offsetZ);
        }
      }
    }

    return result;
  }
}

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

public class EndLake extends Structure {

  public static final MapCodec<EndLake> CODEC = simpleCodec(EndLake::new);

  public EndLake(Structure.StructureSettings structureSettings) {
    super(structureSettings);
  }

  @Override
  public StructureType<EndLake> type() {
    return LighterEndStructures.END_LAKE;
  }

  protected void generatePieces(
      StructurePiecesBuilder structurePiecesBuilder,
      GenerationContext context
  ) {
    final RandomSource random = context.random();
    final ChunkPos chunkPos = context.chunkPos();
    final ChunkGenerator chunkGenerator = context.chunkGenerator();
    final LevelHeightAccessor levelHeightAccessor = context.heightAccessor();
    final RandomState rState = context.randomState();

    int x = chunkPos.getBlockX(Mth.nextInt(random, 4, 12));
    int z = chunkPos.getBlockZ(Mth.nextInt(random, 4, 12));
    int y = chunkGenerator.getBaseHeight(x, z, Types.WORLD_SURFACE_WG, levelHeightAccessor, rState);

    Holder<Biome> biome = getNoiseBiome(chunkGenerator, rState, x >> 2, y >> 2, z >> 2);
    if (y > 5) {
      float radius = Mth.nextFloat(random, 20, 40);
      float depth = Mth.nextFloat(random, 5, 10);
      LakePiece piece = new LakePiece(new BlockPos(x, y, z), radius, depth, random, biome);
      structurePiecesBuilder.addPiece(piece);
    }
  }

  protected Holder<Biome> getNoiseBiome(
      ChunkGenerator cg, RandomState rState, int i, int j, int k
  ) {
    return cg.getBiomeSource().createCachingResolver(rState).getNoiseBiome(i, j, k);
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
      return Optional.of(new Structure.GenerationStub(pos, (structurePiecesBuilder) -> {
        generatePieces(structurePiecesBuilder, context);
      }));
    }
    return Optional.empty();
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

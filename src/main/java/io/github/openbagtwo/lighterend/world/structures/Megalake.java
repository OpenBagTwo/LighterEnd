package io.github.openbagtwo.lighterend.world.structures;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.registries.LighterEndStructures;
import io.github.openbagtwo.lighterend.world.structures.pieces.LakePiece;
import java.util.Optional;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public class Megalake extends Structure {

  public static final MapCodec<Megalake> CODEC = createCodec(Megalake::new);


  public Megalake(Structure.Config structureSettings) {
    super(structureSettings);
  }

  @Override
  public StructureType<Megalake> getType() {
    return LighterEndStructures.MEGALAKE;
  }

  protected void generatePieces(StructurePiecesCollector structurePiecesBuilder,
      Structure.Context context) {
    final Random random = context.random();
    final ChunkPos chunkPos = context.chunkPos();
    final ChunkGenerator chunkGenerator = context.chunkGenerator();
    final NoiseConfig rState = context.noiseConfig();

    final HeightLimitView levelHeightAccessor = context.world();

    int x = chunkPos.getOffsetX(MathHelper.nextInt(random, 4, 12));
    int z = chunkPos.getOffsetZ(MathHelper.nextInt(random, 4, 12));
    int y = chunkGenerator.getHeight(x, z, Type.WORLD_SURFACE_WG, levelHeightAccessor, rState);

    if (y > 5) {
      RegistryEntry<Biome> biome = getNoiseBiome(chunkGenerator, rState, x >> 2, y >> 2, z >> 2);

      float radius = MathHelper.nextFloat(random, 32F, 64F);
      float depth = MathHelper.nextFloat(random, 7F, 15F);
      LakePiece piece = new LakePiece(new BlockPos(x, y, z), radius, depth, random, biome);
      structurePiecesBuilder.addPiece(piece);
    }
  }

  protected static final BlockState AIR = Blocks.AIR.getDefaultState();

  @Override
  public Optional<StructurePosition> getStructurePosition(Context context) {
    BlockPos pos = getGenerationHeight(
        context.chunkPos(),
        context.chunkGenerator(),
        context.world(),
        context.noiseConfig()
    );
    if (pos.getY() >= 10) {
      return Optional.of(new Structure.StructurePosition(pos, (structurePiecesBuilder) -> {
        generatePieces(structurePiecesBuilder, context);
      }));
    }
    return Optional.empty();
  }

  protected RegistryEntry<Biome> getNoiseBiome(ChunkGenerator cg, NoiseConfig rState, int i, int j,
      int k) {
    return cg.getBiomeSource().getBiome(i, j, k, rState.getMultiNoiseSampler());
  }

  private static BlockPos getGenerationHeight(
      ChunkPos chunkPos,
      ChunkGenerator chunkGenerator,
      HeightLimitView levelHeightAccessor,
      NoiseConfig rState
  ) {
    CheckedRandom random = new CheckedRandom(chunkPos.x + chunkPos.z * 10387313);
    BlockRotation blockRotation = BlockRotation.random(random);

    int offsetX = 5;
    int offsetZ = 5;
    if (blockRotation == BlockRotation.CLOCKWISE_90) {
      offsetX = -5;
    } else if (blockRotation == BlockRotation.CLOCKWISE_180) {
      offsetX = -5;
      offsetZ = -5;
    } else if (blockRotation == BlockRotation.COUNTERCLOCKWISE_90) {
      offsetZ = -5;
    }

    int blockX = chunkPos.getOffsetX(7);
    int blockZ = chunkPos.getOffsetZ(7);
    int minZ = Integer.MAX_VALUE;
    BlockPos.Mutable result = new BlockPos.Mutable(blockX, Integer.MIN_VALUE, blockZ);
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        int z = chunkGenerator.getHeightInGround(
            blockX + i * offsetX,
            blockZ + j * offsetZ,
            Heightmap.Type.WORLD_SURFACE_WG,
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

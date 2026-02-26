package io.github.openbagtwo.lighterend.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFRotate;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFTranslate;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFUnion;
import io.github.openbagtwo.lighterend.utils.math.sdf.primitives.SDFCappedCone;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.joml.Vector3f;

public class IceStar extends Feature<IceStar.Config> {

  public IceStar() {
    super(IceStar.Config.CODEC);
  }

  @Override
  public boolean place(FeaturePlaceContext<IceStar.Config> featureConfig) {
    final RandomSource random = featureConfig.random();
    BlockPos pos = featureConfig.origin();
    final WorldGenLevel world = featureConfig.level();
    Config cfg = featureConfig.config();
    float size = Mth.nextFloat(random, cfg.minSize, cfg.maxSize);
    int count = Mth.nextInt(random, cfg.minCount, cfg.maxCount);
    List<Vector3f> points = getFibonacciPoints(count);
    SDF sdf = null;
    SDF spike = new SDFCappedCone().setRadius1(3 + (size - 5) * 0.2F)
        .setRadius2(0)
        .setHeight(size)
        .setBlock(Blocks.SNOW_BLOCK);
    spike = new SDFTranslate().setTranslate(0, size - 0.5F, 0).setSource(spike);

    Vector3f yp = new Vector3f(0, 1, 0);
    for (Vector3f point : points) {
      SDF rotated = spike;
      point = normalize(point);
      float angle = angle(yp, point);
      if (angle > 0.01F && angle < 3.14F) {
        Vector3f axis = normalize(cross(yp, point));
        rotated = new SDFRotate().setRotation(axis, angle).setSource(spike);
      } else if (angle > 1) {
        rotated = new SDFRotate().setRotation(yp, (float) Math.PI).setSource(spike);
      }
      sdf = (sdf == null) ? rotated : new SDFUnion().setSourceA(sdf).setSourceB(rotated);
    }

    int x1 = (pos.getX() >> 4) << 4;
    int z1 = (pos.getZ() >> 4) << 4;
    pos = new BlockPos(x1 + random.nextInt(16), Mth.nextInt(random, 32, 128),
        z1 + random.nextInt(16));

    final float ancientRadius = size * 0.7F;
    final float denseRadius = size * 0.9F;
    final float iceRadius = size < 7 ? size * 5 : size * 1.3F;
    final float randScale = size * 0.3F;

    final BlockPos center = pos;

    final BlockState ice;
    final BlockState dense;
    final BlockState ancient;

    switch (cfg.variant % 3) {
      case 2:
        ice = LighterEndBlocks.AUROUS_ICE.defaultBlockState();
        dense = Blocks.RAW_GOLD_BLOCK.defaultBlockState();
        ancient = Blocks.RAW_GOLD_BLOCK.defaultBlockState();
        break;
      case 1:
        ice = LighterEndBlocks.FERROUS_ICE.defaultBlockState();
        dense = LighterEndBlocks.FERROUS_ICE.defaultBlockState();
        ancient = Blocks.RAW_IRON_BLOCK.defaultBlockState();
        break;
      case 0:
      default:
        ice = LighterEndBlocks.EMERALD_ICE.defaultBlockState();
        dense = Blocks.RAW_COPPER_BLOCK.defaultBlockState();
        ancient = Blocks.RAW_COPPER_BLOCK.defaultBlockState();
    }

    final SDF sdfCopy = sdf;

    sdf.addPostProcess((info) -> {
      BlockPos bpos = info.getPos();
      float px = bpos.getX() - center.getX();
      float py = bpos.getY() - center.getY();
      float pz = bpos.getZ() - center.getZ();
      float distance = Mth.sqrt(px * px + py * py + pz * pz) + sdfCopy.getDistance(
          px,
          py,
          pz
      ) * 0.4F + random.nextFloat() * randScale;
      if (distance < ancientRadius) {
        return ancient;
      } else if (distance < denseRadius) {
        return dense;
      } else if (distance < iceRadius) {
        return ice;
      }
      return info.getState();
    }).fillRecursive(world, pos);

    return true;
  }

  private List<Vector3f> getFibonacciPoints(int count) {
    float max = count - 1;
    List<Vector3f> result = new ArrayList(count);
    for (int i = 0; i < count; i++) {
      float y = 1F - (i / max) * 2F;
      float radius = (float) Math.sqrt(1F - y * y);
      float theta = ((float) (Math.PI * (3 - Math.sqrt(5)))) * i;
      float x = (float) Math.cos(theta) * radius;
      float z = (float) Math.sin(theta) * radius;
      result.add(new Vector3f(x, y, z));
    }
    return result;
  }

  // TODO: refactor if needed
  public static Vector3f normalize(Vector3f vec) {
    float length = vec.x * vec.x + vec.y * vec.y + vec.z * vec.z;
    if (length > 0) {
      length = (float) Math.sqrt(length);
      float x = vec.x / length;
      float y = vec.y / length;
      float z = vec.z / length;
      vec.set(x, y, z);
    }
    return vec;
  }

  // TODO: refactor if needed
  private static float angle(Vector3f vec1, Vector3f vec2) {
    float dot = vec1.x * vec2.x + vec1.y * vec2.y + vec1.z * vec2.z;
    float length1 = vec1.x * vec1.x + vec1.y * vec1.y + vec1.z * vec1.z;
    float length2 = vec2.x * vec2.x + vec2.y * vec2.y + vec2.z * vec2.z;
    return (float) Math.acos(dot / Math.sqrt(length1 * length2));
  }

  // TODO: refactor if needed
  private static Vector3f cross(Vector3f vec1, Vector3f vec2) {
    float cx = vec1.y * vec2.z - vec1.z * vec2.y;
    float cy = vec1.z * vec2.x - vec1.x * vec2.z;
    float cz = vec1.x * vec2.y - vec1.y * vec2.x;
    return new Vector3f(cx, cy, cz);
  }

  public record Config(
      int variant,
      float minSize,
      float maxSize,
      int minCount,
      int maxCount
  ) implements FeatureConfiguration {

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            Codec.INT.fieldOf("variant").forGetter(o -> o.variant),
            Codec.FLOAT.fieldOf("min_size").forGetter(o -> o.minSize),
            Codec.FLOAT.fieldOf("max_size").forGetter(o -> o.maxSize),
            Codec.INT.fieldOf("min_count").forGetter(o -> o.minCount),
            Codec.INT.fieldOf("max_count").forGetter(o -> o.maxCount)
        ).apply(instance, Config::new));
  }
}

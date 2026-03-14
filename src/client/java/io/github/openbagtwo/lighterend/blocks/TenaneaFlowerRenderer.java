package io.github.openbagtwo.lighterend.blocks;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class TenaneaFlowerRenderer {

  public static final Vec3i[] COLORS = new Vec3i[]{
      new Vec3i(250, 111, 222),
      new Vec3i(167, 89, 255),
      new Vec3i(120, 207, 239),
      new Vec3i(255, 87, 182)
  };

  public static BlockTintSource getBlockColor() {
    return new BlockTintSource() {
      @Override
      public int color(final BlockState state) {
        return colorInWorld(state, null, BlockPos.ZERO);
      }

      @Override
      public int colorInWorld(
          final BlockState state,
          final BlockAndTintGetter level,
          final BlockPos pos
      ) {
        long i = (getRandom(pos.getX(), pos.getZ()) & 63) + pos.getY();
        double delta = i * 0.1;
        int index = Mth.floor(delta);
        int index2 = (index + 1) & 3;
        delta -= index;
        index &= 3;

        Vec3i color1 = COLORS[index];
        Vec3i color2 = COLORS[index2];

        int r = Mth.floor(Mth.lerp(delta, color1.getX(), color2.getX()));
        int g = Mth.floor(Mth.lerp(delta, color1.getY(), color2.getY()));
        int b = Mth.floor(Mth.lerp(delta, color1.getZ(), color2.getZ()));
        float[] hsb = RGBtoHSB(r, g, b, new float[3]);

        return HSBtoRGB(hsb[0], Math.max(0.5F, hsb[1]), hsb[2]);
      }
    };
  }

  // Utils (TODO: refactor out as needed)

  private static int getRandom(int x, int z) {
    int h = x * 374761393 + z * 668265263;
    h = (h ^ (h >> 13)) * 1274126177;
    return h ^ (h >> 16);
  }

  private static float[] RGBtoHSB(int r, int g, int b, float[] hsbvals) {
    float hue, saturation, brightness;
    if (hsbvals == null) {
      hsbvals = new float[4];
    }
    int cmax = (r > g) ? r : g;
    if (b > cmax) {
      cmax = b;
    }
    int cmin = (r < g) ? r : g;
    if (b < cmin) {
      cmin = b;
    }

    brightness = ((float) cmax) / 255.0F;
    if (cmax != 0) {
      saturation = ((float) (cmax - cmin)) / ((float) cmax);
    } else {
      saturation = 0;
    }
    if (saturation == 0) {
      hue = 0;
    } else {
      float redc = ((float) (cmax - r)) / ((float) (cmax - cmin));
      float greenc = ((float) (cmax - g)) / ((float) (cmax - cmin));
      float bluec = ((float) (cmax - b)) / ((float) (cmax - cmin));
      if (r == cmax) {
        hue = bluec - greenc;
      } else if (g == cmax) {
        hue = 2.0F + redc - bluec;
      } else {
        hue = 4.0F + greenc - redc;
      }
      hue = hue / 6.0F;
      if (hue < 0) {
        hue = hue + 1.0F;
      }
    }
    hsbvals[0] = hue;
    hsbvals[1] = saturation;
    hsbvals[2] = brightness;
    return hsbvals;
  }

  private static int HSBtoRGB(float hue, float saturation, float brightness) {
    int r = 0, g = 0, b = 0;
    if (saturation == 0) {
      r = g = b = (int) (brightness * 255.0F + 0.5F);
    } else {
      float h = (hue - (float) Math.floor(hue)) * 6.0F;
      float f = h - (float) Math.floor(h);
      float p = brightness * (1.0F - saturation);
      float q = brightness * (1.0F - saturation * f);
      float t = brightness * (1.0F - (saturation * (1.0F - f)));
      switch ((int) h) {
        case 0:
          r = (int) (brightness * 255.0F + 0.5F);
          g = (int) (t * 255.0F + 0.5F);
          b = (int) (p * 255.0F + 0.5F);
          break;
        case 1:
          r = (int) (q * 255.0F + 0.5F);
          g = (int) (brightness * 255.0F + 0.5F);
          b = (int) (p * 255.0F + 0.5F);
          break;
        case 2:
          r = (int) (p * 255.0F + 0.5F);
          g = (int) (brightness * 255.0F + 0.5F);
          b = (int) (t * 255.0F + 0.5F);
          break;
        case 3:
          r = (int) (p * 255.0F + 0.5F);
          g = (int) (q * 255.0F + 0.5F);
          b = (int) (brightness * 255.0F + 0.5F);
          break;
        case 4:
          r = (int) (t * 255.0F + 0.5F);
          g = (int) (p * 255.0F + 0.5F);
          b = (int) (brightness * 255.0F + 0.5F);
          break;
        case 5:
          r = (int) (brightness * 255.0F + 0.5F);
          g = (int) (p * 255.0F + 0.5F);
          b = (int) (q * 255.0F + 0.5F);
          break;
      }
    }
    return 0xFF000000 | (r << 16) | (g << 8) | (b << 0);
  }
}

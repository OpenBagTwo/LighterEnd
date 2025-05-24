package io.github.openbagtwo.lighterend.utils.math.sdf.operators;

import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;
import java.util.function.Function;
import org.joml.Vector3f;

public class SDFDisplace extends SDF.UnaryOperator {

  private final Vector3f pos = new Vector3f();
  private Function<Vector3f, Float> displace;

  public SDFDisplace setFunction(Function<Vector3f, Float> displace) {
    this.displace = displace;
    return this;
  }

  @Override
  public float getDistance(float x, float y, float z) {
    pos.set(x, y, z);
    return this.source.getDistance(x, y, z) + displace.apply(pos);
  }
}

package io.github.openbagtwo.lighterend.utils.math.sdf.operators;

import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;
import java.util.function.Consumer;
import org.joml.Vector3f;

public class SDFCoordsModify extends SDF.UnaryOperator {

  private final Vector3f pos = new Vector3f();
  private Consumer<Vector3f> function;

  public SDFCoordsModify setFunction(Consumer<Vector3f> function) {
    this.function = function;
    return this;
  }

  @Override
  public float getDistance(float x, float y, float z) {
    pos.set(x, y, z);
    function.accept(pos);
    return this.source.getDistance(pos.x(), pos.y(), pos.z());
  }

}

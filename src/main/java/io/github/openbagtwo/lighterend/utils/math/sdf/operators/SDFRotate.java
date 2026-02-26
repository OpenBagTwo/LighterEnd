package io.github.openbagtwo.lighterend.utils.math.sdf.operators;

import com.mojang.math.Axis;
import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SDFRotate extends SDF.UnaryOperator {

  private final Vector3f pos = new Vector3f();
  private Quaternionf rotation;

  public SDFRotate setRotation(Axis axis, float rotationAngle) {
    rotation = axis.rotation(rotationAngle);
    return this;
  }

  public SDFRotate setRotation(Vector3f axis, float rotationAngle) {
    rotation = new Quaternionf().setAngleAxis(rotationAngle, axis.x, axis.y, axis.z);
    return this;
  }

  @Override
  public float getDistance(float x, float y, float z) {
    pos.set(x, y, z);
    pos.rotate(rotation);
    return source.getDistance(pos.x(), pos.y(), pos.z());
  }

}

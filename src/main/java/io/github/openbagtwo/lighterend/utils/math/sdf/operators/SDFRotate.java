package io.github.openbagtwo.lighterend.utils.math.sdf.operators;

import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SDFRotate extends SDF.UnaryOperator {

  private final Vector3f pos = new Vector3f();
  private Quaternionf rotation;

  public SDFRotate setRotation(RotationAxis axis, float rotationAngle) {
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

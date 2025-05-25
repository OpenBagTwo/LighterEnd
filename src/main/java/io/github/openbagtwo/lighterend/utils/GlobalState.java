package io.github.openbagtwo.lighterend.utils;

import net.minecraft.util.math.BlockPos.Mutable;

public class GlobalState {

  private static final ThreadLocal<GlobalState> STATE = ThreadLocal.withInitial(
      () -> new GlobalState());

  public static GlobalState stateForThread() {
    return STATE.get();
  }

  public final Mutable POS = new Mutable();
}

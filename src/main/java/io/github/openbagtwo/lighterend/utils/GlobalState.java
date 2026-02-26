package io.github.openbagtwo.lighterend.utils;

import net.minecraft.core.BlockPos.MutableBlockPos;

public class GlobalState {

  private static final ThreadLocal<GlobalState> STATE = ThreadLocal.withInitial(
      () -> new GlobalState());

  public static GlobalState stateForThread() {
    return STATE.get();
  }

  public final MutableBlockPos POS = new MutableBlockPos();
}

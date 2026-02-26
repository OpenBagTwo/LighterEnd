package io.github.openbagtwo.lighterend.networking;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record GravityPayload(double gravity, boolean flyFix) implements CustomPacketPayload {

  public static final Type<GravityPayload> ID = new Type<>(LighterEnd.of("gravity"));
  public static final StreamCodec<FriendlyByteBuf, GravityPayload> CODEC = CustomPacketPayload.codec(
      (p, buffer) -> write(p.gravity(), p.flyFix(), buffer),
      buffer -> read(buffer)
  );

  @Override
  public Type<GravityPayload> type() {
    return ID;
  }

  static GravityPayload read(FriendlyByteBuf buffer) {
    return new GravityPayload(buffer.readDouble(), buffer.readBoolean());
  }

  static FriendlyByteBuf write(double gravity, boolean flyFix, FriendlyByteBuf buffer) {
    buffer.writeDouble(gravity);
    buffer.writeBoolean(flyFix);
    return buffer;
  }
}

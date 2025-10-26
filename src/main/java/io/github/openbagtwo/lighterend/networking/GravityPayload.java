package io.github.openbagtwo.lighterend.networking;

import io.github.openbagtwo.lighterend.LighterEnd;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record GravityPayload(double gravity, boolean flyFix) implements CustomPayload {

  public static final Id<GravityPayload> ID = new Id<>(LighterEnd.of("gravity"));
  public static final PacketCodec<PacketByteBuf, GravityPayload> CODEC = CustomPayload.codecOf(
      (p, buffer) -> write(p.gravity(), p.flyFix(), buffer),
      buffer -> read(buffer)
  );

  @Override
  public Id<GravityPayload> getId() {
    return ID;
  }

  static GravityPayload read(PacketByteBuf buffer) {
    return new GravityPayload(buffer.readDouble(), buffer.readBoolean());
  }

  static PacketByteBuf write(double gravity, boolean flyFix, PacketByteBuf buffer) {
    buffer.writeDouble(gravity);
    buffer.writeBoolean(flyFix);
    return buffer;
  }
}

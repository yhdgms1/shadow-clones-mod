package dev.yhdgms1.shadow_clones;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SummonShadowClonesC2SPayload(String modelType) implements CustomPayload {
    public static final Identifier ID_RAW = Identifier.of(ShadowClones.MOD_ID, "summon_shadow_clones");
    public static final CustomPayload.Id<SummonShadowClonesC2SPayload> ID = new CustomPayload.Id<>(ID_RAW);
    public static final PacketCodec<RegistryByteBuf, SummonShadowClonesC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING,
            SummonShadowClonesC2SPayload::modelType,
            SummonShadowClonesC2SPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

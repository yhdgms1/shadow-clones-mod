package dev.yhdgms1.shadow_clones;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class CloneRenderState extends PlayerEntityRenderState {
    public boolean skinUpdated = false;
    public Identifier skinTexture = DefaultSkinHelper.getSteve().texture();

    public CloneRenderState() {
        super();
    }
}

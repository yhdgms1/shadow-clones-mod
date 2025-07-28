package dev.yhdgms1.shadow_clones;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;

@Environment(EnvType.CLIENT)
public class CloneRenderState extends PlayerEntityRenderState {
    public boolean skinUpdated = false;
    public SkinTextures skinTextures = DefaultSkinHelper.getSteve();

    public CloneRenderState() {
        super();
    }
}

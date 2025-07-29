package dev.yhdgms1.shadow_clones;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class TextureManager {
    private TextureManager() {}

    private static final HashMap<String, LoadingTexture> CACHE = new HashMap<>();
    private static final long TTL = 10 * 60 * 1000;

    private static NativeImage fetchImage(String url) {
        try (InputStream in = URI.create(url).toURL().openStream()) {
            return NativeImage.read(in);
        } catch (IOException e) {
            return null;
        }
    }

    public enum State {
        PENDING,
        COMPLETE
    }

    public static class LoadingTexture {
        public State state;
        public Optional<Identifier> texture;
        public long expiry;

        public LoadingTexture() {
            this.state = State.PENDING;
            this.texture = Optional.empty();
            this.expiry = TTL + System.currentTimeMillis();
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expiry;
        }
    }

    public static LoadingTexture loadTexture(String url) {
        if (CACHE.containsKey(url)) {
            LoadingTexture loadingTexture = CACHE.get(url);

            if (!loadingTexture.isExpired()) {
                return loadingTexture;
            }
        }

        LoadingTexture loadingTexture = new LoadingTexture();
        CACHE.put(url, loadingTexture);

        try (NativeImage image = fetchImage(url)) {
            if (image == null) {
                loadingTexture.state = State.COMPLETE;
            } else {
                UUID uuid = UUID.randomUUID();
                NativeImageBackedTexture texture = new NativeImageBackedTexture(uuid::toString, image);

                Identifier identifier = Identifier.of(ShadowClones.MOD_ID, uuid.toString());
                MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, texture);

                loadingTexture.state = State.COMPLETE;
                loadingTexture.texture = Optional.of(identifier);
            }
        }

        return loadingTexture;
    }

    public static Optional<SkinTextures> getOfflineTextures(CloneEntity entity) {
        String ownerId = entity.getOwnerId();

        if (!ownerId.isEmpty()) {
            GameProfile gameProfile = new GameProfile(UUID.fromString(ownerId), entity.getProfileName());
            SkinTextures textures = MinecraftClient.getInstance().getSkinProvider().getSkinTextures(gameProfile);

            return Optional.of(textures);
        }

        return Optional.empty();
    }
}

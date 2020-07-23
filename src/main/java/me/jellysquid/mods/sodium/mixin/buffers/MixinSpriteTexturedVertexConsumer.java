package me.jellysquid.mods.sodium.mixin.buffers;

import me.jellysquid.mods.sodium.client.model.consumer.ParticleVertexConsumer;
import me.jellysquid.mods.sodium.client.model.consumer.QuadVertexConsumer;
import com.mojang.blaze3d.vertex.IVertexConsumer;
import net.minecraft.client.renderer.SpriteAwareVertexBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SpriteAwareVertexBuilder.class)
public abstract class MixinSpriteTexturedVertexConsumer implements QuadVertexConsumer, ParticleVertexConsumer {
    @Shadow
    @Final
    private IVertexConsumer parent;

    @Shadow
    @Final
    private TextureAtlasSprite sprite;

    @Override
    public void vertexQuad(float x, float y, float z, int color, float u, float v, int light, int overlay, int norm) {
        u = this.sprite.getFrameU(u * 16.0F);
        v = this.sprite.getFrameV(v * 16.0F);

        ((QuadVertexConsumer) this.parent).vertexQuad(x, y, z, color, u, v, light, overlay, norm);
    }

    @Override
    public void vertexParticle(float x, float y, float z, float u, float v, int color, int light) {
        u = this.sprite.getFrameU(u * 16.0F);
        v = this.sprite.getFrameV(v * 16.0F);

        ((ParticleVertexConsumer) this.parent).vertexParticle(x, y, z, u, v, color, light);
    }

}

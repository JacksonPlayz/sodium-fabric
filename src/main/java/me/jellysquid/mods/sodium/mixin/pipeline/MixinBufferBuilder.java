package me.jellysquid.mods.sodium.mixin.pipeline;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.DefaultColorVertexBuilder;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadViewMutable;
import me.jellysquid.mods.sodium.client.model.quad.sink.ModelQuadSink;
import me.jellysquid.mods.sodium.client.util.ModelQuadUtil;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.ByteBuffer;

@Mixin(BufferBuilder.class)
public abstract class MixinBufferBuilder extends DefaultColorVertexBuilder implements ModelQuadSink {
    @Shadow
    private VertexFormat vertexFormat;

    @Shadow
    private int vertexFormatIndex;

    @Shadow
    private int nextElementBytes;

    @Shadow
    private VertexFormatElement vertexFormatElement;

    @Shadow
    private ByteBuffer byteBuffer;

    @Shadow
    protected abstract void growBuffer(int increaseAmount);

    @Shadow
    public abstract void addVertex(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ);

    @Shadow
    private int vertexCount;

    /**
     * @author JellySquid
     * @reason Remove modulo operations and recursion
     */
    @Overwrite
    public void nextVertexFormatIndex() {
        ImmutableList<VertexFormatElement> elements = this.vertexFormat.getElements();

        do {
            this.nextElementBytes += this.vertexFormatElement.getSize();

            // Wrap around the element pointer without using modulo
            if (++this.vertexFormatIndex >= elements.size()) {
                this.vertexFormatIndex -= elements.size();
            }

            this.vertexFormatElement = elements.get(this.vertexFormatIndex);
        } while (this.vertexFormatElement.getUsage() == VertexFormatElement.Usage.PADDING);

        if (this.defaultColor && this.vertexFormatElement.getUsage() == VertexFormatElement.Usage.COLOR) {
            this.color(this.defaultRed, this.defaultGreen, this.defaultBlue, this.defaultAlpha);
        }
    }

    @Override
    public void write(ModelQuadViewMutable quad) {
        this.growBuffer(ModelQuadUtil.VERTEX_SIZE_BYTES);

        quad.copyInto(this.byteBuffer, this.nextElementBytes);

        this.nextElementBytes += ModelQuadUtil.VERTEX_SIZE_BYTES;
        this.vertexCount += 4;
    }
}

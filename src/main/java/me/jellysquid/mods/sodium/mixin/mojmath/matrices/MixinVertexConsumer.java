package me.jellysquid.mods.sodium.mixin.mojmath.matrices;

import me.jellysquid.mods.sodium.client.util.math.vector.Matrix3fExtended;
import me.jellysquid.mods.sodium.client.util.math.vector.Matrix4fExtended;
import me.jellysquid.mods.sodium.client.util.math.vector.MatrixUtil;
import com.mojang.blaze3d.vertex.IVertexConsumer;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(IVertexConsumer.class)
public interface MixinVertexConsumer {
    @Shadow
    IVertexConsumer normal(float x, float y, float z);

    @Shadow
    IVertexConsumer vertex(double x, double y, double z);

    /**
     * @reason Avoid allocations
     * @author JellySquid
     */
    @Overwrite
    default IVertexConsumer vertex(Matrix4f matrix, float x, float y, float z) {
        Matrix4fExtended ext = MatrixUtil.getExtendedMatrix(matrix);
        float x2 = ext.transformVecX(x, y, z);
        float y2 = ext.transformVecY(x, y, z);
        float z2 = ext.transformVecZ(x, y, z);

        return this.vertex(x2, y2, z2);
    }

    /**
     * @reason Avoid allocations
     * @author JellySquid
     */
    @Overwrite
    default IVertexConsumer normal(Matrix3f matrix, float x, float y, float z) {
        Matrix3fExtended ext = MatrixUtil.getExtendedMatrix(matrix);
        float x2 = ext.transformVecX(x, y, z);
        float y2 = ext.transformVecY(x, y, z);
        float z2 = ext.transformVecZ(x, y, z);

        return this.normal(x2, y2, z2);
    }
}

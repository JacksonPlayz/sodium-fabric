package me.jellysquid.mods.sodium.mixin.mojmath.matrices;

import me.jellysquid.mods.sodium.client.util.Norm3b;
import me.jellysquid.mods.sodium.client.util.math.Matrix3fExtended;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Matrix3f.class)
public class MixinMatrix3f implements Matrix3fExtended {
    @Shadow
    protected float m00;

    @Shadow
    protected float m10;

    @Shadow
    protected float m20;

    @Shadow
    protected float m01;

    @Shadow
    protected float m11;

    @Shadow
    protected float m21;

    @Shadow
    protected float m02;

    @Shadow
    protected float m12;

    @Shadow
    protected float m22;

    @Override
    public float transformVecX(float x, float y, float z) {
        return this.m00 * x + this.m01 * y + this.m02 * z;
    }

    @Override
    public float transformVecY(float x, float y, float z) {
        return this.m10 * x + this.m11 * y + this.m12 * z;
    }

    @Override
    public float transformVecZ(float x, float y, float z) {
        return this.m20 * x + this.m21 * y + this.m22 * z;
    }

    @Override
    public void rotate(Quaternion quaternion) {
        boolean x = quaternion.getX() != 0.0F;
        boolean y = quaternion.getY() != 0.0F;
        boolean z = quaternion.getZ() != 0.0F;

        // Try to determine if this is a simple rotation on one axis component only
        if (x) {
            if (!y && !z) {
                this.rotateX(quaternion);
            } else {
                this.rotateXYZ(quaternion);
            }
        } else if (y) {
            if (!z) {
                this.rotateY(quaternion);
            } else {
                this.rotateXYZ(quaternion);
            }
        } else if (z) {
            this.rotateZ(quaternion);
        }
    }

    @Override
    public int computeNormal(Direction dir) {
        Vector3i faceNorm = dir.getDirectionVec();

        float x = faceNorm.getX();
        float y = faceNorm.getY();
        float z = faceNorm.getZ();

        float x2 = this.m00 * x + this.m01 * y + this.m02 * z;
        float y2 = this.m10 * x + this.m11 * y + this.m12 * z;
        float z2 = this.m20 * x + this.m21 * y + this.m22 * z;

        return Norm3b.pack(x2, y2, z2);
    }

    private void rotateX(Quaternion quaternion) {
        float x = quaternion.getX();
        float w = quaternion.getW();

        float xx = 2.0F * x * x;

        float tm11 = 1.0F - xx;
        float tm22 = 1.0F - xx;

        float xw = x * w;
        float tm21 = 2.0F * xw;
        float tm12 = 2.0F * -xw;

        float m01 = this.m01 * tm11 + this.m02 * tm21;
        float m02 = this.m01 * tm12 + this.m02 * tm22;
        float m11 = this.m11 * tm11 + this.m12 * tm21;
        float m12 = this.m11 * tm12 + this.m12 * tm22;
        float m21 = this.m21 * tm11 + this.m22 * tm21;
        float m22 = this.m21 * tm12 + this.m22 * tm22;

        this.m01 = m01;
        this.m02 = m02;
        this.m11 = m11;
        this.m12 = m12;
        this.m21 = m21;
        this.m22 = m22;
    }

    private void rotateY(Quaternion quaternion) {
        float y = quaternion.getY();
        float w = quaternion.getW();

        float yy = 2.0F * y * y;

        float tm00 = 1.0F - yy;
        float tm22 = 1.0F - yy;

        float yw = y * w;

        float tm20 = 2.0F * (-yw);
        float tm02 = 2.0F * (+yw);

        float m00 = this.m00 * tm00 + this.m02 * tm20;
        float m02 = this.m00 * tm02 + this.m02 * tm22;
        float m10 = this.m10 * tm00 + this.m12 * tm20;
        float m12 = this.m10 * tm02 + this.m12 * tm22;
        float m20 = this.m20 * tm00 + this.m22 * tm20;
        float m22 = this.m20 * tm02 + this.m22 * tm22;

        this.m00 = m00;
        this.m02 = m02;
        this.m10 = m10;
        this.m12 = m12;
        this.m20 = m20;
        this.m22 = m22;
    }

    private void rotateZ(Quaternion quaternion) {
        float z = quaternion.getZ();
        float w = quaternion.getW();

        float zz = 2.0F * z * z;

        float tm00 = 1.0F - zz;
        float tm11 = 1.0F - zz;

        float zw = z * w;

        float tm10 = 2.0F * (0.0F + zw);
        float tm01 = 2.0F * (0.0F - zw);

        float m00 = this.m00 * tm00 + this.m01 * tm10;
        float m01 = this.m00 * tm01 + this.m01 * tm11;
        float m10 = this.m10 * tm00 + this.m11 * tm10;
        float m11 = this.m10 * tm01 + this.m11 * tm11;
        float m20 = this.m20 * tm00 + this.m21 * tm10;
        float m21 = this.m20 * tm01 + this.m21 * tm11;

        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
        this.m20 = m20;
        this.m21 = m21;
    }

    private void rotateXYZ(Quaternion quaternion) {
        float x = quaternion.getX();
        float y = quaternion.getY();
        float z = quaternion.getZ();
        float w = quaternion.getW();

        float xx = 2.0F * x * x;
        float yy = 2.0F * y * y;
        float zz = 2.0F * z * z;

        float tm00 = 1.0F - yy - zz;
        float tm11 = 1.0F - zz - xx;
        float tm22 = 1.0F - xx - yy;

        float xy = x * y;
        float yz = y * z;
        float zx = z * x;
        float xw = x * w;
        float yw = y * w;
        float zw = z * w;

        float tm10 = 2.0F * (xy + zw);
        float tm01 = 2.0F * (xy - zw);
        float tm20 = 2.0F * (zx - yw);
        float tm02 = 2.0F * (zx + yw);
        float tm21 = 2.0F * (yz + xw);
        float tm12 = 2.0F * (yz - xw);

        float m00 = this.m00 * tm00 + this.m01 * tm10 + this.m02 * tm20;
        float m01 = this.m00 * tm01 + this.m01 * tm11 + this.m02 * tm21;
        float m02 = this.m00 * tm02 + this.m01 * tm12 + this.m02 * tm22;
        float m10 = this.m10 * tm00 + this.m11 * tm10 + this.m12 * tm20;
        float m11 = this.m10 * tm01 + this.m11 * tm11 + this.m12 * tm21;
        float m12 = this.m10 * tm02 + this.m11 * tm12 + this.m12 * tm22;
        float m20 = this.m20 * tm00 + this.m21 * tm10 + this.m22 * tm20;
        float m21 = this.m20 * tm01 + this.m21 * tm11 + this.m22 * tm21;
        float m22 = this.m20 * tm02 + this.m21 * tm12 + this.m22 * tm22;

        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
    }
}

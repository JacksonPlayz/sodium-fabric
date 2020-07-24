package me.jellysquid.mods.sodium.mixin.entity_rendering;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.model.light.EntityLighter;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer<T extends Entity> {
    @Shadow
    protected abstract int getBlockLight(T entity, BlockPos blockPos);

    private static final int POISON_VALUE = 0xDEADBEEF;

    private boolean isAdvancedLightEnabled = false;

    /**
     * @reason Use a return value poison to detect the default light path being used
     * @author JellySquid
     */
    @Inject(method = "getBlockLight", at = @At("HEAD"), cancellable = true)
    public void getBlockLight(T entity, BlockPos blockPos, CallbackInfoReturnable<Integer> cir) {
        if (this.isAdvancedLightEnabled) {
            cir.setReturnValue(POISON_VALUE);
        }
    }

    /**
     * @reason Check that the entity renderer doesn't have special lighting, and if so, use our new lighting system
     * @author JellySquid
     */
    @Overwrite(remap = false)
    public final int getPackedLight(T entity, float tickDelta) {
        BlockPos pos = new BlockPos(entity.getLook(tickDelta));

        int blockLight = this.getBlockLightWrapper(entity, pos);

        if (blockLight == 0xDEADBEEF) {
            return EntityLighter.getBlendedLight(entity, tickDelta);
        }

        return this.getSimpleLight(entity, tickDelta, blockLight);
    }

    private int getBlockLightWrapper(T entity, BlockPos pos) {
        this.isAdvancedLightEnabled = SodiumClientMod.options().quality.smoothLighting == SodiumGameOptions.LightingQuality.HIGH;

        int blockLight;

        try {
            blockLight = this.getBlockLight(entity, pos);
        } finally {
            // We're just being paranoid with a finally block...
            this.isAdvancedLightEnabled = false;
        }

        return blockLight;
    }

    // [VanillaCopy] EntityRenderer#getLight(Entity, float)
    private int getSimpleLight(T entity, float tickDelta, int blockLight) {
        return LightTexture.packLight(blockLight, entity.world.getLightFor(LightType.SKY, new BlockPos(entity.getLook(tickDelta))));
    }

    @Inject(method = "shouldRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/culling/ClippingHelper;isBoundingBoxInFrustum(Lnet/minecraft/util/math/AxisAlignedBB;)Z", shift = At.Shift.AFTER), cancellable = true)
    private void preShouldRender(T entity, ClippingHelper frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        // If the entity isn't culled already by other means, try to perform a second pass
        if (cir.getReturnValue() && !SodiumWorldRenderer.getInstance().isEntityVisible(entity)) {
            cir.setReturnValue(false);
        }
    }
}

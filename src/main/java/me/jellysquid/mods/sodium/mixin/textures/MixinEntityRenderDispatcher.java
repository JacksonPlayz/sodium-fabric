package me.jellysquid.mods.sodium.mixin.textures;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.jellysquid.mods.sodium.client.render.texture.SpriteUtil;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ModelBakery;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRendererManager.class)
public class MixinEntityRenderDispatcher {
    @Inject(method = "renderFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/IRenderTypeBuffer;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/IVertexBuilder;", shift = At.Shift.AFTER))
    private void preRenderFire(MatrixStack matrices, IRenderTypeBuffer vertices, Entity entity, CallbackInfo ci) {
        SpriteUtil.markSpriteActive(ModelBakery.LOCATION_FIRE_0.getSprite());
        SpriteUtil.markSpriteActive(ModelBakery.LOCATION_FIRE_1.getSprite());
    }
}

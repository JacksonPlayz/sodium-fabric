package me.jellysquid.mods.sodium.mixin.textures;

import me.jellysquid.mods.sodium.client.render.texture.SpriteUtil;
import com.mojang.blaze3d.vertex.IVertexConsumerProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.model.ModelLoader;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {
    @Inject(method = "renderFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/IVertexConsumer;", shift = At.Shift.AFTER))
    private void preRenderFire(MatrixStack matrices, VertexConsumerProvider vertices, Entity entity, CallbackInfo ci) {
        SpriteUtil.markSpriteActive(ModelLoader.FIRE_0.getSprite());
        SpriteUtil.markSpriteActive(ModelLoader.FIRE_1.getSprite());
    }
}

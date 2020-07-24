package me.jellysquid.mods.sodium.mixin.textures;

import me.jellysquid.mods.sodium.client.render.texture.SpriteUtil;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.renderer.OverlayRenderer;
import net.minecraft.client.renderer.model.ModelBakery;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OverlayRenderer.class)
public class MixinInGameOverlayRenderer {
    @Inject(method = "renderFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V", shift = At.Shift.AFTER))
    private static void preFireOverlayRender(Minecraft client, MatrixStack stack, CallbackInfo ci) {
        SpriteUtil.markSpriteActive(ModelBakery.LOCATION_FIRE_1.getSprite());
    }
}

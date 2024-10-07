
package com.guhao.client.gui;

import com.guhao.skills.GuHaoSkills;
import com.guhao.utils.ArrayUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.GameRenderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;



@Mod.EventBusSubscriber({Dist.CLIENT})
public class PassiveOverlay {
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void eventHandler(RenderGameOverlayEvent.Pre event) {
		Minecraft minecraft = Minecraft.getInstance();
		if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            int w = event.getWindow().getGuiScaledWidth();
            int h = event.getWindow().getGuiScaledHeight();
            int posX = w / 2;
            int posY = h / 2;
            LocalPlayerPatch lpp = EpicFightCapabilities.getEntityPatch(minecraft.player, LocalPlayerPatch.class);
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.setShaderColor(1, 1, 1, 1);

            if (lpp != null && lpp.getSkill(GuHaoSkills.GUHAO_PASSIVE) != null && lpp.getAnimator().getPlayerFor(null).getAnimation() instanceof StaticAnimation staticAnimation && ArrayUtils.isEyes(staticAnimation)) {
                    RenderSystem.setShaderTexture(0, new ResourceLocation("guhao:textures/gui/skills/guhao_passive.png"));
                    GuiComponent.blit(event.getMatrixStack(), posX - 16, posY - 16, 0, 0, 32, 32, 32, 32);
            }
            RenderSystem.depthMask(true);
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
	}
}

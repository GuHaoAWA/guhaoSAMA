package com.guhao.item;

import com.guhao.init.Tabs;
import com.guhao.renderers.GUHAORenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GUHAO extends SwordItem implements IAnimatable {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public String animationprocedure = "empty";
    public static ItemTransforms.TransformType transformType;

    public GUHAO() {
        super(new Tier() {
            public int getUses() {
                return 0;
            }

            public float getSpeed() {
                return 9.0f;
            }

            public float getAttackDamageBonus() {
                return 32f;
            }

            public int getLevel() {
                return 4;
            }

            public int getEnchantmentValue() {
                return 15;
            }


            public @NotNull Ingredient getRepairIngredient() {
                return Ingredient.of();
            }
        }, 3, -1.9f,
                new Item.Properties().tab(Tabs.TAB_GU_HAO_SAMA).fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new GUHAORenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

    public void getTransformType(ItemTransforms.TransformType type) {
        transformType = type;
    }

    protected <P extends IAnimatable> void customInstructionListener(CustomInstructionKeyframeEvent<P> event) {
        List<String> instructions = Arrays.stream(event.instructions.split(";")).filter(s -> s.length() > 0).toList();
        List<List<String>> instructionTokens = instructions.stream().map(s -> Arrays.stream(s.split(" ")).filter(tk -> tk.length() > 0).collect(Collectors.toList())).filter(tks -> !tks.isEmpty()).collect(Collectors.toList());
        if (instructionTokens.isEmpty())
            return;
        GUHAORenderer renderer = new GUHAORenderer();
        instructionTokens.stream().filter(tks -> !tks.isEmpty()).forEach(tks -> this.interpretFirstPersonInstructions(tks, renderer));
    }

    protected void interpretFirstPersonInstructions(List<String> tokens, GUHAORenderer renderer) {
        String firstTok = tokens.get(0);
        if (tokens.size() < 2)
            return;
        String boneName = tokens.get(1);
        if (firstTok.equals("set_hidden")) {
            boolean hidden = Boolean.valueOf(tokens.get(2));
            renderer.hideBone(boneName, hidden);
        } else if (firstTok.equals("move")) {
            float x = Float.valueOf(tokens.get(2));
            float y = Float.valueOf(tokens.get(3));
            float z = Float.valueOf(tokens.get(4));
            renderer.setBonePosition(boneName, x, y, z);
        } else if (firstTok.equals("rotate")) {
            float x = Float.valueOf(tokens.get(2));
            float y = Float.valueOf(tokens.get(3));
            float z = Float.valueOf(tokens.get(4));
            renderer.setBoneRotation(boneName, x, y, z);
        } else if (firstTok.equals("suppress_mod")) {
            renderer.suppressModification(boneName);
        } else if (firstTok.equals("allow_mod")) {
            renderer.allowModification(boneName);
        }
    }

    private <P extends Item & IAnimatable> PlayState idlePredicate(AnimationEvent<P> event) {
        if (transformType != null) {
            if (this.animationprocedure.equals("empty")) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("0", ILoopType.EDefaultLoopTypes.LOOP));
                return PlayState.CONTINUE;
            }
        }
        return PlayState.STOP;
    }

    private <P extends Item & IAnimatable> PlayState procedurePredicate(AnimationEvent<P> event) {
        if (transformType != null) {
            if (!(this.animationprocedure.equals("empty")) && event.getController().getAnimationState().equals(software.bernie.geckolib3.core.AnimationState.Stopped)) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(this.animationprocedure, ILoopType.EDefaultLoopTypes.PLAY_ONCE));
                if (event.getController().getAnimationState().equals(software.bernie.geckolib3.core.AnimationState.Stopped)) {
                    this.animationprocedure = "empty";
                    event.getController().markNeedsReload();
                }
            }
        }
        return PlayState.CONTINUE;
    }

    public void setupAnimationState(GUHAORenderer renderer, ItemStack stack, PoseStack matrixStack, float aimProgress) {
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController procedureController = new AnimationController(this, "procedureController", 0, this::procedurePredicate);
        data.addAnimationController(procedureController);
        AnimationController idleController = new AnimationController(this, "idleController", 0, this::idlePredicate);
        data.addAnimationController(idleController);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(new TextComponent("\u00A7k66666666666666666666666"));
        list.add(new TextComponent("\u00A74feel r'luh ot GuHao_"));
        list.add(new TextComponent("\u00A7k66666666666666666666666"));
    }
}
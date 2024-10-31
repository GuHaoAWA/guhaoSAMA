package com.guhao.item;

import com.guhao.client.particle.text.ColorPutter;
import com.guhao.events.HitEvent;
import com.guhao.renderers.GUHAORenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.jobin.stellariscraft.init.StellariscraftModTabs;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.LivingEntity;
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
import yesman.epicfight.world.item.WeaponItem;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GUHAO extends WeaponItem implements IAnimatable {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public String animationprocedure = "empty";
    public static boolean timeStop = false;
    public static ItemTransforms.TransformType transformType;

    public GUHAO() {
        super(new Tier() {

            public int getUses() {return 100000;}

            public float getSpeed() {
                return 9.0f;
            }

            public float getAttackDamageBonus() {
                return 23f;
            }

            public int getLevel() {
                return 4;
            }

            public int getEnchantmentValue() {
                return 99;
            }

            public @NotNull Ingredient getRepairIngredient() {return Ingredient.of(new ItemStack(Items.ENDER_EYE));}

              }, 3, -1.9f,
                new Item.Properties().tab(
//                        StellarisdlcModTabs.STELLARISDLC
                        StellariscraftModTabs.TAB_STELLARIS_CRAFT
                ).fireResistant().rarity(Rarity.EPIC));
    }

//    @Override
//    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player entity, @NotNull InteractionHand hand) {
//        InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
//        if (Screen.hasShiftDown()) {
//            timeStop = false;
//            try {
//                Field field = Minecraft.class.getDeclaredField("pause");
//                field.setAccessible(true);
//                field.set(Minecraft.getInstance(), false);
//            } catch (NoSuchFieldException | IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//        } else {
//            timeStop = true;
//            try {
//                Field field = Minecraft.class.getDeclaredField("pause");
//                field.setAccessible(true);
//                field.set(Minecraft.getInstance(), true);
//            } catch (NoSuchFieldException | IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return ar;
//    }


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
        list.add(new TextComponent("\u00A74\u00A7k6666666666guhao6666666666666"));
        list.add(new TextComponent("\u00A7c消散于迷雾之中吧..."));
        list.add(new TextComponent("<GuHao_>\u00A7clord ot bthnkor ng gn'th'bthnk"));
        list.add(new TextComponent("\u00A7cfeel r'luh ot GuHao_"));
        list.add(new TextComponent("\u00A74\u00A7k6666666666potato6666666666666"));
        list.add(new TextComponent("\n"));
        list.add(new TextComponent(ColorPutter.rainbow("\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7\u00A7")));
        list.add(new TextComponent("\n"));
        list.add(new TextComponent("\u00A74请查看jei"));
        list.add(new TextComponent("\n"));
        list.add(new TextComponent(ColorPutter.rainbow4(I18n.get("word.explain2"))));
        list.add(new TextComponent("\n"));
    }

    @Override
    public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
        boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
        HitEvent.execute(entity.level, entity.getX(), entity.getY(), entity.getZ(), entity, sourceentity);
        return retval;
    }
}
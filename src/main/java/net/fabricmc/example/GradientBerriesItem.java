package net.fabricmc.example;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.IndexedIterable;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;

public class GradientBerriesItem extends Item {

    public GradientBerriesItem(Settings settings) {
        super(settings);
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            IndexedIterable<RegistryEntry<StatusEffect>> iterable = Registry.STATUS_EFFECT.getIndexedEntries();
            StatusEffectInstance instanceToGive = new StatusEffectInstance(StatusEffects.LUCK);
            int index = (int)Math.floor(Math.random()*iterable.size());
            for(RegistryEntry<StatusEffect> i : iterable) {
                if(index == 0) {
                    instanceToGive = new StatusEffectInstance(i.value(), (int)Math.floor(Math.random()*1200), (int)Math.floor(Math.pow(Math.random(), 3)*10));
                    break;
                }
                index--;
            }
            if(instanceToGive.getEffectType().isInstant()) {
                instanceToGive = new StatusEffectInstance(instanceToGive.getEffectType(), 1, (int)Math.floor(Math.random()*2));
            }
            user.setStatusEffect(instanceToGive, user);
        }
        return super.finishUsing(stack, world, user);
    }
}

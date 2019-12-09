package com.jorlando989.mineChuckmod.init;

import java.util.ArrayList;
import java.util.List;

import com.jorlando989.mineChuckmod.items.ItemBase;
import com.jorlando989.mineChuckmod.items.food.FoodBase;
import com.jorlando989.mineChuckmod.items.food.FoodEffectBase;

import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;

public class ModItems {
	
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	//Items
	public static final Item RUBY = new ItemBase("ruby");
	public static final Item TAPE_PLAYER = new ItemBase("tape_player");
	
	//Food
	public static final Item EVIL_APPLE = new FoodEffectBase("evil_apple", 4, 2.4f, false, new PotionEffect(MobEffects.NAUSEA, 600, 1, false, true));
	
}

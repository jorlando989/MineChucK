package com.jorlando989.mineChuckmod.items.food;

import com.jorlando989.mineChuckmod.Main;
import com.jorlando989.mineChuckmod.init.ModItems;
import com.jorlando989.mineChuckmod.util.IHasModel;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;

public class FoodBase extends ItemFood implements IHasModel {
	
	public FoodBase(String name, int amount, float saturation, boolean isAnimalFood) {
		super(amount, saturation, isAnimalFood);
		
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(Main.tabMineChuck);
		
		ModItems.ITEMS.add(this);
	}
	
	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
	
}

package com.jorlando989.mineChuckmod.items;

import com.jorlando989.mineChuckmod.Main;
import com.jorlando989.mineChuckmod.init.ModItems;
import com.jorlando989.mineChuckmod.util.IHasModel;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {

	public ItemBase(String name) {
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

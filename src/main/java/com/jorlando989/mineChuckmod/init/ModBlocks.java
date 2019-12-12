package com.jorlando989.mineChuckmod.init;

import java.util.ArrayList;
import java.util.List;

import com.jorlando989.mineChuckmod.blocks.BlockBase;
import com.jorlando989.mineChuckmod.blocks.OscBlock;
import com.jorlando989.mineChuckmod.blocks.RubyBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks {
	
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block RUBY_BLOCK = new RubyBlock("ruby_block", Material.IRON);
	public static final Block OSC_BLOCK = new OscBlock("osc_block", Material.GROUND);
	
}

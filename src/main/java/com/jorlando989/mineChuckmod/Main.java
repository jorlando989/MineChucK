package com.jorlando989.mineChuckmod;

import com.jorlando989.mineChuckmod.blocks.OscBlock;
import com.jorlando989.mineChuckmod.init.ModItems;
import com.jorlando989.mineChuckmod.proxy.CommonProxy;
import com.jorlando989.mineChuckmod.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oscP5.OscMessage;
import oscP5.OscP5;

@Mod(modid=Reference.MOD_ID, name=Reference.NAME, version=Reference.VERSION)
public class Main {
	
	final OscP5 thisOscP5= new OscP5((Object)this, configInPort);	
	public World myWorld;
	
	// Configuration file variables
	public static Configuration configFile;					
	public static String configIpAddress = "127.0.0.1";
	public static int configOutPort = 6666;
	public static int configInPort = 6667;

	@Instance
	public static Main instance;
	
	@SidedProxy(clientSide=Reference.CLIENT_PROXY_CLASS, serverSide=Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void PreInit(FMLPreInitializationEvent event) {}
	
	public static void syncConfig() {
        configIpAddress = configFile.getString("Target IP", Configuration.CATEGORY_GENERAL, configIpAddress, "Target IP");
        configOutPort = configFile.getInt("Target Port (output)", Configuration.CATEGORY_GENERAL, configOutPort, 0, 65535, "Target Port (output)");
        configInPort = configFile.getInt("Receive Port (input)", Configuration.CATEGORY_GENERAL, configInPort, 0, 65535, "Receive Port (input)");
        
        if(configFile.hasChanged())
            configFile.save();
    }
	
	@SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
    	if(eventArgs.getModID().equals("mm"))
    		syncConfig();
    }
	
	@EventHandler
	public static void init(FMLInitializationEvent event) {
		
	}
	
	@EventHandler
	public static void PostInit(FMLPostInitializationEvent event) {
		
	}
	
	/* incoming osc message are forwarded to the oscEvent method. */
    void oscEvent(OscMessage theOscMessage) {
    	/* print the address pattern and the typetag of the received OscMessage */
    	System.out.println("#############> received an osc message: \n");
    	theOscMessage.print();
    	
    	switch (theOscMessage.addrPattern()) {
    		case "/mm/block/add":
    			addOscBlock(theOscMessage);
    			break;
    		case "/mm/block/remove":
    			removeBlock(theOscMessage);
    			break;       		
    	}
    }      

    private void addOscBlock(OscMessage theOscMessage){
    	
    	System.out.print("addOscBlock: message received\n");
		System.out.println(" addrpattern: "+theOscMessage.addrPattern());
    	System.out.println(" typetag: "+theOscMessage.typetag());
    	System.out.println(" length: "+theOscMessage.arguments().length);
    
    	// gets client world only
    	myWorld = Minecraft.getMinecraft().world;
    	   
		// Create block at xyz coordinates of specified type
		OscBlock thisBlock = new OscBlock("oscblock", Material.GROUND);
		String blockType = theOscMessage.get(3).stringValue();
	
		BlockPos location = new BlockPos(theOscMessage.get(0).intValue(), theOscMessage.get(1).intValue(), theOscMessage.get(2).intValue()); 
		IBlockState blockState = thisBlock.getDefaultState(); 
		myWorld.setBlockState(location, blockState);      
		System.out.println(" HERE: Osccraft > addOscBlock");
		// send OSC output now for newly created block
		thisBlock.blockAdded(theOscMessage.get(0).intValue(), theOscMessage.get(1).intValue(), theOscMessage.get(2).intValue());	
    }        
    
    private void oscAddedBlockOutput(Block thisBlock){}
    
    // Remove block at OSC coordinates
    private void removeBlock(OscMessage theOscMessage) { 
    	// 1.8 - this may have to run on the server (?) http://www.minecraftforge.net/forum/index.php?topic=33665.0
    	BlockPos location = new BlockPos(theOscMessage.get(0).intValue(), theOscMessage.get(1).intValue(), theOscMessage.get(2).intValue());
    	myWorld.setBlockToAir(location);
    }
    
    private void initBlocks(){
    	final String blockName = "OscDirt";
    	
    	// initialize osc-emitting "dirt" block
        Block oscblock = new OscBlock(blockName, Material.GROUND);
  
        oscblock.setHarvestLevel("shovel", 0);
    	proxy.registerRenderers();       
    }
	
	public static CreativeTabs tabMineChuck = new CreativeTabs("tab_minechuck") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.TAPE_PLAYER);
		}
	};
	
}

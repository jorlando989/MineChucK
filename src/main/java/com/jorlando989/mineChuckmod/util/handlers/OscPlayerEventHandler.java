package com.jorlando989.mineChuckmod.util.handlers;

import com.jorlando989.mineChuckmod.Main;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import netP5.NetAddress;
import oscP5.OscMessage;
import oscP5.OscP5;

public class OscPlayerEventHandler {
	
	final OscP5 thisOscP5= new OscP5((Object)this, 7000);
	final OscP5 thisOscP5Listener= new OscP5((Object)this, Main.configInPort);
	NetAddress playerRemoteLocation = new NetAddress(Main.configIpAddress, Main.configOutPort);
	NetAddress blockHitRemoteLocation = new NetAddress(Main.configIpAddress, Main.configOutPort);	
	NetAddress blockDestroyedRemoteLocation = new NetAddress(Main.configIpAddress, Main.configOutPort);
	NetAddress clickRemoteLocation = new NetAddress(Main.configIpAddress, Main.configOutPort);	
	OscMessage playerMessage = new OscMessage("/mm/player");
	OscMessage blockHitMessage = new OscMessage("/mm/block/hit");
	OscMessage blockDestroyedMessage = new OscMessage("/mm/block/destroyed");
	OscMessage clickMessage = new OscMessage("/mm/click");
    	
	@SubscribeEvent
	public void onLivingUpdateEvent(LivingUpdateEvent theEvent){
		// do something to player every update tick:
		if (theEvent.getEntity() instanceof EntityPlayer){			
			sendOSCPlayerPosition(theEvent);
		}
	}
	
	public void sendOSCPlayerPosition(LivingUpdateEvent theEvent){   	    		
		EntityPlayer player = (EntityPlayer) theEvent.getEntity();

	    if(Minecraft.getMinecraft().player.getEntityId() == player.getEntityId()){	    
	    	this.playerMessage.setAddrPattern("/mm/player");
	    	this.playerRemoteLocation = new NetAddress(Main.configIpAddress, Main.configOutPort);
	    	this.playerMessage.add((float)player.posX);
	    	this.playerMessage.add((float)player.posZ);
	    	this.playerMessage.add((float)player.posY);	    	
	    	this.playerMessage.add((float)player.rotationPitch);
	    	this.playerMessage.add((float)player.rotationYaw);
	    	this.playerMessage.add(Minecraft.getMinecraft().player.getEntityId());
	    	this.playerMessage.add(player.getEntityId());

	    	this.thisOscP5.send(playerMessage, playerRemoteLocation);	    
	    }
	    this.playerMessage.clear();
	}
	
	
	@SubscribeEvent
	public void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock theEvent) {
		sendOSCBlockHit(theEvent);
	}
	
	@SubscribeEvent
	public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock theEvent) {
		sendOSCBlockHit(theEvent);
	}
	
    public void sendOSCBlockHit(PlayerInteractEvent theEvent) {
    	EntityPlayer player = (EntityPlayer) theEvent.getEntityPlayer();
    	if(!player.world.isRemote) {            	       
    		// only sed to same client (ignore other client's block messages)
    		if(Minecraft.getMinecraft().player.getEntityId() == player.getEntityId()) {	
    			int blockX = theEvent.getPos().getX();
    			int blockY = theEvent.getPos().getY();
    			int blockZ = theEvent.getPos().getZ();
    			int face = theEvent.getFace().getIndex();  // D-U-N-S-W-E  	
    			
    			BlockPos location = new BlockPos(blockX, blockY, blockZ);    			
    			String blockname = ((IBlockAccess) theEvent).getBlockState(location).getBlock().getUnlocalizedName().substring(5);
    			
    			int blockType;
    			this.blockHitMessage.setAddrPattern("/mm/block/hit");
    			this.blockHitRemoteLocation = new NetAddress(Main.configIpAddress, Main.configOutPort);
    			this.blockHitMessage.add(blockX);
    			this.blockHitMessage.add(blockZ);
    			this.blockHitMessage.add(blockY);
    			this.blockHitMessage.add(face);
    			this.blockHitMessage.add(Minecraft.getMinecraft().player.getEntityId());
    			this.blockHitMessage.add(player.getEntityId());
	    
    			if(blockname.compareTo("air") == 0) {    		
    				blockType=0; // DESTROYED BLOCK: AIR    	    	
    			} else if(blockname.compareTo("OscDirt") == 0) {    		    		
    				blockType=1; // DIRT
    			} else if(blockname.compareTo("OscStone") == 0) {    		
    				blockType=2; // GRASS
    			} else if(blockname.compareTo("OscKeystone") == 0) {    		    		
    				blockType=3; // STONE    		
    			} else if(blockname.compareTo("log") == 0) {    		    		
    				blockType=4; // LOG
    			} else if(blockname.compareTo("leaves") == 0) {    		    		
    				blockType=5; // LEAVES
    			} else if(blockname.compareTo("sand") == 0) {    		    		
    				blockType=6; // SAND    		    	    		    	    	    		
    			} else {
    				blockType=7; // UNKNOWN TYPE
    				// reeds, 
    			}
    			
    			this.blockHitMessage.add(blockType);
    			this.thisOscP5.send(blockHitMessage, blockHitRemoteLocation);
    		}
    		this.blockHitMessage.clear();    
    	}
    }
    
	@SubscribeEvent		
	public void onBreakEvent(BreakEvent theEvent) {
		sendOSCBlockDestroyed(theEvent);
	}
	
	//@SideOnly(Side.CLIENT)
    public void sendOSCBlockDestroyed(BreakEvent theEvent) {
		int blockX = theEvent.getPos().getX();
		int blockY = theEvent.getPos().getY();
		int blockZ = theEvent.getPos().getZ();
    	
    	EntityPlayer player = (EntityPlayer) theEvent.getPlayer();

		BlockPos location = new BlockPos(blockX, blockY, blockZ);    			
		String blockname = theEvent.getWorld().getBlockState(location).getBlock().getUnlocalizedName().substring(5);
    	
    	int blockType;
    	this.blockDestroyedMessage.setAddrPattern("/mm/block/destroyed");
    	this.blockDestroyedRemoteLocation = new NetAddress(Main.configIpAddress, Main.configOutPort);
    	this.blockDestroyedMessage.add(blockX);
    	this.blockDestroyedMessage.add(blockZ);
    	this.blockDestroyedMessage.add(blockY);
    	this.blockDestroyedMessage.add(Minecraft.getMinecraft().player.getEntityId());
	    this.blockDestroyedMessage.add(player.getEntityId());
    	
    	if(blockname.compareTo("air") == 0) {    		
    		blockType=0; // DESTROYED BLOCK: AIR    	    	
    	} else if(blockname.compareTo("OscDirt") == 0) {    		    		
    		blockType=1; // DIRT
    	} else if(blockname.compareTo("OscStone") == 0) {    		
    		blockType=2; // GRASS
    	} else if(blockname.compareTo("OscKeystone") == 0) {    		    		
    		blockType=3; // STONE    		
    	} else if(blockname.compareTo("log") == 0) {    		    		
    		blockType=4; // LOG
    	} else if(blockname.compareTo("leaves") == 0) {    		    		
    		blockType=5; // LEAVES
    	} else if(blockname.compareTo("sand") == 0) {    		    		
    		blockType=6; // SAND    		    	    		    	    	    		
    	} else {
    		blockType=7; // UNKNOWN TYPE
    	}
    	this.blockDestroyedMessage.add(blockType);
    	
    	if(Minecraft.getMinecraft().player.getEntityId() == player.getEntityId()){	    
    		this.thisOscP5.send(blockDestroyedMessage, blockDestroyedRemoteLocation);
	    }
    	this.blockDestroyedMessage.clear();    	
    }
}

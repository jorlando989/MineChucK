# MineChucK
a minecraft mod using OSC and ChucK
- implements Open Sound Control via OscP5
---
### Features:
	- OSC Blocks create a persistent sound when placed. 
	  Sound changes based on their position in the world.
		- Plays different sound file based on x
		- Reverbs for different length of time based on y.
		
---
### Usage Instructions
	Need:
		- Minecraft 1.12.2 
		- Forge 14.23.5.2768
		- MiniAudicle
		- Eclipse 
	1. Open Minechuck Mod project in Eclipse
	2. Open minechuck_receiver.ck in MiniAudicle
	3. In Eclipse, click the run button. This should open an instance of Minecraft with the mod running.
	4. Once this loads (could take a minute), use "/gamemode creative" to enter creative mode.
	5. Open the inventory using E and go to the custom creative tab. This will have all of the mod blocks.
	6. Choose the OSC block and place anywhere you want to hear a sound.

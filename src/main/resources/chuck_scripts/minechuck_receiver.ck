/*
*. MineChuck (Minecraft 1.12.2 / Forge 14.23.5.2768)
*  ---------------------------------------------
*
* DATA EXAMPLES
*
* /mm/block/placed 114 230 63 11174 11174
*    (int x, y, z, playerid, serverid)
*
*/

public class Audio{  
    
    fun void boom(float rmix, NRev r, Gain g, SndBuf sb){
        rmix => r.mix;
        0.1 => g.gain;
        sb => g;
        300::ms => now;
    }
    /* block_placed audio here */
    fun void block_placed(int debug, int index, int x, int y, int z, int playerid, int serverid, time placedTime){
        //when block is placed
        // play a sound based on x,y,z and have it fade out
        
        Gain g => NRev r => dac;
        0.1 => g.gain;
        0.1 => r.mix;
        SndBuf sb;
        
        ["kick_01.wav", "kick_04.wav", "clap_01.wav", "click_01.wav", "click_02.wav",
            "hihat_01.wav", "hihat_02.wav", "hihat_04.wav", "hihat_01.wav", "snare_01.wav",
            "snare_02.wav", "snare_03.wav"] @=> string files[];
        
        // load file
        Std.ftoi(Math.floor((Std.fabs(x)/4))) % files.size() => int choice;
        <<<"choice:", choice>>>;
        <<<"test", Std.ftoi(Math.floor((Std.fabs(x)/4))) % files.size()>>>;
        me.dir() + files[choice] => sb.read;

        0.1 => float rmix;
        repeat(2){
            spork ~ boom(rmix, r, g, sb);
            (Std.fabs(y) + 50)::ms => now;
            rmix + 0.3 => rmix;
        }
        2::second => now;
        
        if( debug )
            <<< "receive_block_placed: " + index, x, y, z, playerid, serverid, placedTime >>>;                
    }
}

class Block{
    int index;
    int coordinates[3];
    int playerid;
    int serverid;
    time placedTime, destroyedTime;
    int active;
    int blockType;
    
    fun void create(int _index, int _x, int _y, int _z, int _playerid, int _serverid){
        _index => index;
        [ _x, _y, _z ] @=> coordinates;
        _playerid => playerid;
        _serverid => serverid;
        1 => active;
        now => placedTime;   
        -1 => blockType;     
    }
    
    fun void destroy(int _blockType){
        0 => active;
        now => destroyedTime;
        _blockType => blockType;        
    }
}

Audio audio;  // all audio processing in Audio class

6666 => int receive_port;

1 => int debug_placed;
1 => int debug_destroyed;
1 => int debug_hit;
1 => int debug_position;

Block blocks_placed[0];

class Helpers{
    [ "air", "OscBlock"] @=> string block_types[];
    
    fun void print_placed_blocks(){
        for(0=>int i; i<blocks_placed.size(); i++){
            <<< blocks_placed[i].active, blocks_placed[i].placedTime, blocks_placed[i].destroyedTime, blocks_placed[i].coordinates[0], blocks_placed[i].coordinates[1], blocks_placed[i].coordinates[2] >>>;
        }        
    }
    
    fun string get_block_type(int id){            
        return block_types[id];
    }
}

Helpers help;

fun void receive_block_placed(){
    OscIn oin;
    OscMsg msg;
    receive_port => oin.port;        
    oin.addAddress( "/mm/block/placed, i i i i i" ); // x y z playerid entityid
    
    int x, y, z, playerid, serverid, index;  
    time placedTime;
    
    while ( true ){
        oin => now;
        
        while ( oin.recv(msg) != 0 ){                                 
            msg.getInt(0) => x;
            msg.getInt(1) => y;
            msg.getInt(2) => z;
            msg.getInt(3) => playerid;
            msg.getInt(4) => serverid;
            now => time placedTime;
            
            blocks_placed.size() => index;
            
            Block currentBlock;
            currentBlock.create( index, x, y, z, playerid, serverid );            
            blocks_placed << currentBlock;
             
            // int index, int x, int y, int z, int playerid, int serverid, time placedTime)
            spork ~ audio.block_placed(debug_placed, index, x, y, z, playerid, serverid, placedTime);
             
            <<< blocks_placed[blocks_placed.size()-1].index, blocks_placed[blocks_placed.size()-1].placedTime >>>;
        }        
    }        
}

/*fun void mark_block_destroyed(int x, int y, int z, int blockType){
    blocks_placed.size() => int arraySize;
    
    for ( 0 => int i; i<arraySize; i++ ){
        if( blocks_placed[i].coordinates[0] == x){
            if( blocks_placed[i].coordinates[1] == y) {
                if( blocks_placed[i].coordinates[2] == z){
                    if( blocks_placed[i].active == 1){
                        blocks_placed[i].destroy(blockType); // set blockType on destroy for archiving                        
                        help.print_placed_blocks();                        
                    }
                }
            }
        }
    }
}*/

/*fun void receive_block_destroyed(){
    OscIn oin;
    OscMsg msg;
    receive_port => oin.port;        
    oin.addAddress( "/osccraft/block/destroyed, i i i i i i" );
    
    int x, y, z, playerid, serverid, blockType;
    time destroyedTime;
    
    while ( true ){
        oin => now;
        
        while ( oin.recv(msg) != 0 ){                                 
            msg.getInt(0) => x;
            msg.getInt(1) => y;
            msg.getInt(2) => z;
            msg.getInt(3) => playerid;
            msg.getInt(4) => serverid;
            msg.getInt(5) => blockType;
            now => destroyedTime;

            spork ~ mark_block_destroyed(x, y, z, blockType);            
            spork ~ audio.block_destroyed(debug_destroyed, x, y, z, playerid, serverid, blockType, destroyedTime);            
        }        
    }        
}*/

/*fun void receive_block_hit(){
    OscIn oin;
    OscMsg msg;
    receive_port => oin.port;        
    oin.addAddress( "/osccraft/block/hit, i i i i i i i" );
    
    int x, y, z, face, playerid, serverid, blockType;
    time hitTime;
    
    while ( true ){
        oin => now;
        
        while ( oin.recv(msg) != 0 ){                                 
            msg.getInt(0) => x;
            msg.getInt(1) => y;
            msg.getInt(2) => z;
            msg.getInt(3) => face;
            msg.getInt(4) => playerid;
            msg.getInt(5) => serverid;
            msg.getInt(6) => blockType;
            now => hitTime;           
            
            spork ~ audio.block_hit(debug_hit, x, y, z, face, playerid, serverid, blockType, hitTime);
        }        
    }    
}*/

/*fun void receive_player_position(){
    //x y z pitch yaw playerid entityid
    OscIn oin;
    OscMsg msg;
    receive_port => oin.port;        
    oin.addAddress( "/osccraft/player, f f f f f i i" );
        
    float x, y, z, pitch, yaw; 
    int playerid, serverid, blockType;
    time destroyedTime;
    
    while ( true ){
        oin => now;
        
        while ( oin.recv(msg) != 0 ){                                 
            msg.getFloat(0) => x;
            msg.getFloat(1) => y;
            msg.getFloat(2) => z;
            msg.getFloat(3) => pitch;
            msg.getFloat(4) => yaw;
            msg.getInt(5) => playerid;
            msg.getInt(6) => serverid;
            
            spork ~ audio.player_position(debug_position, x, y, z, pitch, yaw, playerid, serverid);
        }        
    }      
}*/

spork ~ receive_block_placed();
//spork ~ receive_block_destroyed();
//spork ~ receive_block_hit();
//spork ~ receive_player_position();

1::day => now;

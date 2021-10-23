package net.spookysquad.spookster.utils;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class BlockUtil extends Wrapper {

    public static Block getBlock(AxisAlignedBB bb) {
       int y = (int) bb.minY;

       for(int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; ++x) {
    	   for(int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; ++z) {
    		   Block block = getWorld().getBlock(x, y, z);
    		   if(block != null) {
    			   return block;
    		   }
          }
       }

       return null;
    }
}

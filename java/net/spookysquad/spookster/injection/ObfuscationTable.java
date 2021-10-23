package net.spookysquad.spookster.injection;

import com.mumfrey.liteloader.core.runtime.Obf;


public class ObfuscationTable extends Obf
{
	public static ObfuscationTable shutdownMinecraftApplet = new ObfuscationTable("func_71405_e", "e", "shutdownMinecraftApplet");
	
	public static ObfuscationTable Block = new ObfuscationTable("net.minecraft.block.Block", "aji");
	public static ObfuscationTable addCollisionBoxesToList = new ObfuscationTable("func_149743_a", "a", "addCollisionBoxesToList");
	public static ObfuscationTable getPlayerRelativeBlockHardness = new ObfuscationTable("func_149737_a", "a", "getPlayerRelativeBlockHardness");
	public static ObfuscationTable RenderBlocks = new ObfuscationTable("net.minecraft.client.renderer.RenderBlocks", "blm");
	public static ObfuscationTable renderBlockByRenderType = new ObfuscationTable("func_147805_b", "b", "renderBlockByRenderType");
	public static ObfuscationTable preMotionUpdate = new ObfuscationTable("func_71166_b", "a", "sendMotionUpdates");
	public static ObfuscationTable postMotionUpdate = new ObfuscationTable("func_71166_b", "a", "sendMotionUpdates");

	public static ObfuscationTable Entity = new ObfuscationTable("net.minecraft.entity.Entity", "sa");
	public static ObfuscationTable EntityPlayer = new ObfuscationTable("net.minecraft.entity.player.EntityPlayer", "yz");
	public static ObfuscationTable EntityPlayerSP = new ObfuscationTable("net.minecraft.client.entity.EntityPlayerSP", "blk");
	public static ObfuscationTable isEntityInsideOpaqueBlock = new ObfuscationTable("func_70094_T", "aa", "isEntityInsideOpaqueBlock");
	public static ObfuscationTable pushOutOfBlocks = new ObfuscationTable("func_145771_j", "j", "pushOutOfBlocks");
	public static ObfuscationTable AxisAlignedBB = new ObfuscationTable("net.minecraft.util.AxisAlignedBB", "azt");
	public static ObfuscationTable World = new ObfuscationTable("net.minecraft.world.World", "ahb");
	public static ObfuscationTable renderHand = new ObfuscationTable("func_78476_b", "b", "renderHand");
	public static ObfuscationTable orientCamera = new ObfuscationTable("func_78467_g", "g", "orientCamera");
	        
	public static ObfuscationTable PlayerControllerMP = new ObfuscationTable("net.minecraft.client.multiplayer.PlayerControllerMP", "bje");
	public static ObfuscationTable onPlayerDestroyBlock = new ObfuscationTable("func_78751_a", "a", "onPlayerDestroyBlock");
	public static ObfuscationTable onPlayerRightClick = new ObfuscationTable("func_78760_a", "a", "onPlayerRightClick");
	public static ObfuscationTable attackEntity = new ObfuscationTable("func_78764_a", "a", "attackEntity");
	public static ObfuscationTable blockHitDelay = new ObfuscationTable("field_78781_i", "i", "blockHitDelay");
	public static ObfuscationTable curBlockDamageMP = new ObfuscationTable("field_78770_f", "f", "curBlockDamageMP");
	
	public static ObfuscationTable ItemStack = new ObfuscationTable("net.minecraft.item.ItemStack", "add");
	public static ObfuscationTable Vec3 = new ObfuscationTable("net.minecraft.util.Vec3", "azw");

	public static ObfuscationTable NetHandlerPlayClient = new ObfuscationTable("net.minecraft.client.network.NetHandlerPlayClient", "bjb");
	public static ObfuscationTable Packet = new ObfuscationTable("net.minecraft.network.Packet", "ft");
	public static ObfuscationTable addToSendQueue = new ObfuscationTable("func_147297_a", "a", "addToSendQueue");
	
	
	public static ObfuscationTable RendererLivingEntity = new ObfuscationTable("net.minecraft.client.renderer.entity.RendererLivingEntity", "boh");
	public static ObfuscationTable doRender = new ObfuscationTable("func_76986_a", "a", "doRender");
	public static ObfuscationTable passSpecialRender = new ObfuscationTable("func_77033_b", "b", "passSpecialRender");
	public static ObfuscationTable Render = new ObfuscationTable("net.minecraft.client.renderer.entity.Render", "bno");
	public static ObfuscationTable renderLivingLabel = new ObfuscationTable("func_147906_a", "a", "renderLivingLabel");
	public static ObfuscationTable EntityLivingBase = new ObfuscationTable("net.minecraft.entity.EntityLivingBase", "sv");
	
    protected ObfuscationTable(final String seargeName, final String obfName) {
        super(seargeName, obfName, seargeName);
    }
    
    protected ObfuscationTable(final String seargeName, final String obfName, final String mcpName) {
        super(seargeName, obfName, mcpName);
    }
}

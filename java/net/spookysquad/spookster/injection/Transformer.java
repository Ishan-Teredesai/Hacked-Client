package net.spookysquad.spookster.injection;

import java.util.List;

import com.mumfrey.liteloader.core.runtime.Obf;
import com.mumfrey.liteloader.transformers.event.Event;
import com.mumfrey.liteloader.transformers.event.EventInjectionTransformer;
import com.mumfrey.liteloader.transformers.event.MethodInfo;
import com.mumfrey.liteloader.transformers.event.inject.BeforeReturn;
import com.mumfrey.liteloader.transformers.event.inject.MethodHead;

public class Transformer extends EventInjectionTransformer
{	
    @Override
	protected void addEvents() {
    	String listenerClass = "net.spookysquad.spookster.injection.GameEvents";
    	
    	final Event preMotionUpdateEvent = Event.getOrCreate("preMotionUpdateEvent", true);
        final MethodInfo preMotionUpdateMethodInfo = new MethodInfo(Obf.EntityClientPlayerMP, (Obf)ObfuscationTable.preMotionUpdate, (Object)Void.TYPE, new Object[0]);
        final MethodHead preMotionUpdateInjectionPoint = new MethodHead();
        this.addEvent(preMotionUpdateEvent, preMotionUpdateMethodInfo, preMotionUpdateInjectionPoint);
        preMotionUpdateEvent.addListener(new MethodInfo(listenerClass, "preMotionUpdateEvent"));
        
        final Event postMotionUpdateEvent = Event.getOrCreate("postMotionUpdateEvent");
        final MethodInfo postMotionUpdateMethodInfo = new MethodInfo(Obf.EntityClientPlayerMP, (Obf)ObfuscationTable.postMotionUpdate, (Object)Void.TYPE, new Object[0]);
        final BeforeReturn postMotionUpdateInjectionPoint = new BeforeReturn();
        this.addEvent(postMotionUpdateEvent, postMotionUpdateMethodInfo, postMotionUpdateInjectionPoint);
        postMotionUpdateEvent.addListener(new MethodInfo(listenerClass, "postMotionUpdateEvent"));
        
        final Event onRenderBlockByRenderTypeEvent = Event.getOrCreate("onRenderBlockByRenderTypeEvent", true);
        final MethodInfo onRenderBlockByRenderTypeMethodInfo = new MethodInfo((Obf)ObfuscationTable.RenderBlocks, (Obf)ObfuscationTable.renderBlockByRenderType, (Object)Boolean.TYPE, new Object[] { ObfuscationTable.Block, Integer.TYPE, Integer.TYPE, Integer.TYPE });
        final MethodHead onRenderBlockByRenderTypeInjectionPoint = new MethodHead();
		this.addEvent(onRenderBlockByRenderTypeEvent, onRenderBlockByRenderTypeMethodInfo, onRenderBlockByRenderTypeInjectionPoint);
        onRenderBlockByRenderTypeEvent.addListener(new MethodInfo(listenerClass, "onRenderBlockByRenderTypeEvent"));
       
        final Event onPushOutOfBlocksEvent = Event.getOrCreate("onPushOutOfBlocksEvent", true);
        final MethodInfo onPushOutOfBlocksMethodInfo = new MethodInfo((Obf)ObfuscationTable.EntityPlayerSP, (Obf)ObfuscationTable.pushOutOfBlocks, (Object)Boolean.TYPE, new Object[] { Double.TYPE, Double.TYPE, Double.TYPE });
        final MethodHead onPushOutOfBlocksInjectionPoint = new MethodHead();
        this.addEvent(onPushOutOfBlocksEvent, onPushOutOfBlocksMethodInfo, onPushOutOfBlocksInjectionPoint);
        onPushOutOfBlocksEvent.addListener(new MethodInfo(listenerClass, "onPushOutOfBlocksEvent"));
        
        final Event isEntityInsideOpaqueBlockEvent = Event.getOrCreate("isEntityInsideOpaqueBlockEvent", true);
        final MethodInfo isEntityInsideOpaqueBlockEventMethodInfo = new MethodInfo((Obf)ObfuscationTable.Entity, (Obf)ObfuscationTable.isEntityInsideOpaqueBlock, (Object)Boolean.TYPE, new Object[0]);
        final MethodHead isEntityInsideOpaqueBlockEventInjectionPoint = new MethodHead();
        this.addEvent(isEntityInsideOpaqueBlockEvent, isEntityInsideOpaqueBlockEventMethodInfo, isEntityInsideOpaqueBlockEventInjectionPoint);
        isEntityInsideOpaqueBlockEvent.addListener(new MethodInfo(listenerClass, "isEntityInsideOpaqueBlockEvent"));
        
        final Event onGetPlayerRelativeBlockHardnessEvent = Event.getOrCreate("onGetPlayerRelativeBlockHardnessEvent", true);
        final MethodInfo onGetPlayerRelativeBlockHardnessMethodInfo = new MethodInfo((Obf)ObfuscationTable.Block, (Obf)ObfuscationTable.getPlayerRelativeBlockHardness, (Object)Float.TYPE, new Object[] { ObfuscationTable.EntityPlayer, ObfuscationTable.World, Integer.TYPE, Integer.TYPE, Integer.TYPE });
        final MethodHead onGetPlayerRelativeBlockHardnessInjectionPoint = new MethodHead();
        this.addEvent(onGetPlayerRelativeBlockHardnessEvent, onGetPlayerRelativeBlockHardnessMethodInfo, onGetPlayerRelativeBlockHardnessInjectionPoint);
        onGetPlayerRelativeBlockHardnessEvent.addListener(new MethodInfo(listenerClass, "onGetPlayerRelativeBlockHardnessEvent"));
        
        final Event onPassSpecialRenderEvent = Event.getOrCreate("onPassSpecialRenderEvent", true);
        final MethodInfo onPassSpecialRenderMethodInfo = new MethodInfo((Obf)ObfuscationTable.RendererLivingEntity, (Obf)ObfuscationTable.passSpecialRender, (Object)Void.TYPE, new Object[] { ObfuscationTable.EntityLivingBase, Double.TYPE, Double.TYPE, Double.TYPE });
        final MethodHead onPassSpecialRenderInjectionPoint = new MethodHead();
        this.addEvent(onPassSpecialRenderEvent, onPassSpecialRenderMethodInfo, onPassSpecialRenderInjectionPoint);
        onPassSpecialRenderEvent.addListener(new MethodInfo(listenerClass, "onPassSpecialRenderEvent"));
        
        final Event onRenderLivingLabelEvent = Event.getOrCreate("onRenderLivingLabelEvent", true);
        final MethodInfo onRenderLivingLabelMethodInfo = new MethodInfo((Obf)ObfuscationTable.Render, (Obf)ObfuscationTable.renderLivingLabel, (Object)Void.TYPE, new Object[] { ObfuscationTable.Entity, String.class, Double.TYPE, Double.TYPE, Double.TYPE, Integer.TYPE });
        final MethodHead onRenderLivingLabelInjectionPoint = new MethodHead();
        this.addEvent(onRenderLivingLabelEvent, onRenderLivingLabelMethodInfo, onRenderLivingLabelInjectionPoint);
        onRenderLivingLabelEvent.addListener(new MethodInfo(listenerClass, "onRenderLivingLabelEvent"));   
    
        final Event onAddToSendQueueEvent = Event.getOrCreate("onAddToSendQueueEvent", true);
        final MethodInfo onAddToSendQueueMethodInfo = new MethodInfo((Obf)ObfuscationTable.NetHandlerPlayClient, ObfuscationTable.addToSendQueue, (Object)Void.TYPE, new Object[] {ObfuscationTable.Packet});
        final MethodHead onAddToSendQueueInjectionPoint = new MethodHead();
        this.addEvent(onAddToSendQueueEvent, onAddToSendQueueMethodInfo, onAddToSendQueueInjectionPoint);
        onAddToSendQueueEvent.addListener(new MethodInfo(listenerClass, "onAddToSendQueueEvent"));
        
        final Event onAddCollisionEvent = Event.getOrCreate("onAddCollisionEvent", true);
        final MethodInfo onAddCollisionMethodInfo = new MethodInfo(ObfuscationTable.Block, ObfuscationTable.addCollisionBoxesToList, Void.TYPE, new Object[] {ObfuscationTable.World, Integer.TYPE, Integer.TYPE, Integer.TYPE, ObfuscationTable.AxisAlignedBB, List.class, ObfuscationTable.Entity});
        final MethodHead onAddCollisionInjectionPoint = new MethodHead();
        this.addEvent(onAddCollisionEvent, onAddCollisionMethodInfo, onAddCollisionInjectionPoint);
        onAddCollisionEvent.addListener(new MethodInfo(listenerClass, "onAddCollisionEvent"));
        
        final Event onAttackEntityEvent = Event.getOrCreate("onAttackEntityEvent", true);
        final MethodInfo onAttackEntityMethodInfo = new MethodInfo(ObfuscationTable.PlayerControllerMP, ObfuscationTable.attackEntity, Void.TYPE, new Object[] {ObfuscationTable.EntityPlayer, ObfuscationTable.Entity});
        final MethodHead onAttackEntityInjectionPoint = new MethodHead();
        this.addEvent(onAttackEntityEvent, onAttackEntityMethodInfo, onAttackEntityInjectionPoint);
        onAttackEntityEvent.addListener(new MethodInfo(listenerClass, "onAttackEntityEvent"));
        
        // public void onPlayerDamageBlock(int p_78759_1_, int p_78759_2_, int p_78759_3_, int p_78759_4_)
        final Event onPlayerDestroyBlockEvent = Event.getOrCreate("onPlayerDestroyBlockEvent", true);
        final MethodInfo onPlayerDestroyBlockMethodInfo = new MethodInfo(ObfuscationTable.PlayerControllerMP, ObfuscationTable.onPlayerDestroyBlock, Boolean.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        final MethodHead onPlayerDestroyBlockInjectionPoint = new MethodHead();
        this.addEvent(onPlayerDestroyBlockEvent, onPlayerDestroyBlockMethodInfo, onPlayerDestroyBlockInjectionPoint);
        onPlayerDestroyBlockEvent.addListener(new MethodInfo(listenerClass, "onPlayerDestroyBlock"));
        
        final Event onRenderHandEvent = Event.getOrCreate("renderHand", true);
        final MethodInfo onRenderHandMethodInfo = new MethodInfo(ObfuscationTable.EntityRenderer, ObfuscationTable.renderHand, Void.TYPE, new Object[] {Float.TYPE, Integer.TYPE});
        final MethodHead onRenderHandInjectionPoint = new MethodHead();
        this.addEvent(onRenderHandEvent, onRenderHandMethodInfo, onRenderHandInjectionPoint);
        onRenderHandEvent.addListener(new MethodInfo(listenerClass, "renderHand"));
        
        final Event onShutdownMinecraftAppletEvent = Event.getOrCreate("onShutdownMinecraftApplet", true);
        final MethodInfo onShutdownMinecraftAppletMethodInfo = new MethodInfo(ObfuscationTable.Minecraft, ObfuscationTable.shutdownMinecraftApplet, Void.TYPE, new Object[] {});
        final MethodHead onShutdownMinecraftAppletInjectionPoint = new MethodHead();
        this.addEvent(onShutdownMinecraftAppletEvent, onShutdownMinecraftAppletMethodInfo, onShutdownMinecraftAppletInjectionPoint);
        onShutdownMinecraftAppletEvent.addListener(new MethodInfo(listenerClass, "onShutdownMinecraftApplet"));
        
        
    }
}

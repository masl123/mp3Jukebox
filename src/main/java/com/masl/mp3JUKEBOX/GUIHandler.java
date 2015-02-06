package com.masl.mp3JUKEBOX;

import org.darkstorm.minecraft.gui.ExampleGuiManager;
import org.darkstorm.minecraft.gui.GuiManager;
import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GUIHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}

	
	protected  OptionsGui mgr = new OptionsGui();
	protected  GuiManagerDisplayScreen screen =  new GuiManagerDisplayScreen(mgr);
	
	
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 388850) {
			return screen;
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent()
	public void onEvent(KeyInputEvent event) {
		if (mp3Jukebox.instance.keyBindings.get(0).isPressed()){
			EntityPlayer p = Minecraft.getMinecraft().thePlayer;   
			p.openGui(mp3Jukebox.instance, 388850 , Minecraft.getMinecraft().theWorld, (int)p.posX, (int)p.posY, (int)p.posZ);
		}
	}
	
	public void setup(){
		mgr.setup();
	}
}

  /*
  * mp3JukeBox - a mp3 Player Mod for Minecraft
  * Copyright (C) 2015 masll (minecraftforum.net)
  * 
  * This program is free software: you can redistribute it and/or modify
  *  it under the terms of the GNU General Public License as published by
  *  the Free Software Foundation, either version 3 of the License, or
  *  (at your option) any later version.
  *
  *  This program is distributed in the hope that it will be useful,
  *  but WITHOUT ANY WARRANTY; without even the implied warranty of
  *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  *  GNU General Public License for more details.
  *
  *  You should have received a copy of the GNU General Public License
  *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
  */


package com.masl.mp3JUKEBOX;

import org.darkstorm.minecraft.gui.ExampleGuiManager;
import org.darkstorm.minecraft.gui.GuiManager;
import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GUIHandler implements IGuiHandler {
	
	private  CustomGuiManagerDisplayScreen screen;
	protected  OptionsGui mgr;
	
	public GUIHandler(Player mp3Player){
		mgr = new OptionsGui(mp3Player);
		screen =  new CustomGuiManagerDisplayScreen(mgr, mp3Player);
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}
	
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
	
	
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClickMainMenuButton(ActionPerformedEvent event){
		if(event.button.id == 1000){
			  Minecraft.getMinecraft().displayGuiScreen(mp3Jukebox.instance.guiHandlerInstance.screen); 
		}
	}
	
	protected void setup(){
		mgr.setup();
	}
}

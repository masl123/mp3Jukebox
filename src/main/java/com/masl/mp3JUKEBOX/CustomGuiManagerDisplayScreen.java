  /*
  * mp3JukeBox - a mp3 Player Mod for Minecraft
  * Copyright (C) 2015 masll (minecraftforum.net)
  * 
  * This program is free software: you can redistribute it and/or modify
  *  it under the terms of the GNU Lesser General Public License as published by
  *  the Free Software Foundation, either version 3 of the License, or
  *  (at your option) any later version.
  *
  *  This program is distributed in the hope that it will be useful,
  *  but WITHOUT ANY WARRANTY; without even the implied warranty of
  *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  *  GNU Lesser General Public License for more details.
  *
  *  You should have received a copy of the GNU Lesser General Public License
  *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
  */


package com.masl.mp3Jukebox;

import java.awt.Rectangle;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import org.darkstorm.minecraft.gui.GuiManager;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;
import org.lwjgl.input.Mouse;

public class CustomGuiManagerDisplayScreen extends GuiManagerDisplayScreen {


	private GuiManager guiManager;
	private Player mp3Player; 
	
	public CustomGuiManagerDisplayScreen(GuiManager guiManager, Player mp3Player) {
		super(guiManager);
		this.guiManager=guiManager;
		this.mp3Player=mp3Player;
	}
	
	@Override
	public void initGui() {
		buttonList.add(new GuiButton(1001, this.width / 2 - 100, this.height - 30, I18n.format("menu.returnToGame", new Object[0])));
		super.initGui();
	}
	 
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		mp3Player.stopMusic(Minecraft.getMinecraft().getSoundHandler());
	}
	
	
	@Override
	public void drawScreen(int par2, int par3, float par4) {
		drawWorldBackground(10);
		super.drawScreen(par2, par3, par4);
	}

	
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
        int x = Mouse.getX() * this.width / this.mc.displayWidth;
        int y = this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1;
        
   
        
        
		if(button.id == 1001){
			for(Frame frame : guiManager.getFrames()) {
				if(frame.getArea().contains(x,y)){
					return;
				}	
			}
			
			 this.mc.displayGuiScreen((GuiScreen)null);
             this.mc.setIngameFocus();
		}
	}
	

}

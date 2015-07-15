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


package com.masl.mp3JUKEBOX;

import net.minecraft.client.Minecraft;

import org.darkstorm.minecraft.gui.GuiManager;
import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;

public class CustomGuiManagerDisplayScreen extends GuiManagerDisplayScreen {

	public CustomGuiManagerDisplayScreen(GuiManager guiManager) {
		super(guiManager);
	}
	
	@Override
	public void onGuiClosed() {
		
		super.onGuiClosed();
		mp3Jukebox.instance.soundloader.stopMusic(Minecraft.getMinecraft().getSoundHandler());
	}
	
	
	
	

}

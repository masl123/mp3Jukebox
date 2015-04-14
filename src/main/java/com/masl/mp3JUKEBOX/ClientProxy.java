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


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.common.network.NetworkRegistry;


public class ClientProxy extends CommonProxy {
	@Override
	public void registerGUIs(mp3Jukebox mod) {
		NetworkRegistry.INSTANCE.registerGuiHandler(mod, mp3Jukebox.instance.guiHandlerInstance);
	}
	
	
       
}
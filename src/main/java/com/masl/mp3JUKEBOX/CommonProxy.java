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


import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;

import net.minecraft.client.Minecraft;


public class CommonProxy {

		protected  OptionsGui mgr = new OptionsGui();
		protected  GuiManagerDisplayScreen screen =  new GuiManagerDisplayScreen(mgr);
	
        // Client stuff
        public void registerRenderers() {
                // Nothing here as the server doesn't render graphics or entities!
        }

		public void registerGUIs(mp3Jukebox mod) {
			// TODO Auto-generated method stub
			
		}
		
		
		
}

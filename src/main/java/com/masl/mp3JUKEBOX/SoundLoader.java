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

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;


import de.cuina.fireandfuel.CodecJLayerMP3;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.MusicTicker.MusicType;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundList;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.client.event.sound.SoundSetupEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.sound.*;

public class SoundLoader {
	private Player mp3Player;

	public SoundLoader(Player mp3Player){
		this.mp3Player=mp3Player;
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onSoundsLoaded(SoundLoadEvent SLEvent) {
		loadmusic();
	}

	@SideOnly(Side.CLIENT)
	public void loadmusic() {
		File musicfile = null;

		musicfile = new File(Minecraft.getMinecraft().mcDataDir, "music");
		mp3Jukebox.logger.log(Level.INFO,"Music-Folder at: "+ musicfile.getAbsolutePath());
		musicfile.mkdirs();

		mp3Player.resetMusicList();
		
		
		int count = 0;
		for (File m : musicfile.listFiles()) {
			if (FilenameUtils.getExtension(m.getAbsolutePath()).equals("ogg") || FilenameUtils.getExtension(m.getAbsolutePath()).equals("mp3")) {
				mp3Player.addMusicTitle(m);	
				count++;
			}
		}
		mp3Jukebox.logger.log(Level.INFO,"ADDED " +count+ " Songs to Playlist.");
	}
	  
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void soundCodecSetup(SoundSetupEvent event) {
		try {
			SoundSystemConfig.setCodec("mp3", CodecJLayerMP3.class);
			// SoundSystemConfig.setCodec("m4a", Mp4Codecc.class);
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
		} catch (SoundSystemException e) {
			e.printStackTrace();
		}
	}
}




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

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import paulscode.sound.IStreamListener;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Player {
	
	private String currUUID;
	private boolean soundPlaying = false;
	private SoundSystem sndSystem;
	private List<File> music;
	private int titleindex = 0;
	private float volume = 0.5f;
	private float newVolume;
	
	@SideOnly(Side.CLIENT)
	protected  void playSound() {
		synchronized(Minecraft.getMinecraft().getSoundHandler()){
			if(music!=null&&music.size()>0){ 
				stopSound();
				soundPlaying=true;
				currUUID = UUID.randomUUID().toString();
				if(sndSystem==null){
					getsndSystem();
				}
				try {
					if(titleindex < music.size()){
						stopMusic(Minecraft.getMinecraft().getSoundHandler());
						
						
						File f = music.get(titleindex);
						System.out.println(currUUID+" "+f);
					
						sndSystem.backgroundMusic(currUUID, f.toURI().toURL(), f.getName(), false);
					}
					
					
					sndSystem.setVolume(currUUID, volume);
					sndSystem.play(currUUID);
					
					stopMusic(Minecraft.getMinecraft().getSoundHandler());
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private ISound currentMusic;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void PlaySoundEvent(PlaySoundEvent event) {
		if (event.category == SoundCategory.MUSIC && soundPlaying) {
			System.out.println("Stoped Streamed Sound:  " + event.category);
			event.result = null;
			event.setResult(Result.DENY);
		} else if (event.category == SoundCategory.MUSIC) {

			currentMusic = event.sound;
		}
	}

	protected void stopMusic(SoundHandler soundHandler) {
		if (currentMusic != null) {
			soundHandler.stopSound(currentMusic);
			soundHandler.update();
		}
	}
	
	protected String getName() {
		int index = titleindex;
		if (index < music.size() && index >= 0)
			return music.get(index).getName();
		else
			return "NONE";
	}
	
	
	@SideOnly(Side.CLIENT)
	protected  void stopSound() {
		if(sndSystem==null){
			getsndSystem();
		}
		synchronized(Minecraft.getMinecraft().getSoundHandler()){
			if (currUUID != null && soundPlaying) {
				soundPlaying=false;
				sndSystem.stop(currUUID);
				sndSystem.removeSource(currUUID);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected void nextSound(){
		if (soundPlaying) {
			stopSound();
		}
		
		if(titleindex+1<music.size()){
			titleindex++;
		}else{
			titleindex=0;
		}
		
		try {
			if (!soundPlaying) {
				playSound();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected void prevSound(){
		if (soundPlaying) {
			stopSound();
		}
		
		if(titleindex-1>=0){
			titleindex--;
		}else{
			titleindex=music.size()-1;
		}
		
		try {
			if (!soundPlaying) {
				playSound();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SideOnly(Side.CLIENT)
	protected void setVolume(float oldVolume){
		if(sndSystem==null){
			getsndSystem();
		}
		this.volume = oldVolume * Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.MASTER);
		sndSystem.setVolume(currUUID, this.volume );
	}
	
	
	
	@SideOnly(Side.CLIENT)
	protected  void getsndSystem(){
		synchronized(Minecraft.getMinecraft().getSoundHandler()){
			try {
				//Field sndManagerField = SoundHandler.class.getDeclaredField("sndManager");
				Field[] soundHandlerfields = SoundHandler.class.getDeclaredFields();
				SoundManager manager = null;
				
				for(Field f : soundHandlerfields){
					f.setAccessible(true);
					Object o = f.get(Minecraft.getMinecraft().getSoundHandler());
					if(o instanceof SoundManager ){
						manager = (SoundManager) o;
						break;
					}
				}
				
				
				Field[] sndSystemFields = SoundManager.class.getDeclaredFields();
				for(Field f : sndSystemFields){
					f.setAccessible(true);
					Object o = f.get(manager);
					if(o instanceof SoundSystem ){
						sndSystem = (SoundSystem) o;
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
	
	
	 @SideOnly(Side.CLIENT)
	protected void addStreamListener(){	

		SoundSystemConfig.addStreamListener(new IStreamListener(){
			@Override
			public void endOfStream(String arg0, int arg1) {
				if(arg0!=null&&currUUID!=null&&currUUID.equals(arg0)&&soundPlaying){
					if (soundPlaying) {
						stopSound();
					}
					
					if(titleindex+1 < music.size()){
						titleindex++;
					}else{
						titleindex=0;
					}
					
					try {
						if (!soundPlaying) {
							playSound();
							mp3Jukebox.instance.guiHandlerInstance.mgr.setNameLabel(getName());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	protected void resetMusicList(){
		music = new LinkedList();
		titleindex = 0;
    }
	
	protected void addMusicTitle(File f){
		music.add(f);
	}
	
	protected boolean isSoundPlaying(){
		return soundPlaying;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void GUIMainMenuOpened(InitGuiEvent event) {
		if (event.gui instanceof GuiMainMenu) {
			event.buttonList.add(new GuiButton(1000, event.gui.width / 2 - 100, event.gui.height - 30, "mp3 Jukebox"));
		} 
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void GUIConfigChanged(ActionPerformedEvent event) {
		if (event.gui instanceof GuiOptions) {
			synchronized (Minecraft.getMinecraft().getSoundHandler()) {
				getsndSystem();
				stopSound();
				setVolume((float) (mp3Jukebox.instance.guiHandlerInstance.mgr.getVolumeSliderVar() / 100));
			}
		}
	}
	
}

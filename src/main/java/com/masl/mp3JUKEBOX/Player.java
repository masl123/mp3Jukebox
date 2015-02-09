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

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.UUID;

import paulscode.sound.IStreamListener;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Player {
	
	private String currUUID;
	protected static boolean soundPlaying = false;
	private SoundSystem sndSystem;
	private long timeLastClicked = 0;
	int titleindex = 0;
	protected float volume = 0.5f;
	
	
	@SideOnly(Side.CLIENT)
	protected  void playSound() {
		synchronized(Minecraft.getMinecraft().getSoundHandler()){
			if(SoundLoader.music!=null&&SoundLoader.music.size()>0){ 
				stopSound();
				soundPlaying=true;
				currUUID = UUID.randomUUID().toString();
				if(sndSystem==null){
					getsndSystem();
				}
				
				try {
					
					
					if(titleindex<SoundLoader.music.size()){
						File f = SoundLoader.music.get(titleindex);
						sndSystem.backgroundMusic(currUUID, f.toURI().toURL(), f.getName(), false);
					}
					
					
					sndSystem.setVolume(currUUID, volume);
					sndSystem.play(currUUID);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	
	@SideOnly(Side.CLIENT)
	protected  void stopSound() {
		synchronized(Minecraft.getMinecraft().getSoundHandler()){
			if (currUUID != null && soundPlaying) {
				
				
				
				
				soundPlaying=false;
				if(sndSystem==null){
					getsndSystem();
				}
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
		
		if(titleindex+1<SoundLoader.music.size()){
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
			titleindex=SoundLoader.music.size()-1;
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
	protected void setVolume(float volume){
		if(sndSystem==null){
			getsndSystem();
		}
		this.volume = volume * Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.MASTER);
		sndSystem.setVolume(currUUID, this.volume );
	}
	
	
	
	@SideOnly(Side.CLIENT)
	private  void getsndSystem(){
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
			
				//Field sndSystemField = SoundManager.class.getDeclaredField("sndSystem");
				//sndSystemField.setAccessible(true);
				//sndSystem = (SoundSystem) sndSystemField.get(manager);
				
			} catch (Exception e) {
				
				e.printStackTrace();
			} 
		}
	}
	
	
	 @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
	 @SideOnly(Side.CLIENT)
	 public void onSoundsLoaded(SoundLoadEvent SLEvent)
	 {
		 getsndSystem();
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
					
					if(titleindex+1<SoundLoader.music.size()){
						titleindex++;
					}else{
						titleindex=0;
					}
					
					try {
						if (!soundPlaying) {
							playSound();
							mp3Jukebox.instance.guiHandlerInstance.mgr.label.setText(mp3Jukebox.soundloader.getName());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	
	}
}

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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;





import com.masl.Mp4Codecc;

import de.cuina.fireandfuel.CodecJLayerMP3;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.MusicTicker.MusicType;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundList;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
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

	 public static List<File> music = new LinkedList();
	 public static float newVolume;
	 
	 
	 
	  @net.minecraftforge.fml.common.eventhandler.SubscribeEvent
	  @SideOnly(Side.CLIENT)
	  public void onSoundsLoaded(SoundLoadEvent SLEvent)
	  {
		 loadmusic();
	  }
	  
	  
	  @SideOnly(Side.CLIENT)
      public void loadmusic(){
  	   File musicfile = null;
         
  	   	musicfile = new File(Minecraft.getMinecraft().mcDataDir, "music");
        System.out.println(musicfile);
        musicfile.mkdirs();
        
        
        for(File m: musicfile.listFiles()){
     	   	if(FilenameUtils.getExtension(m.getAbsolutePath()).equals("ogg") || FilenameUtils.getExtension(m.getAbsolutePath()).equals("mp3")){
     	   		music.add(m);
     	   		mp3Jukebox.logger.log(Level.INFO,"FOUND SONG: "+m.getAbsolutePath());
     	   	}
        }
      }
	  
	  
	  
//	  public void setMusicTickerTime(int a) throws Exception{
//		  Minecraft mc = Minecraft.getMinecraft();
//		  for(Field f : Minecraft.class.getDeclaredFields()){
//			  if(f.getType().equals(MusicTicker.class)){
//				  f.setAccessible(true);
//				  MusicTicker tick = (MusicTicker)f.get(mc);
//				  
//				  
//				  for(Field field : MusicTicker.class.getDeclaredFields()){
//					  if(field.getType().equals(Integer.TYPE)){
//						  field.setAccessible(true);
//						  field.set(tick, a);  
//					  }
//				  }
//				  break;
//			  }   
//		  }  
//	  }
	  
	  
	  public String getName(){
		  int index = mp3Jukebox.instance.mp3Player.titleindex;
		  return index<music.size() && index>=0 ? music.get(index).getName() :  "NONE";
	  }
	  
	  @SubscribeEvent
	  @SideOnly(Side.CLIENT)
	  public void soundsetup(SoundSetupEvent event){
		  try {
				SoundSystemConfig.setCodec("mp3", CodecJLayerMP3.class);
				//SoundSystemConfig.setCodec("m4a", Mp4Codecc.class);
			} catch (NoClassDefFoundError e) {
				e.printStackTrace();
			} catch (SoundSystemException e) {
				e.printStackTrace();
			}
	  }
	  
	
	  
	  
	  @SubscribeEvent
	  @SideOnly(Side.CLIENT)
	  public void GUIConfigchanged(ActionPerformedEvent event){
		  if(event.gui instanceof GuiOptions){
			  synchronized(Minecraft.getMinecraft().getSoundHandler()){
				  mp3Jukebox.instance.mp3Player.getsndSystem();
				  mp3Jukebox.instance.mp3Player.setVolume((float) (mp3Jukebox.instance.guiHandlerInstance.mgr.slider.getValue()/100));
			  }
		}
      }
	  
	  @SubscribeEvent
      @SideOnly(Side.CLIENT)
      public void FMLServerStopped(Unload event){
		  	mp3Jukebox.instance.mp3Player.stopSound();
		  	mp3Jukebox.instance.guiHandlerInstance.mgr.label.setText("NONE");
      }
	  
	  
	  ISound currentMusic;

	  
	  
	  @SubscribeEvent
	  @SideOnly(Side.CLIENT)
	  public void PlaySoundEvent (PlaySoundEvent event){
		  
		
		 if(event.category == SoundCategory.MUSIC && mp3Jukebox.instance.mp3Player.soundPlaying){
			System.out.println("Stoped Streamed Sound:  "+event.category);
			event.result=null;	
			event.setResult(Result.DENY);
		 }else if(event.category == SoundCategory.MUSIC){
			 
			 currentMusic = event.sound;
		 }
	  }
	  
	  public void stopMusic(SoundHandler soundHandler){
		  if(currentMusic!=null){
			  soundHandler.stopSound(currentMusic);
			  soundHandler.update();
		  }
	  }
	  
	  

}




  /*
  * mp3JukeBox - a mp3 Player Mod for Minecraft
  * Copyright (C) 2015 masll (minecraftforum.net)
  * 
  *  This program is free software: you can redistribute it and/or modify
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import de.cuina.fireandfuel.CodecJLayerMP3;

@Mod(modid="mp3Jukebox", name="mp3Jukebox", version="2.1.0")
public class mp3Jukebox {

		public static Logger logger = LogManager.getLogger();
		public static String MODID = "mp3Jukebox";
		
		public  List<KeyBinding> keyBindings;
		protected  GUIHandler guiHandlerInstance;
		private SoundLoader soundloader;
		private  Player mp3Player;
		
		
        // The instance of your mod that Forge uses.
        @Instance(value = "mp3Jukebox")
        public static mp3Jukebox instance;
       
        // Says where the client and server 'proxy' code is loaded.
        @SidedProxy(clientSide="com.masl.mp3JUKEBOX.ClientProxy", serverSide="com.masl.mp3JUKEBOX.CommonProxy")
        public static CommonProxy proxy;
        
        @EventHandler // used in 1.6.2
        public void preInit(FMLPreInitializationEvent event) {
        	mp3Player = new Player();
        	guiHandlerInstance = new GUIHandler(mp3Player);
        	soundloader = new SoundLoader(mp3Player);
        	
        	
        	MinecraftForge.EVENT_BUS.register(soundloader);
        	MinecraftForge.EVENT_BUS.register(guiHandlerInstance);
        	MinecraftForge.EVENT_BUS.register(guiHandlerInstance.mgr);
        	MinecraftForge.EVENT_BUS.register(mp3Player);
        	FMLCommonHandler.instance().bus().register(guiHandlerInstance);
        	FMLCommonHandler.instance().bus().register(guiHandlerInstance.mgr);
        	//FMLCommonHandler.instance().bus().register(mp3Player);
        	
        	keyBindings = new ArrayList<KeyBinding>(1);
        	keyBindings.add(new KeyBinding("Mp3 Player GUI", Keyboard.KEY_END, "mp3Jukebox"));
        	
        	
        }
       
        @EventHandler 
        public void load(FMLInitializationEvent event) {	
        	 mp3Jukebox.logger.log(Level.INFO,"LOADING "+ MODID);
        	 mp3Jukebox.logger.log(Level.INFO,"~~~~~~~~~~~~~~~~");
        	 addKeys();
        	 proxy.registerGUIs(this);
        }

        
        @EventHandler 
        public void postInit(net.minecraftforge.fml.common.event.FMLPostInitializationEvent event) {
        	mp3Player.addStreamListener();
        	guiHandlerInstance.setup();
        }
        

        public void addKeys(){
        	for(KeyBinding b : keyBindings){
        		 ClientRegistry.registerKeyBinding(b);
        	}
        }
}
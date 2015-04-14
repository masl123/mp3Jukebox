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

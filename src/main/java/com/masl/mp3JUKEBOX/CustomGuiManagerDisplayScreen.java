package com.masl.mp3JUKEBOX;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import org.darkstorm.minecraft.gui.GuiManager;
import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;

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
		if(button.id==1001){
			 this.mc.displayGuiScreen((GuiScreen)null);
             this.mc.setIngameFocus();
		}
	}
	

}

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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.darkstorm.minecraft.gui.AbstractGuiManager;
import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.component.Slider;
import org.darkstorm.minecraft.gui.component.basic.BasicButton;
import org.darkstorm.minecraft.gui.component.basic.BasicComboBox;
import org.darkstorm.minecraft.gui.component.basic.BasicFrame;
import org.darkstorm.minecraft.gui.component.basic.BasicLabel;
import org.darkstorm.minecraft.gui.component.basic.BasicPanel;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import org.darkstorm.minecraft.gui.layout.Constraint;
import org.darkstorm.minecraft.gui.layout.GridLayoutManager;
import org.darkstorm.minecraft.gui.layout.GridLayoutManager.HorizontalGridConstraint;
import org.darkstorm.minecraft.gui.layout.GridLayoutManager.VerticalGridConstraint;
import org.darkstorm.minecraft.gui.layout.LayoutManager;
import org.darkstorm.minecraft.gui.listener.ButtonListener;
import org.darkstorm.minecraft.gui.listener.SliderListener;
import org.darkstorm.minecraft.gui.theme.Theme;
import org.darkstorm.minecraft.gui.theme.simple.SimpleTheme;
import org.darkstorm.minecraft.gui.theme.textured.TexturedTheme;

public class OptionsGui extends AbstractGuiManager {
	
	private final AtomicBoolean setup;
	private int counter = 1;

	private Frame testFrame;
	private Slider slider;
	private BasicLabel label;
	private Player mp3Player;
	
	public OptionsGui(Player mp3Player){
		setup = new AtomicBoolean();
		this.mp3Player=mp3Player;
	}
	
	
	
	@Override
	public void setup() {
		if(!setup.compareAndSet(false, true))
			return;
		
		setTheme(new TexturedTheme());
		
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		testFrame = new CFrame("mp3 Jukebox Mod", 0, 0, sr.getScaledWidth() - (10*sr.getScaleFactor()),  sr.getScaledHeight()-(10*sr.getScaleFactor()));
		
		LayoutManager mgr = new GridLayoutManager(3,3);
		testFrame.setLayoutManager(mgr);
		testFrame.setTheme(getTheme());
		testFrame.setClosable(false);

		
		
		
		//add Prev Button
		BasicButton prevBtn = new BasicButton("<< Prev");
		prevBtn.setWidth(33);
		prevBtn.setHeight(10);
		prevBtn.setEnabled(true);
		prevBtn.addButtonListener(new ButtonListener() {
			@Override
			public void onButtonPress(Button button) {
				mp3Player.prevSound();
				label.setText(mp3Player.getName());
			}
		});
		testFrame.add(prevBtn, HorizontalGridConstraint.LEFT, VerticalGridConstraint.TOP);
		
		
		//add PlayPause Button
		BasicButton playBtn = new BasicButton("Play/Stop");
		playBtn.setWidth(33);
		playBtn.setHeight(10);
		playBtn.setEnabled(true);
		playBtn.addButtonListener(new ButtonListener() {
			@Override
			public void onButtonPress(Button button) {
				if(!mp3Player.isSoundPlaying()){
					mp3Player.playSound();
					label.setText(mp3Player.getName());
					
				}else{
					label.setText("NONE");
					mp3Player.stopSound();
				}
			}
		});
		testFrame.add(playBtn, HorizontalGridConstraint.CENTER,VerticalGridConstraint.TOP);
		
		
		//add Next Button
		BasicButton nextBtn = new BasicButton("Next >>");
		nextBtn.setWidth(33);
		nextBtn.setHeight(10);
		nextBtn.setEnabled(true);
		nextBtn.addButtonListener(new ButtonListener() {
			@Override
			public void onButtonPress(Button button) {
				mp3Player.nextSound();
				label.setText(mp3Player.getName());
			}
		});
		testFrame.add(nextBtn, HorizontalGridConstraint.RIGHT, VerticalGridConstraint.TOP);
		
		
		label = new BasicLabel();
		label.setText("NONE");
		label.setWidth(100);
		testFrame.add(label,  HorizontalGridConstraint.LEFT,VerticalGridConstraint.CENTER);

		
		
		slider = new BasicSlider("Volume");
		slider.setMaximumValue(100);
		slider.setMinimumValue(0);
		slider.setWidth(100);
		slider.setValue(50);
		slider.addSliderListener(new SliderListener(){
			@Override
			public void onSliderValueChanged(Slider slider) {
				mp3Player.setVolume((float)(slider.getValue()/100.0f));
				//Minecraft.getMinecraft().gameSettings.setSoundLevel(SoundCategory.MUSIC, (float) (slider.getValue()/100.0f));				
			}
		});
		slider.setEnabled(true);
		testFrame.add(slider,HorizontalGridConstraint.CENTER, VerticalGridConstraint.BOTTOM);
		
		
		
		
		
		//set Frame Size
		testFrame.setX(50);
		testFrame.setY(50);

		Dimension defaultDimension = getTheme().getUIForComponent(testFrame).getDefaultSize(testFrame);
		testFrame.setWidth(105);
		testFrame.setHeight(defaultDimension.height);
		testFrame.layoutChildren();
		testFrame.setVisible(true);
		testFrame.setMinimized(false);
		

		
		resizeComponents();
		addFrame(testFrame);
	}

	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent()
	public void tickUpdate(TickEvent upd){
		if(testFrame!=null && testFrame.isVisible())
			update();
	}


	@Override
	protected void resizeComponents() {
		if(slider!=null)
			slider.resize();
	}

	protected void setVolumeSliderVar(int i){
		slider.setValue(i);
	}
	
	protected void setNameLabel(String s){
		label.setText(s);
	}
	
	protected double getVolumeSliderVar(){
		return slider.getValue();
	}
	
	private class CFrame extends BasicFrame{
		Rectangle rect;
		
		CFrame(String s, int x, int y , int width, int height){
			super(s);
			
			rect = new Rectangle(x,y,width, height);
		}
		
		
		@Override
		public void setX(int x) {
			if(x > rect.getX() && x < rect.getMaxX()){
				super.setX(x);
			}
		}
		@Override
		public void setY(int y) {
			if(y > rect.getY() && y < rect.getMaxY()){
				super.setY(y);
			}
		}
	}
	
}





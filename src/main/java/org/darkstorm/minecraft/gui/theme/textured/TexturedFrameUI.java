package org.darkstorm.minecraft.gui.theme.textured;

import static org.lwjgl.opengl.GL11.*;

import java.awt.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.layout.Constraint;
import org.darkstorm.minecraft.gui.theme.AbstractComponentUI;
import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.opengl.GL11;

public class TexturedFrameUI extends AbstractComponentUI<Frame> {
	private final TexturedTheme theme;
	private final ResourceLocation bg, min, pin, close;
	
	
	TexturedFrameUI(TexturedTheme texturedTheme, ResourceLocation bg, ResourceLocation min, ResourceLocation pin, ResourceLocation close) {
		super(Frame.class);
		this.theme = texturedTheme;
		this.bg = bg;
		this.min = min; 
		this.pin=pin;
		this.close = close;
		
		foreground = Color.black;
		background = new Color(128, 128, 128, 128);
	}
	
	@Override
	protected void renderComponent(Frame component) {
		Rectangle area = new Rectangle(component.getArea());
		int fontHeight = theme.getFontRenderer().FONT_HEIGHT;
		translateComponent(component, false);

		// Draw frame background
		if(component.isMinimized())
			area.height = fontHeight + 4;

		
		
		double height = area.getHeight() / component.getArea().getHeight();
		
		
		glColor4f(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(bg);
		WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		wr.pos(0, area.getHeight(), 0).tex(0, height).endVertex();
		wr.pos(area.getWidth(), area.getHeight(), 0).tex(1, height).endVertex();
		wr.pos(area.getWidth(), 0, 0).tex(1, 0).endVertex();;
		wr.pos(0, 0, 0).tex(0, 0).endVertex();
		Tessellator.getInstance().draw();
		
		
		
		
		// Draw controls
		int offset = component.getWidth() - 2;
		Point mouse = RenderUtil.calculateMouseLocation();
		Component parent = component;
		while(parent != null) {
			mouse.x -= parent.getX();
			mouse.y -= parent.getY();
			parent = parent.getParent();
		}
		boolean[] checks = new boolean[] { component.isClosable(),
				component.isPinnable(), component.isMinimizable() };
		boolean[] overlays = new boolean[] { false, component.isPinned(),
				component.isMinimized() };
		ResourceLocation[] textures = new ResourceLocation[]{
				close, pin, min};
		
		
		for(int i = 0; i < checks.length; i++) {
			if(!checks[i])
				continue;
			
			boolean mouseOnBtn = mouse.x >= offset - fontHeight && mouse.x <= offset && mouse.y >= 2 && mouse.y <= fontHeight + 2;
			drawButton(mouseOnBtn, overlays[i], textures[i], offset, fontHeight);
			offset -= fontHeight + 2;
		}

		glColor4f(0f, 0f, 0f, 1f);
		glLineWidth(1.0f);
		glBegin(GL_LINES);
		{
			glVertex2d(2, theme.getFontRenderer().FONT_HEIGHT + 4);
			glVertex2d(area.width - 2, theme.getFontRenderer().FONT_HEIGHT + 4);
		}
		glEnd();
		
		
		glEnable(GL_TEXTURE_2D);
		theme.getFontRenderer().drawString(component.getTitle(), 2,2, RenderUtil.toRGBA(component.getForegroundColor()));
		glEnable(GL_CULL_FACE);
		glDisable(GL_BLEND);
		translateComponent(component, true);
	}

	private void drawButton(boolean mouseover, boolean overlay, ResourceLocation tex, int offset,int height){
		double pos = 1.0/3.0;
		
		glColor4f(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(tex);
		WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		
		if(!mouseover & !overlay){
			wr.pos(offset - height, height + 2, 0).tex(0, pos).endVertex();
			wr.pos(offset, height + 2, 0).tex(1, pos).endVertex();
			wr.pos(offset, 2, 0).tex(1, 0).endVertex();;
			wr.pos(offset - height, 2, 0).tex(0, 0).endVertex();	
			Tessellator.getInstance().draw();
		}else if(mouseover){
			wr.pos(offset - height, height + 2, 0).tex(0, 2 * pos).endVertex();
			wr.pos(offset, height + 2, 0).tex(1, 2 * pos).endVertex();
			wr.pos(offset, 2, 0).tex(1, pos).endVertex();;
			wr.pos(offset - height, 2, 0).tex(0, pos).endVertex();
			Tessellator.getInstance().draw();
		}else if(overlay){
			wr.pos(offset - height, height + 2, 0).tex(0, 3 * pos).endVertex();
			wr.pos(offset, height + 2, 0).tex(1, 3 * pos).endVertex();
			wr.pos(offset, 2, 0).tex(1, 2 * pos).endVertex();;
			wr.pos(offset - height, 2, 0).tex(0, 2 * pos).endVertex();Tessellator.getInstance().draw();
		}		
	}
	
	
	
	@Override
	protected Rectangle getContainerChildRenderArea(Frame container) {
		Rectangle area = new Rectangle(container.getArea());
		area.x = 2;
		area.y = theme.getFontRenderer().FONT_HEIGHT + 6;
		area.width -= 4;
		area.height -= theme.getFontRenderer().FONT_HEIGHT + 8;
		return area;
	}

	@Override
	protected Dimension getDefaultComponentSize(Frame component) {
		Component[] children = component.getChildren();
		Rectangle[] areas = new Rectangle[children.length];
		Constraint[][] constraints = new Constraint[children.length][];
		for(int i = 0; i < children.length; i++) {
			Component child = children[i];
			Dimension size = child.getTheme().getUIForComponent(child)
					.getDefaultSize(child);
			areas[i] = new Rectangle(0, 0, size.width, size.height);
			constraints[i] = component.getConstraints(child);
		}
		Dimension size = component.getLayoutManager().getOptimalPositionedSize(
				areas, constraints);
		size.width += 4;
		size.height += theme.getFontRenderer().FONT_HEIGHT + 8;
		return size;
	}

	@Override
	protected Rectangle[] getInteractableComponentRegions(Frame component) {
		return new Rectangle[] { new Rectangle(0, 0, component.getWidth(),
				theme.getFontRenderer().FONT_HEIGHT + 4) };
	}

	@Override
	protected void handleComponentInteraction(Frame component, Point location,
			int button) {
		if(button != 0)
			return;
		int offset = component.getWidth() - 2;
		int textHeight = theme.getFontRenderer().FONT_HEIGHT;
		if(component.isClosable()) {
			if(location.x >= offset - textHeight && location.x <= offset
					&& location.y >= 2 && location.y <= textHeight + 2) {
				component.close();
				return;
			}
			offset -= textHeight + 2;
		}
		if(component.isPinnable()) {
			if(location.x >= offset - textHeight && location.x <= offset
					&& location.y >= 2 && location.y <= textHeight + 2) {
				component.setPinned(!component.isPinned());
				return;
			}
			offset -= textHeight + 2;
		}
		if(component.isMinimizable()) {
			if(location.x >= offset - textHeight && location.x <= offset
					&& location.y >= 2 && location.y <= textHeight + 2) {
				component.setMinimized(!component.isMinimized());
				return;
			}
			offset -= textHeight + 2;
		}
		if(location.x >= 0 && location.x <= offset && location.y >= 0
				&& location.y <= textHeight + 4) {
			component.setDragging(true);
			return;
		}
	}
}

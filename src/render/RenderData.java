package render;

import org.newdawn.slick.opengl.Texture;

public class RenderData {
	
	public Texture texture;
	public float x, y;
	public int width, height;
	public boolean center = true;
	public boolean flip = false;
	public float scale = 1.0f;
	public float alpha = 1.0f;
	public float angle = 0.0f;
	
	// create the object
	public RenderData() {}
	
}
package co.uk.epicguru.UI.loading;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import co.uk.epicguru.UI.Observer;
import co.uk.epicguru.UI.SpriteImage;
import co.uk.epicguru.main.Main;

public class LoadingSymbol {

	private static SpriteImage triangle;
	private static SpriteImage cog;
	private static SpriteImage square;
	private static SpriteImage ghost;
	private static SpriteImage point;

	private static float timer;
	public static float speed = 1;

	public static void load(){
		triangle = new SpriteImage(Main.INSTANCE.getAsset("Textures/UI/Loading Triangle.png", TextureRegion.class));
		cog = new SpriteImage(Main.INSTANCE.getAsset("Textures/UI/Loading Cog.png", TextureRegion.class));
		square = new SpriteImage(Main.INSTANCE.getAsset("Textures/UI/Loading Square.png", TextureRegion.class));
		ghost = new SpriteImage(Main.INSTANCE.getAsset("Textures/UI/Loading Ghost.png", TextureRegion.class));
		point = new SpriteImage(Main.INSTANCE.getAsset("Textures/UI/Loading Point.png", TextureRegion.class));
	}

	public static void render(float x, float y, Observer obs, float delta){

		timer += delta * speed;

		float old = obs.getScale();
		obs.setScale(1);

		// Ghost cog
		ghost.getSprite().setScale(MathUtils.sin(timer * 0.25f));
		ghost.bounds.setCenter(x, y);
		ghost.getSprite().setOriginCenter();
		ghost.setRotation(MathUtils.cos(timer * .5f) * 360f);
		ghost.render(obs, delta);

		// Square
		square.bounds.setCenter(x, y);
		square.getSprite().setOriginCenter();
		square.setRotation(-120f * timer);
		square.render(obs, delta);

		// Triangle
		triangle.bounds.setCenter(x, y);
		triangle.getSprite().setOriginCenter();
		triangle.setRotation(200f * timer);
		triangle.render(obs, delta);

		// Centre cog
		cog.bounds.setCenter(x, y);
		cog.getSprite().setOriginCenter();
		cog.setRotation(-300f * timer);
		cog.render(obs, delta);
		
		// Points flying around
		int points = 20;
		for(int i = 0; i < points; i++){
			
			float p = i / (float)points;
			float angle = 360f * p;
			angle += timer * 20f;
			float dst = 150;
			
			float X, Y;
			X = MathUtils.cosDeg(angle) * dst + x;
			Y = MathUtils.sinDeg(angle) * dst + y;
			
			point.getSprite().setScale(MathUtils.sin(timer + i * 0.5f) * 1f);
			point.bounds.setCenter(X, Y);
			point.getSprite().setOriginCenter();
			point.setRotation(300f * timer * (i % 2 == 0 ? -1 : 1));
			point.render(obs, delta);
		}

		obs.setScale(old);
	}	
}

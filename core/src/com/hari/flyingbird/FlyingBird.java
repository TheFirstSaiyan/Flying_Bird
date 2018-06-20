package com.hari.flyingbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;



public class FlyingBird extends ApplicationAdapter {
	Viewport viewport;
	SpriteBatch batch;
	Texture bg,bird1,bird2,birddown;
	Texture bottomtube;
	Texture toptube;
	Texture gameover;
	int i=2;
	int gamestate=0;
	float gravity =2;
	float speed=0;
	float posY=0;
	float tubeX[];
	Random random;
	float maxdisplacement;
	float gap=400;
	float tubeY[];
	Circle birdCircle;
	Rectangle []rectangleTop;
	Rectangle []rectangleBottom;
	ShapeRenderer shapeRenderer;
	BitmapFont font;
	int score=0;
	boolean check=true;
	boolean drawFont=true;
	boolean gameActive=false;
	int highscore=0;
	Preferences preferences;
	Sound jumpsound,impactsound;
	@Override
	public void create () {
		viewport=new FitViewport(300,800);
		batch = new SpriteBatch();
		bg = new Texture("bg.png");
		bird1=new Texture("bird.png");
		bird2=new Texture("bird2.png");
		gameover=new Texture("gameover.png");
		birddown=new Texture("birddown.png");
		jumpsound=Gdx.audio.newSound(Gdx.files.internal("jumpsound.mp3"));
		impactsound=Gdx.audio.newSound(Gdx.files.internal("impactsound.mp3"));
		bottomtube=new Texture("bottomtube.png");
		toptube=new Texture("toptube.png");
		posY=Gdx.graphics.getHeight()/2-bird2.getHeight()/2;
		font=new BitmapFont();
		font.setColor(Color.BLACK);
		font.getData().setScale(8);
		tubeX=new float[2];
		tubeY=new float[2];
		tubeX[0]=Gdx.graphics.getWidth();
		tubeX[1]=tubeX[0]+Gdx.graphics.getWidth()/2+toptube.getWidth();
		random=new Random();
		maxdisplacement=Gdx.graphics.getHeight()-gap-200;
		birdCircle=new Circle();
		rectangleBottom=new Rectangle[2];
		rectangleTop = new Rectangle[2];
		for(int j=0;j<2;j++)
		{
			rectangleTop[j]=new Rectangle();
			rectangleBottom[j]=new Rectangle();
		}
		tubeY[0] = (random.nextFloat() - 0.5f) * (maxdisplacement);
		tubeY[1] = (random.nextFloat() - 0.5f) * (maxdisplacement);
		preferences=Gdx.app.getPreferences("My preferences");
		highscore=preferences.getInteger("highscore",0);

	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		if (i == 2)
			i = 1;
		else
			i = 2;

		if (gamestate == 1)
		{
			drawFont=true;
			font.getData().setScale(8);
			speed = speed + gravity;
			if (posY >= 1)
				posY = posY - speed;

			if (Gdx.input.justTouched()||Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
			{
				Gdx.app.log("yes", Float.toString(posY));
				jumpsound.play();
				speed = -20;
				posY = posY - speed;

			}

			for (int m = 0; m < 2; m++)
			{

				batch.draw(bottomtube, tubeX[m], Gdx.graphics.getHeight() / 2 - bottomtube.getHeight() - gap / 2 + tubeY[m]);

				batch.draw(toptube, tubeX[m], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeY[m]);

				if (tubeX[m] < -toptube.getWidth())
				{
					check=true;
					tubeX[m]= Gdx.graphics.getWidth()+toptube.getWidth();
					tubeY[m] = (random.nextFloat() - 0.5f) * (maxdisplacement);
				}

				rectangleBottom[m] = new Rectangle(tubeX[m], Gdx.graphics.getHeight() / 2 - bottomtube.getHeight() - gap / 2 + tubeY[m], bottomtube.getWidth(), bottomtube.getHeight());

				rectangleTop[m] = new Rectangle(tubeX[m], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeY[m], toptube.getWidth(), toptube.getHeight());
				tubeX[m] -= 5;

				if ((Gdx.graphics.getWidth() / 2 - bird1.getWidth() / 2 >= tubeX[m] + toptube.getWidth()) && check)
				{
					score++;//increment score by 1
					check = false;
				}
				if (Intersector.overlaps(birdCircle, rectangleTop[m]) || Intersector.overlaps(birdCircle, rectangleBottom[m]) || posY <= 0)
				{

					gamestate=-1;
					impactsound.play();
					drawFont=false;
					speed=0;


				}
			}




		}
		else if(gamestate==0)
		{
			drawFont=false;
			font.setColor(Color.BLACK);
			font.getData().setScale(5);
			font.draw(batch,"tap to play",Gdx.graphics.getWidth()/2-gameover.getWidth()/2+100,100);
			if (Gdx.input.justTouched()||Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
			{
				gamestate = 1;
			}
		}


		else
		{
			speed+=gravity;
			if(posY>2)
				posY-=speed;
			if(posY<=2)
				gameActive=true;
			batch.draw(birddown, Gdx.graphics.getWidth() / 2 - bird1.getWidth() / 2, posY);
			batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2 + gameover.getHeight(), gameover.getWidth(), gameover.getHeight() * 3 / 2);

			if(gameActive)
			{
				font.setColor(Color.BLACK);
				font.getData().setScale(5);
				font.draw(batch, "tap to play again", Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2 - 35, Gdx.graphics.getHeight() - 100);
				if (score > highscore)
					highscore = score;
				preferences.putInteger("highscore", highscore);
				preferences.flush();
				font.getData().setScale(8);
				font.setColor(Color.MAGENTA);
				font.draw(batch, "SCORE :" + Integer.toString(score) + "\n" + "BEST :" + Integer.toString(highscore), Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 - 100);
				tubeX[0] = Gdx.graphics.getWidth();
				tubeX[1] = tubeX[0] + Gdx.graphics.getWidth() / 2 + toptube.getWidth();
			}


			if ((Gdx.input.justTouched()||Gdx.input.isKeyJustPressed(Input.Keys.SPACE))&&gameActive)
			{           speed=0;
				check=true;
				font.setColor(Color.BLACK);
				tubeY[0] = (random.nextFloat() - 0.5f) * (maxdisplacement);
				tubeY[1] = (random.nextFloat() - 0.5f) * (maxdisplacement);
				posY=Gdx.graphics.getHeight()/2-bird2.getHeight()/2;
				gamestate = 0;
				score=0;
				gameActive=false;


			}

		}
		if(gamestate!=-1)
		{

			if (i == 1)
				batch.draw(bird1, Gdx.graphics.getWidth() / 2 - bird1.getWidth() / 2, posY);
			else
				batch.draw(bird2, Gdx.graphics.getWidth() / 2 - bird2.getWidth() / 2, posY);
		}

		birdCircle.set(Gdx.graphics.getWidth() / 2, posY + bird1.getHeight() / 2, bird1.getWidth() / 2);

		if(drawFont)
			font.draw(batch, Integer.toString(score), 100, 200);

		batch.end();


	}


	@Override
	public void dispose () {
		batch.dispose();
		bg.dispose();
		bird1.dispose();
		bird2.dispose();
		toptube.dispose();
		bottomtube.dispose();
		font.dispose();
		jumpsound.dispose();
		impactsound.dispose();
	}
}


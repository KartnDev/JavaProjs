package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Background bg;
	private Bird bird;
	private Walls walls;
	boolean gameOver;
	Texture restart;


	@Override
	public void create () {
		batch = new SpriteBatch();
		bg = new Background();
		bird = new Bird();
		walls = new Walls();
		gameOver = false;
		restart = new Texture("RestartBtn.png");
	}

	@Override
	public void render () {
		update();
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		bg.render(batch);
		walls.render(batch);
		if(!gameOver){
			bird.render(batch);
		} else {
			batch.draw(restart, 200, 200);
		}
		batch.end();
	}

	public void update(){
		bg.update();
		bird.update();
		walls.update();
		for(int i =0; i<Walls.walls.length; i++){
			if(bird.position.x > Walls.walls[i].position.x && bird.position.x < Walls.walls[i].position.x+50){
				if(Walls.walls[i].emptySpace.contains(bird.position)){
					gameOver = true;
				}
			}
		}
		if(bird.position.y < 0 || bird.position.y > 600){
			gameOver = true;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && gameOver){
			//restartGame\
			reCreate();
		}

	}

	@Override
	public void dispose () {
		batch.dispose();
	}


	public void reCreate(){
		bird.reCreate();
		walls.reCreate();
		gameOver = false;
	}
}

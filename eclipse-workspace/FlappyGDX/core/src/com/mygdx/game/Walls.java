package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Walls {
    class WallPair {
        public Vector2 position;
        private float speed;
        private int offset;
        public Rectangle emptySpace;


        public WallPair(Vector2 position) {
            this.position = position;
            speed = 2;
            offset = new Random().nextInt(250);
            emptySpace = new Rectangle(position.x, position.y-offset+300, 50, distance);
        }

        public void update() {
            position.x -= speed;
            if(position.x < -50){
                position.x = 800;
                offset = new Random().nextInt(250);
            }
            emptySpace.x = position.x;
        }
    }

    public static WallPair[] walls;
    private Texture texture;
    private int distance;
    public Walls(){
        texture = new Texture("Wall.png");
        walls = new WallPair[4];
        distance = 300;
        int startPosX = 400;
        for (int i=0; i< walls.length; i++){
            walls[i] = new WallPair(new Vector2(startPosX, 0));
            startPosX += 220;
        }
    }
    public void render(SpriteBatch batch){
        for(int i =0; i < walls.length; i++){
            batch.draw(texture, walls[i].position.x, walls[i].position.y);
            batch.draw(texture, walls[i].position.x, walls[i].position.y + texture.getHeight() + distance - walls[i].offset );
        }

    }
    public void update(){
        for(int i =0; i<walls.length; i++){
            walls[i].update();
        }
    }

    public void reCreate(){
        int startPosX = 400;
        for (int i=0; i< walls.length; i++){
            walls[i] = new WallPair(new Vector2(startPosX, 0));
            startPosX += 220;
        }
    }
}

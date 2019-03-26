package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Background {

    class BGPic{
        private Texture texture;
        private Vector2 position;
        BGPic(Vector2 position){
            texture = new Texture("back.png");
            this.position = position;
        }
    }


    private int speed;
    private BGPic[] backgrounds;

    public Background(){
        speed = 4;
        backgrounds = new BGPic[2];
        backgrounds[0] = new BGPic(new Vector2(0,0));
        backgrounds[1] = new BGPic(new Vector2(800, 0));
    }
    public void render(SpriteBatch batch){
        for (int i =0;i < backgrounds.length;i++){
            batch.draw(backgrounds[i].texture, backgrounds[i].position.x, backgrounds[i].position.y);
        }
    }
    public void update(){
        for (int i=0;i < backgrounds.length; i++){
            backgrounds[i].position.x -= speed;
        }

        if(backgrounds[0].position.x <= -800){
            backgrounds[0].position.x = 0;
            backgrounds[1].position.x = 800;
        }
    }
}

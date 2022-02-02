package com.inaki.animation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.Vector;

public class Animator extends ApplicationAdapter {

    // Constant rows and columns of the sprite sheet
    private static final int FRAME_COLS = 10, FRAME_ROWS =4;

    // Objects used
    Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
    Texture walkSheet;
    Texture background,background2,background3,statue;
    SpriteBatch spriteBatch;
    TextureRegion[] walkUp,walkRight,walkDown,walkLeft;
    Texture idle,swordImage;
    float posx,posy,tretPosx,tretPosY;
    Rectangle sword = new Rectangle();
    Vector2 velocity = new Vector2();
    int map = 1;
    int lastMap;
    boolean mapChange = false;
    boolean shoot = false;
    String state = "";
    Pixmap tretPixmap;

    // A variable for tracking elapsed time for the animation
    float stateTime;

    @Override
    public void create() {

        background = new Texture(Gdx.files.internal("templeBackground.jpg"));
        // Load the sprite sheet as a Texture
        posy = 680;
        posx = 580;
        walkSheet = new Texture(Gdx.files.internal("spritesheetLink2.png"));
        idle = new Texture(Gdx.files.internal("idle.PNG"));
//        tretPixmap = new Pixmap( 60, 20, Pixmap.Format.RGB888);
//        tretPixmap.setColor( Color.BLACK );
//        tretPixmap.fill();
//        tretTexture = new Texture(tretPixmap);
        swordImage = new Texture(Gdx.files.internal("sword.png"));

        sword.height = 20;
        sword.width = 60;
        velocity.x = 15;
        velocity.y = 15;
        sword.x = (int) posx;
        sword.y = (int) posy;
        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.

        walkDown = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {

                walkDown[index++] = tmp[0][j];
            }
        }
        walkLeft = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkLeft[index++] = tmp[1][j];
            }
        }
        walkUp = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkUp[index++] = tmp[2][j];
            }
        }
        walkRight = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkRight[index++] = tmp[3][j];
            }
        }

        // Initialize the Animation with the frame interval and array of frames
        walkAnimation = new Animation<TextureRegion>(0.075f, walkUp);

        // Instantiate a SpriteBatch for drawing and reset the elapsed animation
        // time to 0

        stateTime = 0f;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen
        if(posx <= 0 || posx >= 1200 || posy <= 0 || posy >= 800){
            if(map == 1){
                posx = 580;
                posy = 680;
            }
            if(map == 2){
                posx = 950;
                posy = 220;
            }
            if(map == 3) {
                posx = 700;
                posy = 680;
            }
        }
        if(map == 1 && mapChange){
            if(lastMap == 2) {
                posx = 950;
                posy = 520;
            }
            if(lastMap == 3) {
                posx = 930;
                posy = 80;
            }
            mapChange = false;
        }
        if(map == 2 && mapChange){
            posx = 960;
            posy = 100;
            mapChange = false;
        }
        if(map == 3 && mapChange){
            posx = 700;
            posy = 680;
            mapChange = false;
        }
        if (map == 1){
            background = new Texture(Gdx.files.internal("templeBackground.jpg"));
            if(posx >= 950 && posx <=1000 && posy >= 550 ){
                map = 2;
                mapChange = true;
            }
            if(posx <= 1010 && posy <= 20 ){
                map = 3;
                mapChange = true;
            }
        }
        if (map == 2){
            background = new Texture(Gdx.files.internal("templeBackground2.jpg"));
            if(posx >= 950 && posy < 100 ){
                map = 1;
                lastMap = 2;
                mapChange = true;
            }

        }
        if (map == 3){
            background = new Texture(Gdx.files.internal("templeBackground3.jpg"));
            if(posx >=410 && posx <=700 && posy >= 700 ){
                map = 1;
                lastMap = 3;
                mapChange = true;
            }

        }
        spriteBatch = new SpriteBatch();
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        spriteBatch.end();
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);

        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            state = "up";
            walkAnimation = new Animation<TextureRegion>(0.075f, walkUp);
            spriteBatch.begin();
            posy = posy +10;
            spriteBatch.draw(currentFrame, posx, posy);
            spriteBatch.end();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            state = "down";
            walkAnimation = new Animation<TextureRegion>(0.075f, walkDown);
            spriteBatch.begin();
            posy = posy -10;
            spriteBatch.draw(currentFrame, posx, posy);
            spriteBatch.end();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            state = "right";
            walkAnimation = new Animation<TextureRegion>(0.075f, walkRight);
            spriteBatch.begin();
            posx = posx+10;
            spriteBatch.draw(currentFrame, posx, posy);
            spriteBatch.end();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            state = "left";
            walkAnimation = new Animation<TextureRegion>(0.075f, walkLeft);
            spriteBatch.begin();
            posx = posx-10;
            spriteBatch.draw(currentFrame, posx, posy);
            spriteBatch.end();
        }
        else{
            state = "down";
            spriteBatch.begin();
            spriteBatch.draw(idle, posx, posy);
            spriteBatch.end();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            shoot = true;
            switch (state){
                case "up":
                    swordImage = new Texture(Gdx.files.internal("swordUp.png"));

                    spriteBatch.begin();
                    spriteBatch.draw(swordImage, sword.x+10, sword.y + 80);
                    spriteBatch.end();
                    break;
                case "down":
                    swordImage = new Texture(Gdx.files.internal("sword.png"));

                    spriteBatch.begin();
                    spriteBatch.draw(swordImage, sword.x+10, sword.y - 50);
                    spriteBatch.end();
                    break;
                case "right":
                    swordImage = new Texture(Gdx.files.internal("swordRight.png"));

                    spriteBatch.begin();
                    spriteBatch.draw(swordImage, sword.x + 80, sword.y + 10);
                    spriteBatch.end();
                    break;
                case "left":
                    swordImage = new Texture(Gdx.files.internal("swordLeft.png"));

                    spriteBatch.begin();
                    spriteBatch.draw(swordImage, sword.x - 80, sword.y + 10);
                    spriteBatch.end();
                    break;
            }

        }else{
            shoot = false;
        }
        if(shoot){
            switch (state){
                case "up":
                    sword.y = (int) (sword.y + velocity.y);
                    break;
                case "down":
                    sword.y = (int) (sword.y - velocity.y);
                    break;
                case "right":
                    sword.x = (int) (sword.x + velocity.x);
                    break;
                case "left":
                    sword.x = (int) (sword.x - velocity.x);
                    break;
            }
        }
        if(!shoot || sword.x >= 1200 || sword.x <= 0 || sword.y >= 800 || sword.y <= 0){
            swordImage.dispose();
            sword.x = (int) posx;
            sword.y = (int) posy;
        }
    }
}

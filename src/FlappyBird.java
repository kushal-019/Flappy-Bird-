import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class FlappyBird extends JPanel implements ActionListener, KeyListener{
    int boardWidth = 360;
    int boardHeight = 640;
    // Considering Dimentions of BG image

    // images
    Image backgroundImg;
    Image birdImage;
    Image topPipeImg;
    Image bottomPipeImg;

    // Bird Image variables
    int birdX = boardWidth/8;
    int birdY = boardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird{
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img){
            this.img = img;
        }

    } 
    // Game Logic
    Bird bird;
    // speed at which bird encounters pipe;
    int velocityX = -4;
    // change in bird height per space key pressed
    int velocityY = 0;
    // Gravity pulling bird down
    int gravity = 1;

    Timer gameLoop;
    Timer placepipeTimer;
    boolean gameover = false;
    double score = 0;

    Clip pointClip;
    Clip dieClip;

    // pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    public class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img){
            this.img= img;
        }
    }


    ArrayList<Pipe>pipes;

    Random random = new Random();

    public void placePipes(){
        int randomPipeY =(int)(pipeY - pipeHeight/4- Math.random()*(pipeHeight/2));
        int openSpace = boardHeight/4;

        Pipe topPipe = new Pipe(topPipeImg); 
        topPipe.y = randomPipeY;
        pipes.add(topPipe);
        Pipe bottompipe = new Pipe(bottomPipeImg); 
        bottompipe.y = topPipe.y + pipeHeight + openSpace ;
        pipes.add(bottompipe);
    }


    FlappyBird(){

        setFocusable(true);
        addKeyListener(this);

        setPreferredSize(new Dimension(boardWidth , boardHeight));

        // we used ImageIcon so in end to extract images we use getImage();
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        // Bird
        bird = new Bird(birdImage);
        pipes = new ArrayList<Pipe>();
        
        gameLoop = new Timer(1000/60 , this);

         if (!loadSounds()) {
            System.out.println("Error loading sounds. The game may not have sound effects.");
        }

        placepipeTimer = new Timer(1500 , new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });
        gameLoop.start();
        placepipeTimer.start();

    }

    private boolean loadSounds() {
        try {
            // Check if the resource is available
            if (getClass().getResourceAsStream("point.wav") == null) {
                System.out.println("point.wav not found");
            }
            if (getClass().getResourceAsStream("die.wav") == null) {
                System.out.println("die.wav not found");
            }
    
            // Load point sound
            AudioInputStream pointStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("point.wav"));
            pointClip = AudioSystem.getClip();
            pointClip.open(pointStream);
    
            // Load die sound
            AudioInputStream dieStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("die.wav"));
            dieClip = AudioSystem.getClip();
            dieClip.open(dieStream);
    
            return true; // Success
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            return false; // Failure
        }
    }

    public void paintComponent(Graphics g){
        // super refers to JPanel as flappybird class too extends to JPanel
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        // background
        g.drawImage(backgroundImg , 0,0 , boardWidth , boardHeight , null);

        // bird
        g.drawImage(birdImage , bird.x , bird.y , bird.width , birdHeight ,null );

        //pipes
        for(int i=0;i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img , pipe.x , pipe.y , pipe.width , pipe.height , null);
        }


        // Game Over and score updation
        g.setColor(Color.white);
        g.setFont(new Font("Arial" , Font.PLAIN , 32));
        if(gameover){
            g.drawString("Game Over " + String.valueOf((int)score) , 10   ,35);
        }
        else{
            g.drawString(String.valueOf((int)score) , 10 , 35);
        }

    }

    public void move(){
        velocityY += gravity;
        bird.y += velocityY;
        // preventing it to go out of frame;
        bird.y = Math.max(bird.y , 0);
        
        // moving pipes out of frame to add new pipes
        for(int i=0;i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;
            
            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score += 0.5; // Increment score
                if (pointClip != null) {
                    pointClip.setFramePosition(0); // Rewind to the beginning
                    pointClip.start(); // Play sound
                }
            }

            // Check for collision
            if (collision(bird, pipe)) {
                gameover = true;
            }

            // Remove pipes that have gone off the screen
            if (pipe.x + pipe.width < 0) {
                pipes.remove(pipe);
            }
        }
        if(bird.y > boardHeight){gameover = true;}
    }
    // Over riding to prevent default behave and calling repaint() to keep updated 
    @Override 
    public void actionPerformed(ActionEvent e ){
        move();
        repaint();
        
        if(gameover == true){
            placepipeTimer.stop();   
            gameLoop.stop();
            if(dieClip != null){
                dieClip.setFramePosition(0);
                dieClip.start();
            }
        }
    }
    public boolean collision(Bird a , Pipe b){
        // Conditions for collision
        return a.x < b.x + b.width && a.x + a.width >b.x && a.y < b.y + b.height && a.y + a.height > b.y ;
    }

    @Override 
    public void keyPressed(KeyEvent e ){
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9;

            if(gameover){
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameover  =false;
                gameLoop.start();
                placepipeTimer.start();

            }
        }
    }
    @Override 
    public void keyTyped(KeyEvent e ){
        
    }
    @Override 
    public void keyReleased(KeyEvent e ){
        
    }
}
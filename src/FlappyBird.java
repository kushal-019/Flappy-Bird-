import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

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
    int velocityY = 0;
    int gravity = 1;

    Timer gameLoop;


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
        
        gameLoop = new Timer(1000/60 , this);
        gameLoop.start();

    }

    public void paintComponent(Graphics g){
        // super refers to JPanel as flappybird class too extends to JPanel
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.drawImage(backgroundImg , 0,0 , boardWidth , boardHeight , null);

        g.drawImage(birdImage , bird.x , bird.y , bird.width , birdHeight ,null );


    }

    public void move(){
        velocityY += gravity;
        bird.y += velocityY;
        // preventing it to go out of frame;
        bird.y = Math.max(bird.y , 0);
    }
    // Over riding to prevent default behave and calling repaint() to keep updated 
    @Override 
    public void actionPerformed(ActionEvent e ){
        move();
        repaint();
    }

    @Override 
    public void keyPressed(KeyEvent e ){
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9;
        }
    }
    @Override 
    public void keyTyped(KeyEvent e ){
        
    }
    @Override 
    public void keyReleased(KeyEvent e ){
        
    }
}
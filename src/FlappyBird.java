import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel{
    int boardWidth = 360;
    int boardHeight = 640;
    // Considering Dimentions of BG image

    // images
    Image backgroundImg;
    Image birdImage;
    Image topPipeImg;
    Image bottomPipeImg;

    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth , boardHeight));

        // we used ImageIcon so in end to extract images we use getImage;
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
    }

    public void paintComponent(Graphics g){
        // super refers to JPanel as flappybird class too extends to JPanel
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.drawImage(backgroundImg , 0,0 , boardWidth , boardHeight , null);
    }
}
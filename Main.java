import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;


public class Main {
	
	public static void main(String[] args){
		JFrame frame = new JFrame("Diamond Square Test");
		Game g = new Game();

		final int width = 10;
		final int height = 10;
		final int dimension = 9;
		
		final long seed = 31;
		
		//execute the algorithm with bound
		int[][] heights = DSquare.generateHeightMapWithBound(new Random(seed), 0.05, 255, width, height, dimension);
				
		for(int x = 0; x < width * dimension; x++){
			for(int y = 0; y < height * dimension; y++){
				g.setPixel(x, y, heights[x][y]);
			}
		}
		
		frame.add(g);
		frame.pack();
		frame.setSize(640, 480);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	
	}
	
}

class Game extends Canvas{

	private static final long serialVersionUID = 5986085377374594305L;
	
	public BufferedImage img = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
	
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(img, 0, 0, null);
	}
	
	public void setPixel(int x, int y, int height){
		final int size = 10;
		Graphics g = img.getGraphics();
		g.setColor(new Color(height, height, height));
		g.fillRect(x * size, y * size, size, size);
		g.dispose();
	}
	
}



import java.util.Random;

public class DSquare {
	
	private static final int EMPTY = -1;
	
	private double[][] array;
	private int dimension;
	private double noise;
	private Random rng;
	
	private DSquare(int dimension, double noise, Random rng){
		this.dimension = dimension;
		this.rng = rng;
		this.noise = noise;
		this.array = new double[dimension][dimension];
		
		//fill the empty array
		for(int y = 0; y < dimension; y++){
			for(int x = 0; x < dimension; x++){
				array[x][y] = EMPTY;
			}
		}
	}
	
	/***
	 * Executes the Diamond Square
	 * @param left Last column of chunk onto the left
	 * @param top Last row of the chunk onto the top
	 */
	private void execute(double[] left, double[] top){
		//load random values wherever necessary
		loadRandomValues();
		
		//set the first column of the chunk to previous chunk's final column
		if(left != null){
			for(int y = 0; y < dimension; y++){
				array[0][y] = left[y];
			}
		}
		
		//set the first row of the chunk to previous chunk's final row
		if(top != null){
			for(int x = 0; x < dimension; x++){
				array[x][0] = top[x];
			}
		}
		
		//init division variable
		int div = 1;
		
		//while dimension / div more or equal to 2
		//so 3 width/height (0, 1, 2)
		while(dimension / div >= 2){
			int inc = (dimension - 1) / div;	
			for(int y = 0; y < dimension; y += inc){
				if(inc + y >= dimension) break;
				for(int x = 0; x < dimension; x += inc){
					if(inc + x >= dimension) break;
					//execute diamond square for each place until every box is filled
					diamondSquare(x, y, (x + inc), (y + inc));
				}
			}
			div *= 2;
		}

	}
	
	/**
	 * @return Last column of the chunk
	 */
	private double[] getLastColumn(){
		if(array != null){
			double[] buffer = new double[dimension];
			for(int i = 0; i < dimension; i++){
				buffer[i] = array[dimension - 1][i];
			}
			return buffer;
		}
		return null;
	}
	
	/**
	 * @return Last row of the chunk
	 */
	private double[] getLastRow(){
		if(array != null){
			double[] buffer = new double[dimension];
			for(int i = 0; i < dimension; i++){
				buffer[i] = array[i][dimension - 1];
			}
			return buffer;
		}
		return null;
	}
	
	/**
	 * @return Created chunks
	 */
	private double[][] getResult() {
		return array;
	}

	/**
	 * Creates noise within -noise and +noise limits
	 * @return Created random noise
	 */
	private double noise(){
		int sign = (rng.nextBoolean())? 1 : -1;
		return (rng.nextDouble() * noise) * sign;
	}
	
	/***
	 * Sets array position if its empty
	 * @param x -> X position
	 * @param y -> Y position
	 * @param val value to be set
	 */
	private void setIfEmpty(int x, int y, double val){
		//check if empty is so change.
		if(array[x][y] == EMPTY){
			array[x][y] = val;
		}
	}
	
	/***
	 * Loads 4 random values to 4 corners if they are EMPTY
	 */
	private void loadRandomValues(){
		//load 4 random values to 4 corners if not set
		setIfEmpty(0, 0, rng.nextDouble());
		setIfEmpty(dimension - 1, 0, rng.nextDouble());
		setIfEmpty(0, dimension - 1, rng.nextDouble());
		setIfEmpty(dimension - 1, dimension - 1, rng.nextDouble());
	}
	
	private void diamondSquare(int x1, int y1, int x2, int y2){
	
		//DIAMOND PART
		/*
		 * X O O O X
		 * O O O O O
		 * O O Y O O
		 * O O O O O
		 * X O O O X
		 * 
		 * Using x calculate Y.
		 */
		double tL = array[x1][y1];
		double tR = array[x2][y1];
		double bL = array[x1][y2];
		double bR = array[x2][y2];
		
		double m = (tL + tR + bL + bR + noise()) / 4;
		
		//get the 4 corners calculate and set the middle from these
		//also add some noise
		int mX = x1 + ((x2 - x1) / 2);
		int mY = y1 + ((y2 - y1) / 2);		
		setIfEmpty(mX, mY, m);

		
		//SQUARE PART
		/*
		 * X O Y O X
		 * O O O O O
		 * Y O X O Y
		 * O O O O O
		 * X O Y O X
		 * 
		 * Using Xs calculate Ys.
		 */
		//get middle and 4 corners and fill the middles
		double l = (tL + bL + m + noise()) / 3;
		setIfEmpty(x1, mY, l);
		
		double r = (tR + bR + m + noise()) / 3; //right
		setIfEmpty(x2, mY, r);
		
		double t = (tL + m + tR + noise()) / 3;
		setIfEmpty(mX, y1, t);
		
		double b = (bL + m + bR + noise()) / 3;
		setIfEmpty(mX, y2, b);
	
	}
	
	public static double[][] generateHeightMap(Random rng, double noise, int horzCount, int #, int dimension){
		
		//calculate width and height of the output array
		int width = horzCount * dimension;
		int height = vertCount * dimension;
		
		//create output array and dsquare array
		double[][] heights = new double[width][height];
		DSquare[][] array = new DSquare[horzCount][vertCount];
		
		//calculate each chunk using the diamond square algorithm
		//if chunk is not start chunk use the previous chunk's last row and column
		//to keep the procudural generation.
		for(int x = 0; x < horzCount; x++){
			for(int y = 0; y < vertCount; y++){
				DSquare d = new DSquare(dimension, noise, rng);
				double[] left = (x > 0)? array[x - 1][y].getLastColumn() : null;
				double[] top = (y > 0)? array[x][y - 1].getLastRow() : null;
				d.execute(left, top);
				array[x][y] = d;
			}
		}
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				double val = array[x / dimension][y / dimension].getResult()[x % dimension][y % dimension];
				val = (val >= 0)? val : 0;
				val = (val <= 1)? val : 1;
				heights[x][y] = val; 
			}
		}
		
		return heights;
	}
	
	/***
	 * Generates an int height map. Range can be set from arguments.
	 * @param rng Instance of Random
	 * @param noise Noise limit (can be minus)
	 * @param bound Maximum integer value
	 * @param horzCount Horizontal chunk count
	 * @param vertCount Vertical chunk count
	 * @param dimension Dimension of each chunk
	 * @return
	 */
	public static int[][] generateHeightMapWithBound(Random rng, double noise, int bound, int horzCount, int vertCount, int dimension ){		
		//get double map
		double d[][] = generateHeightMap(rng, noise, horzCount, vertCount, dimension);
		
		//get dimensions of the map
		int width = horzCount * dimension;
		int height = vertCount * dimension;
		int[][] scaled = new int[width][height];
		
		//scale the map into integer map
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				scaled[x][y] = (int) (d[x][y] * bound);
			}
		}		
		return scaled;
	}

}

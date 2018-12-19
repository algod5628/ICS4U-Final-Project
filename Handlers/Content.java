/*Austin Van Braeckel
12/13/2018
This class loads, on startup, all resources that need to be repeatedly used, such
as enemies of which there may be a large number.  Instead of reading the image and sprites
every time an enemy is created, it is done once in this class.
 */

package Handlers;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;



public class Content {
	
	public static BufferedImage[][] Slime = load("/Sprites/Enemies/slime-Sheet.png", 32, 25);
	public static BufferedImage[][] Explosion = load("/Sprites/Enemies/ExplosionAVBb.png", 30, 30);
        public static BufferedImage[][] Arrow = load("/Sprites/Player/EnergyArrowAVB.png",30,30);
	
        /**
         * Loads the given sprite-sheet with the specified number of rows and columns 
         * @param s String of the file path for the sprite-sheet
         * @param w number of columns
         * @param h number of rows
         * @return filled BufferedImage 2D array
         */
	public static BufferedImage[][] load(String s, int w, int h) {
		BufferedImage[][] ret;
		try {
			BufferedImage spritesheet = ImageIO.read(Content.class.getResourceAsStream(s));
			int width = spritesheet.getWidth() / w;
			int height = spritesheet.getHeight() / h;
			ret = new BufferedImage[height][width];
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					ret[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
				}
			}
			return ret;
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}
	
}

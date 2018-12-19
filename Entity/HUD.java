/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;


import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
/**
 *
 * @author Family
 */
public class HUD {
    
    private Player player;
    private BufferedImage image;
    private Font font;
    
    public HUD(Player p){
        player = p;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/HUD/HUD.png"));
            font = new Font("Century Gothic", Font.PLAIN, 10);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g){
        g.drawImage(image,0,10,null);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString((int)(player.getHealth()) + "/" + (int)(player.getMaxHealth()), 30, 23);
        g.drawString(player.getAmmo() / 100 + "/" + (int)(player.getMaxAmmo() / 100), 30, 43);
    }
    
}

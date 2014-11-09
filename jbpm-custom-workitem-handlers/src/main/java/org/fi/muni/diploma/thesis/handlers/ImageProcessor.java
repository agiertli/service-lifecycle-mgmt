package org.fi.muni.diploma.thesis.handlers;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageProcessor {

	public static void main(String[] args) throws Exception {
//		final BufferedImage image = ImageIO.read(new URL(
//				"file:///home/osiris/Documents/Diplomka/Development/active.png"));
//
//		Graphics g = image.getGraphics();
//
//		String text = "Hello World";
//
//		g.setFont(g.getFont().deriveFont(20f));
//		g.setColor(Color.BLACK);
//
//		// draw a centered string
//		FontMetrics fm = g.getFontMetrics();
//		int height = image.getHeight();
//		int width = image.getWidth();
//
//		Rectangle2D r = fm.getStringBounds(text, g);
//		int x = (width - (int) r.getWidth()) / 2;
//		int y = (height - (int) r.getHeight()) / 2 + fm.getAscent();
//		g.drawString(text, x, y);
//		g.dispose();
		
		
		BufferedImage image = ImageIO.read(new URL("file:///home/osiris/Documents/Diplomka/Development/active.png"));
		 BufferedImage page = new BufferedImage(image.getWidth()*3,image.getHeight(),BufferedImage.TYPE_INT_ARGB);
		 Graphics2D paint = page.createGraphics();
		  paint.setPaint(Color.WHITE);
		    paint.fillRect ( 0, 0, page.getWidth(), page.getHeight() ); 
		    paint.setBackground(Color.WHITE);
		

		    int width = 193;
		    int height = 112;
		    for (int i=0; i < 3; i++){
		    
		        int row = i / 3; // This will truncate to 0 or 1.
		        int column = i % 3; // Mod will produce the remainder of i / 6 in the range 0-5
		        paint.drawImage(image, column * width, row * height, null);
		    }
		    
		    
		
		
		ImageIO.write(page, "png", new File("/home/osiris/Documents/Diplomka/Development/test.png"));
		
		
		
	}

}

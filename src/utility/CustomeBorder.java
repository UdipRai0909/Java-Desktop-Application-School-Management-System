package utility;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.border.AbstractBorder;


public class CustomeBorder extends AbstractBorder{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public void paintBorder(Component c, Graphics g, int x, int y,
            int width, int height) {
        super.paintBorder(c, g, x, y, width, height);
        Graphics2D g2d = (Graphics2D)g;
        Paint COLOR_BORDE_SIMPLE = null;
		g2d.setPaint(COLOR_BORDE_SIMPLE);
        Shape shape = new RoundRectangle2D.Float(0, 0, c.getWidth()-1, c.getHeight()-1,9, 9);
        g2d.draw(shape);
    }
}
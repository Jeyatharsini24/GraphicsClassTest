import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;

import javax.swing.GrayFilter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;

public class MyUI implements Border, UIResource, SwingConstants {
	boolean isEnabledButton = true;

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setSize(500, 500);
		JPanel borderPanel = new JPanel();
		borderPanel.setBackground(Color.PINK);
		MyUI myBorder = new MyUI();
		myBorder.setEnabledButton(true);
		borderPanel.setBorder(myBorder);

		Object[] stringFor = new Object[] { "1", "2", null };
		JComboBox compoBox = new JComboBox(stringFor);
		compoBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (compoBox.getSelectedItem() == null) {
					myBorder.setEnabledButton(false);
					borderPanel.repaint();
				} else {
					myBorder.setEnabledButton(true);
					borderPanel.repaint();
				}
			}
		});
		f.getContentPane().add(compoBox, BorderLayout.NORTH);
		f.getContentPane().add(borderPanel, BorderLayout.CENTER);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		makeCray2(c, g, x, y, width, height);
	}
	
	private void makeCray(Component c, Graphics g, int x, int y, int width, int height)
	{
		Icon i = new ImageIcon("home.png");
		Graphics2D g2c = (Graphics2D) g.create();
		try {
			g2c.setColor(Color.LIGHT_GRAY);
			Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
			g2c.setComposite(comp);
			i.paintIcon(c, g2c, x, y);
		} finally {
			g2c.dispose();
		}
		g.setColor(Color.GREEN);
		g.fillRect(50, 50, 50, 50);
	}
	
	private void makeCray2(Component c, Graphics g, int x, int y, int width, int height)
	{
		Icon i = getDisabledIconCustom(new ImageIcon("home.png"));
		Graphics2D g2c = (Graphics2D) g.create();
		try {
			i.paintIcon(c, g2c, x, y);
		} finally {
			g2c.dispose();
		}
	}
	
	public Icon getDisabledIcon(Icon icon) {
	    return new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) icon).getImage()));
	}
	
	public Icon getDisabledIconCustom(Icon icon) {
		Image image = ((ImageIcon) icon).getImage();
		
		GrayFilter filter = new GrayFilter(true, 75);
        ImageProducer prod = new FilteredImageSource(image.getSource(), filter);
        Image grayImage = Toolkit.getDefaultToolkit().createImage(prod);
        
        return new ImageIcon(grayImage);
	}

	private void boundsWithClip(Component c, Graphics g, int x, int y, int width, int height) {
		Icon i = new ImageIcon("home.png");
		JButton b = new JButton(i);
		Graphics2D g2c = (Graphics2D) g.create();
		try {
			b.setBounds(50, 0, width, height);
			g2c.setClip(0, 0, i.getIconWidth(), i.getIconHeight()); // crop
			b.setEnabled(isEnabledButton);
			b.paint(g2c);
		} finally {
			g2c.dispose();
		}
		g.setColor(Color.GREEN);
		g.fillRect(50, 50, 50, 50);
	}
	
	private void boundsWithClip2(Component c, Graphics g, int x, int y, int width, int height) {
		Icon i = new ImageIcon("home.png");
		JButton b = new JButton(i);
		Graphics2D g2c = (Graphics2D) g.create();
		try {
			b.setBounds(0, 0, i.getIconWidth(), i.getIconHeight());
			b.setEnabled(isEnabledButton);
			b.paint(g2c);
		} finally {
			g2c.dispose();
		}
	}

	public void setEnabledButton(boolean isEnabledButton) {
		this.isEnabledButton = isEnabledButton;
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(10, 10, 10, 10);
	}

	@Override
	public boolean isBorderOpaque() {
		return false;
	}
}

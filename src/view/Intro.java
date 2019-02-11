package view;

import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Intro {
	private final ImageIcon b1 = new ImageIcon(this.getClass().getResource("/image/baris/baris_1.jpg"));
	private final ImageIcon b2 = new ImageIcon(this.getClass().getResource("/image/baris/baris_2.jpg"));
	private final ImageIcon b3 = new ImageIcon(this.getClass().getResource("/image/baris/baris_3.jpg"));
	private final ImageIcon b4 = new ImageIcon(this.getClass().getResource("/image/baris/baris_4.jpg"));
	private final ImageIcon mehmet = new ImageIcon(this.getClass().getResource("/image/text/mehmet.png"));
	private final ImageIcon baris = new ImageIcon(this.getClass().getResource("/image/text/baris.png"));
	private final ImageIcon cirit = new ImageIcon(this.getClass().getResource("/image/text/cirit.png"));
	
	public void generate(ArrayList<Pixels> pixels,int mazeSize){
		
		pixels.get(mazeSize).setIcon(b1);
		pixels.get(mazeSize+1).setIcon(b2);
		pixels.get(mazeSize*2).setIcon(b3);
		pixels.get(mazeSize*2+1).setIcon(b4);

		pixels.get(mazeSize*2+3).setIcon(mehmet);
		pixels.get(mazeSize*2+4).setIcon(baris);
		pixels.get(mazeSize*2+5).setIcon(cirit);
		
	}
}

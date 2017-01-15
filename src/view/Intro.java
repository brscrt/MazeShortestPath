package view;

import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Intro {
	private final ImageIcon b1 = new ImageIcon(this.getClass().getResource("/resim/baris/baris_1.jpg"));
	private final ImageIcon b2 = new ImageIcon(this.getClass().getResource("/resim/baris/baris_2.jpg"));
	private final ImageIcon b3 = new ImageIcon(this.getClass().getResource("/resim/baris/baris_3.jpg"));
	private final ImageIcon b4 = new ImageIcon(this.getClass().getResource("/resim/baris/baris_4.jpg"));
	private final ImageIcon r1 = new ImageIcon(this.getClass().getResource("/resim/remzi/remzi_1.jpg"));
	private final ImageIcon r2 = new ImageIcon(this.getClass().getResource("/resim/remzi/remzi_2.jpg"));
	private final ImageIcon r3 = new ImageIcon(this.getClass().getResource("/resim/remzi/remzi_3.jpg"));
	private final ImageIcon r4 = new ImageIcon(this.getClass().getResource("/resim/remzi/remzi_4.jpg"));
	private final ImageIcon no100 = new ImageIcon(this.getClass().getResource("/resim/text/100.png"));
	private final ImageIcon no201 = new ImageIcon(this.getClass().getResource("/resim/text/201.png"));
	private final ImageIcon no107 = new ImageIcon(this.getClass().getResource("/resim/text/107.png"));
	private final ImageIcon no109 = new ImageIcon(this.getClass().getResource("/resim/text/109.png"));
	private final ImageIcon mehmet = new ImageIcon(this.getClass().getResource("/resim/text/mehmet.png"));
	private final ImageIcon baris = new ImageIcon(this.getClass().getResource("/resim/text/baris.png"));
	private final ImageIcon cirit = new ImageIcon(this.getClass().getResource("/resim/text/cirit.png"));
	private final ImageIcon remzi = new ImageIcon(this.getClass().getResource("/resim/text/remzi.png"));
	private final ImageIcon ozkan = new ImageIcon(this.getClass().getResource("/resim/text/ozkan.png"));
	
	public void generate(ArrayList<Pixels> pixels,int mazeSize){
		
		pixels.get(mazeSize).setIcon(b1);
		pixels.get(mazeSize+1).setIcon(b2);
		pixels.get(mazeSize*2).setIcon(b3);
		pixels.get(mazeSize*2+1).setIcon(b4);
		
		pixels.get(mazeSize*4).setIcon(r1);
		pixels.get(mazeSize*4+1).setIcon(r2);
		pixels.get(mazeSize*5).setIcon(r3);
		pixels.get(mazeSize*5+1).setIcon(r4);
		
		pixels.get(mazeSize+3).setIcon(no100);
		pixels.get(mazeSize+4).setIcon(no201);
		pixels.get(mazeSize+5).setIcon(no107);
		pixels.get(mazeSize*2+3).setIcon(mehmet);
		pixels.get(mazeSize*2+4).setIcon(baris);
		pixels.get(mazeSize*2+5).setIcon(cirit);
		
		pixels.get(mazeSize*4+3).setIcon(no100);
		pixels.get(mazeSize*4+4).setIcon(no201);
		pixels.get(mazeSize*4+5).setIcon(no109);
		pixels.get(mazeSize*5+3).setIcon(remzi);
		pixels.get(mazeSize*5+4).setIcon(ozkan);
		
	}
}

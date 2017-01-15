package main;

import javax.swing.SwingUtilities;

import view.Panel;

public class MainLab {

	public static void main(String[] args) {
		
		 SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	            	new Panel().initFrame();
	            }
	        });
	}

}

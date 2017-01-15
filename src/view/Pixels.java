package view;

import java.util.Queue;

import javax.swing.JButton;

import intelligence.Rotation;

public class Pixels extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Queue<Rotation> acikYonler;

	public Queue<Rotation> getAcikYonler() {
		return acikYonler;
	}

	public void setAcikYonler(Queue<Rotation> acikYonler) {
		this.acikYonler = acikYonler;
	}

	

}

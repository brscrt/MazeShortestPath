package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import intelligence.ArtificialIntelligence;

public class Panel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final ImageIcon empty = new ImageIcon(this.getClass().getResource("/image/empty.png"));
	public final ImageIcon full = new ImageIcon(this.getClass().getResource("/image/full.png"));
	public final ImageIcon ball = new ImageIcon(this.getClass().getResource("/image/ball.png"));
	public final ImageIcon destination = new ImageIcon(this.getClass().getResource("/image/finish.png"));
	public final ImageIcon passed = new ImageIcon(this.getClass().getResource("/image/passed.png"));

	public final ArrayList<Pixels> pixels = new ArrayList<>();
	final int startX = 300;
	final int startY = 20;
	public final int mazeSize = 8;
	private boolean sample;

	ArtificialIntelligence artificialIntelligence;
	Intro intro;

	JTextArea result;

	public Panel() {
		super();
		this.setLayout(null);
		drawThings();
		drawMap();
		artificialIntelligence = new ArtificialIntelligence(this);
		intro = new Intro();
	}

	public void initFrame() {
		JFrame frame = new JFrame("Maze Shortest Path");

		frame.add(this);

		frame.setSize(720, 480);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setVisible(true);
	}

	private void drawMap() {

		for (int i = 0; i < mazeSize; i++) {
			for (int j = 0; j < mazeSize; j++) {
				final Pixels button = new Pixels();
				button.setIcon(empty);
				button.setBorder(null);
				button.setBorderPainted(false);
				button.setBounds(startX + j * 40, startY + i * 40, 39, 39);
				add(button);

				button.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseReleased(MouseEvent e) {
						super.mouseReleased(e);
						if (e.getButton() == MouseEvent.BUTTON2) {
							for (int k = 0; k < pixels.size(); k++) {
								if (pixels.get(k).getIcon().equals(ball)) {
									if (pixels.get(k).equals(button) || button.getIcon().equals(full)
											|| button.getIcon().equals(destination))
										break;
									pixels.get(k).setIcon(empty);
									break;
								}
							}

							if (button.getIcon().equals(empty))
								button.setIcon(ball);
							else if (button.getIcon().equals(ball)) {
								button.setIcon(empty);
							}

						} else if (e.getButton() == MouseEvent.BUTTON3) {
							for (int k = 0; k < pixels.size(); k++) {
								if (pixels.get(k).getIcon().equals(destination)) {
									if (pixels.get(k).equals(button) || button.getIcon().equals(full)
											|| button.getIcon().equals(ball))
										break;
									pixels.get(k).setIcon(empty);
									break;
								}
							}
							if (button.getIcon().equals(empty))
								button.setIcon(destination);
							else if (button.getIcon().equals(destination))
								button.setIcon(empty);

						} else if (e.getButton() == MouseEvent.BUTTON1) {
							if (button.getIcon().equals(empty))
								button.setIcon(full);
							else if (button.getIcon().equals(full))
								button.setIcon(empty);
						}

					}

				});

				pixels.add(button);
			}
		}

	}

	public void resetMap() {
		for (Pixels pixel : pixels) {
			pixel.setAcikYonler(null);
		}
	}

	private void drawThings() {
		JButton start = new JButton("Start");
		start.setBounds(50, 20, start.getPreferredSize().width, start.getPreferredSize().height);
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int[] points = getStartAndEnd();
				if (points[0] == -1)
					JOptionPane.showMessageDialog(null, "Determine a start point");
				else if (points[1] == -1)
					JOptionPane.showMessageDialog(null, "Determine an end point");
				else {
					result.setText("");
					clearPassed();
					resetMap();
					artificialIntelligence.start();
				}

			}
		});
		add(start);

		JButton sampleMaze = new JButton("Sample Maze");
		sampleMaze.setBounds(50, 50, sampleMaze.getPreferredSize().width, sampleMaze.getPreferredSize().height);
		sampleMaze.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearMap();
				if (sample)
					drawMySample();
				else
					drawSampleLabyrinth();
				sample = !sample;

			}
		});
		add(sampleMaze);

		JButton clear = new JButton("Clear Map");
		clear.setBounds(50, 80, clear.getPreferredSize().width, clear.getPreferredSize().height);
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearMap();
			}
		});
		add(clear);

		JButton introB = new JButton("About Me");
		introB.setBounds(50, 110, introB.getPreferredSize().width, introB.getPreferredSize().height);
		introB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearMap();
				intro.generate(pixels, mazeSize);
			}
		});
		add(introB);

		result = new JTextArea();
		result.setEditable(false);
		result.setBackground(getBackground());
		add(result);

		JTextArea not = new JTextArea();
		not.setEditable(false);
		not.setBackground(getBackground());
		not.setText("Note : Left mouse click draws walls, middle click draws start point and right click draws destination point on the map."
				+ "\n\t If you wish, you can use sample map also.");
		not.setBounds(30, 400, not.getPreferredSize().width, not.getPreferredSize().height);
		add(not);
	}

	public int[] getStartAndEnd() {
		int start = -1, end = -1;
		for (int k = 0; k < pixels.size(); k++) {
			if (pixels.get(k).getIcon().equals(destination)) {
				end = k;
			} else if (pixels.get(k).getIcon().equals(ball)) {
				start = k;
			}
			if (start != -1 && end != -1)
				break;
		}
		return new int[] { start, end };
	}

	private void drawSampleLabyrinth() {
		int[] borders = new int[] { 6, 9, 10, 11, 12, 20, 21, 22, 24, 26, 28, 30, 32, 33, 36, 38, 43, 49, 51, 52, 53,
				54, 57, 62 };
		for (int i : borders) {
			pixels.get(i).setIcon(full);
		}
		pixels.get(56).setIcon(ball);
		pixels.get(29).setIcon(destination);
	}

	private void drawMySample() {
		int[] borders = new int[] { 4, 8, 10, 12, 16, 20, 25, 27, 32, 36, 42, 44, 48, 52, 56, 57, 58, 59 };
		for (int i : borders) {
			pixels.get(i).setIcon(full);
		}
		pixels.get(0).setIcon(ball);
		pixels.get(40).setIcon(destination);
	}

	private void clearMap() {
		for (Pixels pixel : pixels) {
			if (!pixel.getIcon().equals(empty)) {
				pixel.setIcon(empty);
			}
		}
	}

	public void clearPassed() {
		result.setText("");
		for (Pixels pixel : pixels) {
			if (pixel.getIcon().equals(passed)) {
				pixel.setIcon(empty);
			}
		}
	}

	public void drawPath(Integer[] path) {
		if (path != null)
			for (int i : path) {
				pixels.get(i).setIcon(passed);
			}
	}

	public void showResult(boolean full, int min, int adim) {
		if (full)
			result.setText(adim + " steps was taken totally on the map\n\nThe shortest path is far as much as " + min + " steps");
		else
			result.setText("No result");
		result.setBounds(30, 160, result.getPreferredSize().width, result.getPreferredSize().height);
	}

}

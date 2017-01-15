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

import intelligence.YapayZeka;

public class Panel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final ImageIcon bos = new ImageIcon(this.getClass().getResource("/resim/bos.png"));
	public final ImageIcon dolu = new ImageIcon(this.getClass().getResource("/resim/dolu.png"));
	public final ImageIcon top = new ImageIcon(this.getClass().getResource("/resim/top.png"));
	public final ImageIcon hedef = new ImageIcon(this.getClass().getResource("/resim/finish.png"));
	public final ImageIcon passed = new ImageIcon(this.getClass().getResource("/resim/passed.png"));

	public final ArrayList<Pixels> pixels = new ArrayList<>();
	final int startX = 300;
	final int startY = 20;
	public final int labirentSize = 8;
	private boolean ornek;

	YapayZeka yapayZeka;
	Intro intro;

	JTextArea sonuc;

	public Panel() {
		super();
		this.setLayout(null);
		drawThings();
		drawMap();
		yapayZeka = new YapayZeka(this);
		intro = new Intro();
	}

	public void initFrame() {
		JFrame frame = new JFrame("Labirentte en kısa yol bulma");

		frame.add(this);

		frame.setSize(720, 480);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setVisible(true);
	}

	private void drawMap() {

		for (int i = 0; i < labirentSize; i++) {
			for (int j = 0; j < labirentSize; j++) {
				final Pixels button = new Pixels();
				button.setIcon(bos);
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
								if (pixels.get(k).getIcon().equals(top)) {
									if (pixels.get(k).equals(button) || button.getIcon().equals(dolu)
											|| button.getIcon().equals(hedef))
										break;
									pixels.get(k).setIcon(bos);
									break;
								}
							}

							if (button.getIcon().equals(bos))
								button.setIcon(top);
							else if (button.getIcon().equals(top)) {
								button.setIcon(bos);
							}

						} else if (e.getButton() == MouseEvent.BUTTON3) {
							for (int k = 0; k < pixels.size(); k++) {
								if (pixels.get(k).getIcon().equals(hedef)) {
									if (pixels.get(k).equals(button) || button.getIcon().equals(dolu)
											|| button.getIcon().equals(top))
										break;
									pixels.get(k).setIcon(bos);
									break;
								}
							}
							if (button.getIcon().equals(bos))
								button.setIcon(hedef);
							else if (button.getIcon().equals(hedef))
								button.setIcon(bos);

						} else if (e.getButton() == MouseEvent.BUTTON1) {
							if (button.getIcon().equals(bos))
								button.setIcon(dolu);
							else if (button.getIcon().equals(dolu))
								button.setIcon(bos);
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
		JButton start = new JButton("Başla");
		start.setBounds(50, 20, start.getPreferredSize().width, start.getPreferredSize().height);
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int[] points = getStartAndEnd();
				if (points[0] == -1)
					JOptionPane.showMessageDialog(null, "Başlangıç noktası belirleyin");
				else if (points[1] == -1)
					JOptionPane.showMessageDialog(null, "Bitiş noktası belirleyin");
				else {
					sonuc.setText("");
					clearPassed();
					resetMap();
					yapayZeka.start();
				}

			}
		});
		add(start);

		JButton sample = new JButton("Örnek Labirent");
		sample.setBounds(50, 50, sample.getPreferredSize().width, sample.getPreferredSize().height);
		sample.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearMap();
				if (ornek)
					drawMySample();
				else
					drawSampleLabyrinth();
				ornek = !ornek;

			}
		});
		add(sample);

		JButton clear = new JButton("Haritayı temizle");
		clear.setBounds(50, 80, clear.getPreferredSize().width, clear.getPreferredSize().height);
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearMap();
			}
		});
		add(clear);

		JButton introB = new JButton("Hakkımızda");
		introB.setBounds(50, 110, introB.getPreferredSize().width, introB.getPreferredSize().height);
		introB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearMap();
				intro.generate(pixels, labirentSize);
			}
		});
		add(introB);

		sonuc = new JTextArea();
		sonuc.setEditable(false);
		sonuc.setBackground(getBackground());
		add(sonuc);

		JTextArea not = new JTextArea();
		not.setEditable(false);
		not.setBackground(getBackground());
		not.setText("Not : Farenin sol tuşu duvarları, orta tuş başlangıç noktasını , sağ tuş ise hedef noktasını"
				+ " ekrana çizdiriyor.\n\t İsterseniz örnek olarak çizlimiş labirenti kullanabilirsiniz.");
		not.setBounds(30, 400, not.getPreferredSize().width, not.getPreferredSize().height);
		add(not);
	}

	public int[] getStartAndEnd() {
		int start = -1, end = -1;
		for (int k = 0; k < pixels.size(); k++) {
			if (pixels.get(k).getIcon().equals(hedef)) {
				end = k;
			} else if (pixels.get(k).getIcon().equals(top)) {
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
			pixels.get(i).setIcon(dolu);
		}
		pixels.get(56).setIcon(top);
		pixels.get(29).setIcon(hedef);
	}

	private void drawMySample() {
		int[] borders = new int[] { 4, 8, 10, 12, 16, 20, 25, 27, 32, 36, 42, 44, 48, 52, 56, 57, 58, 59 };
		for (int i : borders) {
			pixels.get(i).setIcon(dolu);
		}
		pixels.get(0).setIcon(top);
		pixels.get(40).setIcon(hedef);
	}

	private void clearMap() {
		for (Pixels pixel : pixels) {
			if (!pixel.getIcon().equals(bos)) {
				pixel.setIcon(bos);
			}
		}
	}

	public void clearPassed() {
		sonuc.setText("");
		for (Pixels pixel : pixels) {
			if (pixel.getIcon().equals(passed)) {
				pixel.setIcon(bos);
			}
		}
	}

	public void drawPath(Integer[] path) {
		if (path != null)
			for (int i : path) {
				pixels.get(i).setIcon(passed);
			}
	}

	public void showResult(boolean dolu, int min, int adim) {
		if (dolu)
			sonuc.setText("Haritada toplam " + adim + " adım atıldı\n\nEn kısa yol " + min + " adım uzaklıkta");
		else
			sonuc.setText("Sonuç bulunamadı");
		sonuc.setBounds(30, 160, sonuc.getPreferredSize().width, sonuc.getPreferredSize().height);
	}

}

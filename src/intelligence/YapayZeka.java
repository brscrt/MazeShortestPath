package intelligence;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import view.Panel;

public class YapayZeka {

	Timer timer;
	TimerTask timerTask;
	Panel panel;

	Stack<Integer> currentPath;
	ArrayList<Integer[]> dogruYollar;
	ArrayList<Integer> tempPath;
	ArrayList<Integer[]> allTempPath;
	Rotation gelinenYon, gidilenYon;
	int startPoint, endPoint, currentPoint, nextPoint, adim;
	int ilkKesisme, ikinciKesisme;
	ArrayList<int[]> kesismeler;
	boolean back, backHedef, finish, kesisme;

	public YapayZeka(Panel panel) {
		this.panel = panel;
	}

	public void start() {
		currentPath = new Stack<>();
		dogruYollar = new ArrayList<>();
		tempPath = new ArrayList<>();
		kesismeler = new ArrayList<>();
		allTempPath = new ArrayList<>();
		int[] points = panel.getStartAndEnd();
		startPoint = points[0];
		endPoint = points[1];
		currentPoint = startPoint;
		gelinenYon = Rotation.hedef;
		gidilenYon = Rotation.hedef;
		back = false;
		backHedef = false;
		finish = false;
		kesisme = true;
		adim = 0;
		ilkKesisme = ikinciKesisme = -1;
		timer = new Timer();
		setTask();
		timer.scheduleAtFixedRate(timerTask, 100, 200);
	}

	private void setTask() {
		timerTask = new TimerTask() {
			@Override
			public void run() {
				yonBelirle();
				topHareket();
			}
		};

	}

	private void topHareket() {

		if (!finish) {
			panel.pixels.get(nextPoint).setIcon(panel.top);

			panel.pixels.get(currentPoint).setIcon(panel.passed);

			currentPoint = nextPoint;
		} else {
			timer.cancel();
		}

	}

	private void yonBul() {
		Queue<Rotation> yonler = new LinkedList<>();
		if (!back) {
			if (currentPoint - panel.labirentSize >= 0) {
				if (!panel.pixels.get(currentPoint - panel.labirentSize).getIcon().equals(panel.dolu)) {
					if (panel.pixels.get(currentPoint - panel.labirentSize).getIcon().equals(panel.hedef)) {
						backHedef = true;
					} else if (gelinenYon != Rotation.yukari) {
						yonler.add(Rotation.yukari);
					}

				}

			}
			if (currentPoint % panel.labirentSize != panel.labirentSize - 1) {
				if (!panel.pixels.get(currentPoint + 1).getIcon().equals(panel.dolu)) {
					if (panel.pixels.get(currentPoint + 1).getIcon().equals(panel.hedef)) {
						backHedef = true;
					} else if (gelinenYon != Rotation.sag) {
						yonler.add(Rotation.sag);
					}

				}

			}
			if (currentPoint + panel.labirentSize < panel.labirentSize * panel.labirentSize) {
				if (!panel.pixels.get(currentPoint + panel.labirentSize).getIcon().equals(panel.dolu)) {
					if (panel.pixels.get(currentPoint + panel.labirentSize).getIcon().equals(panel.hedef)) {

						backHedef = true;
					} else if (gelinenYon != Rotation.asagi) {
						yonler.add(Rotation.asagi);
					}

				}

			}
			if (currentPoint % panel.labirentSize != 0) {
				if (!panel.pixels.get(currentPoint - 1).getIcon().equals(panel.dolu)) {
					if (panel.pixels.get(currentPoint - 1).getIcon().equals(panel.hedef)) {
						backHedef = true;
					} else if (gelinenYon != Rotation.sol) {
						yonler.add(Rotation.sol);
					}

				}

			}

			panel.pixels.get(currentPoint).setAcikYonler(yonler);

		}

	}

	private void yonBelirle() {
		adim++;
		if (panel.pixels.get(currentPoint).getAcikYonler() == null)
			yonBul();
		else if (panel.pixels.get(currentPoint).getAcikYonler().size() != 0) {
			back = false;
		}

		if (backHedef) {
			currentPath.push(currentPoint);
			dogruYollar.add(currentPath.toArray(new Integer[0]));
			backHedef = false;
		}

		else if (panel.pixels.get(currentPoint).getAcikYonler().size() != 0) {

			Rotation yon = panel.pixels.get(currentPoint).getAcikYonler().poll();
			if (yon == Rotation.yukari) {
				nextPoint = currentPoint - panel.labirentSize;
				gelinenYon = Rotation.asagi;
			} else if (yon == Rotation.sag) {
				nextPoint = currentPoint + 1;
				gelinenYon = Rotation.sol;
			} else if (yon == Rotation.asagi) {
				nextPoint = currentPoint + panel.labirentSize;
				gelinenYon = Rotation.yukari;
			} else if (yon == Rotation.sol) {
				nextPoint = currentPoint - 1;
				gelinenYon = Rotation.sag;
			}

			int ilk = ilkKesisme();
			if (ilk != -1) {
				ilkKesisme = ilk;
				tempPath.clear();
				tempPath.add(ilk);
				kesisme = true;		

			}
			if (!tempPath.contains(currentPoint)) {
				tempPath.add(currentPoint);
			}

			currentPath.push(currentPoint);

		} else {
			if (!currentPath.isEmpty()) {
				nextPoint = currentPath.pop();
				back = true;

				if (ilkKesisme != -1) {
					int iki = ikinciKesisme();
					if (iki != ilkKesisme && kesisme) {
					

						if (!tempPath.contains(currentPoint)) {
							tempPath.add(currentPoint);
						}
						if (iki != -1) {
							ikinciKesisme = iki;
							kesisme = false;
							kesismeler.add(new int[] { ilkKesisme, ikinciKesisme });
							
							siraKontrol();
							
							allTempPath.add(tempPath.toArray(new Integer[0]));

							tempPath.clear();

						}

					}
				}

			} else {
				finish();
			}

		}

	}

	// turn back
	private int ikinciKesisme() {
		if (dogruYollar.size() != 0)
			for (int i : dogruYollar.get(0)) {
				if (i == currentPoint)
					return i;
			}
		return -1;
	}

	// go forward
	private int ilkKesisme() {
		for (int i : currentPath) {
			if (i == currentPoint)
				return i;
		}
		return -1;
	}
	
	// fix broken order
	private void siraKontrol(){
		ArrayList<Integer> temp=new ArrayList<>();
		for(int i=0;i<tempPath.size()-1;i++){
			if(Math.abs(tempPath.get(i)-tempPath.get(i+1))==1||Math.abs(tempPath.get(i)-tempPath.get(i+1))==8)
				temp.add(tempPath.get(i));
			if(i==tempPath.size()-2)
				temp.add(tempPath.get(i+1));
		}
		tempPath=temp;
	}

	private void finish() {
		finish = true;
		panel.clearPassed();
		panel.drawPath(getShortestPath());
		panel.pixels.get(startPoint).setIcon(panel.top);
	}

	private Integer[] getShortestPath() {

		findShortest();
		int min = 100, minQ = 0;
		for (int i = 0; i < dogruYollar.size(); i++) {
			if (min > dogruYollar.get(i).length) {
				minQ = i;
				min = dogruYollar.get(i).length;
			}
		}
		boolean dolu = true;
		if (dogruYollar.size() == 0) {
			dolu = false;
		}

		if (dolu) {
			panel.showResult(dolu, dogruYollar.get(minQ).length, adim);
			return dogruYollar.get(minQ);
		} else {
			panel.showResult(dolu, 0, 0);
			return null;
		}

	}

	private void findShortest() {

		int l = 0;
		for (int[] kesisme : kesismeler) {
			for (Integer[] i : dogruYollar) {
				boolean arttir = false, start = false, finish = false;
				int bas = 0, son = 0, mesafe = 0;
				int k = 0;
				for (int j = 0; j < i.length; j++) {
					if (i[j] == kesisme[0]) {
						start = true;
						arttir = true;
						bas = j;
					}
					if (i[j] == kesisme[1]) {
						arttir = false;
						finish = true;
						son = j;
					}
					if (arttir)
						mesafe++;
				}
				if (start && finish) {

					if (mesafe > allTempPath.get(l).length - 1) {
						ArrayList<Integer> temp = new ArrayList<>();
						for (int j = 0; j < bas; j++) {
							temp.add(i[j]);
						}
						for (int j = 0; j < allTempPath.get(l).length; j++) {
							temp.add(allTempPath.get(l)[j]);
						}
						for (int j = son + 1; j < i.length; j++) {
							temp.add(i[j]);
						}
						dogruYollar.set(k, temp.toArray(new Integer[0]));
					}

				}
				k++;
			}
			l++;
		}

	}

}

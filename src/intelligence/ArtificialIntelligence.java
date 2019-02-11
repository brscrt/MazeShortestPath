package intelligence;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import view.Panel;

public class ArtificialIntelligence {

	Timer timer;
	TimerTask timerTask;
	Panel panel;

	Stack<Integer> currentPath;
	ArrayList<Integer[]> correctWays;
	ArrayList<Integer> tempPath;
	ArrayList<Integer[]> allTempPath;
	Rotation fromPath;
	int startPoint, endPoint, currentPoint, nextPoint, step;
	int firstIntersection, secondIntersection;
	ArrayList<int[]> intersections;
	boolean back, backDestination, finish, intersection;

	public ArtificialIntelligence(Panel panel) {
		this.panel = panel;
	}

	public void start() {
		currentPath = new Stack<>();
		correctWays = new ArrayList<>();
		tempPath = new ArrayList<>();
		intersections = new ArrayList<>();
		allTempPath = new ArrayList<>();
		int[] points = panel.getStartAndEnd();
		startPoint = points[0];
		endPoint = points[1];
		currentPoint = startPoint;
		fromPath = Rotation.destination;
		back = false;
		backDestination = false;
		finish = false;
		intersection = true;
		step = 0;
		firstIntersection = secondIntersection = -1;
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
			panel.pixels.get(nextPoint).setIcon(panel.ball);

			panel.pixels.get(currentPoint).setIcon(panel.passed);

			currentPoint = nextPoint;
		} else {
			timer.cancel();
		}

	}

	private void yonBul() {
		Queue<Rotation> yonler = new LinkedList<>();
		if (!back) {
			if (currentPoint - panel.mazeSize >= 0) {
				if (!panel.pixels.get(currentPoint - panel.mazeSize).getIcon().equals(panel.full)) {
					if (panel.pixels.get(currentPoint - panel.mazeSize).getIcon().equals(panel.destination)) {
						backDestination = true;
					} else if (fromPath != Rotation.up) {
						yonler.add(Rotation.up);
					}

				}

			}
			if (currentPoint % panel.mazeSize != panel.mazeSize - 1) {
				if (!panel.pixels.get(currentPoint + 1).getIcon().equals(panel.full)) {
					if (panel.pixels.get(currentPoint + 1).getIcon().equals(panel.destination)) {
						backDestination = true;
					} else if (fromPath != Rotation.right) {
						yonler.add(Rotation.right);
					}

				}

			}
			if (currentPoint + panel.mazeSize < panel.mazeSize * panel.mazeSize) {
				if (!panel.pixels.get(currentPoint + panel.mazeSize).getIcon().equals(panel.full)) {
					if (panel.pixels.get(currentPoint + panel.mazeSize).getIcon().equals(panel.destination)) {

						backDestination = true;
					} else if (fromPath != Rotation.down) {
						yonler.add(Rotation.down);
					}

				}

			}
			if (currentPoint % panel.mazeSize != 0) {
				if (!panel.pixels.get(currentPoint - 1).getIcon().equals(panel.full)) {
					if (panel.pixels.get(currentPoint - 1).getIcon().equals(panel.destination)) {
						backDestination = true;
					} else if (fromPath != Rotation.left) {
						yonler.add(Rotation.left);
					}

				}

			}

			panel.pixels.get(currentPoint).setAcikYonler(yonler);

		}

	}

	private void yonBelirle() {
		step++;
		if (panel.pixels.get(currentPoint).getAcikYonler() == null)
			yonBul();
		else if (panel.pixels.get(currentPoint).getAcikYonler().size() != 0) {
			back = false;
		}

		if (backDestination) {
			currentPath.push(currentPoint);
			correctWays.add(currentPath.toArray(new Integer[0]));
			backDestination = false;
		}

		else if (panel.pixels.get(currentPoint).getAcikYonler().size() != 0) {

			Rotation yon = panel.pixels.get(currentPoint).getAcikYonler().poll();
			if (yon == Rotation.up) {
				nextPoint = currentPoint - panel.mazeSize;
				fromPath = Rotation.down;
			} else if (yon == Rotation.right) {
				nextPoint = currentPoint + 1;
				fromPath = Rotation.left;
			} else if (yon == Rotation.down) {
				nextPoint = currentPoint + panel.mazeSize;
				fromPath = Rotation.up;
			} else if (yon == Rotation.left) {
				nextPoint = currentPoint - 1;
				fromPath = Rotation.right;
			}

			int ilk = firstIntersection();
			if (ilk != -1) {
				firstIntersection = ilk;
				tempPath.clear();
				tempPath.add(ilk);
				intersection = true;		

			}
			if (!tempPath.contains(currentPoint)) {
				tempPath.add(currentPoint);
			}

			currentPath.push(currentPoint);

		} else {
			if (!currentPath.isEmpty()) {
				nextPoint = currentPath.pop();
				back = true;

				if (firstIntersection != -1) {
					int iki = secondIntersection();
					if (iki != firstIntersection && intersection) {
					

						if (!tempPath.contains(currentPoint)) {
							tempPath.add(currentPoint);
						}
						if (iki != -1) {
							secondIntersection = iki;
							intersection = false;
							intersections.add(new int[] { firstIntersection, secondIntersection });
							
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
	private int secondIntersection() {
		if (correctWays.size() != 0)
			for (int i : correctWays.get(0)) {
				if (i == currentPoint)
					return i;
			}
		return -1;
	}

	// go forward
	private int firstIntersection() {
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
		panel.pixels.get(startPoint).setIcon(panel.ball);
	}

	private Integer[] getShortestPath() {

		findShortest();
		int min = 100, minQ = 0;
		for (int i = 0; i < correctWays.size(); i++) {
			if (min > correctWays.get(i).length) {
				minQ = i;
				min = correctWays.get(i).length;
			}
		}
		boolean dolu = true;
		if (correctWays.size() == 0) {
			dolu = false;
		}

		if (dolu) {
			panel.showResult(dolu, correctWays.get(minQ).length, step);
			return correctWays.get(minQ);
		} else {
			panel.showResult(dolu, 0, 0);
			return null;
		}

	}

	private void findShortest() {

		int l = 0;
		for (int[] intersection : intersections) {
			for (Integer[] i : correctWays) {
				boolean arttir = false, start = false, finish = false;
				int bas = 0, son = 0, mesafe = 0;
				int k = 0;
				for (int j = 0; j < i.length; j++) {
					if (i[j] == intersection[0]) {
						start = true;
						arttir = true;
						bas = j;
					}
					if (i[j] == intersection[1]) {
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
						correctWays.set(k, temp.toArray(new Integer[0]));
					}

				}
				k++;
			}
			l++;
		}

	}

}

package entity.ai;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.PriorityQueue;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AStar {
	
	private byte[][] map;
	private Node[][] nodes;
	private Node start;
	private Node end;
	
	private PriorityQueue<Node> open;
	
	private PriorityQueue<Node> closed;
	
	private PriorityQueue<Node> path;
	
	// modes
	private boolean debugMode = false;
	private boolean showCost = false;
	private boolean corners = false;
	private boolean output = false;
	private boolean render = true;
	private int delay = 0;
	
	// timer
	private long startTimer;
	private boolean finished = false;
	private double time = 0;
	
	private int counter;
	private int maxTries = 200000;
	
	private String[] mapLines;
	
	// rendering to window to watch progress
	private JFrame window;
	private JPanel panel;
	private int scale = 2;
	private Font font = new Font("Consolas", Font.PLAIN, 10);
	
	// debug mode
	private Scanner scanner;
	
	public AStar(byte[][] map) {
		this.map = map;
		init();
		if (render) initWindow();
	}
	
	/*
	 * Initialize the algorithm by creating all necessary nodes using the data
	 * stored in the map.
	 */
	private void init() {
		if (output) System.out.println("[AStar] Initializing AStar...");
		
		nodes = new Node[map.length][map[0].length];
		mapLines = new String[map.length];
		
		for (int r = 0; r < nodes.length; r++) {
			mapLines[r] = "";
			for (int c = 0; c < nodes[r].length; c++) {
				
				Node n = new Node(c,r);
				
				if (map[r][c] == 1) n.blocked = true;
				else if (map[r][c] == 2) {
					n.isStart = true;
					start = n;
				}
				else if (map[r][c] == 3) {
					n.isEnd = true;
					end = n;
				}
				
				nodes[r][c] = n;
				mapLines[r] += map[r][c];
				
			}
		}
		scanner = new Scanner(System.in);
		
		scale = 700 / map.length;
		if (scale <= 1) scale = 2;
		//if (map.length < 40) scale = 40;
		//else if (map.length < 80) scale = 20;
		//else if (map.length < 200) scale = 8;
		//else if (map.length < 400) scale = 4;
		//else if (map.length < 800) scale = 2;
		//else scale = 1;
		
		if (output) System.out.println("[AStar] Initialization complete. Ready to pathfind!");
	}
	
	/*
	 * Open a window for rendering a simulation of the algorithm
	 * as it progresses, for testing purposes.
	 */
	@SuppressWarnings("serial")
	private void initWindow() {
		window = new JFrame("A* Test");
		panel = new JPanel() {
			public void paintComponent(Graphics g) {
				for (int r = 0; r < mapLines.length; r++) {
					for (int c = 0; c < mapLines[r].length(); c++) {
						// set color for tile
						switch (mapLines[r].charAt(c)) {
						case '1':
							g.setColor(Color.black);
							break;
						case '2':
							g.setColor(Color.yellow);
							break;
						case '3':
							g.setColor(Color.magenta);
							break;
						case '+':
							g.setColor(Color.red);
							break;
						case '-':
							g.setColor(Color.green.brighter());
							break;
						case '=':
							g.setColor(Color.cyan);
							break;
						default:
							g.setColor(Color.white);
						}
						// draw tile base
						g.fillRect(c * scale, r * scale, scale, scale);
						// set color for test (if applicable)
						g.setColor(Color.black);
						g.setFont(font);
						switch (mapLines[r].charAt(c)) {
						case '+': case '-': case '2':
							if (showCost) {
								g.drawString("" + nodes[r][c].g, c * scale + 2, r * scale + 10); // top left
								g.drawString("" + nodes[r][c].h, c * scale + 20, r * scale + 10); // top right
								g.drawString("" + nodes[r][c].f, c * scale + 18, r * scale + 24); // center
							}
							break;
						}
					}
				}
				// draw grid
				g.setColor(Color.black);
				for (int i = 0; i < map.length; i++) {
					g.drawLine(0, i * scale, panel.getWidth(), i * scale);
				}
				for (int i = 0; i < map[0].length; i++) {
					g.drawLine(i * scale, 0, i * scale, panel.getHeight());
				}
				// draw try count
				g.setColor(Color.green.brighter());
				g.drawString("Cycles: " + counter, 5, 8);
				// draw timer
				if (finished) {
					String timeStr = String.format("Time: %.3fs", time);
					g.drawString(timeStr, 5, 18);
				}
				else {
					long elapsed = (System.nanoTime() - startTimer) / 1000000;
					double seconds = elapsed / 1000.0;
					String timeStr = String.format("Time: %.3fs", seconds);
					g.drawString(timeStr, 5, 18);
				}
			}
		};
		panel.setPreferredSize(new Dimension(map[0].length * scale, map.length * scale));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane(panel);
		window.setFocusable(true);
		window.pack();
		window.setVisible(true);
	}
	
	/*
	 * Run an A* algorithm to find the best possible path to the end, if one
	 * exists, within [maxTries] attempts (to save time).
	 */
	public PriorityQueue<Node> pathfind() {
		if (output) System.out.println("[AStar] Searching for path...");
		
		open = new PriorityQueue<Node>(); // OPEN
		closed = new PriorityQueue<Node>(); // CLOSED
		// INIT START NODE
		start.isStart = true;
		start.g = 0;
		start.calcH(start, end);
		open.add(start); // add start to OPEN
		
		if (debugMode) scanner.nextLine();
		else try { Thread.sleep(1000); } catch (Exception e) { }
		startTimer = System.nanoTime();
		
		counter = 0;
		
		do {
			
			if (output) System.out.println("[AStar] Sorting [open].");
			Node current = open.peek(); // current = open with lowest f cost
			boolean add = closed.add(current); // add current to closed
			boolean remove = open.remove(current); // remove current from open
			//Collections.sort(closed);
			if (!add) {
				if (output) System.out.println("[AStar] Failed to add [current] to [closed]...");
				System.exit(-1);
			}
			if (!remove) {
				if (output) System.out.println("[AStar] Failed to remove [current] from [open]...");
				System.exit(-1);
			}
			
			if (current.isEnd) {
				System.out.println("[AStar] End reached!");
				long elapsed = (System.nanoTime() - startTimer) / 1000000;
				double seconds = elapsed / 1000.0;
				System.out.printf("[AStar] Took " + counter + " cycles, in %.3f seconds.\n", seconds);
				time = seconds;
				finished = true;
				if (render) panel.repaint();
				return getPath(current);
			}
			
			if (output) System.out.println("[AStar] Exploring potential next node.");
			current.explore(nodes, corners);
			
			for (int i = 0; i < current.neighbors.size(); i++) {
				
				if (output) System.out.println("[AStar] Examinging neighbor " + i + ".");
				Node neighbor = current.neighbors.get(i);
				
				// if it's blocked, or already been explored, skip it
				if (neighbor.blocked || closed.contains(neighbor)) {
					if (output) System.out.println("[AStar] Cannot traverse this node. Skipping.");
					continue;
				}
				
				// get the path distance to it
				if (!open.contains(neighbor)) {
					
					if (output) System.out.println("[AStar] Unexplored node. Calculating cost.");
					neighbor.parent = current;
					neighbor.calcF(start, end);
					open.add(neighbor);
					
				}
				else {
					
					if (output) System.out.println("[AStar] Node previously explored. Evaluating current path for potential improvement.");
					// get its f right now
					int f = neighbor.f;
					
					// calculate if going to that node would be faster this way
					Node parent = neighbor.parent;
					neighbor.parent = current;
					neighbor.calcG(start, end);
					neighbor.calcH(start, end);
					neighbor.calcF(start, end);
					
					// if it is, set its parent to this path
					if (neighbor.f < f) {
						if (output) System.out.println("[AStar] Better path found. Setting parent of node to [current].");
						neighbor.parent = current;
					}
					
					// otherwise, go back
					else {
						if (output) System.out.println("[AStar] No improvement found. Reverting to original for neighbor node.");
						neighbor.parent = parent;
						neighbor.f = f;
					}
					
				}
				
			}
			
			updateMap();
			if (output) printMap();
			try { Thread.sleep(delay); } catch (Exception e) { }
			if (render) panel.repaint();
			counter++;
			if (debugMode) {
				scanner.nextLine();
			}
		} while(counter < maxTries);
		
		if (output) System.out.println("[AStar] No path found.");
		return null;
	}
	
	/*
	 * Retrace the path back to the start to get the bes possible path
	 * returned by the A* algorithm.
	 */
	private PriorityQueue<Node> getPath(Node node) {
		path = new PriorityQueue<Node>();
		Node current = node;
		while (!current.isStart) {
			path.add(current);
			Node parent = current.parent;
			current = parent;
		}
		labelPath();
		return path;
	}
	
	private void updateMap() {
		for (Node n : open) {
			if (n.isStart || n.isEnd) continue;
			StringBuilder builder = new StringBuilder(mapLines[n.y]);
			builder.setCharAt(n.x, '-');
			mapLines[n.y] = builder.toString();
		}
		for (Node n : closed) {
			if (n.isStart || n.isEnd) continue;
			StringBuilder builder = new StringBuilder(mapLines[n.y]);
			builder.setCharAt(n.x, '+');
			mapLines[n.y] = builder.toString();
		}
	}
	
	private void labelPath() {
		for (Node n : path) {
			if (n.isStart || n.isEnd) continue;
			StringBuilder builder = new StringBuilder(mapLines[n.y]);
			builder.setCharAt(n.x, '=');
			mapLines[n.y] = builder.toString();
		}
	}
	
	private void printMap() {
		if (!output) return;
		int w = map[0].length;
		int h = map.length;
		
		String top = "+";
		for (int i = 0; i < w; i++) top += "--";
		top += "-+";
		
		System.out.println(top);
		
		for (int i = 0; i < h; i++) {
			System.out.print("| ");
			for (int j = 0; j < mapLines[i].length(); j++) {
				System.out.print(mapLines[i].charAt(j) + " ");
			}
			System.out.println(" |");
		}
		
		System.out.println(top);
	}
	
	/*
	public void testSort() {
		List<Node> testNodes = new ArrayList<Node>();
		Node node1 = new Node(1,3);
		Node node2 = new Node(4,5);
		Node node3 = new Node(3,2);
		node1.f = 40;
		node2.f = 10;
		node3.f = 25;
		testNodes.add(node1);
		testNodes.add(node2);
		testNodes.add(node3);
		for (Node n : testNodes) {
			System.out.print("Node[f=" + n.f + "]");
		}
		System.out.println();
		System.out.println("Sorting...");
		Collections.sort(testNodes);
		for (Node n : testNodes) {
			System.out.print("Node[f=" + n.f + "]");
		}
		System.out.println();
	}
	*/
	
	public void repaint() {
		panel.repaint();
	}
	
}
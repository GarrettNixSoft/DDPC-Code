package entity.ai;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
	
	public int x, y, z;
	public int h, g, f;
	
	public List<Node> neighbors;
	public Node parent;
	
	public boolean blocked;
	public boolean isStart, isEnd;
	
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		neighbors = new ArrayList<Node>();
	}
	
	public void calcG(Node start, Node end) {
		g = 0;
		// get g cost
		if (parent == this) {
			g = 0; return;
		}
		g = parent.g;
		// if it's adjacent, g adds 10; else, it's a corner, add 14
		if (Math.abs(parent.x - x) == 1 && Math.abs(parent.y - y) == 1) g += 14;
		else g += 10;
		//System.out.println("[Node] g cost calculated to: " + g);
	}
	
	public void calcH(Node start, Node end) {
		h = 0;
		//System.out.println("[Node] Calc cost for Node at ("+x+","+y+")");
		// get h cost (in this case, h is distance from end)
		if (parent == null) parent = this;
		int xDiff = x - end.x;
		int yDiff = y - end.y;
		xDiff = Math.abs(xDiff);
		yDiff = Math.abs(yDiff);
		//System.out.println("[Node] xDiff="+xDiff+",yDiff="+yDiff);
		if (xDiff != 0 && yDiff != 0) {
			// add 14 (move diagonally) until it lines up on one axis
			while (!(xDiff == 0 || yDiff == 0)) {
				xDiff--;
				yDiff--;
				h += 14;
				//System.out.println("[Node] xDiff="+xDiff+",yDiff="+yDiff+",h="+h);
			}
			// then add the value for a straight line to the target
			if (xDiff != 0 && yDiff == 0) {
				h += xDiff * 10;
				//System.out.println("[Node] h=" + h);
			}
			else if (yDiff != 0 && xDiff == 0) {
				h += yDiff * 10;
				//System.out.println("[Node] h=" + h);
			}
			// otherwise, it was a direct diagonal, add no line
		}
		else if (xDiff != 0) {
			h = xDiff * 10;
			//System.out.println("[Node] h=" + h);
		}
		else if (yDiff != 0) {
			h = yDiff * 10;
			//System.out.println("[Node] h=" + h);
		}
		//System.out.println("[Node] h cost calculated to " + h);
	}
	
	public void calcF(Node start, Node end) {
		calcG(start, end);
		calcH(start, end);
		f = h + g;
		//System.out.println("[Node] f cost calculated to: " + f);
	}
	
	public void explore(Node[][] nodes, boolean corners) {
		//System.out.println("[Node] Exploring!");
		neighbors = new ArrayList<Node>();
		if (corners) {
			for (int r = y-1; r <= y+1; r++) {
				for (int c = x-1; c <= x+1; c++) {
					// don't count self as neighbor
					if (r == y && c == x) continue;
					// try adding it (if this fails, it means the current node is on an edge of the map, so don't check out of bounds)
					try {
						neighbors.add(nodes[r][c]);
					} catch (ArrayIndexOutOfBoundsException e) {
						continue;
					}
				}
			}
		}
		else {
			for (int r = y-1; r <= y+1; r++) {
				for (int c = x-1; c <= x+1; c++) {
					// don't count self as neighbor
					if (r == y && c == x) continue;
					// don't count corners
					if (r==y-1&&c==x-1||r==y+1&&c==x-1||r==y-1&&c==x+1||r==y+1&&c==x+1) continue;
					// try adding it (if this fails, it means the current node is on an edge of the map, so don't check out of bounds)
					try {
						neighbors.add(nodes[r][c]);
					} catch (ArrayIndexOutOfBoundsException e) {
						continue;
					}
				}
			}
		}
		//System.out.println("[Node] Node explored. Found " + neighbors.size() + " neighbors!");
	}
	
	public int compareTo(Node n) {
		if (f == n.f) return h - n.h;
		return f - n.f;
	}
	
}
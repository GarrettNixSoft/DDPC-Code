package tile;

import static main.Render.drawImage;

import main.Render;

public class VerticalTileMap extends TileMap {
	
	public VerticalTileMap(String level) {
		super(level);
	}
	
	public VerticalTileMap() {
		super();
	}
	
	public int getMaxY() { return (int) -this.y + Render.HEIGHT + 41; }
	
	@Override
	public void setPositionAbsolute(float x, float y) {
		this.x = x;
		this.y = y;
		fixBounds();
		if (allowBoundaryViolation && (sx != 0 || sy != 0)) {
			//System.out.println("[TileMap] Shaking!");
			if (smooth) {
				this.x += (y - this.x) * ease + sx;
				this.y += (y - this.y) * ease + sy;
			}
			else {
				x += sx;
				y += sy;
			}
		}
		colOffset = (int) -this.x / tileSize;
		rowOffset = (int) -this.y / tileSize;
		sx = sy = 0;
	}
	
	@Override
	public void setPosition(float x, float y) {
		//System.out.println("[TileMap] Setting position to " + x + "," + y);
		this.x += (x - this.x) * ease;
		if (y > this.y) this.y += (y - this.y) * ease;
		fixBounds();
		if (allowBoundaryViolation && (sx != 0 || sy != 0)) {
			//System.out.println("[TileMap] Shaking!");
			if (smooth) {
				x += sx;
				//System.out.println("[TileMap] xDelta is " + );
				y += sy;
				this.x += (x - this.x) * ease;
				if (y > this.y) this.y += (y - this.y) * ease;
			}
			else {
				x += sx;
				y += sy;
			}
		}
		//System.out.println("[TileMap] position calculated to " + this.x + "," + this.y);
		//System.out.println("[TileMap] getY() responds with " + getY());
		colOffset = (int) -this.x / tileSize;
		rowOffset = (int) -this.y / tileSize;
		//System.out.println("colOffset: " + colOffset + " rowOffset: " + rowOffset);
		sx = sy = 0;
	}
	
	// override getters?
//	@Override
//	public int getX() { return (int) x; }
	@Override
	public int getY() { return (int) this.y; }
	
	protected void fixBounds() {
		if (x < xmin) x = xmin;
		if (x > xmax) x = xmax;
		if (y < ymin) y = ymin;
		if (y > ymax) y = ymax;
	}
	
	// draw the map
	public void render() {
		for (int row = rowOffset; row < rowOffset + rowsToDraw; row++) {
			if (row >= numRows) break;
			for (int col = colOffset; col < colOffset + colsToDraw; col++) {
				if (col >= numCols) break;
				if (map[row][col] == 0 || map[row][col] == 98 || map[row][col] == 99) continue;
				else {
					int tileRow = map[row][col] / 10;
					int tileCol = map[row][col] % 10;
					drawImage(tileset[tileRow][tileCol], x + col * tileSize, y + row * tileSize);
				}
			}
		}
	}
}
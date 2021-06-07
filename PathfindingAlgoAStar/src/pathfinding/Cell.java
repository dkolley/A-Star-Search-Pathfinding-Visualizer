package pathfinding;

//we define a cell of our grid
//in this case cell = node (interchangeable)
public class Cell {

	//coordinates of a particular cell
	public int x,y;
	//parent cell for path
	public Cell parent;
	//Heuristic cost of the current cell
	//distance from current cell to the target cell
	public int heuristicCost;
	//Final cost ==> H+G
	//H ==> H(n) ==> The heuristic that estimates the cost of the cheapest path from n to the goal
	//G ==> G(n) ==> The cost of the path from start node to n (current cell)
	//Hcost = distance to end node
	//GCost = distance to start node
	public int finalCost;
	
	public boolean solution; //if cell is part of solution path
	
	
	//Init x and y coord values for each cell
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	//Overriding toString() method giving us the coordinates of each cell if need be
	@Override
	public String toString() {
		return "[" + x + "," + y + "]";
	}
	
	
}

package pathfinding;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class AStar {

	
	//Boolean that returns whether a successfull path can be made from a start to end node of current state of the grid
	private boolean isAPath = false;
	
	
	//Stores the x and y values of different cells depending on whether they are part of the solution, in closed or open list
	private ArrayList<Integer> xValuesSolution = new ArrayList<>();
	private ArrayList<Integer> yValuesSolution = new ArrayList<>();
	private ArrayList<Integer> xValuesClosed = new ArrayList<>();
	private ArrayList<Integer> yValuesClosed = new ArrayList<>();
	private ArrayList<Integer> xValuesOpen = new ArrayList<>();
	private ArrayList<Integer> yValuesOpen = new ArrayList<>();
	
	//cost of movement from one cell to another
	//we can either move vertically(up or down), horizontally (left or right)
	//or we can move diagonally
	//Using pythagoras' theorem ==> diagonal cost is √2 (approximately 1.4) (as sides = 1)
	public static final int DIAGONAL_COST = 14;
	//Each side assumed to be 1 (to get int we multiply both by factor of 10)
	public static final int V_H_COST = 10;
	//Cells of out grid
	private Cell[][] grid;
	
	//We define a priority queue for open cells
	//Initially open and closed list are both empty
	//We first put start node in open list
	
	//REPEAT BELOW UNTIL: GOAL FOUND OR OPEN LIST IS EMPTY
	//Find node with lowest Fcost in open list and place in closed list
	//Expand this node and for the adjacent nodes to this node:
	//if on closed list, ignore
	//if not on open list, add to open list, store the current node as the parent for this adjacent node 
	//and calculate the F,G,H costs of the node
	//If open list is empty fail
	
	
	//Open Cells: set of nodes to be evaluated
	//All neighbouring cells added to open list if not in closed
	//However, we put the cells with lowest code in first (i.e higher priority)
	
	private PriorityQueue<Cell> openCells;
	
	//Closed Cells: the set of nodes already evalauted
	private boolean[][] closedCells;
	//Start cell
	private int startX, startY;
	//End/Target cell
	private int endX, endY;
	
	
	
	//blocks = 2D array of the coords of cells that act as blocking cells meaning path must find away around these to reach target node
	public AStar(int width, int height, int startingCellX, int startingCellY, int targetCellX, int targetCellY, int[][] blocks) {
		grid = new Cell[width][height];
		closedCells = new boolean[width][height];
		openCells = new PriorityQueue<Cell>((Cell c1, Cell c2) -> {
			return compareCosts(c1, c2);
		});
		startCell(startingCellX, startingCellY);
		endCell(targetCellX, targetCellY);
		//init Hcost(distance to end node from current node)
		//grid.length = no.rows the 2D array has
		//for every single row do the nested for loop
		for(int x = 0; x<grid.length; x++) {
			//for every single cell on that particular row 
			//i repr. the row
			for(int y = 0; y<grid[x].length; y++) {
				grid[x][y] = new Cell(x, y);
				grid[x][y].heuristicCost = Math.abs(x - endX) + Math.abs(x - endY);
				grid[x][y].solution = false;
			}
		}
		grid[startX][startY].finalCost = 0;
		
		
		
		//now we put all of the 'blocking' blocks on the grid
		for(int i = 0; i<blocks.length; i++) {
			addBlockOnCell(blocks[i][0], blocks[i][1]);
		}
		
	}
		
	   //Each cell in the grid has null assigned to it if it is a blocker cell
		private void addBlockOnCell(int x, int y) {
			grid[x][y] = null;
		}
		
		
		//Setter method's that sets out the coords for start/end cell
		private void startCell(int x, int y) {
			startX = x;
			startY = y;
		}
		
		private void endCell(int x, int y) {
			endX = x;
			endY = y;
		}
		
		
		//Used as a comparator for the priority queue: defines priority for order of the list
		private int compareCosts(Cell c1, Cell c2) {
			int c1Cost = c1.finalCost;
			int c2Cost = c2.finalCost;
			if(c1Cost < c2Cost) return -1;
			else if(c1Cost > c2Cost) return 1;
			else return 0;
		}
		
		
		private void updateCostIfNeeded(Cell current, Cell t, int cost) {
			//updateCostIfNeeded called on every neighbouring cell to current
			//if on closed list or is a blocker cell, ignore
			if(t == null || closedCells[t.x][t.y]) {
				return;
			}
			//current = current cell
			//t cell = all neighboroughing cells to current cell
			//Check if the t cell has a lower cost (meaning it is closer to the goal than current)
			//the t cell (one of the neighboruing cell to current) has a final cost which is the hcost + gcost.
			//cost = gcost = cost of path from start to itself
			int tFinalCost = t.heuristicCost + cost;
			//is it in the list already of the set of nodes to be evaluated
			boolean isOpen = openCells.contains(t);
			//if the neighbouring cell is not on the list to be evaluated or it has a total cost greater than the current
			if(!isOpen || tFinalCost < t.finalCost) {
				//set the final cost of the neighboruging cell equal to final cost of the current
				t.finalCost = tFinalCost;
				//set current cell to be parent of the neighbouring cell
				t.parent = current;
	
				//if not on open list, i.e cells to be evaluated add it to it
				if(!isOpen) {
					openCells.add(t);
					xValuesOpen.add(t.x);
					yValuesOpen.add(t.y);
				}
			}
		}
		
		
		
		
		public void process() {
			//we first add the start cell to the open list
			openCells.add(grid[startX][startY]);
			xValuesOpen.add(startX);
			yValuesOpen.add(startY);
			Cell current;
			
			while(true) {
				//get cell with highest priority in queue, i.e cell with lowest final cost
				current = openCells.poll();
				//exit loop once no more nodes in queue
				if(current == null)
					break;
				//add current cell to closed as its now an 'evaluated' cell
				closedCells[current.x][current.y]=true;
				xValuesClosed.add(current.x);
				yValuesClosed.add(current.y);
				//if the current cell is the target node then we break out of loop
				if(current.equals(grid[endX][endY]))return;
				Cell t;
				//checking every single neighbouring cell to the current cell if possible
				//add the cost of moving from current cell to t and update if its the lowest total final cost
				if(current.x >0) {
					t = grid[current.x - 1][current.y];
					updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
					
					if(current.y - 1>= 0) {
						t = grid[current.x - 1][current.y - 1];
						updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);
					}
					
					if(current.y + 1<grid[0].length) {
						t = grid[current.x - 1][current.y + 1];
						updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);
					}
					
				}
				
				if(current.y - 1 >= 0) {
					t = grid[current.x][current.y-1];
					updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
				}
				
				if(current.y + 1<grid[0].length) {
					t = grid[current.x][current.y + 1];
					updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
				}
				
				if(current.x + 1<grid.length) {
					t = grid[current.x + 1][current.y];
					updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
					
					if(current.y - 1>=0) {
						t = grid[current.x+1][current.y - 1];
						updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);
					}
					
					if(current.y + 1<grid[0].length) {
						t = grid[current.x+1][current.y + 1];
						updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);
					}
				}
			}
		}
		
		
		public void displaySolution() {
			if(closedCells[endX][endY]) {
				//We track back the path
				Cell current = grid[endX][endY];
				grid[current.x][current.y].solution = true;
				//loop until we reach the start node
				while(current.parent != null) {
					//every node in path has a parent except start so we keep getting coords of all parents until start node (essentially the path)
					xValuesSolution.add(current.parent.x);
					yValuesSolution.add(current.parent.y);
					grid[current.parent.x][current.parent.y].solution = true;
					current = current.parent;
				}
				isAPath = true;
			} else {
				//so we can use a getter method from main screen and display a message of 'no path' to user
				isAPath = false;
			}
		}

		
		
		//getter methods for the MainScreen class that returns the fields of an AStar obj.
		public boolean isThereAPath(){
			return isAPath;
		}
		
		public ArrayList<Integer> returnXVals(){
			return xValuesSolution;
		}
		
		public ArrayList<Integer> returnYVals(){
			return yValuesSolution;
		}
		
		public ArrayList<Integer> returnXValsClosed(){
			return xValuesClosed;
		}
		
		public ArrayList<Integer> returnYValsClosed(){
			return yValuesClosed;
		}
		
		public ArrayList<Integer> returnXValsOpen(){
			return xValuesOpen;
		}
		
		public ArrayList<Integer> returnYValsOpen(){
			return yValuesOpen;
		}
	}

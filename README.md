# A-Star-Search-Pathfinding-Visualizer
A pathfinding visualizer based on the A* pathfinding algorithm written in Java.

• Left click on mouse: (over a cell on the grid) displays the start node of colour blue

• Right click on mouse: (over a cell on the grid) displays the end/target node of colour red

• Middle click on mouse: (over a cell on the grid) displays a blocker node of colour black 

Once at least the grid has exactly one start node and one end node, the ENTER button can then be pressed. 

If a path from start to target node is possible, it will be drawn onto the grid along with:

      *Cyan coloured nodes to indicate all nodes part of the solution path
  
      *Orange coloured nodes to indicate all nodes part of the closed list (not including solution path nodes)
  
      *Pink coloured nodes to indicate all nodes part of the open list (nodes that havent been evaluated yet, but is a neighbouring cell to one that was current at time of display)
  
      *Grey coloured nodes to indicate all nodes that are unvisited. (All nodes are grey at the opening of the application)

If not, an appropriate message is displayed to user informing them of no available path for current state of grid.

If the user wants to clear the grid (i.e so they can store the start/end/blocker nodes on different positions on the grid), they can click the 'R(eset)' button on the keyboard


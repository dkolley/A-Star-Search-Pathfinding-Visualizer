package pathfinding;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainScreen extends JPanel{
	
	//A single JPanel represents a node: List below stores all nodes, therefore type JPanel
	private ArrayList<JPanel> allNodes = new ArrayList<>();
	//coords for x and y of start and end node
	private int startNodeX = -1;
	private int startNodeY = -1;
	private int endNodeX = -1;
	private int endNodeY = -1;
	//Stores the coords of every single blocker cell in string format of: [x,y]
	private ArrayList<String> blockers = new ArrayList<>();
	
    public static void main(String[] args) {
        new MainScreen();
    }

    private MainScreen() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFrame mainGrid = new JFrame("A* Pathfinder by Dennis Kolley");
                mainGrid.addKeyListener(new KeyAdapter() {
                	@Override
                	public void keyPressed(KeyEvent e) {
                		//If enter button pressed, we attempt to calculate a path from start to target node
                		if(e.getKeyCode()==KeyEvent.VK_ENTER) {
                			//if exactly one start and target node
                			if(passesCheck()) {
                				calculatePath();
                			} else {
                				 JOptionPane.showMessageDialog(null, "Click ENTER once you have drawn at least the start and end node\nThere can only be one start node (blue cell) and one end node (red cell)\nLeft click = start cell\nRight click = target cell\nMiddle click = blockers\nClick 'r' to reset");
                			}
                			
                			//reset values. i.e clear grid if 'R' button pressed so user can create a new state of grid
                		} else if(e.getKeyCode()=='R') {
                			resetValues();
                		}
                		
                		//when user presses a button that is not one of the recognised ones above
                		else JOptionPane.showMessageDialog(null, "Click ENTER once you have drawn at least the start and end node\nThere can only be one start node (blue cell) and one end node (red cell)\nLeft click = start cell\nRight click = target cell\nMiddle click = blockers\nClick 'r' to reset");
                	}
                });
                mainGrid.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainGrid.getContentPane().setLayout(new BorderLayout());
                mainGrid.getContentPane().add(new TestPane());
                mainGrid.pack();
                mainGrid.setVisible(true);
            }
        });
    }

  //return true if exactly one start and target node, return false if not
    private boolean passesCheck() {
    	int counterStart = 0, counterEnd = 0;
    	for(JPanel j: allNodes) {
    		if(j.getBackground() == Color.BLUE) counterStart++;
    		if(j.getBackground() == Color.RED) counterEnd++;
       	}
    	if(counterStart == 1 && counterEnd == 1) return true;
    	return false;
    }
    

    private class TestPane extends JPanel {
        public TestPane() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            //creation of the grid: fixed as a 20x20 grid
            for (int row = 0; row < 20; row++) {
                for (int col = 0; col < 20; col++) {
                    gbc.gridx = col;
                    gbc.gridy = row;

                    CellPane cellPane = new CellPane();
                    Border border = null;
                    if (row < 10) {
                        if (col < 10) {
                            border = new MatteBorder(1, 1, 0, 0, Color.GRAY);
                        } else {
                            border = new MatteBorder(1, 1, 0, 1, Color.GRAY);
                        }
                    } else {
                        if (col < 10) {
                            border = new MatteBorder(1, 1, 1, 0, Color.GRAY);
                        } else {
                            border = new MatteBorder(1, 1, 1, 1, Color.GRAY);
                        }
                    }
                    cellPane.setBorder(border);
                    cellPane.setName("["+String.valueOf(col)+","+String.valueOf(row)+"]");
                    add(cellPane, gbc);
                    allNodes.add(cellPane);

                }
            }
        }
    }
    
    
    //reset all field variables to initial values (like at startup) so fresh state for user (i.e old visual nodes on grid removed)
    private	void resetValues() {
    	startNodeX = -1;
    	startNodeY = -1;
    	endNodeX = -1;
    	endNodeY = -1;
    	for(JPanel j: allNodes) {
    		j.setBackground(SystemColor.menu);
       	}
    	blockers = new ArrayList<>();
    }

    //A coordinate string is in the form: [x,y]
    //In this case it returns the integer x
    
  //Return the first value from a coordinate string
    private int getFirstValueFromArray(String coords) {
    	 coords = coords.substring(1);
    	 int indexOfComma = coords.indexOf(',');
    	 coords = coords.substring(0,indexOfComma);
    	 int firstValue = Integer.parseInt(coords);
    	 return firstValue;
    } 
    
  //Return the second value from a coordinate string
    private int getSecondValueFromArray(String coords) {
    	 int indexOfComma = coords.indexOf(',');
    	 coords = coords.substring(indexOfComma+1);
    	 coords = coords.replaceAll("]", "");
    	 int lastValue = Integer.parseInt(coords);
    	 return lastValue;
    }

    private class CellPane extends JPanel {
        private Color defaultBackground;
        private CellPane() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	//if left click, create the start node
                	//Represented by colour red
                	  if(e.getButton() == MouseEvent.BUTTON1) {
                          defaultBackground = getBackground();
                      	if(!(defaultBackground == Color.BLUE)) {
                      		String coords = CellPane.this.getName();
                      		if((!(Integer.parseInt(Character.toString(coords.charAt(1))) == endNodeX)) || !(Integer.parseInt(Character.toString(coords.charAt(3))) == endNodeY)){
                      			setBackground(Color.BLUE);
                          		startNodeX = getFirstValueFromArray(coords);
                          		startNodeY = getSecondValueFromArray(coords);
                      		} else {
                      			JOptionPane.showMessageDialog(null, "You can't place a start cell where a end cell is!\nRemove the end cell if you want to add the starting position here.");
                      		}
                      	} else {
                      		setBackground(SystemColor.menu);
                      	}
                   }
                	  
                	  
                	  //If middle click, create a blocker
                	  //Represented by colour black
                        if(e.getButton() == MouseEvent.BUTTON2) {
	                        defaultBackground = getBackground();
	                      	if(!(defaultBackground == Color.BLACK)) {
	                      		setBackground(Color.BLACK);
	                      	} else {
	                      		setBackground(SystemColor.menu);
	                      	}
                        }
                        
                        //If right click, create the target/end node
                        //Represented by colour red
                        if(e.getButton() == MouseEvent.BUTTON3) {
	                        defaultBackground = getBackground();
	                      	if(!(defaultBackground == Color.RED)) {
	                      		String coords = CellPane.this.getName();
	                      		endNodeX = getFirstValueFromArray(coords);
	                      		endNodeY = getSecondValueFromArray(coords);
	                      		setBackground(Color.RED);
	                      	} else {
	                      		setBackground(SystemColor.menu);
	                      	}
                        }
                }
            });
        }

        //each cell is 40 by 40 pixels
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(40, 40);
        }
    }
    
    //Here we calculate a possible path of the current state of the grid
    private void calculatePath() {
    	//get blocker cells coords so can be passed in later to aStar init
    	getBlockers();
    	ArrayList<Integer> blockersIntCoords = fillNewArrayList(blockers);
    	int[][] blockingBlocks = toArray(blockersIntCoords, 2);
    	//create aStar obj, pass in values to constructor to init the obj. so that it a path can be processed
    	AStar aStar = new AStar(20,20,startNodeX, startNodeY, endNodeX, endNodeY,blockingBlocks);
    	aStar.process();
    	aStar.displaySolution();
    	

    	if(aStar.isThereAPath()) {
    		//integer coords of x and y values of cells that are in the category of in the open list, closed list or a solution
    		ArrayList<Integer> xVals = aStar.returnXVals();
        	ArrayList<Integer> yVals = aStar.returnYVals();
        	ArrayList<Integer> xValsC = aStar.returnXValsClosed();
        	ArrayList<Integer> yValsC = aStar.returnYValsClosed();
        	ArrayList<Integer> xValsO = aStar.returnXValsOpen();
        	ArrayList<Integer> yValsO = aStar.returnYValsOpen();
        	

        	//paint a cell pink if its a node that is in the open list
        	int counterO = 0;
        	while(counterO<xValsO.size()) {
        		int currentX = xValsO.get(counterO);
        		int currentY = yValsO.get(counterO);
        		for(JPanel j: allNodes) {
        			String actualXandY = "["+currentX+","+currentY+"]";
        			String cellXandY = j.getName();
             		 if(actualXandY.equals(cellXandY)) {
             			 j.setBackground(Color.PINK);
             		 }
             	 }
        		counterO++;
        	}

        	//paint a cell orange if its a node that is in the closed list
        	int counterC = 0;
        	while(counterC<xValsC.size()) {
        		int currentX = xValsC.get(counterC);
        		int currentY = yValsC.get(counterC);
        		for(JPanel j: allNodes) {
        			String actualXandY = "["+currentX+","+currentY+"]";
        			String cellXandY = j.getName();
             		 if(actualXandY.equals(cellXandY)) {
             			 j.setBackground(Color.orange);
             		 }
             	 }
        		counterC++;
        	}

        	//paint a cell cyan if its a node that part of the solution list
        	//cells can be part of solution and in the closed list
        	//therefore, this block of code comes last so its colour can be overwritten if in multiple lists
        	int counter = 0;
        	while(counter<xVals.size()) {
        		int currentX = xVals.get(counter);
        		int currentY = yVals.get(counter);
        		for(JPanel j: allNodes) {
        			String actualXandY = "["+currentX+","+currentY+"]";
        			String cellXandY = j.getName();
             		 if(actualXandY.equals(cellXandY)) {
             			 j.setBackground(Color.cyan);
             		 }
             		if(cellXandY.equals("["+startNodeX+","+startNodeY+"]")) j.setBackground(Color.BLUE);
             		if(cellXandY.equals("["+endNodeX+","+endNodeY+"]")) j.setBackground(Color.RED);
             	 }
        		counter++;
        	}
    	} else {
    		 JOptionPane.showMessageDialog(null, "No path available for the current state of the grid!");
    	}
    }
    
    //Used for blockers coords
    //Used to convert the string array list of: {[x1,y1], [x2,y2]} to an integer array list of: {x1,y1,x2,y2}
	private ArrayList<Integer> fillNewArrayList(ArrayList<String> list) {
		ArrayList<Integer> ar = new ArrayList<>();
		for(int i = 0; i<list.size(); i++) {
			String coords = "["+getFirstValueFromArray(list.get(i))+","+getSecondValueFromArray(list.get(i))+"]";
			ar.add(getFirstValueFromArray(coords));
			ar.add(getSecondValueFromArray(coords));
		}
		return ar;
	}

	//Turn the array list of type integer to a 2D array list of 2 rows each
	//As each row contains 2 integers: x val for a coord, y val for a coord
	private int[][] toArray(List<Integer> list, int rows) {
	    int[][] result = new int[list.size()/2][rows];
	    int counter = 0;
	    for (Integer value : list) {
	        result[counter/rows][counter%rows] = value;
	        counter++;
	    }
	    return result;
	}
    
    //ArrayList of type string stores the coords of blocker cells in type format: [x,y]
    private void getBlockers() {
    	int xCoordBlocker = 0;
    	int yCoordBlocker = 0; 
    	 for(JPanel j: allNodes) {
       		 if(j.getBackground() == Color.BLACK) {
       			String coords = j.getName();
       	  		xCoordBlocker =  getFirstValueFromArray(coords);;
       	  		yCoordBlocker =  getSecondValueFromArray(coords);;
       	  		String xAndy = "["+xCoordBlocker + ","+ yCoordBlocker+"]";
       	  	    blockers.add(xAndy);
       		 }
       	 }
    }
}
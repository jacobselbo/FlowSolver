# Flow Solver

This solver tries to create a solution to the mobile game "Flow". An example of what a flow board looks like is below.

![Example of the flow board](https://i.stack.imgur.com/ybjg3.png)

## Strategy

This strategy creates every possible path for each color, then tries to apply each path to a board until a board that allows all paths is found. 
That board is the solved board. The path creation and path fitting uses a breadth-first tree system.

## Limitations

This solver fails to solve boards greater than 7/8 due to the exponential time required per increase of board size.
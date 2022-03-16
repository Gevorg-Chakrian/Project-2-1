package Utilities;

import Chessboard.Square;

public class DistanceCalc {

    private final Square origin;
    private final Square destination;
    private final boolean allowDiagonals;

    private final double output;

    public DistanceCalc(Square origin, Square destination, boolean allowDiagonals)
    {
        this.origin=origin;
        this.destination=destination;
        this.allowDiagonals=allowDiagonals;

        if(this.allowDiagonals)
        {
            output=quickDistance();
        }
        else
        {
            output=distance();
        }
    }

    /**
     * With diagonals
     * @return
     */
    private double quickDistance()
    {
        Coord2D originPoint = new Coord2D(origin.getRow(),origin.getColumn());
        Coord2D endPoint = new Coord2D(destination.getRow(),destination.getColumn());
        if(GlobalVariables.PRINTLOG) System.out.println("\t\t\t################Delta X: " + (endPoint.getX()-originPoint.getX()));
        if(GlobalVariables.PRINTLOG) System.out.println("\t\t\t################Delta Y: " + (endPoint.getY()-originPoint.getY()));
        return Math.sqrt(Math.abs((endPoint.getX()-originPoint.getX()))+Math.abs(endPoint.getY()-originPoint.getY()));
    }

    /**
     * WithOUT diagonals
     * @return
     */
    private double distance()
    {
        Coord2D originPoint = new Coord2D(origin.getRow(),origin.getColumn());
        Coord2D endPoint = new Coord2D(destination.getRow(),destination.getColumn());
        if(GlobalVariables.PRINTLOG) System.out.println("\t\t\t################Delta X: " + (endPoint.getX()-originPoint.getX()));
        if(GlobalVariables.PRINTLOG) System.out.println("\t\t\t################Delta Y: " + (endPoint.getY()-originPoint.getY()));
        return (Math.abs((endPoint.getX()-originPoint.getX()))+Math.abs(endPoint.getY()-originPoint.getY()));
    }

    public double getOutput() {
        if(GlobalVariables.PRINTLOG) System.out.println("\t\t\t################Distance calculate between " + origin.getRow() + "x" + origin.getColumn() + " and " + destination.getRow() +"x" + destination.getColumn() + " is " + output);
        return output;
    }
}

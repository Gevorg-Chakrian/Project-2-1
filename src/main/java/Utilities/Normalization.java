package Utilities;

public class Normalization {
    public static double normalizeScore(double coefficient, double score){
        return ((1 / (1+Math.exp(-coefficient*score)))-0.5)*2;
    }
}

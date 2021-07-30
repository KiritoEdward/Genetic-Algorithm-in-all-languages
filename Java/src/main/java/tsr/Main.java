package tsr;

public class Main {
    
    public static void main(String[] args)
    {
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(200,25,0.12,10,Selection.HYBRIDSELECTION,Mutation.SWAPMUTATION,CrossOver.OX1CROSSOVER);
    }
       
}

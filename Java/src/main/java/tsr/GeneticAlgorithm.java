package tsr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GeneticAlgorithm {
    private final int populationSize;
    private final int elitismSize;
    private final double mutationRate;
    private final int touranmentPoolSize;
    private int SUSNumberOfPointers;
    private final Selection selectionMethod;
    private final Mutation mutationMethod;
    private final CrossOver crossOverMethod;

    public GeneticAlgorithm(int popsize , int elitismS,double mutationR,int touranmentS,Selection selectionM,Mutation mutationM,CrossOver crossOverM) {
        this.populationSize = popsize;
        this.elitismSize = elitismS;
        this.mutationRate = mutationR;
        this.touranmentPoolSize = touranmentS;
        this.selectionMethod = selectionM;
        this.crossOverMethod = crossOverM;
        this.mutationMethod = mutationM;
        
        
        Population population = new Population(this.populationSize, DataSet.getNumberOfCities());
        System.out.println("initial Path : ");
        System.out.println(population.getFittestTour());
        System.out.println("initial Distance : "+population.getFittestTour().getFitness());
        int generationCount = 0;
        for(int i=0;i<200;i++)
        {
             population = reproductionWithHybridSelectionAndSwapMutation(population);
              generationCount++;
              System.out.println("generation Number : "+generationCount+" , Fittest  : "+population.getFittestTour().getFitness());
        }
        
        System.out.println("Final Distance : "+population.getFittestTour().getFitness());
        System.out.println("Final Path : ");
        System.out.println(population.getFittestTour());
        
        
    }
    
    public Population reproductionWithHybridSelectionAndSwapMutation(Population population)
    {
        Tour[] newPopulation = new Tour[population.getPopulationSize()];
        
        int index;
        Tour[] NFittestTours = elitismSeelection(population);
        for(index = 0;index<elitismSize;index++)
        {
            newPopulation[index] = NFittestTours[index];
        }
        
        for(int i = index ; i <newPopulation.length;i++)
        {
            Tour parent1 = touranmentSelection(population);
            Tour parent2 = touranmentSelection(population);
            Tour child = OX1CrossOver(parent1, parent2);
            SwapMutation(child);
            newPopulation[i] = child;
        }
        return new Population(newPopulation);
    }
    
    // Mutation Methods 
    /*
    => Scramble Mutation
    => Inversion Mutation
    => Swap Mutation    
    */
    
    public Tour ScrambleMutation(Tour tour)
    {
        Tour newTour = new Tour(tour.getNumberOfCities());
        int startIndex = (int) (Math.random() * tour.getNumberOfCities());
        int endIndex = (int) (Math.random() * tour.getNumberOfCities());
        
        while(startIndex >= endIndex)
        {
            startIndex = (int) (Math.random() * tour.getNumberOfCities());
            endIndex = (int) (Math.random() * tour.getNumberOfCities());
        }
        
        for(int i=0;i<startIndex;i++)
        {
            newTour.insertCity(i, tour.getCity(i));
        }
        
        for(int i=endIndex;i<tour.getNumberOfCities();i++)
        {
            newTour.insertCity(i, tour.getCity(i));
        }
        
        int index ;
        for(int i=startIndex;i<endIndex;i++)
        {
            index = (int) (Math.random() * (endIndex - startIndex)) + startIndex;
            while(newTour.containElement(tour.getCity(index)))
            {
                index = (int) (Math.random() * (endIndex - startIndex)) + startIndex;
            }
            newTour.insertCity(i, tour.getCity(index));
        }
        
        return newTour;
    }
    
    public void SwapMutation(Tour tour)
    {
        int mutationPoint;
        for(int i=0;i<tour.getNumberOfCities();i++)
        {
            if(Math.random() < mutationRate)
            {
                mutationPoint = (int) (Math.random() * tour.getNumberOfCities());
                City tmp = tour.getCity(mutationPoint);
                tour.insertCity(mutationPoint, tour.getCity(i));
                tour.insertCity(i, tmp);
            }           
        }
    }
    
    public Tour InversionMutation(Tour tour)
    {
        Tour newTour = new Tour(tour.getNumberOfCities());
        int startIndex = (int) (Math.random() * tour.getNumberOfCities());
        int endIndex = (int) (Math.random() * tour.getNumberOfCities());
        while(startIndex >= endIndex)
        {
            startIndex = (int) (Math.random() * tour.getNumberOfCities());
            endIndex = (int) (Math.random() * tour.getNumberOfCities());
        }
        for(int i=0;i<startIndex;i++)
        {
            newTour.insertCity(i, tour.getCity(i));
        }
        for(int i=endIndex+1;i<tour.getNumberOfCities();i++)
        {
            newTour.insertCity(i, tour.getCity(i));
        }
        int index = startIndex;
        for(int i=endIndex;i>=startIndex;i--)
        {
            newTour.insertCity(index, tour.getCity(i));
            index++;
        }
        return newTour;
    }
    
    // CrossOver Methods
    /*
    => One Point CrossOver
    => Multi Point CrossOver
    => Davis Order OX1
    */
    
    public Tour OnePointCrossOver(Tour parent1 , Tour parent2)
    {
        Tour child = new Tour(parent1.getNumberOfCities());
        int CrossOverPoint = (int) (Math.random() * child.getNumberOfCities()-1);
        
        while(CrossOverPoint == 0 || CrossOverPoint == child.getNumberOfCities()-1 )
        {
            CrossOverPoint = (int) (Math.random() * child.getNumberOfCities()-1);
        }
        for(int i=0;i<CrossOverPoint;i++)
        {
            City city1 = parent1.getCity(i);
            int indexOfCity = parent2.getIndexOf(city1);
            City city2 = parent2.getCity(i);
            parent2.insertCity(i, city1);
            parent2.insertCity(indexOfCity, city2);            
        }
        return child;
    }
    
    
    public Tour MultiPointCrossOver(Tour parent1 , Tour parent2)
    {
        Tour child = new Tour(parent1.getNumberOfCities());
        int startPoint = (int) (Math.random() * parent1.getNumberOfCities());
        int endPoint = (int) (Math.random() * parent1.getNumberOfCities());
        while(startPoint >= endPoint)
        {
            startPoint = (int) (Math.random() * parent1.getNumberOfCities());
            endPoint = (int) (Math.random() * parent1.getNumberOfCities());
        }
        
        for(int i=startPoint;i<endPoint;i++)
        {
            City city1 = parent1.getCity(i);
            int indexOfCity = parent2.getIndexOf(city1);
            City city2 = parent2.getCity(i);
            parent2.insertCity(i, parent2.getCity(indexOfCity));
            parent2.insertCity(indexOfCity, city2);
        }
        return child;
    }
    
    public Tour OX1CrossOver(Tour parent1,Tour parent2)
    {
        Tour child = new Tour(new City[parent1.getNumberOfCities()]);
        int startIndex = (int) (Math.random() * parent1.getNumberOfCities());
        int endIndex = (int) (Math.random() * parent1.getNumberOfCities());
        while(startIndex >= endIndex)
        {
            startIndex = (int) (Math.random() * parent1.getNumberOfCities());
            endIndex = (int) (Math.random() * parent1.getNumberOfCities());
        }
        for(int i=startIndex ; i<endIndex ; i++)
        {
            child.insertCity(i, parent1.getCity(i));
        }
        
        for(int i=0;i<parent2.getNumberOfCities();i++)
        {
            if(!child.containElement(parent2.getCity(i)))
            {
                for(int j=0;j<child.getNumberOfCities();j++)
                {
                    if(child.getCity(j) == null)
                    {
                        child.insertCity(j, parent2.getCity(i));
                        break;
                    }
                }
            }
        }
        
        return child;
    }
    
    // Selection Methods
    /*
    => Stochastic Universal Sampling SUS
    => elitism Selection
    => touranment Selection
    => Roulette Wheel Selection
    => Rank Selection
    */
    
    
    public Tour StochacticUniversalSampling(Population population)
    {
        double SomeOfFitnesses=0,distance=0;
        List<Tour> SUSList = new ArrayList<>();
        for(int i=0;i<population.getPopulationSize();i++)
        {
            SomeOfFitnesses += population.getTour(i).getFitness();
        }
        
        distance = SomeOfFitnesses / SUSNumberOfPointers;
        int startIndex = (int) (Math.random() * distance);
        int pointer;
        for(int i=0;i<population.getPopulationSize();i++)
        {
            pointer = (int) (startIndex + i * distance);
            if(pointer >population.getPopulationSize())
            {
                break;
            }
            SUSList.add(i, population.getTour(pointer));
        }
        Collections.sort(SUSList);
        return SUSList.get(0);
    }
    
    public Tour[] elitismSeelection(Population population)
    {
        Tour[] eletismArray = new Tour[elitismSize];
        Tour[] allTours = population.getTours();
        Arrays.sort(allTours);
        for(int i=0;i<eletismArray.length;i++)
        {
            eletismArray[i] = allTours[i];
        }
        return eletismArray;
    }
    
    public Tour rouletteWheelSelection(Population population)
    {
        double someOfFitnesses=0,count=0;
        Tour selectedTour = null;
        for(int i=0;i<population.getPopulationSize();i++)
        {
            someOfFitnesses += population.getTour(i).getFitness();
        }
        
        count = Math.random() * someOfFitnesses;
        
        for(int i=0;i<population.getPopulationSize();i++)
        {
            count += population.getTour(i).getFitness();
            if(count > someOfFitnesses)
            {
                selectedTour = population.getTour(i);
            }
        }
        return selectedTour;
    }
    
    public Tour rankSelection(Population population)
    {
        List<Tour> rankList = new ArrayList<>();
        for(int i=0;i<population.getPopulationSize();i++)
        {
            rankList.add(i, population.getTour(i));
        }
        Collections.sort(rankList);
        Collections.reverse(rankList);
        
        Tour SelectedTour = null;
        int Total = rankList.size();
        int count = (int) (Math.random() * Total);
        for(int i=0;i<Total;i++)
        {
            count+=i;
            if(count > Total)
            {
                SelectedTour = rankList.get(i);
                break;
            }
        }
        return SelectedTour;
    }
    
    public Tour touranmentSelection(Population pop)
    {
        Tour[] touranmentPool = new Tour[touranmentPoolSize];
        int index;
        for(int i=0;i<touranmentPoolSize;i++)
        {
            index = (int) (Math.random() * pop.getPopulationSize());
            touranmentPool[i] = pop.getTour(index);
        }
        Arrays.sort(touranmentPool);
        return touranmentPool[0];
    }
    
}

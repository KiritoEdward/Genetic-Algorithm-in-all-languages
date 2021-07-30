package tsr;

import java.util.Arrays;

public class Population {
    private Tour[] tours;
    private Tour fittest;

    public Population(int popSize,int tourSize) {
        tours = new Tour[popSize];
        for(int i=0;i<popSize;i++)
        {
            tours[i] = new Tour(tourSize);
        }
        fittest = getFittestTour();
    }

    public Population(Tour[] tours) {
        this.tours = tours;
        this.fittest = fittest;
    }
    
    
    
    public Tour getFittestTour()
    {
        Arrays.sort(tours);
        return tours[0];
    }
    
    public Tour[] getNFittestTours(int n)
    {
        Tour[] tmp = new Tour[n];
        Arrays.sort(tours);
        for(int i=0;i<n;i++)
        {
            tmp[i] = tours[i];
        }
        return tmp;
    }

    public Tour[] getTours() {
        return tours;
    }
    
    public Tour getTour(int index)
    {
        return tours[index];
    }
    
    public int getPopulationSize()
    {
        return tours.length;
    }
    
}

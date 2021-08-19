# Genetic Algorithm In C

This Folder Contains The Implementation of Genetic Algorithm to Solve Traveling Salesman Problem.

The PseudoCode of Genetic Algorithm is like Below :
 
<div align="center" >
<img src="../Resources/GA.png" width="400" height="300">
</div>

* Genetic Algorithm Contains Five Steps :

      * Initial Population .
      * Fitness Function .
      * Selection .
      * CrossOver .
      * Mutation .
  
---  

### Initial Population :

<div align="center" >
<img src="../Resources/Population.png" width="300" height="200">
</div>

Gene :
```java
public class City{
    private int id;
    private float x;
    private float y;

    public City() {
    }

    public City(int id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return String.valueOf(this.id);
    }
    
    public double getDistance(City city)
    {
        return Math.sqrt(Math.pow(this.x - city.getX(), 2 ) + Math.pow(this.y - city.getY(), 2));
    }
    
}
```

Chromosome :

```java
package tsr;

public class Tour implements Comparable<Tour>{
    private City[] cities;
    private double fitness;

    public Tour(int tourSize) {
        cities = new City[tourSize];
        for(int i=0;i<tourSize;i++)
        {
            cities[i] = DataSet.getCity(i);
        }
        calculateFitness();
    }

    public Tour(City[] citiesarray) {
        this.cities = citiesarray;
    }

    public City[] getCities() {
        return cities;
    }

    private void calculateFitness()
    {
        double fit = 0;
        for(int i=0;i<cities.length-1;i++)
        {
            fit += cities[i].getDistance(cities[i+1]);
        }
        fit += cities[cities.length-1].getDistance(cities[0]);
        this.fitness = fit;
    }
    
    public void setCities(City[] cities) {
        this.cities = cities;
        calculateFitness();
    }
    
    public int getIndexOf(City c)
    {
        
        int index = -1;
        for(int i=0;i<cities.length;i++)
        {
            if(cities[i].getX() == c.getX() && cities[i].getY() == c.getY())
            {
                index = i;
            }
        }
        return index;
    }

    public double getFitness() {
        calculateFitness();
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
    
    public City getCity(int index)
    {
        return cities[index];
    }
    
    public boolean containElement(City city)
    {
        boolean flag = false;
        for(int i=0;i<cities.length;i++)
        {
            if(cities[i] != null)
            {
                if(cities[i].getX() == city.getX() && cities[i].getY() == city.getY())
                {
                    flag = true;
                }
            }
        }
        return flag;
    }
    
    public void insertCity(int index,City city)
    {
        this.cities[index] = city;
    }
    
    public int getNumberOfCities()
    {
        return cities.length;
    }

    @Override
    public int compareTo(Tour t) {
        return this.getFitness() - t.getFitness() > 0 ? 1 : this.getFitness() - t.getFitness() < 0 ? -1 : 0;
    }
    
    @Override
    public String toString() {
        String str = new String();
        str = str.concat("[ ");
        for(int i=0;i<cities.length;i++)
        {
            if (i != cities.length -1)
                str = str.concat(cities[i].toString() + " -> ");
            else
                str = str.concat(cities[i].toString() + " ");
        }
        str = str.concat(" ]");        
        return str;
    }
    
}

```

Population :

```java
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

```

Function To Generate The Initial Population :

```c
void init_population(population *pop)
{
	readDataSet();
	tour tour = getTour(),*pointerT;
	pop->tours[0] = tour;
	for(pointerT = pop->tours + 1 ; pointerT < pop->tours + POP_SIZE ; pointerT++)
	{
		*(pointerT) = getRandomTour(*(pointerT - 1));
	}
	calculateFitnessForAll(pop);
	pop->fittest = getFittestTour(*pop);
}
```
---
### Fitness Function :

```java
private void calculateFitness()
    {
        double fit = 0;
        for(int i=0;i<cities.length-1;i++)
        {
            fit += cities[i].getDistance(cities[i+1]);
        }
        fit += cities[cities.length-1].getDistance(cities[0]);
        this.fitness = fit;
    }
```
---
### Selection Methods:

Elitism Selection :

```java
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
```

Stochactic Universal Sampling :

```java
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
```

Roulette Wheel Selection :
```java
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
```

Touranment Selection :
```java
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
```

Rank Selection :

```java
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
```
---
### CrossOver Methods :
One Point CrossOver :
```java
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
```
Two Point CrossOver :

```java
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
```

OX1 CrossOver :

```java
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
```

### Mutation methods :

Swap Mutation :

```java
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
```

Scramble Mutation :
```java
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
```

Inversion Mutation :
```java
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
```

---
### Reproduction :

```java
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
```

---
### Genetic Algorithm :

```java
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
```
---
This Folder Contains a list of TSP Instances you can find them in this Folder : [Instances](src/main/java/tsr/DataSets) , This Folder Contains :

* bayg29
* Berlin52
* burma14
* Eil51
* Eil101
* pr76
* st70
* ulysses22
* ulysses216

To Test one of this instances Meke this changes :

* Go to this File [DataSet](src/main/java/tsr/DataSet.java) and change The File Name in The Function ReadDataSet .

```java
String fileName = "_Berlin52.txt";
```




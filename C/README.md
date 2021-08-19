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
```c
typedef struct city{
	int id;
	float x;
	float y;
}city;
```

Chromosome :

```c
typedef struct tour{
	city cities[NBR_CITIES];
	float fitness;	
}tour;
```

Population :

```c
typedef struct population{
	tour tours[POP_SIZE];
	tour fittest;
}population;
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
```c
float calculate_Fitness(tour t1)
{
	float fitness = 0;
	int i;
	for(i=0;i<NBR_CITIES;i++)
	{
		fitness += sqrt(pow(t1.cities[i+1].x - t1.cities[i].x, 2) + pow(t1.cities[i+1].y - t1.cities[i].y, 2)) ;
	}
	fitness += sqrt(pow(t1.cities[NBR_CITIES - 1].x - t1.cities[0].x, 2) + pow(t1.cities[NBR_CITIES - 1].y - t1.cities[0].y, 2)) ;
	
	return fitness;
}
```
---
### Selection Methods:

Elitism Selection :

```c
tour* elitismSelection(population p) // Elitism Selection
{
	sortPopulationASC(&p);
	tour *pt,*t = (tour *) malloc(elitismSizeG * sizeof(tour));
	for(pt = t ; pt < t + elitismSizeG ; pt++)
	{
		*pt = p.tours[pt-t];
	}
	return t;
}
```

Roulette Wheel Selection :
```c
tour rouletteWheelSeelction(population p)
{
		float sumOfFitnesses = 0;
		tour selectedTour;
		calculateFitnessForAll(&p);
		for(int i=0;i<POP_SIZE;i++)
		{
			sumOfFitnesses += p.tours[i].fitness;
		}
		
		float count = (float) (((float)rand() / (float) RAND_MAX) * sumOfFitnesses);
		
		
		for(int i=0;i<POP_SIZE;i++)
		{
			count += p.tours[i].fitness;
			if(count > sumOfFitnesses)
			{
				selectedTour = p.tours[i];
				break;
			}
		} 		
		return selectedTour;
}
```

Touranment Selection :
```c
tour touranmentSeelection(population p) // Touranment Selection
{
		tour touranmentPool[touranmentPoolSizeG];
		int index;
		for(int i=0;i<touranmentPoolSizeG;i++)
		{
			index = (int) rand() % (-POP_SIZE) + 1;
			touranmentPool[i] = p.tours[index];
		}
		return *sortToursASC( touranmentPool, touranmentPoolSizeG);
}
```

Rank Selection :

```c
tour RankSeelction(population p)
{
		tour selectedTour ;
		sortPopulationASC(&p);
		
		int count = (int) (((float)rand() / (float) RAND_MAX) * POP_SIZE);
		
		for(int i=0;i<POP_SIZE;i++)
		{
			count += i;
			if(count > POP_SIZE)
			{
				selectedTour = p.tours[i];
				break;
			}
		}
		return selectedTour;
}
```
---
### CrossOver Methods :
One Point CrossOver :
```c
tour OnePointCrossOver(tour parent1 , tour parent2) // One Point CrossOver
{
	int index,crossOverPoint = (int)rand()%(-NBR_CITIES+2) + 2;
	
	if(crossOverPoint == 0)
	{
		printf("index 0 changed .\n");
	}
	
	for(int i=crossOverPoint;i<NBR_CITIES ; i++)
	{
		city c = parent2.cities[i];
		index = getIndexOfCity(parent1,c);
		parent1.cities[index] = parent1.cities[i];
		parent1.cities[i] = c ;		
	}
	
	parent1.fitness = calculate_Fitness(parent2);
	
	return parent1;
}
```
Two Point CrossOver :
```c
tour TwoPointCrossOver(tour parent1 , tour parent2) // Two Point CrossOver
{
	int index,crossOverPointStart,crossOverPointEnd;
	//Initialize The two CrossOver Points
	crossOverPointEnd = (int)rand()%(-NBR_CITIES+2) + 2 ;
	crossOverPointStart = (int)rand()%(-NBR_CITIES+2) + 2;
	//Make sure that the start index less than The End Index
	while(crossOverPointStart < crossOverPointEnd )
	{
		crossOverPointEnd = (int)rand()%(-NBR_CITIES+2) + 2 ;
		crossOverPointStart = (int)rand()%(-NBR_CITIES+2) + 2;
	}
	//Copy The elements of parent2 in parent1
	for(int i=crossOverPointStart;i<crossOverPointEnd ; i++)
	{
		city c = parent2.cities[i];
		index = getIndexOfCity(parent1,c);
		parent1.cities[index] = parent1.cities[i];
		parent1.cities[i] = c ;		
	}
	
	parent1.fitness = calculate_Fitness(parent2);
	return parent1;
}
```

OX1 CrossOver :
```c
tour ox1CrossOver(tour parent1 , tour parent2) //OX1 CrossOver which is cycle crossover 
{
	int Start,End;
	tour child;
	//Insert City 1 in Index 0
	child.cities[0] = parent1.cities[0];
	//Initialze The Tour With NULL
	city nullCity;
	nullCity.id = -1;
	for(int i=1;i<NBR_CITIES;i++)
	{
		child.cities[i] = nullCity;	
	}
	
	//Calculate The Start and End indexes
	End = (int)rand()%(-NBR_CITIES+2) + 2 ;
	Start = (int)rand()%(-NBR_CITIES+2) + 2;
	//Make sure That Start Index less than End Index
	while(End <= Start)
	{
		End = (int)rand()%(-NBR_CITIES+2) + 2 ;
		Start = (int)rand()%(-NBR_CITIES+2) + 2;
	}
	
	//copy The element of Parent1 between Start and end Indexes in The Child
	for(int i=Start;i<End;i++)
	{
			child.cities[i] = parent1.cities[i];
	}
	//Fill The Rest of Child with Cities in Parent2
	for(int i=1;i<NBR_CITIES;i++)
	{
		if(!containCity(child,parent2.cities[i]))
		{
			for(int j=1;j<NBR_CITIES;j++)
			{
				if(child.cities[j].id == -1)
				{
					child.cities[j] = parent2.cities[i];
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
```c
void swapMutation(tour *tour)
{
	double p;
	int mutationPoint ;

	for(int i=1;i<NBR_CITIES;i++)
	{
			p = (double)rand() / (double) RAND_MAX ;
			if(p < mutationRateG)
			{
				mutationPoint = (int)rand()%(-NBR_CITIES+2) + 2;
				city tmp = tour->cities[i];
				tour->cities[i] = tour->cities[mutationPoint]; 
				tour->cities[mutationPoint] = tmp;
			} 
	}
	tour->fitness = calculate_Fitness(*tour);
}
```
---
### Reproduction :

```c
population reproduction(population p)
{
	population newPopulation;
	int index;
	tour *elitism = elitismSelection(p);
	for(index = 0 ; index < elitismSizeG ; index ++)
	{
		newPopulation.tours[index] = *(elitism+index);
	}
	
	tour parent1,parent2,child;
	
	for(int i=index;i<POP_SIZE;i++)
	{
		
		parent1 = touranmentSeelection(p);
		parent2 = touranmentSeelection(p);
		child = ox1CrossOver(parent1,parent2);
		
		while(child.fitness < parent1.fitness && child.fitness < parent2.fitness)
		{
			tour tmp = parent1;
			parent1 = parent2;
			parent2 = tmp;
			
			child = TwoPointCrossOver(parent1,parent2);
			swapMutation(&child);	
		}
		
		
		
		newPopulation.tours[i] = child;
	}
	calculateFitnessForAll(&newPopulation);
	newPopulation.fittest = getFittestTour(newPopulation);
	return newPopulation;
}
```

---
### Genetic Algorithm :

```c
void GeneticAlgorithm(int numberOfGenerations,int elitismSize , float mutationRate , int touranmentPoolSize)
{
	int generationCounter = 0;
	elitismSizeG = elitismSize;
	mutationRateG = mutationRate ;
	touranmentPoolSizeG = touranmentPoolSize ;
	//Initialize The Population
	
	population initial_Population ;
	
	init_population(&initial_Population);
	
	printf("The Fitness for The Initial Population is %f \n",getFittestTour(initial_Population).fitness);
	printf("The Initial Tour : \n");
	toString(initial_Population.tours[0]);
		
	float epidemic_prob ;
	
	population newPopulation = initial_Population;
	for(int i=0;i<numberOfGenerations ; i++)
	{
		newPopulation = reproduction(newPopulation);
		generationCounter ++;
		calculateFitnessForAll(&newPopulation);
		printf("Generation : %d , Fitness : %f .\n",generationCounter,newPopulation.fittest.fitness);			
	}
	printf("Final Result : \n");
	printf("The Fitness For The Fittest Tour is %f \n ",newPopulation.fittest.fitness);
	printf("The Final Tour is : \n");
	toString(newPopulation.fittest);
	WriteResultIntoFile(newPopulation);	
}
```
---
This Folder Contains a list of TSP Instances you can find them in this Folder : [Instances](DataSets/) , This Folder Contains :

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

* Go to this File [DataSet](DataSet.cpp) and change The File Name in The Function ReadDataSet .

```c
Pfile = fopen("DataSets/_Berlin52.txt","r");
```

* Go to this File [structures](structures.h) and change The Constant NBR_CITIES .
```c
#define NBR_CITIES 52
```
This Constant Contains The Number Of Cities in The DataSet that you're Using .




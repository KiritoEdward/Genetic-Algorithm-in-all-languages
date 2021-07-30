#ifndef ___GeneticAlgorithmFile___
#define ___GeneticAlgorithmFile___
#include<stdio.h>
#include<stdlib.h>
#include "tour.cpp"
#include "population.cpp"

//Global Varibales to control The Genetic Algorithm
static int elitismSizeG, touranmentPoolSizeG;
static float mutationRateG; 

static int Population_size;

//Prototypes
void GeneticAlgorithm(int numberOfGenerations,int elitismSize , float mutationRate , int touranmentPoolSize);
population reproduction(population p);
//Mutation Methods
void swapMutation(tour *tour);
//CrossOverMethods
tour OnePointCrossOver(tour parent1 , tour parent2);
tour TwoPointCrossOver(tour parent1 , tour parent2);
tour ox1CrossOver(tour parent1 , tour parent2);
//Selection Methods
tour* elitismSelection(population p);
tour touranmentSeelection(population p);
tour rouletteWheelSeelction(population p);
tour RankSeelction(population p);
//function to Write The Result in a file
void WriteResultIntoFile(population pop);

// Genetic Algorithm
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

// Method to Write The Result in a File
void WriteResultIntoFile(population pop)
{
	FILE *pf;
	pf = fopen("Result.txt","w");
	if(pf == NULL)
	{
		printf("Failed to Open The File .\n");
	}
	
	for(int i=0;i<NBR_CITIES;i++)
	{
		fprintf(pf,"%d\t%f\t%f\n",pop.fittest.cities[i].id,pop.fittest.cities[i].x,pop.fittest.cities[i].y);
	}
	fclose(pf);
}

// Reproduction Methods wich contain All The Methods
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

// Swap Methods
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

// Selection Methods

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

// CrossOver Methods

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

#endif
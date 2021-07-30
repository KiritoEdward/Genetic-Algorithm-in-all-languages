#ifndef __PopulationFile__
#define __PopulationFile__
#include<stdio.h>
#include<stdlib.h>
#include "DataSet.cpp"
#include "tour.cpp"

//ProtoTypes
void init_population(population *pop);
tour getFittestTour(population pop);
tour* getNFittestTours(population pop , int n);
void sortPopulationASC(population *pop);
void sortPopulationDESC(population *pop);
void toStringPopulation(population pop);
//void sortToursASC(tour *t,int size);
void calculateFitnessForAll(population *p);
//Implementation

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

tour getFittestTour(population pop)
{
	sortPopulationASC(&pop);
	return pop.tours[0];
}

tour* getNFittestTours(population pop,int n)
{
	tour *array = (tour*) malloc(n * sizeof(tour));
	sortPopulationASC(&pop);
	for(int i=0;i<n;i++)
	{
		array[i] = pop.tours[i];
	}
	return array;
}

void sortPopulationASC(population *pop)
{
	calculateFitnessForAll(pop);
	for(int i=0;i<POP_SIZE-1 ; i++)
	{
		for(int j=i+1;j<POP_SIZE;j++)
		{
			if(compare(pop->tours[i],pop->tours[j]) > 0)
			{
				tour tmp = pop->tours[i];
				pop->tours[i] = pop->tours[j];
				pop->tours[j] = tmp;	
			}		
		}
	}
}

void sortPopulationDESC(population *pop)
{
	for(int i=0;i<POP_SIZE-1 ; i++)
	{
		for(int j=i+1;j<POP_SIZE;j++)
		{
			if(compare(pop->tours[i],pop->tours[j]) < 0)
			{
				tour tmp = pop->tours[i];
				pop->tours[i] = pop->tours[j];
				pop->tours[j] = tmp;	
			}		
		}
	}
}

void toStringPopulation(population pop)
{
	for(int i=0;i<POP_SIZE ; i++)
	{
		printf("Tour %d ,  Fitness = %.2f \n",i+1,calculate_Fitness(pop.tours[i]));
		//toString(pop.tours[i]);
	}
}

tour* sortToursASC(tour t[] ,int size)
{
	for(int i=0;i<size-1 ; i++)
	{
		for(int j=i+1;j<size;j++)
		{
			if(compare(t[i],t[j]) == 1)
			{
				tour tmp = t[i];
				t[i] = t[j];
				t[j] = tmp;	
			}		
		}
	}
	return &t[0];	
}

void calculateFitnessForAll(population *p)
{
	for(int i=0;i<POP_SIZE ; i++)
	{
		p->tours[i].fitness = calculate_Fitness(p->tours[i]); 
	}
}

#endif


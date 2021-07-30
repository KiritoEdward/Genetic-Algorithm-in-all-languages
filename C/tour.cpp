#ifndef __TourFile__
#define __TourFile__
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<math.h>
#include "structures.h"


//Prototypes
float calculate_Fitness(tour t1);
int containCity(tour t1,city c);
int compare(tour t1,tour t2);
tour getRandomTour(tour t);
void toString(tour t);
int getIndexOfCity(tour t , city c);

//Implemetaion

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

int containCity(tour t1,city c)
{
	int i,contain = 0;
	for(i=0;i<NBR_CITIES;i++)
	{
		if(t1.cities[i].id == c.id)
			contain = 1;
	}
	return contain;
}

int compare(tour t1,tour t2)
{
	return t1.fitness > t2.fitness ? 1 : t1.fitness < t2.fitness ? -1 : 0 ;
}

tour getRandomTour(tour t)
{
	int index;
		for (int i = 1; i < NBR_CITIES - 1; i++) 
        {
          index = (int)rand()%(-NBR_CITIES +2) + 2;
		  city c = t.cities[index];
          t.cities[index] = t.cities[i];
          t.cities[i] = c;
        }
	t.fitness = calculate_Fitness(t);		
	return t;
}

void toString(tour t)
{
	for(int i = 0 ; i < NBR_CITIES ; i++)
	{
		printf("%d",t.cities[i].id);
		if(i != NBR_CITIES -1)
		{
			printf("->");
		}		
	}
	printf("\n");
	
}


int getIndexOfCity(tour t , city c)
{
	for(int i=0;i<NBR_CITIES ; i++)
	{
		if(t.cities[i].id == c.id)
		{
			return i;
		}
	}
    return -1;
}

#endif






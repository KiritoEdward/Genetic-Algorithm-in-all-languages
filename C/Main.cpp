#include<stdio.h>
#include<stdlib.h>
#include<time.h>
#include "GeneticAlgorithm.cpp"

int main(int argc , char** argv)
{
	srand(time(NULL));
	
	GeneticAlgorithm(200,50,0.1,15);	

	return 0;
}
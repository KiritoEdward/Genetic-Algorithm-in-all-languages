#include<stdio.h>
#include<stdlib.h>
#include "structures.h"

static tour t;

void readDataSet()
{
	FILE *Pfile;
	int i=0;
	
	Pfile = fopen("DataSets/_Berlin52.txt","r");
	
	if(Pfile == NULL)
	{
		printf("Failed To Open The File . \n");
		return;
	}
	
	while(!feof(Pfile))
	{
		fscanf(Pfile,"%d\t%f\t%f\n",&t.cities[i].id,&t.cities[i].x,&t.cities[i].y);
		i++;
	}
	
	fclose(Pfile);
}

tour getTour()
{
		return t;
}



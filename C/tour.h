#ifndef __Tour_Methods__
#define __Tour_Methods__
#include "structures.h"
void init_Tour(tour *t);
float calculate_Fitness(tour t);
int containCity(tour t);
int compare(tour t1,tour t2);
char* toString(tour t);

#endif


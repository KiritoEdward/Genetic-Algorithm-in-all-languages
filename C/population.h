#ifndef __population_Methods__
#define __population_Methods__
#include "structures.h"
void init_population(population *pop);
tour getFittestTour(population pop);
tour* getNFittestTours(population pop);
#endif

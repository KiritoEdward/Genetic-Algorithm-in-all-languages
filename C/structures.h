#ifndef __HEADERS__
#define __HEADERS__
#define NBR_CITIES 52
#define POP_SIZE 200

typedef struct city{
	int id;
	float x;
	float y;
}city;

typedef struct tour{
	city cities[NBR_CITIES];
	float fitness;	
}tour;

typedef struct population{
	tour tours[POP_SIZE];
	tour fittest;
}population;


#endif

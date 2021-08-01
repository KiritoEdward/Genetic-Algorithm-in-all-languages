from GeneticAlgorithm import geneticAlgorithm
import DataSet

DataSet.readDataSet("_Berlin52.txt")

ga = geneticAlgorithm(nbrGenerations = 200,popSize = 200 ,elitismSize = 25 ,poolSize = 10 ,mutationRate = 0.1 )


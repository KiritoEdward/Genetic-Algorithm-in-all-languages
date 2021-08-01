from City import city
import random
import os


cities = []
def readDataSet(filename):
    dir_path = os.path.dirname(os.path.realpath(__file__))
    file_path = dir_path + "\DataSets\\" + filename
    with open(file_path , "r") as f:
        for line in f:
            id  = int(line.split()[0].strip())  
            x   = float(line.split()[1].strip())  
            y   = float(line.split()[2].strip('\n'))

            cities.append(city(id,x,y))

def getNbrCities():
    return len(cities)


def getRandomCity():
    index  = random.randint(0,getNbrCities()-1)   
    return cities[index]







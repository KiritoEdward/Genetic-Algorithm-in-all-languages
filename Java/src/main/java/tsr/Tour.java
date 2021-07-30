package tsr;

public class Tour implements Comparable<Tour>{
    private City[] cities;
    private double fitness;

    public Tour(int tourSize) {
        cities = new City[tourSize];
        for(int i=0;i<tourSize;i++)
        {
            cities[i] = DataSet.getCity(i);
        }
        calculateFitness();
    }

    public Tour(City[] citiesarray) {
        this.cities = citiesarray;
    }

    public City[] getCities() {
        return cities;
    }

    private void calculateFitness()
    {
        double fit = 0;
        for(int i=0;i<cities.length-1;i++)
        {
            fit += cities[i].getDistance(cities[i+1]);
        }
        fit += cities[cities.length-1].getDistance(cities[0]);
        this.fitness = fit;
    }
    
    public void setCities(City[] cities) {
        this.cities = cities;
        calculateFitness();
    }
    
    public int getIndexOf(City c)
    {
        
        int index = -1;
        for(int i=0;i<cities.length;i++)
        {
            if(cities[i].getX() == c.getX() && cities[i].getY() == c.getY())
            {
                index = i;
            }
        }
        return index;
    }

    public double getFitness() {
        calculateFitness();
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
    
    public City getCity(int index)
    {
        return cities[index];
    }
    
    public boolean containElement(City city)
    {
        boolean flag = false;
        for(int i=0;i<cities.length;i++)
        {
            if(cities[i] != null)
            {
                if(cities[i].getX() == city.getX() && cities[i].getY() == city.getY())
                {
                    flag = true;
                }
            }
        }
        return flag;
    }
    
    public void insertCity(int index,City city)
    {
        this.cities[index] = city;
    }
    
    public int getNumberOfCities()
    {
        return cities.length;
    }

    @Override
    public int compareTo(Tour t) {
        return this.getFitness() - t.getFitness() > 0 ? 1 : this.getFitness() - t.getFitness() < 0 ? -1 : 0;
    }
    
    @Override
    public String toString() {
        String str = new String();
        str = str.concat("[ ");
        for(int i=0;i<cities.length;i++)
        {
            if (i != cities.length -1)
                str = str.concat(cities[i].toString() + " -> ");
            else
                str = str.concat(cities[i].toString() + " ");
        }
        str = str.concat(" ]");        
        return str;
    }
    
}

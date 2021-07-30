package tsr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DataSet {
    private static final ArrayList<City> listofCities = new ArrayList<>();
    
    
    static {
        try{
        String fileName = "_Berlin52.txt";    
        File file = new File(System.getProperty("user.dir") + "\\src\\main\\java\\tsr\\DataSets/"+fileName);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line;
        StringTokenizer stringTokenizer;
        City city ;
        while((line = bufferedReader.readLine()) != null)
        {
            stringTokenizer = new StringTokenizer(line, " \t ");
            int id = Integer.parseInt(stringTokenizer.nextToken());
            float x = Float.parseFloat(stringTokenizer.nextToken());
            float y = Float.parseFloat(stringTokenizer.nextToken()) ;
            city = new City(id, x, y);
            listofCities.add(city);
        }
        }catch(IOException | NumberFormatException exception){
            exception.printStackTrace();
        }
    }
    
    public static void addCity(City city){
        listofCities.add(city);
    }
    
    public static City getCity(int index)
    {
        return listofCities.get(index);
    }
    
    public static int getNumberOfCities()
    {
        return listofCities.size();
    }
    
    public static List<City> getAllCities()
    {
        return listofCities;
    }
}

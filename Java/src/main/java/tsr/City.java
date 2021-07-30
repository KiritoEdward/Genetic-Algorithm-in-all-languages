package tsr;

public class City{
    private int id;
    private float x;
    private float y;

    public City() {
    }

    public City(int id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return String.valueOf(this.id);
    }
    
    public double getDistance(City city)
    {
        return Math.sqrt(Math.pow(this.x - city.getX(), 2 ) + Math.pow(this.y - city.getY(), 2));
    }
    
}

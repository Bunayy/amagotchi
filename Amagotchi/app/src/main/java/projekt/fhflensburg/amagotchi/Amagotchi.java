package projekt.fhflensburg.amagotchi;

/**
 * Created by bunay_000 on 09.09.2015.
 */
public class Amagotchi
{
    private static Amagotchi instance;

    private String name;
    private double health;
    private double repletion;
    private double sleep;
    private double motivation;
    private double happiness;
    private double fitness;
    private double attention;
    private int level;
    private double age;
    private double weight;

    private Amagotchi(){}

    public static Amagotchi getInstance()
    {
        if(Amagotchi.instance == null)
            Amagotchi.instance = new Amagotchi();

        return Amagotchi.instance;
    }

    public static void resetAmagotchi()
    {
        Amagotchi.instance = new Amagotchi();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getHealth()
    {
        return health;
    }

    public void setHealth(double health)
    {
        this.health = health;
    }

    public double getRepletion()
    {
        return repletion;
    }

    public void setRepletion(double repletion)
    {
        this.repletion = repletion;
    }

    public double getSleep()
    {
        return sleep;
    }

    public void setSleep(double sleep)
    {
        this.sleep = sleep;
    }

    public double getMotivation()
    {
        return motivation;
    }

    public void setMotivation(double motivation)
    {
        this.motivation = motivation;
    }

    public double getHappiness()
    {
        return happiness;
    }

    public void setHappiness(double happiness)
    {
        this.happiness = happiness;
    }

    public double getFitness()
    {
        return fitness;
    }

    public void setFitness(double fitness)
    {
        this.fitness = fitness;
    }

    public double getAttention()
    {
        return attention;
    }

    public void setAttention(double attention)
    {
        this.attention = attention;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public double getAge()
    {
        return age;
    }

    public void setAge(double age)
    {
        this.age = age;
    }

    public double getWeight()
    {
        return weight;
    }

    public void setWeight(double weight)
    {
        this.weight = weight;
    }


}

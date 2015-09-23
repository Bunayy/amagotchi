package projekt.fhflensburg.amagotchi;

import android.content.Context;
import android.util.TypedValue;

// Unterscheidung Eiform und geschlüpfte Form umsetzen
// ToString() Funktion für die Speicherung
/**
 * Created by bunay_000 on 09.09.2015.
 */
public class Amagotchi
{
    private static Amagotchi instance;
    private static Context context;

    // Eigenschaften initial setzen
    // Eigenschaften observable machen
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
    private int developmentPoints;

    // Feld initial setzen
    // Feld observable machen
    private boolean isSick;

    private double healthPerPeriod;
    private double repletionPerPeriod;
    private double sleepPerPeriod;
    private double motivationPerPeriod;
    private double happinessPerPeriod;
    private double fitnessPerPeriod;
    private double attentionPerPeriod;

    private Amagotchi()
    {
        init();
    }

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

    public static void setContext(Context context)
    {
        Amagotchi.context = context;
    }

    public void init()
    {
        TypedValue tempValue = new TypedValue();

        context.getResources().getValue(R.dimen.healthPerPeriodStandardValue, tempValue, true);
        healthPerPeriod = tempValue.getFloat();

        context.getResources().getValue(R.dimen.repletionPerPeriodStandardValue, tempValue, true);
        repletionPerPeriod = tempValue.getFloat();

        context.getResources().getValue(R.dimen.sleepPerPeriodStandardValue, tempValue, true);
        sleepPerPeriod = tempValue.getFloat();

        context.getResources().getValue(R.dimen.motivationPerPeriodStandardValue, tempValue, true);
        motivationPerPeriod = tempValue.getFloat();

        context.getResources().getValue(R.dimen.happinessPerPeriodStandardValue, tempValue, true);
        happinessPerPeriod = tempValue.getFloat();

        context.getResources().getValue(R.dimen.fitnessPerPeriodStandardValue, tempValue, true);
        fitnessPerPeriod = tempValue.getFloat();

        context.getResources().getValue(R.dimen.attentionPerPeriodStandardValue, tempValue, true);
        attentionPerPeriod = tempValue.getFloat();

        context.getResources().getValue(R.dimen.levelStandardValue, tempValue, true);
        level = (int) tempValue.getFloat();

        context.getResources().getValue(R.dimen.ageStandardValue, tempValue, true);
        age = tempValue.getFloat();

        context.getResources().getValue(R.dimen.weightStandardValue, tempValue, true);
        weight = tempValue.getFloat();
    }

    public void updateValues()
    {
        health += healthPerPeriod;
        repletion += repletionPerPeriod;
        sleep += sleepPerPeriod;
        motivation += motivationPerPeriod;
        happiness += happinessPerPeriod;
        fitness += fitnessPerPeriod;
        attention += attentionPerPeriod;
    }

    /**
     * Ermittelt anhand der Werte die Entwicklungspunkte, die das Amagotchi bekommt.
     *
     * Info: Provisorische Grenzwerte
     */
    public void updateDevelopment()
    {
        if(health >= context.getResources().getInteger(R.integer.healthDevelopmentBoundary))
            developmentPoints += 1;
        if(repletion >= context.getResources().getInteger(R.integer.repletionDevelopmentBoundary))
            developmentPoints += 1;
        if(sleep >= context.getResources().getInteger(R.integer.sleepDevelopmentBoundary))
            developmentPoints += 1;
        if(motivation >= context.getResources().getInteger(R.integer.motivationDevelopmentBoundary))
            developmentPoints += 1;
        if(happiness >= context.getResources().getInteger(R.integer.happinessDevelopmentBoundary))
            developmentPoints += 1;
        if(fitness >= context.getResources().getInteger(R.integer.fitnessDevelopmentBoundary))
            developmentPoints += 1;
        if(attention >= context.getResources().getInteger(R.integer.attentionDevelopmentBoundary))
            developmentPoints += 1;

        //Alterung hinzufügen
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

package projekt.fhflensburg.amagotchi;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;

// Unterscheidung Eiform und geschlüpfte Form umsetzen
// ToString() Funktion für die Speicherung
public class Amagotchi
{
    private static final String LOG_TAG = "Amagotchi";
    private static final Integer DEST_SAVE_VERSION = 1; //Savegame version

    private static Amagotchi instance;
    public static Context context;

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

    private int timeToHatch;

    // Feld observable machen
    private boolean isSickInfection;
    private boolean isSickOvereating;

    // Feld observable machen
    private boolean feces;
    private Long fecesTimestamp;

    // Feld observable machen
    private boolean isdead;

    private double healthPerPeriod;
    private double repletionPerPeriod;
    private double sleepPerPeriod;
    private double motivationPerPeriod;
    private double happinessPerPeriod;
    private double fitnessPerPeriod;
    private double attentionPerPeriod;

    private FoodFactory foodFactory;
    private Food food;

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

    //Check if instance exists
    public static Amagotchi getState() {

        if(Amagotchi.instance == null)
            return null;
        else
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

    private void init()
    {
        TypedValue tempValue = new TypedValue();

        health = context.getResources().getInteger(R.integer.startValueHealth);
        repletion = context.getResources().getInteger(R.integer.startValueRepletion);
        sleep = context.getResources().getInteger(R.integer.startValueSleep);
        motivation = context.getResources().getInteger(R.integer.startValueMotivation);
        happiness = context.getResources().getInteger(R.integer.startValueHappiness);
        fitness = context.getResources().getInteger(R.integer.startValueFitness);
        attention = context.getResources().getInteger(R.integer.startValueAttention);

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

        timeToHatch = context.getResources().getInteger(R.integer.startValuetimeToHatch);

        isSickInfection = false;
        isSickOvereating = false;
        feces = false;
        isdead = false;

        foodFactory = new FoodFactory();
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

    public void feedAmagotchi(String foodType)
    {
        food = foodFactory.getFood(foodType);
        food.feed(this);
    }

    // Muss noch umgesetzt werden
    public void developAmagotchi()
    {

    }

    //Muss noch umgesetzt werden
    public void ripAmagotchi()
    {
        isdead = true;
    }

    public Boolean getIsSickInfection() { return isSickInfection; }

    public void setIsSickInfection(Boolean val) { this.isSickInfection = val; }

    public Boolean getisSickOveraeting() { return isSickOvereating; }

    public void setIsSickOveraeting(Boolean val) { this.isSickOvereating = val; }

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

    public String getSaveString()
    {
        Log.d(LOG_TAG, "getSaveString() " + name);
        return name;
    }

    public void loadSaveString(String input)
    {
        name = input;
    }
}

package projekt.fhflensburg.amagotchi;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;

// Unterscheidung Eiform und geschlüpfte Form umsetzen
// ToString() Funktion für die Speicherung
public class Amagotchi
{
    private static final String LOG_TAG = "Amagotchi";
    private static final String DEST_SAVE_VERSION = "1"; //Savegame version in der gespeichert wird

    private static Amagotchi instance;
    public static Context context;

    // Eigenschaften observable machen
    private String name;
    private Long saveTimestamp; //Vergleiche mit System.currentTimeMillis()/60000 zur berechnung der Offline Zeit
    private String type;
    private String mutation;
    private double health;
    private double repletion;
    private double sleep;
    private double motivation;
    private double happiness;
    private double fitness;
    private double attention;

    private int level;
    private int age; //in Tage, alle 30 Mins
    private double weight;
    private int developmentPoints;

    private int timeToHatch;

    // Feld observable machen
    private boolean isSickInfection;
    private boolean isSickOvereating;

    // Feld observable machen
    private boolean feces;
    private int fecesCountdown;

    // Feld observable machen
    private boolean isDead;

    private double healthPerPeriod;
    private double repletionPerPeriod;
    private double sleepPerPeriod;
    private double motivationPerPeriod;
    private double happinessPerPeriod;
    private double fitnessPerPeriod;
    private double attentionPerPeriod;

    private FoodFactory foodFactory;
    private Food food;

    public Amagotchi(String pName, String pType, Context ctx)
    {
        context = ctx;
        init(pName, pType);
        Amagotchi.instance = this;
    }

    //Setzt die Instanz
    public static void setInstance(Amagotchi pInstance)
    {
        Amagotchi.instance = pInstance;
    }

    //Gibt die Instanz oder null zurück
    public static Amagotchi getState() {

        if(Amagotchi.instance == null)
            return null;
        else
            return Amagotchi.instance;

    }

    public static void resetAmagotchi(String pName, String pType)
    {
        Amagotchi.instance = new Amagotchi(pName, pType, context);
    }

    public static void setContext(Context context)
    {
        Amagotchi.context = context;
    }

    private void init(String pName, String pType)
    {
        TypedValue tempValue = new TypedValue();
        saveTimestamp = 0L;

        name = pName;
        type = pType;
        mutation = "0";
        health = context.getResources().getInteger(R.integer.startValueHealth);
        repletion = context.getResources().getInteger(R.integer.startValueRepletion);
        sleep = context.getResources().getInteger(R.integer.startValueSleep);
        motivation = context.getResources().getInteger(R.integer.startValueMotivation);
        happiness = context.getResources().getInteger(R.integer.startValueHappiness);
        fitness = context.getResources().getInteger(R.integer.startValueFitness);
        attention = context.getResources().getInteger(R.integer.startValueAttention);
        age = context.getResources().getInteger(R.integer.ageStandardValue);

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

        context.getResources().getValue(R.dimen.weightStandardValue, tempValue, true);
        weight = tempValue.getFloat();

        timeToHatch = context.getResources().getInteger(R.integer.startValuetimeToHatch);

        isSickInfection = false;
        isSickOvereating = false;
        feces = false;
        fecesCountdown = 0;
        isDead = false;

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

    public void healAmagotchi()
    {
        if (isSickInfection == true)
        {
            if (feces == false)
                isSickInfection = false;
        }

        else if (isSickOvereating == true)
        {
            if (repletion <= 100)
                isSickOvereating = false;
        }

        if(health > 100)
            health = 100;
        else
            health += 40;

        if(happiness < 35)
            happiness = 0;
        else
            happiness += -35;

        if(attention > 97)
            attention = 100;
        else
            attention += 3;
    }

    public void checkBeforeUpdate(String id, double valueToAdd)
    {
        int[] borders;
        double oldValue;
        double newValue;
        boolean bordersAvailable;

        switch (id)
        {
            case "Repletion":
                borders = context.getResources().getIntArray(R.array.repletionBorders);
                oldValue = getRepletion();
                bordersAvailable = true;
                break;
            case "Sleep":
                borders = context.getResources().getIntArray(R.array.sleepBorders);
                oldValue = getSleep();
                bordersAvailable = true;
                break;
            case "Fitness":
                borders = context.getResources().getIntArray(R.array.fitnessBorders);
                oldValue = getFitness();
                bordersAvailable = true;
                break;
            case "Attention":
                borders = context.getResources().getIntArray(R.array.attentionBorders);
                oldValue = getAttention();
                bordersAvailable = true;
                break;
            case "Health":
                borders = null;
                oldValue =getHealth();
                bordersAvailable = false;
                break;
            case "Motivation":
                borders = null;
                oldValue =getMotivation();
                bordersAvailable = false;
                break;
            case "Happiness":
                borders = null;
                oldValue =getHappiness();
                bordersAvailable = false;
                break;
            default:
                borders = null;
                oldValue = -1;
                bordersAvailable = false;
        }

        if (bordersAvailable)
        {
            int borderCounterOld = 0;
            int borderCounterNew = 0;
            int multiplier = 1;

            for (int i = 0; i < borders.length; i++)
            {
                if (oldValue < borders[i])
                    borderCounterOld ++;
                if (oldValue + valueToAdd < borders[i])
                    borderCounterNew ++;

                if (borderCounterNew != borderCounterOld)
                {
                    if (borderCounterNew == 1)
                        multiplier = -1;

                    switch (id)
                    {
                        case "Repletion":
                            if (i == 0)
                            {
                                motivationPerPeriod += multiplier * 0.125;
                            }
                            else if (i == 1)
                            {
                                healthPerPeriod += multiplier * 0.17;
                                happinessPerPeriod += multiplier * 0.17;
                            }
                            else if (i == 2)
                            {
                                healthPerPeriod += multiplier * 0.34;
                                checkBeforeUpdate("Attention",  multiplier * 25);
                            }
                            break;
                        case "Sleep":
                            if (i == 0)
                            {
                                happinessPerPeriod += multiplier * 0.17;
                                checkBeforeUpdate("Attention",  multiplier * 25);
                            }
                            else if (i == 1)
                            {
                                healthPerPeriod += multiplier * 0.17;
                            }
                            else if (i == 2)
                            {
                                healthPerPeriod += multiplier * 0.34;
                            }
                            break;
                        case "Fitness":
                            if (i == 0)
                            {
                                healthPerPeriod += multiplier * 0.042;
                            }
                            else if (i == 1)
                            {
                                healthPerPeriod += multiplier * 0.021;
                            }
                            break;
                        case "Attention":
                            if (i == 0)
                            {
                                checkBeforeUpdate("Happiness",  multiplier * 5);
                            }
                            else if (i == 1)
                            {
                                checkBeforeUpdate("Happiness",  multiplier * 10);
                            }
                            else if (i == 2)
                            {
                                checkBeforeUpdate("Happiness",  multiplier * 20);
                            }
                            break;
                    }
                }
            }
        }

        newValue = oldValue + valueToAdd;
        if (newValue < 0)
            newValue = 0;
        else if (newValue > 100 && id != "Repletion")
            newValue = 100;

        switch (id)
        {
            case "Repletion":
                repletion = newValue;
                break;
            case "Sleep":
                sleep = newValue;
                break;
            case "Fitness":
                fitness = newValue;
                break;
            case "Attention":
                attention = newValue;
                break;
            case "Health":
                health = newValue;
                break;
            case "Motivation":
                motivation = newValue;
                break;
            case "Happiness":
                happiness = newValue;
                break;
            default:
        }
    }

    // Muss noch umgesetzt werden
    public void developAmagotchi()
    {

    }

    //Muss noch umgesetzt werden
    public void ripAmagotchi()
    {
        isDead = true;
    }

    public boolean getFeces() { return feces; }

    public void setFeces(boolean feces) { this.feces = feces; }

    public Boolean getIsSickInfection() { return isSickInfection; }

    public void setIsSickInfection(Boolean val) { this.isSickInfection = val; }

    public Boolean getIsSickOveraeting() { return isSickOvereating; }

    public void setIsSickOveraeting(Boolean val) { this.isSickOvereating = val; }

    public boolean getIsDead() { return isDead; }

    public void setisDead(boolean isDead) { this.isDead = isDead; }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getMutation()
    {
        return mutation;
    }

    public void setMutation(String mutation)
    {
        this.mutation = mutation;
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

    public int getDevelopmentPoints()
    {
        return developmentPoints;
    }

    public void setDevelopmentPoints(int developmentPoints)
    {
        this.developmentPoints = developmentPoints;
    }

    public int getTimeToHatch()
    {
        return timeToHatch;
    }

    public void setTimeToHatch(int timeToHatch)
    {
        this.timeToHatch = timeToHatch;
    }

    public int getFecesCountdown()
    {
        return fecesCountdown;
    }

    public void setFecesCountdown(int fecesCountdown)
    {
        this.fecesCountdown = fecesCountdown;
    }

    public double getAge()
    {
        return age;
    }

    public void setAge(int age)
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

        String saveString = "";

        if(DEST_SAVE_VERSION.equals("1"))
        {
            saveString = DEST_SAVE_VERSION + "," + System.currentTimeMillis()/60000;

            saveString += "," + name;
            saveString += "," + type;
            saveString += "," + mutation;

            saveString += "," + level;
            saveString += "," + developmentPoints;
            saveString += "," + timeToHatch;

            saveString += "," + isSickInfection;
            saveString += "," + isSickOvereating;
            saveString += "," + feces;
            saveString += "," + isDead;

            saveString += "," + health;
            saveString += "," + repletion;
            saveString += "," + sleep;
            saveString += "," + motivation;
            saveString += "," + happiness;
            saveString += "," + fitness;
            saveString += "," + attention;
            saveString += "," + age;
            saveString += "," + weight;

            saveString += "," + fecesCountdown;

            String hc = String.valueOf(saveString.hashCode()); // Hash
            saveString += "|" + hc;
        }

        return saveString;
    }

    public void loadSaveString(String load)
    {
        String[] hashSplit;
        Boolean hashCorrect;

        try
        {
            hashSplit = load.split("\\|");
            hashCorrect = String.valueOf(hashSplit[0].hashCode()).equals(hashSplit[1]);

        }
        catch(Exception e)
        {
            Log.v(LOG_TAG, "Loading Game failed while checking hash. Corrupt Gamefile ?");
            return;
        }

        if(hashCorrect)
        { //hashSplit[0] contains correct SaveString
            String[] valSplit = hashSplit[0].split(",");

            Log.v(LOG_TAG, "count: " + valSplit.length);

            if(valSplit[0].equals("1"))//Save version 1
            {
                saveTimestamp = Long.getLong(valSplit[1]);

                name = valSplit[2];
                type = valSplit[3];
                mutation = valSplit[4];

                level = Integer.parseInt(valSplit[5]);
                developmentPoints = Integer.parseInt(valSplit[6]);
                timeToHatch = Integer.parseInt(valSplit[7]);

                isSickInfection = Boolean.parseBoolean(valSplit[8]);
                isSickOvereating = Boolean.parseBoolean(valSplit[9]);
                feces = Boolean.parseBoolean(valSplit[10]);
                isDead = Boolean.parseBoolean(valSplit[11]);

                health = Double.parseDouble(valSplit[12]);
                repletion = Double.parseDouble(valSplit[13]);
                sleep = Double.parseDouble(valSplit[14]);
                motivation = Double.parseDouble(valSplit[15]);
                happiness = Double.parseDouble(valSplit[16]);
                fitness = Double.parseDouble(valSplit[17]);
                attention = Double.parseDouble(valSplit[18]);
                age = Integer.parseInt(valSplit[19]);
                weight = Double.parseDouble(valSplit[20]);

                fecesCountdown = Integer.parseInt(valSplit[21]);
            }

        }
        else
        {
            Log.v(LOG_TAG, "Checking hash failed. Corrupt Gamefile ?");
            return;
        }

    }
}

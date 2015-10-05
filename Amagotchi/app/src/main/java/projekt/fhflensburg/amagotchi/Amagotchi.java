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

    // Muss noch umgesetzt werden
    public void developAmagotchi()
    {

    }

    //Muss noch umgesetzt werden
    public void ripAmagotchi()
    {
        isdead = true;
    }

    public boolean getFeces() { return feces; }

    public void setFeces(boolean feces) { this.feces = feces; }

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
            saveString += "," + isdead;

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
                isdead = Boolean.parseBoolean(valSplit[11]);

                health = Double.parseDouble(valSplit[12]);
                health = Double.parseDouble(valSplit[13]);
                health = Double.parseDouble(valSplit[14]);
                health = Double.parseDouble(valSplit[15]);
                health = Double.parseDouble(valSplit[16]);
                health = Double.parseDouble(valSplit[17]);
                health = Double.parseDouble(valSplit[18]);
                health = Double.parseDouble(valSplit[19]);
                health = Double.parseDouble(valSplit[20]);

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

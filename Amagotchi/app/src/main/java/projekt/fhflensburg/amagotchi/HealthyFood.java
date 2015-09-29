package projekt.fhflensburg.amagotchi;

/**
 * Created by bunay_000 on 29.09.2015.
 */
public class HealthyFood implements Food
{
    private static HealthyFood instance;

    public double health;
    public double repletion;
    public double sleep;
    public double happiness;
    public double fitness;
    public double attention;

    private HealthyFood()
    {
        init();
    }

    public static HealthyFood getInstance()
    {
        if(HealthyFood.instance == null)
            HealthyFood.instance = new HealthyFood();

        return HealthyFood.instance;
    }

    private void init()
    {
        health = Amagotchi.context.getResources().getInteger(R.integer.healthyFoodHealth);
        repletion = Amagotchi.context.getResources().getInteger(R.integer.healthyFoodRepletion);
        sleep = Amagotchi.context.getResources().getInteger(R.integer.healthyFoodSleep);
        happiness = Amagotchi.context.getResources().getInteger(R.integer.healthyFoodHappiness);
        fitness = Amagotchi.context.getResources().getInteger(R.integer.healthyFoodFitness);
        attention = Amagotchi.context.getResources().getInteger(R.integer.healthyFoodAttention);
    }

    @Override
    public void feed(Amagotchi obj)
    {
        obj.setHealth(obj.getHealth() + health);
        obj.setRepletion(obj.getRepletion() + repletion);
        obj.setHappiness(obj.getHappiness() + happiness);
        obj.setFitness(obj.getFitness() + fitness);
        obj.setAttention(obj.getAttention() + attention);

        if(obj.getRepletion() > 100)
            obj.setSleep(obj.getSleep() + sleep);
    }
}

package projekt.fhflensburg.amagotchi;


public class SolidFood implements Food
{
    private static SolidFood instance;

    public double repletion;
    public double sleep;
    public double happiness;
    public double fitness;
    public double attention;

    private SolidFood()
    {
        init();
    }

    public static SolidFood getInstance()
    {
        if(SolidFood.instance == null)
            SolidFood.instance = new SolidFood();

        return SolidFood.instance;
    }

    private void init()
    {
        repletion = Amagotchi.context.getResources().getInteger(R.integer.solidFoodRepletion);
        sleep = Amagotchi.context.getResources().getInteger(R.integer.solidFoodSleep);
        happiness = Amagotchi.context.getResources().getInteger(R.integer.solidFoodHappiness);
        fitness = Amagotchi.context.getResources().getInteger(R.integer.solidFoodFitness);
        attention = Amagotchi.context.getResources().getInteger(R.integer.solidFoodAttention);
    }

    @Override
    public void feed(Amagotchi obj)
    {
        obj.setRepletion(obj.getRepletion() + repletion);
        obj.setHappiness(obj.getHappiness() + happiness);
        obj.setFitness(obj.getFitness() + fitness);
        obj.setAttention(obj.getAttention() + attention);

        if(obj.getRepletion() > 100)
            obj.setSleep(obj.getSleep() + sleep);
    }
}

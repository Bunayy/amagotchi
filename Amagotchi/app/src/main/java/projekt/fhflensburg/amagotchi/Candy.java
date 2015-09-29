package projekt.fhflensburg.amagotchi;

/**
 * Created by bunay_000 on 29.09.2015.
 */
public class Candy implements Food
{
    private static Candy instance;

    public double repletion;
    public double sleep;
    public double motivation;
    public double happiness;
    public double fitness;
    public double attention;

    private Candy()
    {
        init();
    }

    public static Candy getInstance()
    {
        if(Candy.instance == null)
            Candy.instance = new Candy();

        return Candy.instance;
    }

    private void init()
    {
        repletion = Amagotchi.context.getResources().getInteger(R.integer.candyRepletion);
        sleep = Amagotchi.context.getResources().getInteger(R.integer.candySleep);
        motivation = Amagotchi.context.getResources().getInteger(R.integer.candyMotivation);
        happiness = Amagotchi.context.getResources().getInteger(R.integer.candyHappiness);
        fitness = Amagotchi.context.getResources().getInteger(R.integer.candyFitness);
        attention = Amagotchi.context.getResources().getInteger(R.integer.candyAttention);
    }

    @Override
    public void feed(Amagotchi obj)
    {
        obj.setRepletion(obj.getRepletion() + repletion);
        obj.setMotivation(obj.getMotivation() + motivation);
        obj.setHappiness(obj.getHappiness() + happiness);
        obj.setFitness(obj.getFitness() + fitness);
        obj.setAttention(obj.getAttention() + attention);

        if(obj.getRepletion() > 100)
            obj.setSleep(obj.getSleep() + sleep);
    }
}

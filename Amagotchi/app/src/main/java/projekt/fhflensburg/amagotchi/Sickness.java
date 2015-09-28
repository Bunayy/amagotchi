package projekt.fhflensburg.amagotchi;

/**
 * Created by bunay_000 on 28.09.2015.
 */
public abstract class Sickness
{
    public String name;
    public double initialHealthLoss;
    public double initialMotivationLoss;
    public double initialAttentionLoss;

    public double periodicHealthLoss;
    public double periodicMotivationLoss;
    public double periodicHappinessLoss;

    public Sickness(){}

    private void init(){}
}

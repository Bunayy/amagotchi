package projekt.fhflensburg.amagotchi;


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

    public void breakOut(Amagotchi obj){}
}

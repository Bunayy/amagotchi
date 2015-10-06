package projekt.fhflensburg.amagotchi;

import android.util.TypedValue;


public class Infection extends Sickness
{
    private static Infection instance;

    private Infection()
    {
        super();
        init();
    }

    public static Sickness getInstance()
    {
        if(Infection.instance == null)
            Infection.instance = new Infection();

        return Infection.instance;
    }

    private void init()
    {
        TypedValue tempValue = new TypedValue();

        initialHealthLoss = Amagotchi.context.getResources().getInteger(R.integer.infectionInitialHealthLoss);
        initialMotivationLoss = Amagotchi.context.getResources().getInteger(R.integer.infectionInitialMotivationLoss);
        initialAttentionLoss = Amagotchi.context.getResources().getInteger(R.integer.infectionInitialAttentionLoss);

        Amagotchi.context.getResources().getValue(R.dimen.infectionPeriodicHealthLoss, tempValue, true);
        periodicHealthLoss = tempValue.getFloat();

        Amagotchi.context.getResources().getValue(R.dimen.infectionPeriodicMotivationLoss, tempValue, true);
        periodicMotivationLoss = tempValue.getFloat();

        Amagotchi.context.getResources().getValue(R.dimen.infectionPeriodicHappinessLoss, tempValue, true);
        periodicHappinessLoss = tempValue.getFloat();
    }

    public void breakOut(Amagotchi obj)
    {
        obj.setHealth(obj.getHealth() + initialHealthLoss);
        obj.setMotivation(obj.getMotivation() + initialMotivationLoss);
        obj.setAttention(obj.getAttention() + initialAttentionLoss);
    }
}

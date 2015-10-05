package projekt.fhflensburg.amagotchi;

import android.util.TypedValue;

/**
 * Created by bunay_000 on 28.09.2015.
 */
public class Overeating extends Sickness
{
    private static Overeating instance;

    private Overeating()
    {
        super();
        init();
    }

    public static Sickness getInstance()
    {
        if(Overeating.instance == null)
            Overeating.instance = new Overeating();

        return Overeating.instance;
    }

    private void init()
    {
        TypedValue tempValue = new TypedValue();

        initialHealthLoss = Amagotchi.context.getResources().getInteger(R.integer.overeatingInitialHealthLoss);
        initialMotivationLoss = Amagotchi.context.getResources().getInteger(R.integer.overeatingInitialMotivationLoss);
        initialAttentionLoss = Amagotchi.context.getResources().getInteger(R.integer.overeatingInitialAttentionLoss);

        Amagotchi.context.getResources().getValue(R.dimen.overeatingPeriodicHealthLoss, tempValue, true);
        periodicHealthLoss = tempValue.getFloat();

        Amagotchi.context.getResources().getValue(R.dimen.overeatingPeriodicMotivationLoss, tempValue, true);
        periodicMotivationLoss = tempValue.getFloat();

        Amagotchi.context.getResources().getValue(R.dimen.overeatingPeriodicHappinessLoss, tempValue, true);
        periodicHappinessLoss = tempValue.getFloat();
    }

    public void breakOut(Amagotchi obj)
    {
        obj.setHealth(obj.getHealth() + initialHealthLoss);
        obj.setMotivation(obj.getMotivation() + initialMotivationLoss);
        obj.setAttention(obj.getAttention() + initialAttentionLoss);
    }
}

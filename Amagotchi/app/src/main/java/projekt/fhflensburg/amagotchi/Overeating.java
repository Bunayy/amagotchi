package projekt.fhflensburg.amagotchi;

import android.util.TypedValue;

/**
 * Created by bunay_000 on 28.09.2015.
 */
public class Overeating extends Sickness
{
    public Overeating()
    {
        super();
        init();
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
}

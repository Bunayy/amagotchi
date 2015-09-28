package projekt.fhflensburg.amagotchi;

import android.util.TypedValue;

/**
 * Created by bunay_000 on 28.09.2015.
 */
public class Infection extends Sickness
{
    public Infection()
    {
        super();
        init();
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
}

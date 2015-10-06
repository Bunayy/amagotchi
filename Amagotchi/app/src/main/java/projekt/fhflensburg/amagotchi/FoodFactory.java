package projekt.fhflensburg.amagotchi;


public class FoodFactory
{
    public Food getFood(String foodType)
    {
        Food tempFood;
        switch (foodType)
        {
            case "Healthy":
                tempFood = HealthyFood.getInstance();
                break;
            case "Solid":
                tempFood = SolidFood.getInstance();
                break;
            case "Candy":
                tempFood = Candy.getInstance();
                break;
            default:
                tempFood = null;
        }
        return tempFood;
    }
}

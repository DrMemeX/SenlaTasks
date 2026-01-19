package task3_3.tank;

import task3_3.interfaces.ILineStep;
import task3_3.interfaces.IProductPart;

public class BodyStep implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println("Создаётся корпус танка...");
        return new TankBody();
    }
}

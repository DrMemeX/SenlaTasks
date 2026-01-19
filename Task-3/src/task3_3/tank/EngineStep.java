package task3_3.tank;

import task3_3.interfaces.ILineStep;
import task3_3.interfaces.IProductPart;

public class EngineStep implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println("Создаётся двигатель танка...");
        return new TankEngine();
    }
}

package task3_3.tank;

import task3_3.interfaces.ILineStep;
import task3_3.interfaces.IProductPart;

public class TurretStep implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println("Создаётся башня танка...");
        return new TankTurret();
    }
}

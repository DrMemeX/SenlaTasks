package task3_3.tank;

import task3_3.interfaces.IAssemblyLine;
import task3_3.interfaces.ILineStep;
import task3_3.interfaces.IProduct;
import task3_3.interfaces.IProductPart;

public class TankAssemblyLine implements IAssemblyLine {
    private final ILineStep bodyStep;
    private final ILineStep engineStep;
    private final ILineStep turretStep;

    public  TankAssemblyLine(ILineStep bodyStep, ILineStep engineStep, ILineStep turretStep) {
        this.bodyStep = bodyStep;
        this.engineStep = engineStep;
        this.turretStep = turretStep;
    }

    @Override
    public IProduct assembleProduct(IProduct product) {
        System.out.println("Начата сборка танка!");
        product.installFirstPart(bodyStep.buildProductPart());
        product.installSecondPart(engineStep.buildProductPart());
        product.installThirdPart(turretStep.buildProductPart());
        System.out.println("Сборка танка завершена!");
        return product;
    }
}

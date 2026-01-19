package task3_3;

import task3_3.interfaces.IAssemblyLine;
import task3_3.interfaces.ILineStep;
import task3_3.interfaces.IProduct;
import task3_3.tank.*;

public class Main {
    public static void main(String[] args) {
        ILineStep body = new BodyStep();
        ILineStep engine = new EngineStep();
        ILineStep turret = new TurretStep();

        IAssemblyLine line = new TankAssemblyLine(body, engine, turret);
        IProduct tank = new Tank();

        line.assembleProduct(tank);

        System.out.println("Результат сборки: " + tank);
    }
}

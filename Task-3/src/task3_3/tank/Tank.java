package task3_3.tank;

import task3_3.interfaces.IProduct;
import task3_3.interfaces.IProductPart;

public class Tank implements IProduct {
    private IProductPart body;
    private IProductPart engine;
    private IProductPart turret;

    @Override
    public void installFirstPart(IProductPart part) {
        this.body = part;
        System.out.println("Установлен " + part);
    }

    @Override
    public void installSecondPart(IProductPart part) {
        this.engine = part;
        System.out.println("Установлен " + part);
    }

    @Override
    public  void installThirdPart(IProductPart part) {
        this.turret = part;
        System.out.println("Установлена " + part);
    }

    @Override
    public String toString() {
        return "Танк (" + body + ", " + engine + ", " + turret + ").";
    }
}

package task3_1;

public class Main {
    public static void main(String[] args) {
       int num = 100 + (new java.util.Random()).nextInt(900);

       int hundreds = num/100;
       int tens = (num/10) % 10;
       int ones = num % 10;

       int sum = hundreds + tens + ones;
        System.out.println("Случайное трёхзначное число: " + num);
        System.out.println("Сумма цифр этого числа: " + sum);
    }
}

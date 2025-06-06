package hello.core.singleton;

public class StatefulService {

    private int price; // 상태 유지 필드

    public void order(String name, int price) {
        System.out.println("name = " + name + ", price = " + price);
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}

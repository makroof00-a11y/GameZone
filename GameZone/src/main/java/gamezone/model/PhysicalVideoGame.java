package gamezone.model;

import gamezone.interfaces.Displayable;
import gamezone.interfaces.Sellable;

public class PhysicalVideoGame extends VideoGame implements Sellable, Displayable {

    private String condition;    // "nuevo" | "usado"
    private String distributor;


    public PhysicalVideoGame(String title, double price, String platform,
                             int stock, String genre,
                             String condition, String distributor) {
        super(title, price, platform, stock, genre);
        this.condition   = condition;
        this.distributor = distributor;
    }


    public String getCondition()   { return condition; }
    public String getDistributor() { return distributor; }


    public void setCondition(String condition)     { this.condition = condition; }
    public void setDistributor(String distributor) { this.distributor = distributor; }


    @Override
    public double calculateFinalPrice() {
        if ("usado".equalsIgnoreCase(condition)) {
            return price * 0.75;
        }
        return price;
    }


    @Override
    public double sell(int qty) {
        stock -= qty;
        return calculateFinalPrice() * qty;
    }

    @Override
    public String getDisplayInfo() {
        return String.format(
            "=== VIDEOJUEGO FÍSICO ===\n" +
            "Título       : %s\n" +
            "Precio base  : $%.2f\n" +
            "Precio final : $%.2f\n" +
            "Plataforma   : %s\n" +
            "Género       : %s\n" +
            "Condición    : %s\n" +
            "Distribuidor : %s\n" +
            "Stock        : %d",
            title, price, calculateFinalPrice(),
            platform, genre, condition, distributor, stock);
    }

    @Override
    public Object[] toTableRow() {
        return new Object[]{
            title,
            String.format("$%.2f", calculateFinalPrice()),
            platform,
            genre,
            stock,
            condition,
            distributor,
            "Físico"
        };
    }

    @Override
    public String toString() {
        return String.format(
            "PhysicalVideoGame[title='%s', price=%.2f, finalPrice=%.2f, platform='%s'," +
            " genre='%s', stock=%d, condition='%s', distributor='%s']",
            title, price, calculateFinalPrice(),
            platform, genre, stock, condition, distributor);
    }
}

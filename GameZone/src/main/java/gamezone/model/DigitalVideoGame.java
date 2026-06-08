package gamezone.model;

import gamezone.interfaces.Displayable;
import gamezone.interfaces.Sellable;

public class DigitalVideoGame extends VideoGame implements Sellable, Displayable {

    private double sizeGB;
    private String downloadPlatform;


    public DigitalVideoGame(String title, double price, String platform,
                            int stock, String genre,
                            double sizeGB, String downloadPlatform) {
        super(title, price, platform, stock, genre);
        this.sizeGB           = sizeGB;
        this.downloadPlatform = downloadPlatform;
    }

    public double getSizeGB()           { return sizeGB; }
    public String getDownloadPlatform() { return downloadPlatform; }


    public void setSizeGB(double sizeGB)                   { this.sizeGB = sizeGB; }
    public void setDownloadPlatform(String downloadPlatform) { this.downloadPlatform = downloadPlatform; }


    @Override
    public double calculateFinalPrice() {
        return sizeGB > 50 ? price + 5000 : price;
    }


    @Override
    public double sell(int qty) {
        stock -= qty;
        return calculateFinalPrice() * qty;
    }

    @Override
    public String getDisplayInfo() {
        return String.format(
            "=== VIDEOJUEGO DIGITAL ===\n" +
            "Título       : %s\n" +
            "Precio base  : $%.2f\n" +
            "Precio final : $%.2f\n" +
            "Plataforma   : %s\n" +
            "Descarga     : %s\n" +
            "Género       : %s\n" +
            "Stock        : %d\n" +
            "Tamaño (GB)  : %.1f",
            title, price, calculateFinalPrice(),
            platform, downloadPlatform, genre, stock, sizeGB);
    }

    @Override
    public Object[] toTableRow() {
        return new Object[]{
            title,
            String.format("$%.2f", calculateFinalPrice()),
            platform,
            genre,
            stock,
            String.format("%.1f GB", sizeGB),
            downloadPlatform,
            "Digital"
        };
    }

    @Override
    public String toString() {
        return String.format(
            "DigitalVideoGame[title='%s', price=%.2f, finalPrice=%.2f, platform='%s'," +
            " genre='%s', stock=%d, sizeGB=%.1f, downloadPlatform='%s']",
            title, price, calculateFinalPrice(),
            platform, genre, stock, sizeGB, downloadPlatform);
    }
}

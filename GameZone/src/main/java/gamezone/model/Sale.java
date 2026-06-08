package gamezone.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sale {

    private String         id;
    private VideoGame      videoGame;
    private int            quantity;
    private double         unitPrice;
    private double         total;
    private LocalDateTime  saleDate;

    // Constructor completo
    public Sale(String id, VideoGame videoGame, int quantity, double unitPrice) {
        this.id        = id;
        this.videoGame = videoGame;
        this.quantity  = quantity;
        this.unitPrice = unitPrice;
        this.total     = unitPrice * quantity;
        this.saleDate  = LocalDateTime.now();
    }

    // Getters
    public String        getId()        { return id; }
    public VideoGame     getVideoGame() { return videoGame; }
    public int           getQuantity()  { return quantity; }
    public double        getUnitPrice() { return unitPrice; }
    public double        getTotal()     { return total; }
    public LocalDateTime getSaleDate()  { return saleDate; }

    // Setters
    public void setId(String id)               { this.id = id; }
    public void setVideoGame(VideoGame vg)     { this.videoGame = vg; }
    public void setQuantity(int quantity)      { this.quantity = quantity; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public void setTotal(double total)         { this.total = total; }
    public void setSaleDate(LocalDateTime d)   { this.saleDate = d; }

    public Object[] toTableRow() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return new Object[]{
            id,
            videoGame.getTitle(),
            quantity,
            String.format("$%.2f", unitPrice),
            String.format("$%.2f", total),
            saleDate.format(fmt)
        };
    }

    @Override
    public String toString() {
        return String.format(
            "Sale[id='%s', game='%s', qty=%d, unitPrice=%.2f, total=%.2f, date=%s]",
            id, videoGame.getTitle(), quantity, unitPrice, total,
            saleDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }
}

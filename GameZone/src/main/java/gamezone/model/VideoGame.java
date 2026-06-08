package gamezone.model;

public abstract class VideoGame {

    protected String title;
    protected double price;
    protected String platform;
    protected int stock;
    protected String genre;


    public VideoGame(String title, double price, String platform, int stock, String genre) {
        this.title    = title;
        this.price    = price;
        this.platform = platform;
        this.stock    = stock;
        this.genre    = genre;
    }


    public String getTitle()    { return title; }
    public double getPrice()    { return price; }
    public String getPlatform() { return platform; }
    public int    getStock()    { return stock; }
    public String getGenre()    { return genre; }


    public void setTitle(String title)       { this.title = title; }
    public void setPrice(double price)       { this.price = price; }
    public void setPlatform(String platform) { this.platform = platform; }
    public void setStock(int stock)          { this.stock = stock; }
    public void setGenre(String genre)       { this.genre = genre; }

    // Metodo abstracto
    public abstract double calculateFinalPrice();

    @Override
    public String toString() {
        return String.format("VideoGame[title='%s', price=%.2f, platform='%s', stock=%d, genre='%s']",
                title, price, platform, stock, genre);
    }
}

package gamezone.service;

import gamezone.model.Sale;
import gamezone.model.VideoGame;
import gamezone.repository.SaleRepository;
import gamezone.repository.VideoGameRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class VideoGameService {

    private final VideoGameRepository gameRepo;
    private final SaleRepository      saleRepo;

    public VideoGameService() {
        this.gameRepo = new VideoGameRepository();
        this.saleRepo = new SaleRepository(gameRepo);
    }


    public void agregarVideojuego(VideoGame game) {
        // Validaciones
        if (game.getTitle() == null || game.getTitle().isBlank()) {
            throw new IllegalArgumentException("El título no puede ser nulo ni vacío.");
        }
        if (game.getPrice() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0.");
        }
        if (game.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        // Delega al repositorio (que verifica duplicados)
        gameRepo.create(game);
    }


    public List<VideoGame> listarTodos() {
        return gameRepo.findAll();
    }


    public VideoGame buscarPorTitulo(String title) {
        return gameRepo.findByTitle(title);
    }


    public List<VideoGame> buscarPorPlataforma(String platform) {
        return gameRepo.findByPlatform(platform);
    }


    public boolean actualizarVideojuego(String title, VideoGame updated) {
        if (gameRepo.findByTitle(title) == null) return false;
        return gameRepo.update(title, updated);
    }


    public boolean eliminarVideojuego(String title) {
        return gameRepo.delete(title);
    }


    //  Ventas


    /**
     * Realiza la venta de un videojuego.
     * @return Sale con todos los datos si fue exitosa.
     * @throws IllegalArgumentException si no existe o no hay stock.
     */
    public Sale venderVideojuego(String title, int qty) {
        VideoGame game = gameRepo.findByTitle(title);
        if (game == null) {
            throw new IllegalArgumentException("El juego '" + title + "' no existe en el catálogo.");
        }
        if (game.getStock() < qty) {
            throw new IllegalArgumentException(
                "Stock insuficiente. Disponible: " + game.getStock() + ", solicitado: " + qty);
        }

        double unitPrice = game.calculateFinalPrice();

        game.setStock(game.getStock() - qty);
        gameRepo.update(title, game);


        String saleId = "VTA-" + System.currentTimeMillis();
        Sale sale = new Sale(saleId, game, qty, unitPrice);
        saleRepo.save(sale);
        return sale;
    }


    public List<Sale> listarVentas() {
        return saleRepo.findAll();
    }
}

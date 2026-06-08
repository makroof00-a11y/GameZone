package gamezone.repository;

import gamezone.model.DigitalVideoGame;
import gamezone.model.PhysicalVideoGame;
import gamezone.model.VideoGame;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistencia CRUD en archivo JSON sin dependencias externas.
 * Usa un parser JSON manual simple para mantener el proyecto liviano.
 */
public class VideoGameRepository {

    private static final String FILE_PATH = "data/videogames.json";

    public VideoGameRepository() {
        // Crear directorio y archivo si no existen
        try {
            Files.createDirectories(Paths.get("data"));
            File f = new File(FILE_PATH);
            if (!f.exists()) {
                Files.writeString(Paths.get(FILE_PATH), "[]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ─────────────────────────────────────────────
    //  CRUD
    // ─────────────────────────────────────────────

    /** CREATE – lanza IllegalArgumentException si el título ya existe. */
    public void create(VideoGame game) {
        List<VideoGame> list = findAll();
        for (VideoGame vg : list) {
            if (vg.getTitle().equalsIgnoreCase(game.getTitle())) {
                throw new IllegalArgumentException("El videojuego ya existe en el catálogo");
            }
        }
        list.add(game);
        saveAll(list);
    }

    /** READ ALL */
    public List<VideoGame> findAll() {
        try {
            String json = Files.readString(Paths.get(FILE_PATH)).trim();
            return parseJsonArray(json);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    /** READ – buscar por título (case-insensitive). */
    public VideoGame findByTitle(String title) {
        for (VideoGame vg : findAll()) {
            if (vg.getTitle().equalsIgnoreCase(title)) {
                return vg;
            }
        }
        return null;
    }

    /** READ – buscar por plataforma (case-insensitive). */
    public List<VideoGame> findByPlatform(String platform) {
        List<VideoGame> result = new ArrayList<>();
        for (VideoGame vg : findAll()) {
            if (vg.getPlatform().equalsIgnoreCase(platform)) {
                result.add(vg);
            }
        }
        return result.isEmpty() ? null : result;
    }

    /** UPDATE – reemplaza el juego con el mismo título. */
    public boolean update(String title, VideoGame updated) {
        List<VideoGame> list = findAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTitle().equalsIgnoreCase(title)) {
                list.set(i, updated);
                saveAll(list);
                return true;
            }
        }
        return false;
    }

    /** DELETE – elimina el juego por título. */
    public boolean delete(String title) {
        List<VideoGame> list = findAll();
        boolean removed = list.removeIf(vg -> vg.getTitle().equalsIgnoreCase(title));
        if (removed) saveAll(list);
        return removed;
    }

    // ─────────────────────────────────────────────
    //  Serialización JSON manual
    // ─────────────────────────────────────────────

    private void saveAll(List<VideoGame> list) {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < list.size(); i++) {
            sb.append(toJson(list.get(i)));
            if (i < list.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        try {
            Files.writeString(Paths.get(FILE_PATH), sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toJson(VideoGame vg) {
        if (vg instanceof DigitalVideoGame d) {
            return String.format(
                "  {\"type\":\"digital\",\"title\":\"%s\",\"price\":%.2f," +
                "\"platform\":\"%s\",\"stock\":%d,\"genre\":\"%s\"," +
                "\"sizeGB\":%.2f,\"downloadPlatform\":\"%s\"}",
                esc(d.getTitle()), d.getPrice(), esc(d.getPlatform()),
                d.getStock(), esc(d.getGenre()),
                d.getSizeGB(), esc(d.getDownloadPlatform()));
        } else {
            PhysicalVideoGame p = (PhysicalVideoGame) vg;
            return String.format(
                "  {\"type\":\"physical\",\"title\":\"%s\",\"price\":%.2f," +
                "\"platform\":\"%s\",\"stock\":%d,\"genre\":\"%s\"," +
                "\"condition\":\"%s\",\"distributor\":\"%s\"}",
                esc(p.getTitle()), p.getPrice(), esc(p.getPlatform()),
                p.getStock(), esc(p.getGenre()),
                esc(p.getCondition()), esc(p.getDistributor()));
        }
    }

    /** Parsea el arreglo JSON sin librerías externas. */
    private List<VideoGame> parseJsonArray(String json) {
        List<VideoGame> list = new ArrayList<>();
        if (json == null || json.equals("[]") || json.isBlank()) return list;

        // Dividir en objetos individuales
        int depth = 0;
        int start = -1;
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') {
                if (depth == 0) start = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && start != -1) {
                    String obj = json.substring(start, i + 1);
                    VideoGame vg = parseJsonObject(obj);
                    if (vg != null) list.add(vg);
                    start = -1;
                }
            }
        }
        return list;
    }

    private VideoGame parseJsonObject(String obj) {
        String type = extractString(obj, "type");
        String title = extractString(obj, "title");
        double price = extractDouble(obj, "price");
        String platform = extractString(obj, "platform");
        int stock = extractInt(obj, "stock");
        String genre = extractString(obj, "genre");

        if ("digital".equals(type)) {
            double sizeGB = extractDouble(obj, "sizeGB");
            String dlPlatform = extractString(obj, "downloadPlatform");
            return new DigitalVideoGame(title, price, platform, stock, genre, sizeGB, dlPlatform);
        } else if ("physical".equals(type)) {
            String condition = extractString(obj, "condition");
            String distributor = extractString(obj, "distributor");
            return new PhysicalVideoGame(title, price, platform, stock, genre, condition, distributor);
        }
        return null;
    }

    // ── Helpers de extracción ──

    private String extractString(String json, String key) {
        String pattern = "\"" + key + "\":\"";
        int idx = json.indexOf(pattern);
        if (idx == -1) return "";
        int start = idx + pattern.length();
        int end = json.indexOf("\"", start);
        return end == -1 ? "" : json.substring(start, end);
    }

    private double extractDouble(String json, String key) {
        String pattern = "\"" + key + "\":";
        int idx = json.indexOf(pattern);
        if (idx == -1) return 0;
        int start = idx + pattern.length();
        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '.')) end++;
        try { return Double.parseDouble(json.substring(start, end)); } catch (Exception e) { return 0; }
    }

    private int extractInt(String json, String key) {
        return (int) extractDouble(json, key);
    }

    private String esc(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }
}

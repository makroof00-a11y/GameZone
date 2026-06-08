package gamezone.repository;

import gamezone.model.Sale;
import gamezone.model.VideoGame;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SaleRepository {

    private static final String FILE_PATH = "data/sales.json";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final VideoGameRepository videoGameRepo;

    public SaleRepository(VideoGameRepository videoGameRepo) {
        this.videoGameRepo = videoGameRepo;
        try {
            Files.createDirectories(Paths.get("data"));
            File f = new File(FILE_PATH);
            if (!f.exists()) Files.writeString(Paths.get(FILE_PATH), "[]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(Sale sale) {
        List<Sale> list = findAll();
        list.add(sale);
        saveAll(list);
    }

    public List<Sale> findAll() {
        try {
            String json = Files.readString(Paths.get(FILE_PATH)).trim();
            return parseJsonArray(json);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void saveAll(List<Sale> list) {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < list.size(); i++) {
            sb.append(toJson(list.get(i)));
            if (i < list.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        try {
            Files.writeString(Paths.get(FILE_PATH), sb.toString());
        } catch (IOException e) { e.printStackTrace(); }
    }

    private String toJson(Sale s) {
        return String.format(
            "  {\"id\":\"%s\",\"gameTitle\":\"%s\",\"quantity\":%d," +
            "\"unitPrice\":%.2f,\"total\":%.2f,\"saleDate\":\"%s\"}",
            s.getId(), esc(s.getVideoGame().getTitle()),
            s.getQuantity(), s.getUnitPrice(), s.getTotal(),
            s.getSaleDate().format(FMT));
    }

    private List<Sale> parseJsonArray(String json) {
        List<Sale> list = new ArrayList<>();
        if (json == null || json.equals("[]") || json.isBlank()) return list;
        int depth = 0, start = -1;
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') { if (depth == 0) start = i; depth++; }
            else if (c == '}') {
                depth--;
                if (depth == 0 && start != -1) {
                    Sale s = parseJsonObject(json.substring(start, i + 1));
                    if (s != null) list.add(s);
                    start = -1;
                }
            }
        }
        return list;
    }

    private Sale parseJsonObject(String obj) {
        String id        = extractString(obj, "id");
        String gameTitle = extractString(obj, "gameTitle");
        int    quantity  = extractInt(obj, "quantity");
        double unitPrice = extractDouble(obj, "unitPrice");
        double total     = extractDouble(obj, "total");
        String dateStr   = extractString(obj, "saleDate");

        VideoGame vg = videoGameRepo.findByTitle(gameTitle);
        if (vg == null) return null; // juego eliminado del catálogo

        Sale sale = new Sale(id, vg, quantity, unitPrice);
        sale.setTotal(total);
        if (!dateStr.isBlank()) {
            try { sale.setSaleDate(LocalDateTime.parse(dateStr, FMT)); }
            catch (Exception ignored) {}
        }
        return sale;
    }

    private String extractString(String json, String key) {
        String pattern = "\"" + key + "\":\"";
        int idx = json.indexOf(pattern);
        if (idx == -1) return "";
        int s = idx + pattern.length();
        int e = json.indexOf("\"", s);
        return e == -1 ? "" : json.substring(s, e);
    }

    private double extractDouble(String json, String key) {
        String pattern = "\"" + key + "\":";
        int idx = json.indexOf(pattern);
        if (idx == -1) return 0;
        int s = idx + pattern.length();
        int e = s;
        while (e < json.length() && (Character.isDigit(json.charAt(e)) || json.charAt(e) == '.')) e++;
        try { return Double.parseDouble(json.substring(s, e)); } catch (Exception ex) { return 0; }
    }

    private int extractInt(String json, String key) { return (int) extractDouble(json, key); }
    private String esc(String s) { return s == null ? "" : s.replace("\"", "\\\""); }
}

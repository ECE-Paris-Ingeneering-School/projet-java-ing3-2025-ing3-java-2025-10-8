package DAO;

import Modele.Image;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImageDAO {

    public List<Image> getImagesByHebergementId(int idHebergement) {
        List<Image> images = new ArrayList<>();

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM image WHERE id_hebergement = ?")) {

            stmt.setInt(1, idHebergement);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                images.add(new Image(
                        rs.getInt("id_image"),
                        rs.getInt("id_hebergement"),
                        rs.getString("url_image"),
                        rs.getString("description")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return images;
    }
}
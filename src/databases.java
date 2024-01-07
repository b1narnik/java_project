import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import java.util.List;
import java.io.File;

public class databases {

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Файл " + filePath + " успешно удален");
            } else {
                System.out.println("Не удалось удалить файл " + filePath);
            }
        } else {
            System.out.println("Файл " + filePath + " не существует");
        }
    }

    public static void creatorDB(List<Player> players) {
        //Подключение к базе данных, создание таблиц
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            System.out.println("База данных успешно подключена");

            Statement statement = conn.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS players (id INTEGER PRIMARY KEY, name TEXT, weight INTEGER, height INTEGER, age REAL)");
            statement.execute("CREATE TABLE IF NOT EXISTS player_info (player_id INTEGER PRIMARY KEY, team TEXT, position TEXT)");

            for (Player player : players){
                addPlayer(conn, player);
            }
            System.out.println("Таблицы успешно созданы");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    private static void addPlayer(Connection conn, Player player) {
        String insertPlayerSQL = "INSERT INTO players(name, weight, height, age) VALUES(?, ?, ?, ?)";
        String insertPlayerInfoSQL = "INSERT INTO player_info(player_id, team, position) VALUES(?, ?, ?)";

        try (PreparedStatement ps1 = conn.prepareStatement(insertPlayerSQL, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement ps2 = conn.prepareStatement(insertPlayerInfoSQL)) {
            // Вставка данных в таблицу players
            ps1.setString(1, player.getName());
            ps1.setInt(2, player.getWeight());
            ps1.setInt(3, player.getHeight());
            ps1.setDouble(4, player.getAge());
            int affectedRows = ps1.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Не удалось заполнить базу данных - нету данных для заполнения");
            }
            ResultSet generatedKeys = ps1.getGeneratedKeys();
            if (generatedKeys.next()) {
                int playerId = generatedKeys.getInt(1);
                // Вставка данных в таблицу player_info
                ps2.setInt(1, playerId);
                ps2.setString(2, player.getTeam());
                ps2.setString(3, player.getPosition());
                ps2.executeUpdate();
            } else {
                throw new SQLException("Не удалось заполнить базу данных - нету ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
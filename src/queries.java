import java.sql.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;

public class queries {
    public static void first_query() {
        String url = "jdbc:sqlite:test.db";

        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {

            // Запрос для нахождения среднего возраста во всех командах
            String query = "SELECT team, AVG(age) AS avg_age FROM players " +
                    "JOIN player_info ON players.id = player_info.player_id " +
                    "GROUP BY team";

            ResultSet resultSet = statement.executeQuery(query);

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            while (resultSet.next()) {
                String team = resultSet.getString("team");
                double avgAge = resultSet.getDouble("avg_age");
                dataset.addValue(avgAge, "Average Age", team);
            }

            JFreeChart barChart = ChartFactory.createBarChart(
                    "Average Age in Teams",
                    "Team",
                    "Average Age",
                    dataset);

            // Отображение графика в окне
            JFrame frame = new JFrame("Average Age in Teams");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ChartPanel chartPanel = new ChartPanel(barChart);
            frame.add(chartPanel);
            frame.pack();
            frame.setVisible(true);

        } catch (SQLException e) {
            System.out.println("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }

    // Найдите команду с самым высоким средним ростом. Выведите в консоль 5 самых высоких игроков команды.
    public static void second_query() {
        String url = "jdbc:sqlite:test.db";

        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {

            // Запрос для нахождения команды с самым высоким средним ростом
            String query = "SELECT team, AVG(height) AS avg_height FROM players " +
                    "JOIN player_info ON players.id = player_info.player_id " +
                    "GROUP BY team ORDER BY avg_height DESC LIMIT 1";

            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                String teamWithHighestAverageHeight = resultSet.getString("team");
                System.out.println("Команда с самым высоким средним ростом: " + teamWithHighestAverageHeight);

                // Запрос для нахождения 5 самых высоких игроков команды
                query = "SELECT name, height FROM players " +
                        "JOIN player_info ON players.id = player_info.player_id " +
                        "WHERE team = ? ORDER BY height DESC LIMIT 5";

                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, teamWithHighestAverageHeight);
                resultSet = preparedStatement.executeQuery();

                System.out.println("Самые высокие игроки команды:");
                while (resultSet.next()) {
                    String playerName = resultSet.getString("name");
                    int playerHeight = resultSet.getInt("height");
                    System.out.println(playerName + " - " + playerHeight + " см");
                }
            } else {
                System.out.println("База данных пустая, либо данных о командах нет");
            }

        } catch (SQLException e) {
            System.out.println("Не удалось подключиться к базе данных" + e.getMessage());
        }
    }
    //Найдите команду, с средним ростом равным от 74 до 78 inches и средним весом от 190 до 210 lbs, с самым высоким средним возрастом.
    public static void third_query() {
        String url = "jdbc:sqlite:test.db";

        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {

            // Запрос для нахождения команды с условиями по росту и весу
            String query = "SELECT team, AVG(height) AS avg_height, AVG(weight) AS avg_weight, AVG(age) AS avg_age FROM players " +
                    "JOIN player_info ON players.id = player_info.player_id " +
                    "GROUP BY team " +
                    "HAVING avg_height BETWEEN 74 AND 78 AND avg_weight BETWEEN 190 AND 210 " +
                    "ORDER BY avg_age DESC LIMIT 1";

            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                String teamWithConditionsMet = resultSet.getString("team");
                double avgHeight = resultSet.getDouble("avg_height");
                double avgWeight = resultSet.getDouble("avg_weight");
                double avgAge = resultSet.getDouble("avg_age");

                System.out.println("Команда, удовлетворяющая условиям:");
                System.out.println("Название: " + teamWithConditionsMet);
                System.out.println("Средний рост: " + avgHeight + " inches");
                System.out.println("Средний вес: " + avgWeight + " lbs");
                System.out.println("Средний возраст: " + avgAge + " лет");
            } else {
                System.out.println("Нет команд, удовлетворяющих условиям");
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }
}

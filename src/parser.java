import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class parser {
    public static List<Player> parseCSV(String csvFile) {
        List<Player> players = new ArrayList<>();
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine();  //Пропускаем заголовок

            String line;
            while ((line = br.readLine()) != null) {
                Player player = createPlayerFromCSVLine(line, cvsSplitBy);
                players.add(player);
            }
            System.out.println("Парсинг прошел успешно");
        } catch (IOException e) {
            System.out.println("При парсинге произошла ошибка");
        }
        return players;
    }

    private static Player createPlayerFromCSVLine(String line, String cvsSplitBy) {
        String[] data = line.split(cvsSplitBy);
        String name = data[0].trim().replace("\"", "");
        String team = data[1].trim().replace("\"", "");
        String position = data[2].trim().replace("\"", "");
        int height = Integer.parseInt(data[3].trim());
        int weight = Integer.parseInt(data[4].trim());
        double age = Double.parseDouble(data[5].trim());

        return new Player(name, team, position, height, weight, age);
    }
}

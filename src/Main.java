import java.util.List;

public class Main {
    public static void main(String[] args) {
        String csvFile = "stat_of_team.csv";
        List<Player> players = parser.parseCSV(csvFile); //парсинг csv
        databases.deleteFile("test.db"); //удаления старой базы
        databases.creatorDB(players); //создание и заполнение базы данных
        queries.first_query(); //запросы
        queries.second_query();
        queries.third_query();
        System.exit(0);
    }
}

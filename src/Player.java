public class Player {
    public String name;
    public String team;
    public String position;
    public int height;
    public int weight;
    public double age;

    public Player(String name, String team, String position, int height, int weight, double age) {
        this.name = name;
        this.team = team;
        this.position = position;
        this.height = height;
        this.weight = weight;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getTeam() {
        return team;
    }

    public String getPosition() {
        return position;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWeight() {
        return weight;
    }

    public Double getAge() {
        return age;
    }
}
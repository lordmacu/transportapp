package co.cristiangarcia.hdtransmileniopro.objects;

/**
 * Created by cristiangarcia on 25/10/16.
 */

public class EstacionTemporal {

    private int id;
    private String name;
    private int type;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "EstacionTemporal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}

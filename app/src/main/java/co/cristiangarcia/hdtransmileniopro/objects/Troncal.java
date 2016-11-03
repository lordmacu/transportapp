package co.cristiangarcia.hdtransmileniopro.objects;
import java.io.Serializable;
public class Troncal implements Serializable{
    private String color;
    private String description;
    private int id;
    private String name;
    private String subDescription;


    public Troncal(int id, String name, String description, String color,String subDescription) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.subDescription=subDescription;
    }


    public Troncal(int id, String name, String description, String color) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public String getSubDescription() {
        return subDescription;
    }

    public void setSubDescription(String subDescription) {
        this.subDescription = subDescription;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getColor() {
        return this.color;
    }

    @Override
    public String toString() {
        return "Troncal{" +
                "color='" + color + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", subDescription='" + subDescription + '\'' +
                '}';
    }
}

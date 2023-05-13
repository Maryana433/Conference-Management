package pl.maryana.conference.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Lecture {

    private long id;
    private int number;
    private String thematicPath;
    private String description;

    private static long nextId;

    public Lecture(long id, int number, String thematicPath,String description){
        this.number = number;
        this.thematicPath = thematicPath;
        this.description = description;
        this.id = id;
    }

    @Override
    public String toString(){
        return "number - " + this.number + ", thematic path - " +
                this.thematicPath + ", description - "  + this.description;
    }

}

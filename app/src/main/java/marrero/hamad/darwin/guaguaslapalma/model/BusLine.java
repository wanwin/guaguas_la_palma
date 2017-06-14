package marrero.hamad.darwin.guaguaslapalma.model;

public class BusLine {

    String id, name;

    public BusLine(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }
}

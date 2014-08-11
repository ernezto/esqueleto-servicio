package ec.gob.senescyt.ejemplo.core;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Ejemplo {
    @Id
    private String id;
    private String nombre;

    private Ejemplo() {}

    public Ejemplo(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}

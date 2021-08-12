package comuprovpob;

public class Comunidad {

    private String nombre_Com;
    private String codigo_Com;

    public Comunidad() {
    }

    public Comunidad(String nombre_Com, String codigo_Com) {
        this.nombre_Com = nombre_Com;
        this.codigo_Com = codigo_Com;
    }

    //............Getters & Setters..............
    public String getCodigo_Com() {
        return codigo_Com;
    }

    public void setCodigo_Com(String codigo_Com) {
        this.codigo_Com = codigo_Com;
    }

    public String getNombre_Com() {
        return nombre_Com;
    }

    public void setNombre_Com(String nombre_Com) {
        this.nombre_Com = nombre_Com;
    }

    //.................toString..................
    @Override
    public String toString() {
        return nombre_Com;
    }
    
}

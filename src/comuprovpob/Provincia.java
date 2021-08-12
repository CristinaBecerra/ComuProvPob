
package comuprovpob;

public class Provincia {
    private String cod_Comunidad;
    private int idProvincia;
    private String provincia;
    private String provinciaSeo;
    private String abrev;
    private int codPostal;

    public Provincia(){}
    
    public Provincia(String cod_Comunidad, int idProvincia, String provincia, String provinciaSeo, String abrev, int codPostal) {
        this.cod_Comunidad = cod_Comunidad;
        this.idProvincia = idProvincia;
        this.provincia = provincia;
        this.provinciaSeo = provinciaSeo;
        this.abrev = abrev;
        this.codPostal = codPostal;
    }
    
    //.............Getters & Setters......................
    public String getCod_Comunidad() {
        return cod_Comunidad;
    }

    public void setCod_Comunidad(String cod_Comunidad) {
        this.cod_Comunidad = cod_Comunidad;
    }

    public int getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(int idProvincia) {
        this.idProvincia = idProvincia;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getProvinciaSeo() {
        return provinciaSeo;
    }

    public void setProvinciaSeo(String provinciaSeo) {
        this.provinciaSeo = provinciaSeo;
    }

    public String getAbrev() {
        return abrev;
    }

    public void setAbrev(String abrev) {
        this.abrev = abrev;
    }

    public int getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(int codPostal) {
        this.codPostal = codPostal;
    }
    
    //...................toString..................

    @Override
    public String toString() {
        return  provincia + "(" + idProvincia + ")";
    }
    
}

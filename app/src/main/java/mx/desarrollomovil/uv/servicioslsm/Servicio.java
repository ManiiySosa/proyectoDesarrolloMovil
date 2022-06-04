package mx.desarrollomovil.uv.servicioslsm;

public class Servicio {
    private String id;
    private int añosExperiencia;
    private String areaEspecialidad;
    private String telefono;
    private int img;
    private String idUsuario;

    public Servicio(String id, int añosExperiencia, String areaEspecialidad, String telefono, int img, String idUsuario) {
        this.id = id;
        this.añosExperiencia = añosExperiencia;
        this.areaEspecialidad = areaEspecialidad;
        this.telefono = telefono;
        this.img = img;
        this.idUsuario=idUsuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAñosExperiencia() {
        return añosExperiencia;
    }

    public void setAñosExperiencia(int añosExperiencia) {
        this.añosExperiencia = añosExperiencia;
    }

    public String getAreaEspecialidad() {
        return areaEspecialidad;
    }

    public void setAreaEspecialidad(String areaEspecialidad) {
        this.areaEspecialidad = areaEspecialidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int video) {
        this.img = video;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}

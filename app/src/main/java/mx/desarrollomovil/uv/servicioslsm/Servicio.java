package mx.desarrollomovil.uv.servicioslsm;

public class Servicio {
    private String id;
    private int añosExperiencia;
    private String areaEspecialidad;
    private String telefono;
    private String videoUrl;
    private String idUsuario;

    public Servicio(){

    }

    public Servicio(String id, int añosExperiencia, String areaEspecialidad, String telefono, String videoUrl, String idUsuario) {
        this.id = id;
        this.añosExperiencia = añosExperiencia;
        this.areaEspecialidad = areaEspecialidad;
        this.telefono = telefono;
        this.videoUrl = videoUrl;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String video) {
        this.videoUrl= video;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}

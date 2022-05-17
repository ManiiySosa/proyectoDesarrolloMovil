package mx.desarrollomovil.uv.servicioslsm;

import java.util.Date;

public class Servicio {
    private int id;
    private int añosExperiencia;
    private String areaEspecialidad;
    private String telefono;
    private int img;
    private int idUsuario;

    public Servicio(int id, int añosExperiencia, String areaEspecialidad, String telefono, int img, int idUsuario) {
        this.id = id;
        this.añosExperiencia = añosExperiencia;
        this.areaEspecialidad = areaEspecialidad;
        this.telefono = telefono;
        this.img = img;
        this.idUsuario=idUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}

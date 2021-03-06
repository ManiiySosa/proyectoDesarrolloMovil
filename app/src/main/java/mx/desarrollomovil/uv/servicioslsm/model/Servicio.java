package mx.desarrollomovil.uv.servicioslsm.model;

import java.io.Serializable;

public class Servicio {
    private String serviceId;
    private String nombreCompleto;
    private String añosExperiencia;
    private String areaEspecialidad;
    private String telefono;
    private String videoUrl;
    private String userId;

    public Servicio(){

    }

    public Servicio(String id, String nombreCompleto, String añosExperiencia, String areaEspecialidad, String telefono, String videoUrl, String userId) {
        this.serviceId = id;
        this.nombreCompleto = nombreCompleto;
        this.añosExperiencia = añosExperiencia;
        this.areaEspecialidad = areaEspecialidad;
        this.telefono = telefono;
        this.videoUrl = videoUrl;
        this.userId=userId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getAñosExperiencia() {
        return añosExperiencia;
    }

    public void setAñosExperiencia(String añosExperiencia) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String idUsuario) {
        this.userId = idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
}

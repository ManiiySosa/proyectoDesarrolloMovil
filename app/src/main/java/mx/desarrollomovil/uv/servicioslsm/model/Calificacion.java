package mx.desarrollomovil.uv.servicioslsm.model;

public class Calificacion {

    private String ratingId;
    private float rating;
    private String videoUrl;
    private String userId;

    public Calificacion(){

    }

    public Calificacion(String ratingId, float rating, String videoUrl, String userId) {
        this.ratingId = ratingId;
        this.rating = rating;
        this.videoUrl = videoUrl;
        this.userId = userId;
    }

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

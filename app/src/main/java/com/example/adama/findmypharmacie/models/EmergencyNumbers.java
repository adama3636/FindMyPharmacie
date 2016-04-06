package com.example.adama.findmypharmacie.models;

public class EmergencyNumbers {
    private String name, telephone, type;
    private int image;

    public EmergencyNumbers(String name, String telephone, String type, int image) {
        this.name = name;
        this.telephone = telephone;
        this.type = type;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

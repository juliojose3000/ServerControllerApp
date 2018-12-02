package com.example.julio.objects;

public class Rol {
    private int idRol;
    private String rolName;

    public Rol(){

    }


    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getRolName() {
        return rolName;
    }

    public void setRolName(String rolName) {
        this.rolName = rolName;
    }

    @Override
    public String toString() {
        return "Rol{" +
                "idRol=" + idRol +
                ", rolName='" + rolName + '\'' +
                '}';
    }
}

package com.example.julio.objects;

import java.sql.Date;

public class Audit {
    private int idAudit;
    private  User user;
    private String serverName;
    private Date admissionDate;
    private Date departureDate;

    public Audit(User user, String serverName, Date admissionDate, Date departureDate){
        this.user=user;
        this.serverName=serverName;
        this.admissionDate=admissionDate;
        this.departureDate=departureDate;
    }

    public Audit(){
        this.user=new User();
    }

    public int getIdAudit() {
        return idAudit;
    }

    public void setIdAudit(int idAudit) {
        this.idAudit = idAudit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    @Override
    public String toString() {
        return "Audit{" +
                "idAudit=" + idAudit +
                ", user=" + user.toString() +
                ", serverName='" + serverName + '\'' +
                ", admissionDate=" + admissionDate +
                ", departureDate=" + departureDate +
                '}';
    }
}

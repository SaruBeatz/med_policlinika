package com.example.med_policlinika;

public class Visit {
    private String patientFullName; // Полное имя пациента
    private String docFullName; // Полное имя врача
    private String dateVisit; // Дата посещения
    private String visitType; // Тип посещения
    private String symptoms; // Симптомы

    // Конструктор класса Visit
    public Visit(String patientFullName, String docFullName, String dateVisit, String visitType, String symptoms) {
        this.patientFullName = patientFullName;
        this.docFullName = docFullName;
        this.dateVisit = dateVisit;
        this.visitType = visitType;
        this.symptoms = symptoms;
    }

    //getters and setters
    public String getPatientFullName() {
        return patientFullName;
    }

    public void setPatientFullName(String patientFullName) {
        this.patientFullName = patientFullName;
    }

    public String getDocFullName() {
        return docFullName;
    }

    public void setDocFullName(String docFullName) {
        this.docFullName = docFullName;
    }

    public String getDateVisit() {
        return dateVisit;
    }

    public void setDateVisit(String dateVisit) {
        this.dateVisit = dateVisit;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

}

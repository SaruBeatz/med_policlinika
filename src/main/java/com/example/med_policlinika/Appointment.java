package com.example.med_policlinika;

public class Appointment {
    private int id; // Уникальный идентификатор назначения
    private String patientName; // Имя пациента
    private String medicineName; // Название лекарства
    private int drugsNumber; // Количество лекарств

    // Конструктор класса Appointment
    public Appointment(int id, String patientName, String medicineName, int drugsNumber) {
        this.id = id;
        this.patientName = patientName;
        this.medicineName = medicineName;
        this.drugsNumber = drugsNumber;
    }

    //getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getDrugsNumber() {
        return drugsNumber;
    }

    public void setDrugsNumber(int drugsNumber) {
        this.drugsNumber = drugsNumber;
    }
}

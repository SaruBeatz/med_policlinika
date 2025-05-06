package com.example.med_policlinika;

public class Doctor {
    private int id; // Уникальный идентификатор доктора
    private String name; // Имя доктора
    private String surname; // Фамилия доктора
    private String patronymic; // Отчество доктора
    private String telephone; // Номер телефона доктора
    private String specialization; // Специализация доктора
    private String dateAssignment; // Дата назначения специализации

    private String dateAppointment; // Дата назначения на работу
    private String fullName; // Полное имя доктора

    // Конструктор класса Doctor для инициализации всех полей
    public Doctor(int id, String name, String surname, String patronymic, String telephone, String specialization, String dateAssignment) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.telephone = telephone;
        this.specialization = specialization;
        this.dateAssignment = dateAssignment;
    }

    public Doctor(int id, String fullName, String telephone, String dateAppointment, String specialization, String dateAssignment) {
        this.id = id;
        this.fullName = fullName;
        this.telephone = telephone;
        this.dateAppointment = dateAppointment;
        this.specialization = specialization;
        this.dateAssignment = dateAssignment;
    }

    // Геттеры и сеттеры для всех полей
    public int getId() {
        return id;
    }
    public String getDateAppointment() {
        return dateAppointment;
    }

    public void setDateAppointment(String dateAppointment) {
        this.dateAppointment = dateAppointment;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDateAssignment() {
        return dateAssignment;
    }

    public void setDateAssignment(String dateAssignment) {
        this.dateAssignment = dateAssignment;
    }

    // Метод для печати информации о докторе
    public void printDoctor() {
        String info = "ID: " + id + "\n";
        info += "ФИО: " + fullName + "\n";
        info += "Телефон: " + telephone + "\n";
        info += "Дата начала работы: " + dateAppointment + "\n";
        info += "Специализация: " + specialization + "\n";
        info += "Дата назначения специализации: " + dateAssignment + "\n";

        System.out.println(info);
    }
}
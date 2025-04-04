package com.example.aplicativomovil.entidades;
/**
 * Clase que representa un contacto de emergencia, con nombre, teléfono y correo electrónico.
 */
public class ContactoEmergencia {

    /** Nombre del contacto de emergencia */
    String nombreContacto;

    /** Teléfono del contacto de emergencia */
    String telefonoContacto;

    /** Correo electrónico del contacto de emergencia */
    String correoContacto;

    /**
     * Constructor vacío requerido para algunas operaciones como deserialización con Firebase.
     */
    public ContactoEmergencia() {
        // Constructor por defecto
    }

    /**
     * Constructor que inicializa todos los campos del contacto.
     *
     * @param nombreContacto  Nombre del contacto.
     * @param telefonoContacto Teléfono del contacto.
     * @param correoContacto   Correo electrónico del contacto.
     */
    public ContactoEmergencia(String nombreContacto, String telefonoContacto, String correoContacto) {
        this.nombreContacto = nombreContacto;
        this.telefonoContacto = telefonoContacto;
        this.correoContacto = correoContacto;
    }

    /**
     * Obtiene el nombre del contacto.
     *
     * @return Nombre del contacto.
     */
    public String getNombreContacto() {
        return nombreContacto;
    }

    /**
     * Obtiene el teléfono del contacto.
     *
     * @return Teléfono del contacto.
     */
    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    /**
     * Obtiene el correo electrónico del contacto.
     *
     * @return Correo del contacto.
     */
    public String getCorreoContacto() {
        return correoContacto;
    }

    /**
     * Establece el nombre del contacto.
     *
     * @param nombreContacto Nombre a asignar.
     */
    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    /**
     * Establece el teléfono del contacto.
     *
     * @param telefonoContacto Teléfono a asignar.
     */
    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    /**
     * Establece el correo del contacto.
     *
     * @param correoContacto Correo a asignar.
     */
    public void setCorreoContacto(String correoContacto) {
        this.correoContacto = correoContacto;
    }
}


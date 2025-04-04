package com.example.aplicativomovil.entidades;

/**
 * Clase que representa un usuario del sistema con nombre, teléfono y correo electrónico.
 * Utilizada para almacenar y transferir información del usuario, por ejemplo con Firebase.
 */
public class Usuarios {

    /** Nombre del usuario */
    private String nombre;

    /** Número de teléfono del usuario */
    private String telefono;

    /** Correo electrónico del usuario */
    private String email;

    /**
     * Constructor que inicializa todos los campos del usuario.
     *
     * @param nombre   Nombre del usuario.
     * @param telefono Teléfono del usuario.
     * @param email    Correo electrónico del usuario.
     */
    public Usuarios(String nombre, String telefono, String email) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    /**
     * Constructor vacío requerido para ciertas operaciones como la deserialización con Firebase.
     */
    public Usuarios() {
        // Constructor vacío
    }

    // ============================
    // Métodos Getter
    // ============================

    /**
     * Obtiene el nombre del usuario.
     *
     * @return Nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el número de teléfono del usuario.
     *
     * @return Teléfono del usuario.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return Email del usuario.
     */
    public String getEmail() {
        return email;
    }

    // ============================
    // Métodos Setter
    // ============================

    /**
     * Establece el nombre del usuario.
     *
     * @param nombre Nombre a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece el número de teléfono del usuario.
     *
     * @param telefono Teléfono a asignar.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param email Email a asignar.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}

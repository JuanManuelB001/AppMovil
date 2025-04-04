package com.example.aplicativomovil.Mensajes;
/**
 * Clase que representa un mensaje enviado entre usuarios.
 * Contiene información sobre el remitente, destinatario, contenido y el momento del envío.
 * Se utiliza con Firebase Firestore.
 */
public class Mensaje {

    /** Correo del remitente */
    private String de;

    /** Correo del destinatario */
    private String para;

    /** Contenido del mensaje */
    private String mensaje;

    /** Marca de tiempo en milisegundos desde la época Unix */
    private long timestamp;

    // Atributos adicionales no utilizados actualmente (opcional eliminar si no se usan)
    private String remitente;
    private String contenido;

    /**
     * Constructor vacío requerido por Firestore para la deserialización automática.
     */
    public Mensaje() {}

    /**
     * Constructor para inicializar un mensaje con los campos principales.
     *
     * @param de        Correo del remitente.
     * @param para      Correo del destinatario.
     * @param mensaje   Contenido del mensaje.
     * @param timestamp Fecha y hora del envío (en milisegundos).
     */
    public Mensaje(String de, String para, String mensaje, long timestamp) {
        this.de = de;
        this.para = para;
        this.mensaje = mensaje;
        this.timestamp = timestamp;
    }

    // Métodos Getters y Setters

    /**
     * @return Correo del remitente.
     */
    public String getDe() {
        return de;
    }

    /**
     * Establece el correo del remitente.
     * @param de Correo del remitente.
     */
    public void setDe(String de) {
        this.de = de;
    }

    /**
     * @return Correo del destinatario.
     */
    public String getPara() {
        return para;
    }

    /**
     * Establece el correo del destinatario.
     * @param para Correo del destinatario.
     */
    public void setPara(String para) {
        this.para = para;
    }

    /**
     * @return Contenido del mensaje.
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Establece el contenido del mensaje.
     * @param mensaje Texto del mensaje.
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * @return Marca de tiempo del mensaje (en milisegundos).
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Establece la marca de tiempo del mensaje.
     * @param timestamp Tiempo en milisegundos desde la época Unix.
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}


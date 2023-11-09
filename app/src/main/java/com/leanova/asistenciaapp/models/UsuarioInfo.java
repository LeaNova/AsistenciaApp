package com.leanova.asistenciaapp.models;

import java.io.Serializable;
import java.util.Date;

public class UsuarioInfo implements Serializable {
    private int idUsuario;
    private String nombre;
    private String apellido;
    private Date fechaNac;
    private String dni;
    private String genero;
    private String estCivil;
    private String direccion;
    private String telefono;
    private String mail;
    private Date fechaIngreso;
    private String rol;

    public UsuarioInfo() {}
    public UsuarioInfo(int idUsuario, String nombre, String apellido, Date fechaNac, String dni, String genero, String estCivil, String direccion, String telefono, String mail, Date fechaIngreso, String rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNac = fechaNac;
        this.dni = dni;
        this.genero = genero;
        this.estCivil = estCivil;
        this.direccion = direccion;
        this.telefono = telefono;
        this.mail = mail;
        this.fechaIngreso = fechaIngreso;
        this.rol = rol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEstCivil() {
        return estCivil;
    }

    public void setEstCivil(String estCivil) {
        this.estCivil = estCivil;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}

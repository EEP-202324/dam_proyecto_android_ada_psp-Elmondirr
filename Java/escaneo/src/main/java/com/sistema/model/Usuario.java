package com.sistema.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@JsonIgnoreProperties({"entradas"})
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int id;

    @Column(nullable = false, length = 255)
   private String nombre;

    @Column(nullable = false, length = 255)
   private String apellidos;

    @Column(nullable = false, length = 255, unique = true)
   private String email;

    @Column(nullable = false, length = 255)
   private String contrasena;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Rol rol = Rol.CLIENTE;

   @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private Set<Entrada> entradas = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellidos;
    }

    public void setApellido(String apellido) {
        this.apellidos = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = Rol.valueOf(rol);
    }

    public Set<Entrada> getEntradas() {
        return entradas;
    }

    public void setEntradas(Set<Entrada> entradas) {
        this.entradas = entradas;
    }

    public enum Rol {
        CLIENTE, ADMIN
    }
}

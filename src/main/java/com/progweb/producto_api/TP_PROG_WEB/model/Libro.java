package com.progweb.producto_api.TP_PROG_WEB.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "libros")
public class Libro {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String autor;
    private String genero;
    private int anioDeCreacion;
    private String urlFoto;
    private Long usuarioId;
 
    public Libro() {
    }
 
    public Libro(String nombre, String autor, String genero, int anioDeCreacion, String urlFoto, Long usuarioId) {
        this.nombre = nombre;
        this.autor = autor;
        this.genero = genero;
        this.anioDeCreacion = anioDeCreacion;
        this.urlFoto = urlFoto;
        this.usuarioId = usuarioId;
    }
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public String getNombre() {
        return nombre;
    }
 
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
 
    public String getAutor() {
        return autor;
    }
 
    public void setAutor(String autor) {
        this.autor = autor;
    }
 
    public String getGenero() {
        return genero;
    }
 
    public void setGenero(String genero) {
        this.genero = genero;
    }
 
    public int getAnioDeCreacion() {
        return anioDeCreacion;
    }
 
    public void setAnioDeCreacion(int anioDeCreacion) {
        this.anioDeCreacion = anioDeCreacion;
    }
 
    public String getUrlFoto() {
        return urlFoto;
    }
 
    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }
 
    public Long getUsuarioId() {
        return usuarioId;
    }
 
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
 
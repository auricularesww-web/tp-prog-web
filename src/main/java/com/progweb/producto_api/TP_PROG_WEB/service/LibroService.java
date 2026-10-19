package com.progweb.producto_api.TP_PROG_WEB.service;


import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.progweb.producto_api.TP_PROG_WEB.model.Libro;
import com.progweb.producto_api.TP_PROG_WEB.repository.LibroRepository;
import com.progweb.producto_api.TP_PROG_WEB.repository.UsuarioRepository;


@Service
public class LibroService {
   private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepo;
 
    public LibroService(LibroRepository libroRepository, UsuarioRepository usuarioRepo) {
        this.libroRepository = libroRepository;
        this.usuarioRepo = usuarioRepo;
    }
 
    public List<Libro> listar() {
        return libroRepository.findAll();
    }
 
    public List<Libro> listarPorUsuario(Long usuarioId) {
        return libroRepository.findByUsuarioId(usuarioId);
    }
 
    public List<Libro> buscarPorNombre(Long usuarioId, String texto) {
        if (texto == null) {
            texto = "";
        }
        return libroRepository.findByUsuarioIdAndNombreContainingIgnoreCase(usuarioId, texto);
    }
 
    public Libro findById(Long id) {
        Optional<Libro> resultado = libroRepository.findById(id);
        if (resultado.isPresent()) {
            return resultado.get();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Libro no encontrado");
    }
 
    public Libro crear(Libro libro) {
        validarCampos(libro);
 
        if (libro.getUsuarioId() == null || !usuarioRepo.existsById(libro.getUsuarioId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El usuario no corresponde a uno existente");
        }
 
        Libro libroNuevo = new Libro(
                libro.getNombre(),
                libro.getAutor(),
                libro.getGenero(),
                libro.getAnioDeCreacion(),
                libro.getUrlFoto(),
                libro.getUsuarioId());
 
        return libroRepository.save(libroNuevo);
    }
 
    public Libro modificar(Long id, Libro libro) {
        Libro libroEncontrado = findById(id);
        validarCampos(libro);
 
        libroEncontrado.setNombre(libro.getNombre());
        libroEncontrado.setAutor(libro.getAutor());
        libroEncontrado.setGenero(libro.getGenero());
        libroEncontrado.setAnioDeCreacion(libro.getAnioDeCreacion());
        libroEncontrado.setUrlFoto(libro.getUrlFoto());
 
        return libroRepository.save(libroEncontrado);
    }
 
    public void eliminar(Long id) {
        Libro libro = findById(id);
        libroRepository.delete(libro);
    }
 
    private void validarCampos(Libro libro) {
        if (libro.getNombre() == null || libro.getNombre().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre es obligatorio");
        }
        if (libro.getAutor() == null || libro.getAutor().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El autor es obligatorio");
        }
        if (libro.getGenero() == null || libro.getGenero().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El género es obligatorio");
        }
        if (libro.getAnioDeCreacion() < 1 || libro.getAnioDeCreacion() > 2100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El año de publicación no es válido");
        }
    }
}
 
package com.progweb.producto_api.TP_PROG_WEB.controller;

import org.springframework.web.bind.annotation.RestController;

import com.progweb.producto_api.TP_PROG_WEB.model.Libro;
import com.progweb.producto_api.TP_PROG_WEB.service.LibroService;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RequestMapping("/libros")


@RestController 
public class LibrosController {

    private final   LibroService libroService;


    public LibrosController(LibroService libroService) {
        this.libroService = libroService;
    }
 
    @GetMapping
    public List<Libro> listarLibros() {
        return libroService.listar();
    }
 
    @GetMapping("/usuario/{usuarioId}")
    public List<Libro> listarPorUsuario(@PathVariable Long usuarioId) {
        return libroService.listarPorUsuario(usuarioId);
    }
 
    @GetMapping("/usuario/{usuarioId}/buscar")
    public List<Libro> buscarLibros(@PathVariable Long usuarioId, @RequestParam String texto) {
        return libroService.buscarPorNombre(usuarioId, texto);
    }
 
    @GetMapping("/{id}")
    public Libro buscarLibro(@PathVariable Long id) {
        return libroService.findById(id);
    }
 
    @PostMapping
    public Libro agregarLibro(@RequestBody Libro libro) {
        return libroService.crear(libro);
    }
 
    @PutMapping("/{id}")
    public Libro modificarLibro(@PathVariable Long id, @RequestBody Libro libro) {
        return libroService.modificar(id, libro);
    }
 
    @DeleteMapping("/{id}")
    public String eliminarLibro(@PathVariable Long id) {
        libroService.eliminar(id);
        return "Libro eliminado correctamente";
    }
}
 

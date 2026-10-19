package com.progweb.producto_api.TP_PROG_WEB.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.progweb.producto_api.TP_PROG_WEB.model.Usuario;
import com.progweb.producto_api.TP_PROG_WEB.service.UsuarioService;
@RequestMapping("/usuario")

@RestController 
public class UsuarioController {

     private final UsuarioService usuarioService;
 
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
 
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listar();
    }
 
    @GetMapping("/{id}")
    public Usuario buscarUsuario(@PathVariable Long id) {
        return usuarioService.findById(id);
    }
 
    @PostMapping
    public Usuario agregarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.crear(usuario);
    }
 
    @PutMapping("/{id}")
    public Usuario modificarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.modificar(id, usuario);
    }
 
    @DeleteMapping("/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return "Usuario eliminado correctamente";
    }
 
    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario usuario) {
        return usuarioService.login(usuario.getEmail(), usuario.getPassword());
    }
}
 
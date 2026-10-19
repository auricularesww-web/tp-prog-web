package com.progweb.producto_api.TP_PROG_WEB.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.progweb.producto_api.TP_PROG_WEB.model.Usuario;
import com.progweb.producto_api.TP_PROG_WEB.repository.UsuarioRepository;
@Service 
public class UsuarioService {
   
    private final UsuarioRepository usuarioRepo;
    
  public UsuarioService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }
 
    public Usuario findById(Long id) {
        Optional<Usuario> resultado = usuarioRepo.findById(id);
        if (resultado.isPresent()) {
            return resultado.get();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Usuario no encontrado");
    }
 
    public List<Usuario> listar() {
        return usuarioRepo.findAll();
    }
 
    public Usuario crear(Usuario usuario) {
        validarDatos(usuario);
 
        if (usuarioRepo.existsByEmail(usuario.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ya existe un usuario con ese email");
        }
 
        Usuario usuarioNuevo = new Usuario(
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getPassword());
 
        return usuarioRepo.save(usuarioNuevo);
    }
 
    public Usuario modificar(Long id, Usuario usuario) {
        Usuario usuarioEncontrado = findById(id);
        validarDatos(usuario);
 
        usuarioEncontrado.setNombre(usuario.getNombre());
        usuarioEncontrado.setEmail(usuario.getEmail());
        usuarioEncontrado.setPassword(usuario.getPassword());
 
        return usuarioRepo.save(usuarioEncontrado);
    }
 
    public void eliminar(Long id) {
        Usuario usuario = findById(id);
        usuarioRepo.delete(usuario);
    }
 
    public Usuario login(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email y contraseña son obligatorios");
        }
        return usuarioRepo.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Credenciales inválidas"));
    }
 
    private void validarDatos(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre es obligatorio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank() || !usuario.getEmail().contains("@")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El email es obligatorio y debe ser válido");
        }
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La contraseña es obligatoria");
        }
    }
}
 
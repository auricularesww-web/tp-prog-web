package com.progweb.producto_api.TP_PROG_WEB.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.progweb.producto_api.TP_PROG_WEB.model.Usuario;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {
                Optional<Usuario> findByEmailAndPassword(String email, String password);
                boolean existsByEmail(String email);
}

    
   





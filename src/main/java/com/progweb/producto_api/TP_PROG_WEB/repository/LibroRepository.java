package com.progweb.producto_api.TP_PROG_WEB.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.progweb.producto_api.TP_PROG_WEB.model.Libro;

public interface LibroRepository
        extends JpaRepository<Libro, Long> {
                 List<Libro> findByUsuarioId(Long usuarioId);
 
    List<Libro> findByUsuarioIdAndNombreContainingIgnoreCase(Long usuarioId, String texto);
}
 






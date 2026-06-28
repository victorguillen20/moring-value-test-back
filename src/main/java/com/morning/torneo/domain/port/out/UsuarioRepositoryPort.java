package com.morning.torneo.domain.port.out;

import com.morning.torneo.domain.model.Usuario;
import java.util.Optional;

public interface UsuarioRepositoryPort {

    Usuario save(Usuario usuario);

    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}

package com.javanauta.demo.infrastructure.repository;


import com.javanauta.demo.infrastructure.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);
    boolean existsByNome(String nome);

    Optional<Usuario> findByNome(String nome);
    Optional<Usuario> findByEmail(String email);

    @Transactional
    void deleteByEmail(String email);



}

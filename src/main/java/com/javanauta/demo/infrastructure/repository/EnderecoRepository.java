package com.javanauta.demo.infrastructure.repository;

import com.javanauta.demo.infrastructure.entity.Endereco;
import com.javanauta.demo.infrastructure.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    Optional<Endereco> findById(Long id);


}

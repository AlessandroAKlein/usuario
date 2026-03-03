package com.javanauta.demo.business;

import com.javanauta.demo.business.converter.UsuarioConverter;
import com.javanauta.demo.business.dto.UsuarioDTO;
import com.javanauta.demo.infrastructure.entity.Usuario;
import com.javanauta.demo.infrastructure.exceptions.ConflictException;
import com.javanauta.demo.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.demo.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletionException;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;


    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        emailExistente(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public void emailExistente(String email){
        try{
            boolean existe = verificarEmailExistente(email);
            if (existe){
                throw new ConflictException("Email já cadastrado "+email);
            }
        } catch (CompletionException e){
            throw new ConflictException("Email ja cadastrado: ", e.getCause());
        }
    }

    public boolean verificarEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUserPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Não existe" + email ));
    }

   public Usuario buscarUserPorNome(String nome){
       return usuarioRepository.findByNome(nome).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o nome: '" + nome + "'"));
    }


    public void deletarUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }









}

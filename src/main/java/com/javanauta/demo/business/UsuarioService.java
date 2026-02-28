package com.javanauta.demo.business;

import com.javanauta.demo.business.converter.UsuarioConverter;
import com.javanauta.demo.business.dto.UsuarioDTO;
import com.javanauta.demo.infrastructure.entity.Usuario;
import com.javanauta.demo.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;


    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        return usuarioConverter.paraUsuarioDTO(usuario);
    }




}

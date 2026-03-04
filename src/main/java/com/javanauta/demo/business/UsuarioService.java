package com.javanauta.demo.business;

import com.javanauta.demo.business.converter.UsuarioConverter;
import com.javanauta.demo.business.dto.UsuarioDTO;
import com.javanauta.demo.infrastructure.entity.Usuario;
import com.javanauta.demo.infrastructure.exceptions.ConflictException;
import com.javanauta.demo.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.demo.infrastructure.repository.UsuarioRepository;
import com.javanauta.demo.infrastructure.security.JwtUtil;
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
    private final JwtUtil jwtUtil;


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

    public UsuarioDTO atualizarDadosUsuario(String token, UsuarioDTO usuarioDTO){
        //busca o email do usuario pelo token(TIRA OBRIGATORIDADE DE POR EMAIL)
       String email = jwtUtil.extractUsername(token.substring(7));
       usuarioDTO.setSenha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO.getSenha()) : null);
       //buscou os dados do usuário no banco de dados
       Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow((
               ) -> new ResourceNotFoundException("Email não encontrado"));
       // mesclou dados que recebemos na requisição DTO com os dados do banco de dados
       Usuario usuario = usuarioConverter.updateUsuario(usuarioDTO, usuarioEntity);
       //criptografia na senha
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        //salvou os dados do usuario convertido e depois pegou o retorno e converteu para usuarioDTO.
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));


    }









}

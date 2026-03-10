package com.javanauta.demo.controller;


import com.javanauta.demo.business.UsuarioService;
import com.javanauta.demo.business.dto.EnderecoDTO;
import com.javanauta.demo.business.dto.TelefoneDTO;
import com.javanauta.demo.business.dto.UsuarioDTO;
import com.javanauta.demo.infrastructure.entity.Usuario;
import com.javanauta.demo.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor


public class UsuarioController {


    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @PostMapping
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));
    }

    @PostMapping("/login")
    public String login(@RequestBody UsuarioDTO usuarioDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha())
        );
        return "Bearer " + jwtUtil.generateToken(authentication.getName());

    }

     ///por email
    @GetMapping("/email")
    public ResponseEntity<UsuarioDTO> listarTodosUsuarios(@RequestParam("email") String email) {
        return ResponseEntity.ok(usuarioService.buscarUserPorEmail(email));
    }

   @GetMapping
    public ResponseEntity<Usuario> listarUsuarioPorNome(@RequestParam("nome") String nome) {
       return ResponseEntity.ok(usuarioService.buscarUserPorNome(nome));
    }


    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletarUsuarioPorEmail(@PathVariable String email) {
        usuarioService.deletarUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> editarUsuario (@RequestBody UsuarioDTO usuarioDTO,
                                                     @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.atualizarDadosUsuario(token,usuarioDTO));
    }

    @PutMapping("/endereco")
    public ResponseEntity<EnderecoDTO> editarEndereco (@RequestBody EnderecoDTO enderecoDTO,
                                                       @RequestParam("Id") Long id){
        return ResponseEntity.ok(usuarioService.atualizaEndereco(id,enderecoDTO));
    }

    @PutMapping("/telefone")
    public ResponseEntity<TelefoneDTO> editarTelefone(@RequestBody TelefoneDTO telefoneDTO,
                                                      @RequestParam("Id") Long id){
        return ResponseEntity.ok(usuarioService.atualizarTelefone(id, telefoneDTO));
    }

    @PostMapping("/endereco")
    public ResponseEntity<EnderecoDTO> adicionarEndereco(@RequestBody EnderecoDTO enderecoDTO,
                                                         @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.adicionarEndereco(enderecoDTO,token));
    }

    @PostMapping("/telefone")
    public ResponseEntity<TelefoneDTO> adicionarTelefone(@RequestBody TelefoneDTO telefoneDTO,
                                                         @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.adicionarTelefone(telefoneDTO,token));
    }






}

package com.javanauta.demo.controller;


import com.javanauta.demo.business.UsuarioService;
import com.javanauta.demo.business.ViaCepService;
import com.javanauta.demo.business.dto.EnderecoDTO;
import com.javanauta.demo.business.dto.TelefoneDTO;
import com.javanauta.demo.business.dto.UsuarioDTO;
import com.javanauta.demo.infrastructure.client.ViaCepClient;
import com.javanauta.demo.infrastructure.client.ViaCepDTO;
import com.javanauta.demo.infrastructure.entity.Usuario;
import com.javanauta.demo.infrastructure.security.JwtUtil;
import com.javanauta.demo.infrastructure.security.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor

@Tag(name = "Usuario", description = "Cadastra usuarios")
@SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
public class UsuarioController {


    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ViaCepService client;


    @PostMapping
    @Operation(summary = "Salvar usuários", description = "Cria um novo usuário")
    @ApiResponse(responseCode = "200", description = "Usuário salvo com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro servidor")
    @ApiResponse(responseCode = "409", description = "Usuário já cadastrado")
    public ResponseEntity<UsuarioDTO> salvaUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.salvaUsuario(usuarioDTO));
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "faz login no sistema")
    @ApiResponse(responseCode = "200", description = "logado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais invalidas")
    @ApiResponse(responseCode = "500", description = "Erro servidor")
    public String login(@RequestBody UsuarioDTO usuarioDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha())
        );
        return "Bearer " + jwtUtil.generateToken(authentication.getName());

    }

    @GetMapping
    @Operation(summary = "Busca dados do usuário por Email", description = "Busca dados do usuário por email")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado/cadastrado")
    @ApiResponse(responseCode = "500", description = "Erro servidor")
    @ApiResponse(responseCode = "401", description = "Credenciais invalidas")
    public ResponseEntity<UsuarioDTO> listarTodosUsuarios(@RequestParam("email") String email) {
        return ResponseEntity.ok(usuarioService.buscarUserPorEmail(email));
    }

   @GetMapping("/nome")
    public ResponseEntity<Usuario> listarUsuarioPorNome(@RequestParam("nome") String nome) {
       return ResponseEntity.ok(usuarioService.buscarUserPorNome(nome));
    }


    @DeleteMapping("/{email}")
    @Operation(summary = "Deletar usuários", description = "deleta usuário pelo email")
    @ApiResponse(responseCode = "200", description = "usuário deletado com sucesso")
    @ApiResponse(responseCode = "403", description = "usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro servidor")
    @ApiResponse(responseCode = "401", description = "Credenciais invalidas")
    public ResponseEntity<Void> deletarUsuarioPorEmail(@PathVariable String email) {
        usuarioService.deletarUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Operation(summary = "Edita dados dos usuários", description = "atualiza os dados do usuário")
    @ApiResponse(responseCode = "200", description = "usuário editado com sucesso")
    @ApiResponse(responseCode = "404", description = "usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro servidor")
    @ApiResponse(responseCode = "401", description = "Credenciais invalidas")
    public ResponseEntity<UsuarioDTO> editarUsuario (@RequestBody UsuarioDTO usuarioDTO,
                                                     @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.atualizarDadosUsuario(token,usuarioDTO));
    }

    @PutMapping("/endereco")
    @Operation(summary = "Edita endereço dos usuários", description = "atualiza os dados do endereço do usuário")
    @ApiResponse(responseCode = "200", description = "dados de endereço editado com sucesso")
    @ApiResponse(responseCode = "403", description = "usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro servidor")
    @ApiResponse(responseCode = "401", description = "Credenciais invalidas")
    public ResponseEntity<EnderecoDTO> editarEndereco (@RequestBody EnderecoDTO enderecoDTO,
                                                       @RequestParam("Id") Long id){
        return ResponseEntity.ok(usuarioService.atualizaEndereco(id,enderecoDTO));
    }

    @PutMapping("/telefone")
    @Operation(summary = "Edita telefone dos usuários", description = "atualiza os dados do telefone do usuário")
    @ApiResponse(responseCode = "200", description = "dados de telefone editado com sucesso")
    @ApiResponse(responseCode = "403", description = "usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro servidor")
    @ApiResponse(responseCode = "401", description = "Credenciais invalidas")
    public ResponseEntity<TelefoneDTO> editarTelefone(@RequestBody TelefoneDTO telefoneDTO,
                                                      @RequestParam("Id") Long id){
        return ResponseEntity.ok(usuarioService.atualizarTelefone(id, telefoneDTO));
    }

    @PostMapping("/endereco")
    @Operation(summary = "Adiciona endereço", description = "adiciona um endereço ao usuário")
    @ApiResponse(responseCode = "200", description = "endereço inserido com sucesso")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro servidor")
    @ApiResponse(responseCode = "401", description = "Credenciais invalidas")
    public ResponseEntity<EnderecoDTO> adicionarEndereco(@RequestBody EnderecoDTO enderecoDTO,
                                                         @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.adicionarEndereco(enderecoDTO,token));
    }

    @PostMapping("/telefone")
    @Operation(summary = "Adiciona Telefone", description = "adiciona um telefone ao usuário")
    @ApiResponse(responseCode = "200", description = "telefone inserido com sucesso")
    @ApiResponse(responseCode = "403", description = "Usuário não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro servidor")
    @ApiResponse(responseCode = "401", description = "Credenciais invalidas")
    public ResponseEntity<TelefoneDTO> adicionarTelefone(@RequestBody TelefoneDTO telefoneDTO,
                                                         @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.adicionarTelefone(telefoneDTO,token));
    }

    @GetMapping("/endereco/{cep}")
    public  ResponseEntity<ViaCepDTO> buscarDadosCep(@PathVariable ("cep") String cep){
        return ResponseEntity.ok(client.buscarDadosEndereco(cep));
    }






}

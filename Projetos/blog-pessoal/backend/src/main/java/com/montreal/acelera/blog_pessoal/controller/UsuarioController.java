/**
 * Controller responsável por gerenciar as operações relacionadas aos usuários.
 * 
 * Endpoints disponíveis:
 * - POST /api/usuario/cadastrar: Cadastra um novo usuário.
 * - PUT /api/usuario/{id}: Atualiza as informações de um usuário existente.
 * - DELETE /api/usuario/{id}: Remove um usuário pelo ID.
 * 
 * Dependências:
 * - UsuarioService: Serviço responsável pela lógica de negócios relacionada aos usuários.
 * 
 * Exceções:
 * - ServiceException: Lançada em caso de erro no serviço.
 * - InvalidRequestException: Lançada em caso de requisição inválida.
 * - IllegalArgumentException: Lançada em caso de argumentos inválidos.
 */
package com.montreal.acelera.blog_pessoal.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.montreal.acelera.blog_pessoal.dto.requests.UsuarioDTO;
import com.montreal.acelera.blog_pessoal.dto.responses.UsuarioResponseDTO;
import com.montreal.acelera.blog_pessoal.exeception.InvalidRequestException;
import com.montreal.acelera.blog_pessoal.exeception.ServiceException;
import com.montreal.acelera.blog_pessoal.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioController extends BaseController {

    private final UsuarioService usuarioService;

    /**
     * Cadastra um novo usuário.
     * 
     * @param usuarioDTO Os dados do usuário a serem cadastrados.
     * @return A resposta com os dados do usuário cadastrado.
     * @throws ServiceException        Se ocorrer um erro no serviço.
     * @throws InvalidRequestException Se a requisição for inválida.
     */
    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO)
            throws ServiceException, InvalidRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrarUsuario(usuarioDTO));
    }

    /**
     * Atualiza as informações de um usuário existente.
     * 
     * @param id         O ID do usuário a ser atualizado.
     * @param usuarioDTO Os novos dados do usuário.
     * @return A resposta com os dados do usuário atualizado.
     * @throws ServiceException        Se ocorrer um erro no serviço.
     * @throws InvalidRequestException Se a requisição for inválida.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> alterarUsuario(@PathVariable Long id,
            @RequestBody @Valid UsuarioDTO usuarioDTO)
            throws ServiceException, InvalidRequestException {
        return ResponseEntity.ok(usuarioService.atualizarUsuario(usuarioDTO, id));
    }

    /**
     * Remove um usuário pelo ID.
     * 
     * @param id O ID do usuário a ser removido.
     * @return A resposta sem conteúdo (204 No Content).
     * @throws ServiceException         Se ocorrer um erro no serviço.
     * @throws InvalidRequestException  Se a requisição for inválida.
     * @throws IllegalArgumentException Se o ID for inválido.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id)
            throws ServiceException, InvalidRequestException, IllegalArgumentException {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar-por-id/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorId(@PathVariable Long id) 
            throws ServiceException, InvalidRequestException, IllegalArgumentException {
        UsuarioResponseDTO usuarioEncontrado = usuarioService.buscarUsuarioPorId(id);
        return ResponseEntity.ok(usuarioEncontrado);
    }
    

    @GetMapping("/buscar-por-usuario/{usuario}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorUsuario (@PathVariable String usuario) 
            throws ServiceException, InvalidRequestException, IllegalArgumentException {
        UsuarioResponseDTO usuarioEncontrado = usuarioService.buscarUsuarioPorUsuario(usuario);
        return ResponseEntity.ok(usuarioEncontrado);
    }
    
}

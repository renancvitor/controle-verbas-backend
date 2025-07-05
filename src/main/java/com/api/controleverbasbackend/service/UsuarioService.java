package com.api.controleverbasbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.controleverbasbackend.domain.pessoa.Pessoa;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEntidade;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.usuario.DadosAtualizacaoUsuarioSenha;
import com.api.controleverbasbackend.dto.usuario.DadosCadastroUsuario;
import com.api.controleverbasbackend.dto.usuario.DadosDetalhamentoUsuario;
import com.api.controleverbasbackend.dto.usuario.DadosListagemUsuario;
import com.api.controleverbasbackend.infra.exception.ValidacaoException;
import com.api.controleverbasbackend.repository.TipoUsuarioRepository;
import com.api.controleverbasbackend.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    public Page<DadosListagemUsuario> listar(Pageable pageable, Usuario usuario) {
        return usuarioRepository.findAll(pageable).map(DadosListagemUsuario::new);
    }

    @Transactional
    public void cadastrar(Pessoa pessoa, DadosCadastroUsuario dadosCadastroUsuario) {
        String senhaCriptografada = passwordEncoder.encode(dadosCadastroUsuario.senha());

        TipoUsuarioEntidade tipoUsuario = tipoUsuarioRepository.findById(TipoUsuarioEnum.COMUM.getId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil do usuário padrão não encontrado."));

        Usuario usuario = new Usuario(senhaCriptografada, pessoa, tipoUsuario);

        usuarioRepository.save(usuario);
    }

    @Transactional
    public DadosDetalhamentoUsuario atualizarSenha(Long id, DadosAtualizacaoUsuarioSenha dados, Usuario usuarioLogado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario com ID " + id + " náo encontrado."));

        if (!usuarioLogado.getId().equals(id)) {
            throw new RuntimeException("Você só pode alterar sua própria senha.");
        }

        if (!passwordEncoder.matches(dados.senhaAtual(), usuario.getSenha())) {
            throw new ValidacaoException("A senha atual está incorreta.");
        }
        if (!dados.novaSenha().equals(dados.confirmarNovaSenha())) {
            throw new ValidacaoException("A nova senha e a confirmação não coincidem.");
        }

        String novaSenhaCriptografada = passwordEncoder.encode(dados.novaSenha());
        usuario.atualizarSenha(novaSenhaCriptografada);

        return new DadosDetalhamentoUsuario(usuario);
    }
}

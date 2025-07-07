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
import com.api.controleverbasbackend.dto.usuario.DadosAtualizacaoUsuarioTipo;
import com.api.controleverbasbackend.dto.usuario.DadosCadastroUsuario;
import com.api.controleverbasbackend.dto.usuario.DadosDetalhamentoUsuario;
import com.api.controleverbasbackend.dto.usuario.DadosListagemUsuario;
import com.api.controleverbasbackend.infra.exception.AutorizacaoException;
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

    public Page<DadosListagemUsuario> listar(Pageable pageable, Usuario usuario, Boolean ativo) {
        if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId())) {
            throw new AutorizacaoException("Apenas o admin pode listar usuários cadastrados.");
        }

        Boolean filtro = (ativo != null) ? ativo : true;
        return usuarioRepository.findAllByAtivo(filtro, pageable).map(DadosListagemUsuario::new);
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
        Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(id)
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

    @Transactional
    public DadosDetalhamentoUsuario atualizarUsuarioTipo(Long id, DadosAtualizacaoUsuarioTipo dados,
            Usuario usuarioLogado) {
        Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario com ID " + id + " náo encontrado."));

        if (!usuarioLogado.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId())) {
            throw new AutorizacaoException("Apenas o admin pode atualizar pessoas.");
        }
        if (usuario.getTipoUsuario().getId() == null) {
            throw new ValidacaoException("O tipo do usuário deve ser preenchido corretamente.");
        }

        usuario.atualizarUsuarioTipo(dados, tipoUsuarioRepository);
        return new DadosDetalhamentoUsuario(usuario);
    }

    @Transactional
    public void deletar(Long id, Usuario usuarioLogado) {
        Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        if (!usuarioLogado.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId())) {
            throw new AutorizacaoException("Apenas o ADMIN pode deletar um usuário.");
        }

        usuario.setAtivo(false);
    }

    @Transactional
    public void ativar(Long id, Usuario usuarioLogado) {
        Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        if (!usuarioLogado.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId())) {
            throw new AutorizacaoException("Apenas o ADMIN pode ativar um usuário.");
        }

        usuario.setAtivo(true);
    }
}

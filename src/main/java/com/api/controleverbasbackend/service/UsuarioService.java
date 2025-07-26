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
        if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId()) &&
                !usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.TESTER.getId())) {
            throw new AutorizacaoException("Apenas o admin pode listar usuários cadastrados.");
        }

        if (ativo != null) {
            return usuarioRepository.findAllByAtivo(ativo, pageable).map(DadosListagemUsuario::new);
        }
        return usuarioRepository.findAll(pageable).map(DadosListagemUsuario::new);
    }

    @Transactional
    public void cadastrar(Pessoa pessoa, DadosCadastroUsuario dadosCadastroUsuario) {
        if (usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.TESTER.getId())) {
            throw new AutorizacaoException("Usuário TESTER não pode alterar dados.");
        }

        String senhaCriptografada = passwordEncoder.encode(dadosCadastroUsuario.senha());

        TipoUsuarioEntidade tipoUsuario = tipoUsuarioRepository.findById(TipoUsuarioEnum.COMUM.getId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil do usuário padrão não encontrado."));

        Usuario usuario = new Usuario(senhaCriptografada, pessoa, tipoUsuario);

        usuarioRepository.save(usuario);
    }

    public boolean senhaForte(String senha) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%#?&])[A-Za-z\\d@$!%#?&]{8,}$";
        return senha != null && senha.matches(regex);
    }

    @Transactional
    public DadosDetalhamentoUsuario atualizarSenha(Long id, DadosAtualizacaoUsuarioSenha dados, Usuario usuarioLogado) {
        Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario com ID " + id + " náo encontrado."));

        if (usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.TESTER.getId())) {
            throw new AutorizacaoException("Usuário TESTER não pode alterar dados.");
        }

        if (!usuarioLogado.getId().equals(id)) {
            throw new RuntimeException("Você só pode alterar sua própria senha.");
        }

        if (!passwordEncoder.matches(dados.senhaAtual(), usuario.getSenha())) {
            throw new ValidacaoException("A senha atual está incorreta.");
        }
        if (!dados.novaSenha().equals(dados.confirmarNovaSenha())) {
            throw new ValidacaoException("A nova senha e a confirmação não coincidem.");
        }

        if (!senhaForte(dados.novaSenha())) {
            throw new ValidacaoException("A nova senha não atende aos requisitos de segurança: " +
                    "Mínimo 8 caracteres, pelo menos 1 letra maiúscula, 1 minúscula, 1 número e 1 caractere especial.");
        }

        String novaSenhaCriptografada = passwordEncoder.encode(dados.novaSenha());
        usuario.atualizarSenha(novaSenhaCriptografada);

        usuario.setPrimeiroAcesso(false);

        return new DadosDetalhamentoUsuario(usuario);
    }

    @Transactional
    public DadosDetalhamentoUsuario atualizarUsuarioTipo(Long id, DadosAtualizacaoUsuarioTipo dados,
            Usuario usuarioLogado) {
        Usuario usuario = usuarioRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario com ID " + id + " náo encontrado."));

        if (usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.TESTER.getId())) {
            throw new AutorizacaoException("Usuário TESTER não pode alterar dados.");
        }

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

        if (usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.TESTER.getId())) {
            throw new AutorizacaoException("Usuário TESTER não pode alterar dados.");
        }

        if (!usuarioLogado.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId())) {
            throw new AutorizacaoException("Apenas o admin pode deletar um usuário.");
        }

        usuario.setAtivo(false);
    }

    @Transactional
    public void ativar(Long id, Usuario usuarioLogado) {
        Usuario usuario = usuarioRepository.findByIdAndAtivoFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        if (usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.TESTER.getId())) {
            throw new AutorizacaoException("Usuário TESTER não pode alterar dados.");
        }

        if (!usuarioLogado.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId())) {
            throw new AutorizacaoException("Apenas o admin pode ativar um usuário.");
        }

        usuario.setAtivo(true);
    }
}

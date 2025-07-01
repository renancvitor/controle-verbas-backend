package com.api.controleverbasbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.controleverbasbackend.domain.pessoa.Pessoa;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEntidade;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.usuario.DadosCadastroUsuario;
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

    @Transactional
    public void cadastrar(Pessoa pessoa, DadosCadastroUsuario dadosCadastroUsuario) {
        String senhaCriptografada = passwordEncoder.encode(dadosCadastroUsuario.senha());

        TipoUsuarioEntidade tipoUsuario = tipoUsuarioRepository.findById(TipoUsuarioEnum.COMUM.getId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil do usuário padrão não encontrado."));

        Usuario usuario = new Usuario(senhaCriptografada, pessoa, tipoUsuario);

        usuarioRepository.save(usuario);
    }
}

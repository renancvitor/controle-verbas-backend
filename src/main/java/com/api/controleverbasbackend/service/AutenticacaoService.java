package com.api.controleverbasbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.api.controleverbasbackend.domain.entity.usuario.Usuario;
import com.api.controleverbasbackend.dto.autenticacao.DadosLogin;
import com.api.controleverbasbackend.dto.autenticacao.DadosTokenJWT;
import com.api.controleverbasbackend.dto.usuario.DadosResumidoUsuario;
import com.api.controleverbasbackend.repository.UsuarioRepository;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByPessoaEmailAndAtivoTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
    }

    public DadosTokenJWT autenticacao(DadosLogin dados, AuthenticationManager manager) {
        var token = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        Authentication autenticacao = manager.authenticate(token);

        Usuario usuario = (Usuario) autenticacao.getPrincipal();
        String jwt = tokenService.gerarToken(usuario);

        DadosResumidoUsuario usuarioDTO = new DadosResumidoUsuario(usuario);

        boolean primeiroAcesso = usuario.getPrimeiroAcesso();

        return new DadosTokenJWT(jwt, usuarioDTO, primeiroAcesso);
    }
}

<h1 align="center"> Organização completa do Projeto</h1>

```plaintext
docs
 ├── gifs
 │    ├── atualizacao-de-pessoas-e-usuarios.gif
 │    ├── cadastro-de-orcamento.gif
 │    ├── cadastro-de-pessoa.gif
 │    ├── documentacao-completa-e-interativa.gif
 │    ├── exclusao-segura-ativar-pessoas-e-usuarios.gif
 │    ├── listar-cargos-e-departamentos.gif
 │    ├── listar-orcamentos.gif
 │    ├── listar-pessoas-e-usuarios.gif
 │    ├── login-via-api-com-jwt.gif
 │    └── reprovar-aprovar-liberar-orcamentos.gif
 ├── documentacao-swagger.md
 ├── project-structure.md
 └── supabase-schema-ggttmuvdnqhlfqrtxuha.png

src/main
 ├── java/com/api/controleverbasbackend
 │    ├── controller
 │    │    ├── AutenticacaoController.java
 │    │    ├── CargoController.java
 │    │    ├── DepartamentoController.java
 │    │    ├── OrcamentoController.java
 │    │    ├── PessoaController.java
 │    │    └── UsuarioController.java
 │    ├── domain
 │    │    ├── entity
 │    │    │    ├── cargo
 │    │    │    │    └── Cargo.java
 │    │    │    ├── departamento
 │    │    │    │    └── Departamento.java
 │    │    │    ├── orcamento
 │    │    │    │    ├── Orcamento.java
 │    │    │    │    └── StatusOrcamentoEntidade.java
 │    │    │    ├── pessoa
 │    │    │    │    └── Pessoa.java
 │    │    │    ├── sistemalog
 │    │    │    │    └── SistemaLog.java
 │    │    │    └── usuario
 │    │    │         ├── PermissaoEntidade.java
 │    │    │         ├── TipoUsuarioEntidade.java
 │    │    │         └── Usuario.java
 │    │    └── enums
 │    │         ├── orcamento
 │    │         │    └── StatusOrcamentoEnum.java
 │    │         ├── sistemalog
 │    │         │    └── TipoLog.java
 │    │         └── usuario
 │    │              ├── PermissaoEnum.java
 │    │              └── TipoUsuarioEnum.java
 │    ├── dto
 │    │    ├── autenticacao
 │    │    │    ├── DadosLogin.java
 │    │    │    └── DadosTokenJWT.java
 │    │    ├── cargo
 │    │    │    ├── DadosAtualizacaoCargo.java
 │    │    │    ├── DadosCadastroCargo.java
 │    │    │    ├── DadosDetalhamentoCargo.java
 │    │    │    └── DadosListagemCargo.java
 │    │    ├── departamento
 │    │    │    ├── DadosAtualizacaoDepartamento.java
 │    │    │    ├── DadosCadastroDepartamento.java
 │    │    │    ├── DadosDetalhamentoDepartamento.java
 │    │    │    └── DadosListagemDepartamento.java
 │    │    ├── orcamento
 │    │    │    ├── DadosCadastroOrcamento.java
 │    │    │    ├── DadosDetalhamentoOrcamento.java
 │    │    │    └── DadosListagemOrcamento.java
 │    │    ├── pessoa
 │    │    │    ├── DadosAtualizacaoPessoa.java
 │    │    │    ├── DadosCadastroPessoa.java
 │    │    │    ├── DadosCadastroPessoaUsuario.java
 │    │    │    ├── DadosDetalhamentoPessoa.java
 │    │    │    └── DadosListagemPessoa.java
 │    │    └── usuario
 │    │         ├── DadosAtualizacaoUsuarioSenha.java
 │    │         ├── DadosAtualizacaoUsuarioTipo.java
 │    │         ├── DadosCadastroUsuario.java
 │    │         ├── DadosDetalhamentoUsuario.java
 │    │         ├── DadosListagemUsuario.java
 │    │         └── DadosResumidoUsuario.java
 │    ├── exception
 │    │    ├── AutorizacaoException.java
 │    │    ├── NaoEncontradoException.java
 │    │    ├── TratadorDeErros.java
 │    │    └── ValidacaoException.java
 │    ├── infra
 │    │    ├── config
 │    │    │    └── WebConfig.java
 │    │    ├── documentation
 │    │    │    └── SpringDocConfigurations.java
 │    │    ├── logging
 │    │    │    ├── LogAspect.java
 │    │    │    ├── Loggable.java
 │    │    │    └── Loggables.java
 │    │    ├── messaging
 │    │    │    ├── LogConsumer.java
 │    │    │    └── LogProducer.java
 │    │    └── security
 │    │         ├── SecurityConfiguration.java
 │    │         └── SecurityFilter.java
 │    ├── repository
 │    │    ├── CargoRepository.java
 │    │    ├── DepartamentoRepository.java
 │    │    ├── OrcamentoRepository.java
 │    │    ├── PessoaRepository.java
 │    │    ├── StatusOrcamentoRepository.java
 │    │    ├── TipoUsuarioRepository.java
 │    │    ├── SistemaLogRepository.java
 │    │    └── UsuarioRepository.java
 │    ├── service
 │    │    ├── AutenticacaoService.java
 │    │    ├── CargoService.java
 │    │    ├── DepartamentoService.java
 │    │    ├── OrcamentoService.java
 │    │    ├── PessoaService.java
 │    │    ├── TokenService.java
 │    │    └── UsuarioService.java
 │    └── ControleVerbasBackendApplication.java
 ├── resources
 │    ├── db
 │    │    ├── V1__create_table_cargos.sql
 │    │    ├── V2__create_table_departamentos.sql
 │    │    ├── V3__create_table_pessoas.sql
 │    │    ├── V4__create_table_permissoes.sql
 │    │    ├── V5__create_table_tipos_usuarios.sql
 │    │    ├── V6__create_table_tipos_usuarios_permissoes.sql
 │    │    ├── V7__seed_tipos_usuarios_e_permissoes.sql
 │    │    ├── V8__create_table_usuarios.sql
 │    │    ├── V9__insert_admin_user.sql
 │    │    ├── V10__add_column_ativo_em_tabelas.sql
 │    │    ├── V11__create_table_status_orcamentos.sql
 │    │    ├── V12__create_table_orcamentos.sql
 │    │    ├── V13__seed_status_orcamentos.sql
 │    │    ├── V14__add_column_liberado_em_orcamento.sql
 │    │    ├── V15__drop_column_liberado_em_orcamentos.sql
 │    │    ├── V16__create_table_sistema_logs.sql
 │    │    ├── V17__alter_sistema_logs_table.sql
 │    │    ├── V18__alter_table_usuarios_add_column.sql
 │    │    ├── V19__insert_tipo_usuario_TESTER.sql
 │    │    ├── V20__insert_tester_user.sql
 │    │    └── V21__update_tester_user.sql
 │    ├── application-dev.properties
 │    ├── application-prod.properties
 │    ├── application-test.properties
 │    └── application.properties
 ├── test/java/com/api/controleverbasbackend
 │    ├── controller
 │    │    ├── negativo
 │    │    │    ├── AutenticacaoControllerTestNegativo.java
 │    │    │    ├── CargoControllerTestNegativo.java
 │    │    │    ├── DepartamentoControllerTestNegativo.java
 │    │    │    ├── OrcamentoControllerTestNegativo.java
 │    │    │    ├── PessoaControllerTestNegativo.java
 │    │    │    └── UsuarioControllerTestNegativo.java
 │    │    └── positivo
 │    │         ├── AutenticacaoControllerTestPositivo.java
 │    │         ├── CargoControllerTestPositivo.java
 │    │         ├── DepartamentoControllerTestPositivo.java
 │    │         ├── OrcamentoControllerTestPositivo.java
 │    │         ├── PessoaControllerTestPositivo.java
 │    │         └── UsuarioControllerTestPositivo.java
 │    ├── kafka
 │    │    └── KafkaTestController.java
 │    ├── service
 │    │    ├── negativo
 │    │    │    ├── AutenticacaoServiceTestNegativo.java
 │    │    │    ├── CargoServiceTestNegativo.java
 │    │    │    ├── DepartamentoServiceTestNegativo.java
 │    │    │    ├── OrcamentoServiceTestNegativo.java
 │    │    │    ├── PessoaServiceTestNegativo.java
 │    │    │    ├── TokenServiceTestNegativo.java
 │    │    │    └── UsuarioServiceTestNegativo.java
 │    │    └── positivo
 │    │         ├── AutenticacaoServiceTestPositivo.java
 │    │         ├── CargoServiceTestPositivo.java
 │    │         ├── DepartamentoServiceTestPositivo.java
 │    │         ├── OrcamentoServiceTestPositivo.java
 │    │         ├── PessoaServiceTestPositivo.java
 │    │         ├── TokenServiceTestPositivo.java
 │    │         └── UsuarioServiceTestPositivo.java
 │    ├── utils
 │    │    └── MockUtils.java
 │    └── ControleVerbasBackendApplicationTests.java
 ├── LICENSE
 └── README.md
 ```

 > Estrutura atualizada em: Agosto/2025
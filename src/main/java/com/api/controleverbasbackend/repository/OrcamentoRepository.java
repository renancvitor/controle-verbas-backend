package com.api.controleverbasbackend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.controleverbasbackend.domain.entity.orcamento.Orcamento;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

    @Query("""
            SELECT o FROM Orcamento o
            WHERE
                o.statusOrcamentoEntidade.id = :aprovadoId
                OR (o.solicitante.id = :idUsuario AND o.statusOrcamentoEntidade.id IN (:enviadoId, :reprovadoId))
                    """)
    Page<Orcamento> findAprovadosOuPropriosNaoAprovados(
            @Param("idUsuario") Long idUsuario,
            @Param("aprovadoId") Integer aprovadoId,
            @Param("enviadoId") Integer enviadoId,
            @Param("reprovadoId") Integer reprovadoId,
            Pageable pageable);

    Page<Orcamento> findBySolicitanteId(Long id, Pageable pageable);

    Page<Orcamento> findByStatusOrcamentoEntidadeId(Integer integer, Pageable pageable);

    @Query("""
            SELECT o FROM Orcamento o
            WHERE
                (:statusId IS NULL OR o.statusOrcamentoEntidade.id = :statusId)
                AND (
                    (:statusId = :aprovadoId)
                    OR (o.solicitante.id = :idUsuario AND o.statusOrcamentoEntidade.id IN (:enviadoId, :reprovadoId))
                )
                    """)
    Page<Orcamento> findAprovadosOuPropriosFiltrandoStatus(
            @Param("idUsuario") Long idUsuario,
            @Param("statusId") Integer statusId,
            @Param("aprovadoId") Integer aprovadoId,
            @Param("enviadoId") Integer enviadoId,
            @Param("reprovadoId") Integer reprovadoId,
            Pageable pageable);

    Page<Orcamento> findBySolicitanteIdAndStatusOrcamentoEntidadeId(Long solicitanteId, Integer statusId,
            Pageable pageable);

    Optional<Orcamento> findByIdAndVerbaLiberadaFalse(Long id);

}

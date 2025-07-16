package br.com.uaitagcartaoconvenio.cartaoconvenio.repository;

import br.com.uaitagcartaoconvenio.cartaoconvenio.model.VigenciaContratoEntidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusContrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VigenciaContratoEntidadeRepository extends JpaRepository<VigenciaContratoEntidade, Long> {

    // Buscar todas as vigencias de uma entidade
    @Query("SELECT v FROM VigenciaContratoEntidade v WHERE v.contratoEntidade.entidade.idEntidade = :idEntidade")
    List<VigenciaContratoEntidade> findByEntidadeId(@Param("idEntidade") Long idEntidade);

    // Buscar vigencias de uma entidade por status
    @Query("SELECT v FROM VigenciaContratoEntidade v WHERE v.contratoEntidade.entidade.idEntidade = :idEntidade AND v.descStatusContrato = :status")
    List<VigenciaContratoEntidade> findByEntidadeIdAndStatus(@Param("idEntidade") Long idEntidade, @Param("status") StatusContrato status);

    // Buscar vigência atual (entre datas ou a mais recente)
    @Query("SELECT v FROM VigenciaContratoEntidade v WHERE v.contratoEntidade.entidade.idEntidade = :idEntidade " +
           "AND :dataAtual BETWEEN v.dtInicio AND v.dtFinal ORDER BY v.dtInicio DESC")
    List<VigenciaContratoEntidade> findVigenciaAtualByEntidadeId(@Param("idEntidade") Long idEntidade, @Param("dataAtual") LocalDate dataAtual);

    // Buscar última vigência (max ID)
    @Query("SELECT v FROM VigenciaContratoEntidade v WHERE v.contratoEntidade.entidade.idEntidade = :idEntidade ORDER BY v.idVigenciaContratoEntidade DESC")
    List<VigenciaContratoEntidade> findLastVigenciaByEntidadeId(@Param("idEntidade") Long idEntidade);

    // Verificar se existe vigência vigente para um contrato
    Optional<VigenciaContratoEntidade> findFirstByContratoEntidadeIdContratoEntidadeAndDescStatusContratoOrderByIdVigenciaContratoEntidadeDesc(
            Long idContratoEntidade, StatusContrato status);
}
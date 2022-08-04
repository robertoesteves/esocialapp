package br.gov.camara.esocial.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.gov.camara.esocial.model.SigHistoricoServidorGrupo;

@Repository
public interface SigHistoricoServidorGrupoRepository extends JpaRepository<SigHistoricoServidorGrupo, Long> {
	
	@Query("select histservgrupo from SigHistoricoServidorGrupo histservgrupo  "+
			"where trunc(histservgrupo.datiniciohistorico) <=  ?1 "+
			"and ( (histservgrupo.datfimhistorico is null) or (trunc(histservgrupo.datfimhistorico) > ?1) ) "+
			"and histservgrupo.grupo.codobjeto = ?2  "+
			"and histservgrupo.datcancelamento is null "+

			"and ( (histservgrupo.servidor.datfimhistorico is null) or (histservgrupo.servidor.datfimhistorico >= histservgrupo.servidor.datiniciohistorico) ) "+
			//verificar essa situação: datfimhistorico < datiniciohistorico
			//"and histservgrupo.servidor.numponto  between  10000  and 11000 "+
			"order by histservgrupo.servidor.numponto ")	
	List<SigHistoricoServidorGrupo>  findCadastroInicial(LocalDate inicio, String codgrupo);
	

}

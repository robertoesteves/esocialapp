package br.gov.camara.esocial.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.gov.camara.esocial.model.SigHistoricoBeneficioPC;

@Repository
public interface SigHistoricoBeneficioPCRepository extends JpaRepository<SigHistoricoBeneficioPC, Long>{
	
	
	  @Query("select histbeneficiopensao from SigHistoricoBeneficioPC histbeneficiopensao  "+
			"where trunc(histbeneficiopensao.datiniciohistorico) <=  ?1 "+
			"and ( (histbeneficiopensao.datfimhistorico is null) or (trunc(histbeneficiopensao.datfimhistorico) > ?1) ) "+
			"and histbeneficiopensao.beneficiariopensaocivil.servidor.grupo.codobjeto = ?2  "+
			"and histbeneficiopensao.datcancelamento is null "+
			"and histbeneficiopensao.beneficiariopensaocivil.datcancelamento is null "+
			//"and histbeneficiopensao.beneficiariopensaocivil.servidor.numponto  = 41814  "+

			"order by histbeneficiopensao.beneficiariopensaocivil.servidor.numponto ")	
			List<SigHistoricoBeneficioPC>  findCadastroInicial(LocalDate inicio, String codgrupo);
		
	
	//  @Query("select histbeneficiopensao from SigHistoricoBeneficioPC histbeneficiopensao  ")	
 //	List<SigHistoricoBeneficioPC>  findCadastroInicial();

}

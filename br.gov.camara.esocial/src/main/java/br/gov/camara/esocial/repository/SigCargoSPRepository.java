package br.gov.camara.esocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.camara.esocial.model.SigCargoComissionadoSP;





@Repository
public interface SigCargoSPRepository extends JpaRepository<SigCargoComissionadoSP, Long> {

}


package br.gov.camara.esocial.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.camara.esocial.model.SigHistoricoRequisicaoServidor;
import br.gov.camara.esocial.model.SigHistoricoServidorRegimePrev;
import br.gov.camara.esocial.repository.SigHistoricoRequisicaoServidorRepository;
import br.gov.camara.esocial.repository.SigHistoricoServidorRegimePrevRepository;

@Service
public class SigHistoricoServidorRegimePrevService {
	
	
	@Autowired
	private SigHistoricoServidorRegimePrevRepository regimeprevdao;
	
	public List<SigHistoricoServidorRegimePrev>  buscaregimeprevidenciario(int numponto, LocalDate datbase){
		
		
		List<SigHistoricoServidorRegimePrev> regimesprev= new   ArrayList<SigHistoricoServidorRegimePrev>();

	
		regimesprev = regimeprevdao.findRegimePrevnaData(numponto, datbase);
				System.out.println(regimesprev.size());
		
		return regimesprev;
	}

}

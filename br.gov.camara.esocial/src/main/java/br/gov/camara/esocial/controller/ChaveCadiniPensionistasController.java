package br.gov.camara.esocial.controller;



import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.bea.xml.stream.samples.Parse;
import com.google.gson.Gson;

import br.gov.camara.esocial.dto.AtributoCadini;
import br.gov.camara.esocial.dto.ChaveDTOCadini;
import br.gov.camara.esocial.dto.ChaveDTOEventoNP;
import br.gov.camara.esocial.dto.ChaveEventoDTO;
import br.gov.camara.esocial.model.SigAfastamento;
import br.gov.camara.esocial.model.SigGrupo;
import br.gov.camara.esocial.model.SigHistoricoBeneficioPC;
import br.gov.camara.esocial.model.SigHistoricoRequisicaoServidor;
import br.gov.camara.esocial.model.SigHistoricoServidorCargoSP;
import br.gov.camara.esocial.model.SigHistoricoServidorGrupo;
import br.gov.camara.esocial.service.AfastamentoService;
import br.gov.camara.esocial.service.ChaveEventoService;
import br.gov.camara.esocial.service.GrupoService;
import br.gov.camara.esocial.service.HistoricoServidorCargoSpService;
import br.gov.camara.esocial.service.HistoricoServidorGrupoService;
import br.gov.camara.esocial.service.HistoricoServidorRequisicaoService;
import br.gov.camara.esocial.service.SigHistoricoBeneficioPCService;



@RestController
@RequestMapping("/cadpensionistas")
public class ChaveCadiniPensionistasController {

	public static HttpHeaders headersJson() {
		HttpHeaders headers = new HttpHeaders();	
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	    return headers;
	}
	
	List<SigAfastamento>  afastamentos;
	
	@Autowired
	private ChaveEventoService chaveservice;
	
	@Autowired
	private GrupoService gruposervice;
	List<SigGrupo>  grupos;
	SigGrupo  grupo;
	ChaveEventoDTO chave;
	SigHistoricoServidorGrupo historico;
	
	private List<SigHistoricoBeneficioPC> historicobeneficiopc;
	
	@Autowired
	private SigHistoricoBeneficioPCService historicobeneficiopcservice;
	
	


	@GetMapping("/abrecadastroinicial/{codtipochave}")
	public  ModelAndView abrecadastroinicial(@PathVariable(value="codtipochave") String codtipochave) {
		String msgvalidacao = "Evento não Localizado";
		String titulo = codtipochave+" -  Cadastro inicial ";
		chave = new ChaveEventoDTO();
		chave.setDatreferencia(LocalDate.of(2021, 11, 22));
		chave.setCodtipochave("S2410");
	
		grupos = gruposervice.gruposPensionistas();
		grupo = grupos.get(0);
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastroinicialpensionistas");
		modelAndView.addObject("grupos", grupos);
		modelAndView.addObject("grupo", grupo);
		modelAndView.addObject("chave", chave);
		modelAndView.addObject("titulo", titulo);
		modelAndView.addObject("msgvalidacao", msgvalidacao);
		
		return modelAndView;
		
	}
	
	
	@PostMapping("/listacadastroinicial")
	public ModelAndView  listacadastroinicial(SigGrupo grupo ) {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastroinicialpensionistas");
		
		historicobeneficiopc = new ArrayList<SigHistoricoBeneficioPC>();
	
	
		historicobeneficiopc = historicobeneficiopcservice.cadastroInicial(chave.getDatreferencia(), grupo.getCodobjeto());


		String msgvalidacao = "Evento Localizado";
		String titulo = chave.getCodtipochave()+" -  Cadastro inicial ";
		grupos = gruposervice.gruposPensionistas();
		grupo = grupos.get(0);
		modelAndView.addObject("historicobeneficiopc", historicobeneficiopc);
		modelAndView.addObject("chave", chave);
		modelAndView.addObject("grupo", grupo);
		modelAndView.addObject("grupos", grupos);
		modelAndView.addObject("titulo", titulo);
		modelAndView.addObject("msgvalidacao", msgvalidacao);
		
		return modelAndView;
		
	}
	
	
	@PostMapping("/geracadastroinicial")
	public ModelAndView GeraCadastroInicial(SigGrupo grupo) {

		for(SigHistoricoBeneficioPC hcc : historicobeneficiopc){						
			chaveservice.CriaChaveCadastroInicialInativosPC(hcc, grupo);
	}

		
		for(SigHistoricoBeneficioPC hcc : historicobeneficiopc){						
				chaveservice.CriaChaveCadastroInicialBeneficariosPC(hcc, grupo);
		}

		chave = new ChaveEventoDTO();
		chave.setDatreferencia(LocalDate.of(2021, 11, 22));
		chave.setCodtipochave("S2410");
	
			   	ModelAndView modelAndView = new ModelAndView("cadastro/cadastroinicialpensionistas");
			 	String msgvalidacao = "Evento Localizado";
				String titulo = chave.getCodtipochave()+" -  Cadastro inicial ";
				grupos = gruposervice.gruposPensionistas();
				modelAndView.addObject("historicobeneficiopc", historicobeneficiopc);
				modelAndView.addObject("chave", chave);
				modelAndView.addObject("grupo", grupo);
				modelAndView.addObject("grupos", grupos);
				modelAndView.addObject("titulo", titulo);
				modelAndView.addObject("msgvalidacao", msgvalidacao);
				
				return modelAndView;			 			 
			 
		    }
	

	

public List<LocalDate>  preenchedata() {
		
		List<LocalDate> resultado = new ArrayList<LocalDate>();
		LocalDate datinicio = LocalDate.now();
		LocalDate datfim = LocalDate.now();
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String texdia = "01";
		String texmes = "";
		
		int ano = Integer.valueOf(String.valueOf(datfim.getYear()));
		int mes = Integer.valueOf(String.valueOf(datfim.getMonthValue()));
		if (mes<10) {texmes="0"+Integer.toString(mes);}
		else
		{texmes=Integer.toString(mes);}
		
		String texdata = texdia+"/"+texmes+"/"+Integer.toString(ano);
		
		datfim = LocalDate.parse(texdata,formatter);
		datfim = datfim.plusDays(-1);

		ano = Integer.valueOf(String.valueOf(datfim.getYear()));
		mes = Integer.valueOf(String.valueOf(datfim.getMonthValue()));
		if (mes<10) {texmes="0"+Integer.toString(mes);}
		else
		{texmes=Integer.toString(mes);}
		 texdata = texdia+"/"+texmes+"/"+Integer.toString(ano);
		datinicio = LocalDate.parse(texdata,formatter);
		resultado.add(datinicio);
		resultado.add(datfim);
		
	
		return resultado;
	}


}

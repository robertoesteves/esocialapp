package br.gov.camara.esocial.service;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import br.gov.camara.esocial.dto.AtributoCadini;
import br.gov.camara.esocial.dto.AtributoEventoNP;

import br.gov.camara.esocial.dto.ChaveDTOCadini;
import br.gov.camara.esocial.dto.ChaveDTOEventoNP;
import br.gov.camara.esocial.dto.ChaveEventoDTO;
import br.gov.camara.esocial.model.SigAfastamento;
import br.gov.camara.esocial.model.SigGrupo;
import br.gov.camara.esocial.model.SigHistoricoBeneficioPC;
import br.gov.camara.esocial.model.SigHistoricoRequisicaoServidor;
import br.gov.camara.esocial.model.SigHistoricoServidorCargoSP;
import br.gov.camara.esocial.model.SigHistoricoServidorGrupo;
import br.gov.camara.esocial.model.SigHistoricoServidorRegimePrev;



@Service
public class ChaveEventoService {
	
	@Autowired
	private AfastamentoService afastamentoservice;

	
	@Autowired
	private SigHistoricoServidorRegimePrevService regimeprevservice;

	@Autowired
	private HistoricoServidorCargoSpService historicocargoservice;
	
	private List<SigHistoricoServidorRegimePrev> regimesprev;
	private List<SigHistoricoServidorCargoSP>  historicocargosp;

	
	private List<SigHistoricoServidorGrupo> historicogrupo;
	
	@Autowired
	private HistoricoServidorGrupoService historicoservgruposervice;
	
	
	List<SigGrupo>  grupos;
	SigGrupo  grupo;
	ChaveEventoDTO chave;
	SigHistoricoServidorGrupo historico;

	List<SigAfastamento>  afastamentos;
	
	
	public static HttpHeaders headersJson() {
		HttpHeaders headers = new HttpHeaders();	
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	    return headers;
	}
	
	
	public static boolean comparaDataIgual(LocalDate inicio, LocalDate fim) {

        Date date1 = Date.from(inicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date date2 = Date.from(fim.atStartOfDay(ZoneId.systemDefault()).toInstant());
       
        if (date1.equals(date2)) {
           return true;
        }else      
        {return false;
      
        }
}	
	

	
	DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	/*
	private void dormeUmPouco(int segundos) {
		try {
			segundos = segundos*1000;
			Thread.sleep(segundos);
		} catch (InterruptedException e) {}
	}	
	
	*/
	
	@Value("${RHESOCIALINTEGRACAO_URL}")
	private String urlEndPoint;


	public void gravaChave(ChaveDTOCadini chavedto)
	{
		Gson gson = new Gson();
		String json = gson.toJson(chavedto);
		 RestTemplate restTemplate = new RestTemplate();
			String uri = urlEndPoint+"chaves/avulsas";
			HttpEntity<String> entity = new HttpEntity<String>(json, headersJson());		
			entity = restTemplate.postForEntity(uri, entity, String.class);
	}
	
	

	public ChaveDTOCadini CriaChaveCadastroInicial(SigHistoricoServidorGrupo hcc, SigGrupo grupo, ChaveEventoDTO chave) {
		
		ChaveDTOCadini chavedto = new ChaveDTOCadini();
		String json ="";
		regimesprev= new   ArrayList<SigHistoricoServidorRegimePrev>();
		regimesprev = regimeprevservice.buscaregimeprevidenciario(hcc.getServidor().getNumponto(), chave.getDatreferencia());
		if ((regimesprev.size() == 0)) {
			chave.setCodtipochave("S2200");
		}
		else {
			chave.setCodtipochave("S2300");
		}
		if (grupo.getCodobjeto().equals("13"))
		{chave.setCodtipochave("S2300");}
		
		
		chavedto.setTipoChave(chave.getCodtipochave());
		AtributoCadini atributo = new AtributoCadini();
		atributo.setPonto(Integer.toString(hcc.getServidor().getNumponto()));
		atributo.setCpf(hcc.getServidor().getPessoafisica().getNumcpf());
		atributo.setCodigoGrupo(hcc.getGrupo().getCodobjeto());
		atributo.setDataReferencia(formatador.format(hcc.getDatiniciohistorico()));
		//chave.setDatreferencia(LocalDate.of(2017, 03, 01));
	//	chave.setDatreferencia(LocalDate.of(2020, 11, 22));
	//	atributo.setDataReferencia(formatador.format(chave.getDatreferencia()));
		chavedto.setAtributos(atributo);	
		
		gravaChave(chavedto);
		 
	
		  return chavedto;  
	
	}
	
	

	public void CriaChaveCadastroInicialInativos(SigHistoricoServidorGrupo hcc, SigGrupo grupo, ChaveEventoDTO chave) {
		
		ChaveDTOCadini chavedto = new ChaveDTOCadini();
		
		chavedto.setTipoChave("S2400");
		AtributoCadini atributo = new AtributoCadini();
		
		atributo.setPonto(Integer.toString(hcc.getServidor().getNumponto()));
		atributo.setCpf(hcc.getServidor().getPessoafisica().getNumcpf());
		atributo.setCodigoGrupo(hcc.getGrupo().getCodobjeto());
		atributo.setDataReferencia(formatador.format(hcc.getDatiniciohistorico()));
		atributo.setDataReferencia(formatador.format(LocalDate.of(2021, 11, 22)));
		chavedto.setAtributos(atributo);	
	
		
		gravaChave(chavedto);
		

	
	}
	
	public void CriaChaveCadastroInicialBeneficarios(SigHistoricoServidorGrupo hcc, SigGrupo grupo, ChaveEventoDTO chave) {
		
		ChaveDTOCadini chavedto = new ChaveDTOCadini();
		
		chavedto.setTipoChave("S2410");
		AtributoCadini atributo = new AtributoCadini();
		
		atributo.setPonto(Integer.toString(hcc.getServidor().getNumponto()));
		atributo.setCpf(hcc.getServidor().getPessoafisica().getNumcpf());
		atributo.setCodigoGrupo(hcc.getGrupo().getCodobjeto());
		atributo.setDataReferencia(formatador.format(hcc.getDatiniciohistorico()));
		atributo.setDataReferencia(formatador.format(LocalDate.of(2021, 11, 22)));
		chavedto.setAtributos(atributo);	
	
		gravaChave(chavedto);
		
	
	}
	
	
public void CriaChaveCadastroInicialInativosPC(SigHistoricoBeneficioPC hcc, SigGrupo grupo) {
		
	ChaveDTOCadini chavedto = new ChaveDTOCadini();
	
	chavedto.setTipoChave("S2400");
	AtributoCadini atributo = new AtributoCadini();
	
	atributo.setPonto(Integer.toString(hcc.getBeneficiariopensaocivil().getServidor().getNumponto()));
	atributo.setCpf(hcc.getBeneficiariopensaocivil().getPessoafisica().getNumcpf());
	atributo.setCodigoGrupo(hcc.getBeneficiariopensaocivil().getServidor().getGrupo().getCodobjeto());
	atributo.setDataReferencia(formatador.format(hcc.getDatiniciohistorico()));
	atributo.setDataReferencia(formatador.format(LocalDate.of(2021, 11, 22)));
	chavedto.setAtributos(atributo);	

	gravaChave(chavedto);
	
	}
		
	
	
	
public void CriaChaveCadastroInicialBeneficariosPC(SigHistoricoBeneficioPC hcc, SigGrupo grupo) {
		
		ChaveDTOCadini chavedto = new ChaveDTOCadini();
		
		
		chavedto.setTipoChave("S2410");
		AtributoCadini atributo = new AtributoCadini();
		
		atributo.setPonto(Integer.toString(hcc.getBeneficiariopensaocivil().getServidor().getNumponto()));
		atributo.setCpf(hcc.getBeneficiariopensaocivil().getPessoafisica().getNumcpf());
		atributo.setCodigoGrupo(hcc.getBeneficiariopensaocivil().getServidor().getGrupo().getCodobjeto());
		atributo.setDataReferencia(formatador.format(hcc.getDatiniciohistorico()));
		atributo.setDataReferencia(formatador.format(LocalDate.of(2021, 11, 22)));
		chavedto.setAtributos(atributo);	
	
		
		gravaChave(chavedto);
		
	
	
	}
	
	
	


	
	

	

	
}

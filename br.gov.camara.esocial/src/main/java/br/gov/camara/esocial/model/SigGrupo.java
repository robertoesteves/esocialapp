package br.gov.camara.esocial.model;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usr_folhacd.grupo")
public class SigGrupo {

	@Id
	private Long ideobjeto;

	
	@Column
	private String codobjeto;

	@Column(length = 100,nullable = false)
	private String texdescricao;

	
}

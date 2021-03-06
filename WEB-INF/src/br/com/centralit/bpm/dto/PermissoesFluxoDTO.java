package br.com.centralit.bpm.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.citframework.dto.IDto;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "PermissoesFluxo") 
public class PermissoesFluxoDTO implements IDto {
	private Integer idTipoFluxo;
	private Integer idGrupo;
	private String criar;
	private String executar;
	private String delegar;
	private String suspender;
	private String reativar;
	private String alterarSLA;
	private String reabrir;
	private String cancelar;
	
	public Integer getIdTipoFluxo() {
		return idTipoFluxo;
	}
	public void setIdTipoFluxo(Integer idTipoFluxo) {
		this.idTipoFluxo = idTipoFluxo;
	}
	public Integer getIdGrupo() {
		return idGrupo;
	}
	public void setIdGrupo(Integer idGrupo) {
		this.idGrupo = idGrupo;
	}
	public String getCriar() {
		return criar;
	}
	public void setCriar(String criar) {
		this.criar = criar;
	}
	public String getExecutar() {
		return executar;
	}
	public void setExecutar(String executar) {
		this.executar = executar;
	}
	public String getDelegar() {
		return delegar;
	}
	public void setDelegar(String delegar) {
		this.delegar = delegar;
	}
	public String getSuspender() {
		return suspender;
	}
	public void setSuspender(String suspender) {
		this.suspender = suspender;
	}
	public String getReativar() {
		return reativar;
	}
	public void setReativar(String reativar) {
		this.reativar = reativar;
	}
	public String getAlterarSLA() {
		return alterarSLA;
	}
	public void setAlterarSLA(String alterarSLA) {
		this.alterarSLA = alterarSLA;
	}
	public String getReabrir() {
	    return reabrir;
	}
	public void setReabrir(String reabrir) {
	    this.reabrir = reabrir;
	}
	public String getCancelar() {
		return cancelar;
	}
	public void setCancelar(String cancelar) {
		this.cancelar = cancelar;
	}
	
}	

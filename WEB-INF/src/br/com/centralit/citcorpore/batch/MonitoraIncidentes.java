package br.com.centralit.citcorpore.batch;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang.StringEscapeUtils;

import br.com.centralit.citcorpore.bean.AcordoNivelServicoDTO;
import br.com.centralit.citcorpore.bean.EmpregadoDTO;
import br.com.centralit.citcorpore.bean.EscalonamentoDTO;
import br.com.centralit.citcorpore.bean.OcorrenciaSolicitacaoDTO;
import br.com.centralit.citcorpore.bean.ProblemaDTO;
import br.com.centralit.citcorpore.bean.RegraEscalonamentoDTO;
import br.com.centralit.citcorpore.bean.RelEscalonamentoSolServicoDto;
import br.com.centralit.citcorpore.bean.SolicitacaoServicoDTO;
import br.com.centralit.citcorpore.bean.UsuarioDTO;
import br.com.centralit.citcorpore.integracao.EmpregadoDao;
import br.com.centralit.citcorpore.integracao.EscalonamentoDAO;
import br.com.centralit.citcorpore.integracao.OcorrenciaSolicitacaoDao;
import br.com.centralit.citcorpore.integracao.RegraEscalonamentoDAO;
import br.com.centralit.citcorpore.integracao.RelEscalonamentoSolServicoDao;
import br.com.centralit.citcorpore.integracao.SolicitacaoServicoDao;
import br.com.centralit.citcorpore.integracao.UsuarioDao;
import br.com.centralit.citcorpore.mail.MensagemEmail;
import br.com.centralit.citcorpore.negocio.AcordoNivelServicoService;
import br.com.centralit.citcorpore.negocio.OcorrenciaSolicitacaoServiceEjb;
import br.com.centralit.citcorpore.negocio.ProblemaService;
import br.com.centralit.citcorpore.negocio.SolicitacaoServicoServiceEjb;
import br.com.centralit.citcorpore.negocio.UsuarioServiceEjb;
import br.com.centralit.citcorpore.util.CriptoUtils;
import br.com.centralit.citcorpore.util.Enumerados.CategoriaOcorrencia;
import br.com.centralit.citcorpore.util.Enumerados.OrigemOcorrencia;
import br.com.centralit.citcorpore.util.Enumerados.ParametroSistema;
import br.com.centralit.citcorpore.util.ParametroUtil;
import br.com.citframework.dto.IDto;
import br.com.citframework.excecao.LogicException;
import br.com.citframework.service.ServiceLocator;
import br.com.citframework.util.UtilDatas;

@SuppressWarnings("rawtypes")
public class MonitoraIncidentes extends Thread {
	public static Semaphore performanceDataSemaphore = new Semaphore(1);
	String LOGINGERENTE = null;

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		while (true) {
			performanceDataSemaphore.acquireUninterruptibly();
			SolicitacaoServicoDao solicitacaoServicoDao = new SolicitacaoServicoDao();
			SolicitacaoServicoServiceEjb solicitacaoServicoServiceEjb = new SolicitacaoServicoServiceEjb();
			String ID_MODELO_EMAIL_ESCALACAO_AUTOMATICA_STR = "";
			Integer ID_MODELO_EMAIL_ESCALACAO_AUTOMATICA = 0;
			String ID_MODELO_EMAIL_PRAZO_VENCENDO_STR = "";
			Integer ID_MODELO_EMAIL_PRAZO_VENCENDO = null;
			String ligaFuncionamentoRegrasEscalonamento = "N";
			Timestamp dataHoraAtual = UtilDatas.getDataHoraAtual();
			try {
				LOGINGERENTE = ParametroUtil.getValorParametroCitSmartHashMap(ParametroSistema.LOGIN_USUARIO_ENVIO_EMAIL_AUTOMATICO, "N");
				ligaFuncionamentoRegrasEscalonamento = ParametroUtil.getValorParametroCitSmartHashMap(ParametroSistema.HABILITA_REGRA_ESCALONAMENTO, "N");
				ID_MODELO_EMAIL_ESCALACAO_AUTOMATICA_STR = ParametroUtil.getValorParametroCitSmartHashMap(ParametroSistema.ID_MODELO_EMAIL_ESCALACAO_AUTOMATICA, "0");				
				ID_MODELO_EMAIL_PRAZO_VENCENDO_STR = ParametroUtil.getValorParametroCitSmartHashMap(ParametroSistema.ID_MODELO_EMAIL_PRAZO_VENCENDO, "");
				if(!ID_MODELO_EMAIL_PRAZO_VENCENDO_STR.trim().equals("")) {
					ID_MODELO_EMAIL_PRAZO_VENCENDO  = new Integer(ID_MODELO_EMAIL_PRAZO_VENCENDO_STR);
				}
			} catch (Exception e) {
			}
			try {
				ID_MODELO_EMAIL_ESCALACAO_AUTOMATICA = new Integer(ID_MODELO_EMAIL_ESCALACAO_AUTOMATICA_STR.trim());
			} catch (Exception e) {
			}
			
			try {
				// System.out.println("CITSMART - Monitorando incidentes...");
				OcorrenciaSolicitacaoDao ocorrenciaSolicitacaoDao = new OcorrenciaSolicitacaoDao();
				Collection<SolicitacaoServicoDTO> col = null;
				try {
					//col = solicitacaoServicoDao.getEmAndamentoParaTratamentoBatch();
					/* Lista todas as solicita��oes relacionadas a regra de escalonamento definido e demais.*/
					col = solicitacaoServicoDao.listSolicitacoesByRegra();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if (col != null) {
					
					for (SolicitacaoServicoDTO solicitacaoServicoDTO: col) {
						
						OcorrenciaSolicitacaoDTO ocorrSolDto = null;
						try {
							ocorrSolDto = ocorrenciaSolicitacaoDao.findUltimoByIdSolicitacaoServico(solicitacaoServicoDTO.getIdSolicitacaoServico());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						if(ligaFuncionamentoRegrasEscalonamento!=null && ligaFuncionamentoRegrasEscalonamento.equalsIgnoreCase("S")) {
							
							RegraEscalonamentoDAO regraEscalonamentoDao = new RegraEscalonamentoDAO();
							EscalonamentoDAO escalonamentoDao = new EscalonamentoDAO();
							RelEscalonamentoSolServicoDao relEscalonamentoSolServicoDao = new RelEscalonamentoSolServicoDao();
							Collection<RegraEscalonamentoDTO> colecaoRegrasEscalonamento = null;
							Collection<EscalonamentoDTO> colecaoEscalonamento = null;
							try {
								//regra: 1 para solicita��o/incidente, 2 para mudan�a, 3 para problema
								colecaoRegrasEscalonamento = regraEscalonamentoDao.findRegraBySolicitacao(solicitacaoServicoDTO, 1);
								if(colecaoRegrasEscalonamento != null) {
									for (RegraEscalonamentoDTO regraEscalonamentoDTO : colecaoRegrasEscalonamento) {

										trataSituacaoVencimentoSolicitacao(solicitacaoServicoDTO, regraEscalonamentoDTO, solicitacaoServicoServiceEjb, dataHoraAtual, ocorrSolDto, ID_MODELO_EMAIL_PRAZO_VENCENDO);
										trataCriacaoAutomaticaDeProblema(solicitacaoServicoDTO, regraEscalonamentoDTO, solicitacaoServicoServiceEjb, dataHoraAtual);
										
										colecaoEscalonamento = escalonamentoDao.findByRegraEscalonamento(regraEscalonamentoDTO);
										if(colecaoEscalonamento != null) {
											for (EscalonamentoDTO escalonamentoDTO : colecaoEscalonamento) {
												//System.out.println("Estabelecendo regra... Solicita��o: " + solicitacaoServicoDTO.getIdSolicitacaoServico());							
												if (escalonamentoDTO.getPrazoExecucao() == null || escalonamentoDTO.getPrazoExecucao().intValue() == 0)
													continue;

												//Verifica se j� existe referencia
												boolean temEscalonamento = relEscalonamentoSolServicoDao.temRelacionamentoSolicitacaoEscalonamento(solicitacaoServicoDTO.getIdSolicitacaoServico(), escalonamentoDTO.getIdEscalonamento());
												if(temEscalonamento) {
													//System.out.println("Escalonamento " + escalonamentoDTO.getIdEscalonamento() + " j� executado.");
													continue;
												}
												
												if(regraEscalonamentoDTO.getTipoDataEscalonamento() != null && regraEscalonamentoDTO.getTipoDataEscalonamento().intValue() == 1) {
													/**
													 * Verifica se o tempo que se passou � maior que o prazo de execu��o
													 */
												
													//se o prazo for a combinar, prazohh e prazomm ser�o 0, n�o vai cair na regra de escalonamento
													if((solicitacaoServicoDTO.getDataHoraSolicitacao() != null) && ((solicitacaoServicoDTO.getPrazoHH() != 0) || (solicitacaoServicoDTO.getPrazoMM() != 0)) && 
															(dataHoraAtual.getTime() - solicitacaoServicoDTO.getDataHoraSolicitacao().getTime()) > (escalonamentoDTO.getPrazoExecucao() * 60 * 1000)){
														if (escalonamentoDTO.getIdGrupoExecutor() != null) {															
															/**
															 * Atualizando a tabela de relacionamento
															 */
															RelEscalonamentoSolServicoDto dto = new RelEscalonamentoSolServicoDto();
															dto.setIdSolicitacaoServico(solicitacaoServicoDTO.getIdSolicitacaoServico());
															dto.setIdEscalonamento(escalonamentoDTO.getIdEscalonamento());
															relEscalonamentoSolServicoDao.create(dto);
															
															//System.out.println("Realizando escala��o autom�tica...");																
															/**
															 * Realizando o escalonamento da solicita��o com base nas regras estabelecidas
															 * Se prioridade for nula est�o se escalonamento com a mesma prioridade antiga
															 */
															solicitacaoServicoServiceEjb.updateTimeAction(escalonamentoDTO.getIdGrupoExecutor(), 
																	(escalonamentoDTO.getIdPrioridade() != null ? escalonamentoDTO.getIdPrioridade() : solicitacaoServicoDTO.getIdPrioridade()), 
																		solicitacaoServicoDTO.getIdSolicitacaoServico());
															/**
															 * Enviando email de escala��o autom�tica
															 */
															enviaEmail(ID_MODELO_EMAIL_ESCALACAO_AUTOMATICA, solicitacaoServicoDTO.getIdSolicitacaoServico());
															
															AcordoNivelServicoService acordoNivelServicoService = (AcordoNivelServicoService) ServiceLocator.getInstance().getService(AcordoNivelServicoService.class, null);
															List<AcordoNivelServicoDTO> listaContratos = acordoNivelServicoService.findIdEmailByIdSolicitacaoServico(solicitacaoServicoDTO.getIdSolicitacaoServico());
															if(listaContratos != null && !listaContratos.isEmpty())
																enviaEmail(listaContratos.get(0).getIdEmail(), solicitacaoServicoDTO.getIdSolicitacaoServico());
														}
													}
												} else if (regraEscalonamentoDTO.getTipoDataEscalonamento() != null && regraEscalonamentoDTO.getTipoDataEscalonamento().intValue() == 2) {
													
													Date dateCons = null;
													OcorrenciaSolicitacaoDTO ocorrEscalacao = ocorrenciaSolicitacaoDao.findUltimoByIdSolicitacaoServicoAndOcorrencia(solicitacaoServicoDTO.getIdSolicitacaoServico());
													
													if (ocorrSolDto != null) {		
														if(ocorrEscalacao != null)
															ocorrSolDto = ocorrEscalacao;
														
														dateCons = ocorrSolDto.getDataregistro();
														String hora = ocorrSolDto.getHoraregistro();
														//System.out.println("Ultima ocorr�ncia: " + hora);
														try {
															Timestamp timeAux = UtilDatas.strToTimestamp(UtilDatas.dateToSTR(dateCons) + " " + hora + ":00");
															if((dataHoraAtual.getTime() - timeAux.getTime()) > (escalonamentoDTO.getPrazoExecucao() * 60 * 1000)){
																if (escalonamentoDTO.getIdGrupoExecutor() != null) {
																	
																	/**
																	 * Atualizando a tabela de relacionamento
																	 */
																	RelEscalonamentoSolServicoDto dto = new RelEscalonamentoSolServicoDto();
																	dto.setIdSolicitacaoServico(solicitacaoServicoDTO.getIdSolicitacaoServico());
																	dto.setIdEscalonamento(escalonamentoDTO.getIdEscalonamento());
																	relEscalonamentoSolServicoDao.create(dto);
																	
																	//System.out.println("Realizando escala��o autom�tica...");																
																	/**
																	 * Realizando o escalonamento da solicita��o com base nas regras estabelecidas
																	 * Se prioridade for nula est�o se escalonamento com a mesma prioridade antiga
																	 */
																	solicitacaoServicoServiceEjb.updateTimeAction(escalonamentoDTO.getIdGrupoExecutor(), 
																			(escalonamentoDTO.getIdPrioridade() != null ? escalonamentoDTO.getIdPrioridade() : solicitacaoServicoDTO.getIdPrioridade()), 
																			solicitacaoServicoDTO.getIdSolicitacaoServico());
																	/**
																	 * Enviando email de escala��o autom�tica
																	 */
																	enviaEmail(ID_MODELO_EMAIL_ESCALACAO_AUTOMATICA, solicitacaoServicoDTO.getIdSolicitacaoServico());
																	
																	AcordoNivelServicoService acordoNivelServicoService = (AcordoNivelServicoService) ServiceLocator.getInstance().getService(AcordoNivelServicoService.class, null);
																	List<AcordoNivelServicoDTO> listaContratos = acordoNivelServicoService.findIdEmailByIdSolicitacaoServico(solicitacaoServicoDTO.getIdSolicitacaoServico());
																	if(listaContratos != null && !listaContratos.isEmpty())
																		enviaEmail(listaContratos.get(0).getIdEmail(), solicitacaoServicoDTO.getIdSolicitacaoServico());
																}
															} 
															System.out.println("Finalizando regra de escalonamento...");
														} catch (Exception e) {
															e.printStackTrace();
														}
													}
												}
											}
										}
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}	
							
						} else  {
							
							if (solicitacaoServicoDTO.getTempoAuto() == null || solicitacaoServicoDTO.getTempoAuto().doubleValue() == 0) {
								continue;
							}
							double auxTempo = solicitacaoServicoDTO.getTempoAuto().doubleValue();
							auxTempo = auxTempo * 60 * 1000;
							
							Date dateCons = null;
							if (ocorrSolDto != null) {
		
								dateCons = ocorrSolDto.getDataregistro();
								String hora = ocorrSolDto.getHoraregistro();
								try {
									Timestamp timeAux = UtilDatas.strToTimestamp(UtilDatas.dateToSTR(dateCons) + " " + hora + ":00");
									Timestamp timeNow = UtilDatas.getDataHoraAtual();
									long diffTempAux = UtilDatas.calculaDiferencaTempoEmMilisegundos(timeNow, timeAux);
									double diffTemp = new Double(diffTempAux);
									if (Math.abs(diffTemp) > Math.abs(auxTempo)) {
										if (solicitacaoServicoDTO.getIdGrupo1() != null || solicitacaoServicoDTO.getIdPrioridadeAuto1() != null) {
											if (solicitacaoServicoDTO.getIdPrioridadeAuto1() != null) {
												if (ocorrSolDto.getRegistradopor() != null && ocorrSolDto.getRegistradopor().trim().equalsIgnoreCase("AUTO")) {
													if (solicitacaoServicoDTO.getIdPrioridadeAuto1().intValue() >= solicitacaoServicoDTO.getIdPrioridade().intValue()) {
														solicitacaoServicoDTO.setIdPrioridadeAuto1(solicitacaoServicoDTO.getIdPrioridade().intValue() - 1);
														if (solicitacaoServicoDTO.getIdPrioridadeAuto1().intValue() <= 0) {
															solicitacaoServicoDTO.setIdPrioridadeAuto1(1);
														}
													}
												}
											}
											solicitacaoServicoServiceEjb.updateTimeAction(solicitacaoServicoDTO.getIdGrupo1(), solicitacaoServicoDTO.getIdPrioridadeAuto1(), solicitacaoServicoDTO.getIdSolicitacaoServico());
											try {
												enviaEmail(ID_MODELO_EMAIL_ESCALACAO_AUTOMATICA, solicitacaoServicoDTO.getIdSolicitacaoServico());
											} catch (Exception e) {
											}
											
											try{
												AcordoNivelServicoService acordoNivelServicoService = (AcordoNivelServicoService) ServiceLocator.getInstance().getService(AcordoNivelServicoService.class, null);
												List<AcordoNivelServicoDTO> listaContratos = acordoNivelServicoService.findIdEmailByIdSolicitacaoServico(solicitacaoServicoDTO.getIdSolicitacaoServico());
												enviaEmail(listaContratos.get(0).getIdEmail(), solicitacaoServicoDTO.getIdSolicitacaoServico());
											}catch(Exception e){
												
											}
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
	
							solicitacaoServicoDTO = null;
						}
					}
				}
				ocorrenciaSolicitacaoDao = null;
			} finally {
				performanceDataSemaphore.release();
			}
			solicitacaoServicoDao = null;
			// System.out.println("CITSMART - Monitorando incidentes... Terminado.");
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
			}
		}
	}

	public void enviaEmail(Integer idModeloEmail, Integer idSolicitacaoServico) throws Exception {
		if (idModeloEmail == null || idModeloEmail.intValue() == 0)
			return;

		String enviaEmail = ParametroUtil.getValorParametroCitSmartHashMap(ParametroSistema.EnviaEmailFluxo, "N");
		if (!enviaEmail.equalsIgnoreCase("S"))
			return;

		String remetente = ParametroUtil.getValorParametroCitSmartHashMap(ParametroSistema.RemetenteNotificacoesSolicitacao, null);
		if (remetente == null)
			throw new LogicException("Remetente para notifica��es de solicita��o de servi�o n�o foi parametrizado");

		String urlSistema = ParametroUtil.getValorParametroCitSmartHashMap(ParametroSistema.URL_Sistema, "");

		SolicitacaoServicoDTO solicitacaoAuxDto = new SolicitacaoServicoServiceEjb().restoreAll(idSolicitacaoServico, null);
		solicitacaoAuxDto.setNomeTarefa("Automatic escalation");

		String idHashValidacao = CriptoUtils.generateHash("CODED" + solicitacaoAuxDto.getIdSolicitacaoServico(), "MD5");
		solicitacaoAuxDto.setLinkPesquisaSatisfacao("<a href=\"" + urlSistema + "/pages/pesquisaSatisfacao/pesquisaSatisfacao.load?idSolicitacaoServico=" + solicitacaoAuxDto.getIdSolicitacaoServico() + "&hash="
				+ idHashValidacao + "\">Clique aqui para fazer a avalia��o do Atendimento</a>");

		MensagemEmail mensagem = new MensagemEmail(idModeloEmail, new IDto[] { solicitacaoAuxDto });
		
		try {
			mensagem.envia(solicitacaoAuxDto.getEmailcontato(), remetente, remetente);
		} catch (Exception e) {
		}
		
		
	}
	
	public void enviaEmailGerente(Integer idModeloEmail, Integer idSolicitacaoServico) throws Exception {
		if (idModeloEmail == null || idModeloEmail.intValue() == 0)
			return;

		String enviaEmail = ParametroUtil.getValorParametroCitSmartHashMap(ParametroSistema.EnviaEmailFluxo, "N");
		if (!enviaEmail.equalsIgnoreCase("S"))
			return;

		String remetente = ParametroUtil.getValorParametroCitSmartHashMap(ParametroSistema.RemetenteNotificacoesSolicitacao, null);
		if (remetente == null)
			throw new LogicException("Remetente para notifica��es de solicita��o de servi�o n�o foi parametrizado");

		String urlSistema = ParametroUtil.getValorParametroCitSmartHashMap(ParametroSistema.URL_Sistema, "");

		SolicitacaoServicoDTO solicitacaoAuxDto = new SolicitacaoServicoServiceEjb().restoreAll(idSolicitacaoServico, null);
		solicitacaoAuxDto.setNomeTarefa("Automatic escalation");

		String idHashValidacao = CriptoUtils.generateHash("CODED" + solicitacaoAuxDto.getIdSolicitacaoServico(), "MD5");
		solicitacaoAuxDto.setLinkPesquisaSatisfacao("<a href=\"" + urlSistema + "/pages/pesquisaSatisfacao/pesquisaSatisfacao.load?idSolicitacaoServico=" + solicitacaoAuxDto.getIdSolicitacaoServico() + "&hash="
				+ idHashValidacao + "\">Clique aqui para fazer a avalia��o do Atendimento</a>");

		MensagemEmail mensagem = new MensagemEmail(idModeloEmail, new IDto[] { solicitacaoAuxDto });
		
		try {
			//Notifica o gerente
			UsuarioDTO  gerente = null;
			if(!LOGINGERENTE.equalsIgnoreCase("N")){
				gerente = new UsuarioDTO();
				UsuarioDao dao = new UsuarioDao();
				gerente = dao.restoreByLogin(LOGINGERENTE);
				EmpregadoDTO empregado = new EmpregadoDTO();
				EmpregadoDao empregadoDao = new EmpregadoDao();
				empregado = empregadoDao.restoreByIdEmpregado(gerente.getIdEmpregado());
			
				if(empregado != null){
					mensagem.envia(empregado.getEmail(), remetente, remetente);
				}
					
				gerente = null;
				dao = null;
				empregado = null;
				empregadoDao = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean criaProblema(SolicitacaoServicoDTO solicitacaoServicoDTO) throws Exception{
		//restaura solicitacao
		SolicitacaoServicoDTO solAux = new SolicitacaoServicoDTO();
		SolicitacaoServicoServiceEjb solServiceEjb = new SolicitacaoServicoServiceEjb();
		solAux = (SolicitacaoServicoDTO) solServiceEjb.restore(solicitacaoServicoDTO);
		
		//traz as outras informa��es referentes a JOINS
		SolicitacaoServicoDao dao = new SolicitacaoServicoDao();
		SolicitacaoServicoDTO solAux2 = new SolicitacaoServicoDTO();
		solAux2 = dao.findInfosCriacaoProblemaByIdSolServico(solicitacaoServicoDTO);

		UsuarioServiceEjb usuarioService = new UsuarioServiceEjb();
		//admin
		UsuarioDTO usuarioDto = usuarioService.restoreByIdEmpregado(1);
		List<SolicitacaoServicoDTO> listIdSolicitacaoServico = new ArrayList<SolicitacaoServicoDTO>();
		listIdSolicitacaoServico.add(solAux);
		solAux.setUsuarioDto(usuarioDto);
		ProblemaDTO problemaDto = new ProblemaDTO();
		ProblemaService problemaService = (ProblemaService) ServiceLocator.getInstance().getService(ProblemaService.class, null);
		problemaDto.setEnviaEmailCriacao("S");		
		problemaDto.setEnviaEmailFinalizacao("S");		
		problemaDto.setEnviaEmailPrazoSolucionarExpirou("S");
		problemaDto.setListIdSolicitacaoServico(listIdSolicitacaoServico);
		problemaDto.setIdGrupo(solAux.getIdGrupo());
		problemaDto.setIdGrupoAtual(solAux.getIdGrupoAtual());
		problemaDto.setIdGrupoNivel1(solAux.getIdGrupoNivel1());
		problemaDto.setIdContrato(solAux2.getIdContrato());
		//Como � o sistegma vai usar o id do Admin
		problemaDto.setIdCriador(1);
		problemaDto.setIdLocalidade(solAux.getIdLocalidade());
		problemaDto.setIdOrigemAtendimento(solAux.getIdOrigem());
		problemaDto.setIdResponsavel(solAux.getIdResponsavel());
		problemaDto.setIdPrioridade(solAux.getIdPrioridade());
		System.out.println();
		if(solAux.getPrioridade() == null){
			solAux.setPrioridade("3");
		}
		problemaDto.setPrioridade(Integer.parseInt(solAux.getPrioridade()));
		problemaDto.setIdServicoContrato(solAux.getIdServicoContrato());
		problemaDto.setIdServico(solAux.getIdServico());
		problemaDto.setDescricao(StringEscapeUtils.escapeHtml(solAux.getDescricaoSemFormatacao()));
		problemaDto.setTitulo("Problema Criado por Rotina autom�tica");
		problemaDto.setTelefoneContato(solAux2.getTelefonecontato());
		problemaDto.setUsuarioDto(solAux.getUsuarioDto());
		problemaDto.setImpacto(solAux.getImpacto());
		problemaDto.setUrgencia(solAux.getUrgencia());
		problemaDto.setDataHoraInicio(UtilDatas.getDataHoraAtual());
		problemaDto.setNomeContato(solAux2.getNomecontato());
		problemaDto.setIdUnidade(solAux.getIdUnidade());
		problemaDto.setIdSolicitante(solAux.getIdSolicitante());
		//categoria padr�o
		problemaDto.setIdCategoriaProblema(1);
		
		problemaDto.setSeveridade("Alta");
		problemaDto.setStatus("Registrada");
		problemaDto.setEmailContato(solAux2.getEmailcontato());
		problemaService.create(problemaDto);
		solAux = null;
		return true;
	}
	
	
	public void trataSituacaoVencimentoSolicitacao(SolicitacaoServicoDTO solicitacaoServicoDTO, RegraEscalonamentoDTO regraEscalonamentoDTO, 
			SolicitacaoServicoServiceEjb solicitacaoServicoServiceEjb, Timestamp dataHoraAtual, OcorrenciaSolicitacaoDTO ocorrSolDto, Integer idModeloEmail) throws Exception{
		//trata regra de classifica��o se est� vencendo
		if((solicitacaoServicoDTO.getDataHoraLimite() != null) && ((solicitacaoServicoDTO.getPrazoHH() != 0) || (solicitacaoServicoDTO.getPrazoMM() != 0)) && ((solicitacaoServicoDTO.getDataHoraLimite().getTime() - dataHoraAtual.getTime()) <= (regraEscalonamentoDTO.getTempoExecucao() * 60 * 1000))){
			
			double intervaloTempo = regraEscalonamentoDTO.getIntervaloNotificacao().intValue() * 60 * 1000;
			
			Date dateCons = null;
			if (ocorrSolDto != null) {						
				dateCons = ocorrSolDto.getDataregistro();
				String hora = ocorrSolDto.getHoraregistro();
				Timestamp timeAux = UtilDatas.strToTimestamp(UtilDatas.dateToSTR(dateCons) + " " + hora + ":00");
				if ((dataHoraAtual.getTime() - timeAux.getTime()) > intervaloTempo) {
					if(regraEscalonamentoDTO.getEnviarEmail() != null && regraEscalonamentoDTO.getEnviarEmail().equals("S")) {
						UsuarioDTO usuarioDTO = new UsuarioDTO();
						usuarioDTO.setLogin("Autom�tico");
						OcorrenciaSolicitacaoServiceEjb.create(solicitacaoServicoDTO, null, "Vencendo", OrigemOcorrencia.OUTROS, 
								CategoriaOcorrencia.Atualizacao, null, CategoriaOcorrencia.Atualizacao.getDescricao(), usuarioDTO, 0, null, null);
						if(idModeloEmail != null)
							enviaEmail(idModeloEmail, solicitacaoServicoDTO.getIdSolicitacaoServico());
					}													
				}
			}
			
			if((solicitacaoServicoDTO.getDataHoraLimite() != null) && ((solicitacaoServicoDTO.getPrazoHH() != 0) || (solicitacaoServicoDTO.getPrazoMM() != 0)) && ((solicitacaoServicoDTO.getDataHoraLimite().getTime() - dataHoraAtual.getTime()) <= (regraEscalonamentoDTO.getTempoExecucao() * 60 * 1000))){
				//trata caso o sla esteja vencendo
				if(solicitacaoServicoDTO.getVencendo() != null && !solicitacaoServicoDTO.getVencendo().equalsIgnoreCase("S")){
					SolicitacaoServicoDTO solicitacao = new SolicitacaoServicoDTO();
					solicitacao.setIdSolicitacaoServico(solicitacaoServicoDTO.getIdSolicitacaoServico());
					solicitacao = (SolicitacaoServicoDTO) solicitacaoServicoServiceEjb.restore(solicitacao);
					solicitacao.setVencendo("S");
					solicitacaoServicoServiceEjb.updateNotNull(solicitacao);
					if(idModeloEmail != null) {
						enviaEmailGerente(idModeloEmail, solicitacaoServicoDTO.getIdSolicitacaoServico());
					}
					solicitacao = null;
				} else if(solicitacaoServicoDTO.getVencendo() == null){
					SolicitacaoServicoDTO solicitacao = new SolicitacaoServicoDTO();
					solicitacao.setIdSolicitacaoServico(solicitacaoServicoDTO.getIdSolicitacaoServico());
					solicitacao = (SolicitacaoServicoDTO) solicitacaoServicoServiceEjb.restore(solicitacao);
					solicitacao.setVencendo("S");
					solicitacaoServicoServiceEjb.updateNotNull(solicitacao);
					solicitacao = null;
				}
			} else {
				if(solicitacaoServicoDTO.getVencendo() == null){ 
					SolicitacaoServicoDTO solicitacao = new SolicitacaoServicoDTO();
					solicitacao.setIdSolicitacaoServico(solicitacaoServicoDTO.getIdSolicitacaoServico());
					solicitacao = (SolicitacaoServicoDTO) solicitacaoServicoServiceEjb.restore(solicitacao);
					solicitacao.setVencendo("N");
					solicitacaoServicoServiceEjb.updateNotNull(solicitacao);
					solicitacao = null;
				}
				//trata caso o sla tenha sido alterado para mais
				if(!solicitacaoServicoDTO.getVencendo().equalsIgnoreCase("N")){
					SolicitacaoServicoDTO solicitacao = new SolicitacaoServicoDTO();
					solicitacao.setIdSolicitacaoServico(solicitacaoServicoDTO.getIdSolicitacaoServico());
					solicitacao = (SolicitacaoServicoDTO) solicitacaoServicoServiceEjb.restore(solicitacao);
					solicitacao.setVencendo("N");
					solicitacaoServicoServiceEjb.updateNotNull(solicitacao);
					solicitacao = null;
				} 
			}
		}
	}
	
	public void trataCriacaoAutomaticaDeProblema(SolicitacaoServicoDTO solicitacaoServicoDTO, RegraEscalonamentoDTO regraEscalonamentoDTO, SolicitacaoServicoServiceEjb solicitacaoServicoServiceEjb, Timestamp dataHoraAtual) throws Exception{
		if(regraEscalonamentoDTO.getPrazoCriarProblema() != null && regraEscalonamentoDTO.getCriaProblema() != null && regraEscalonamentoDTO.getCriaProblema().equalsIgnoreCase("S")){
			if((dataHoraAtual.getTime() - solicitacaoServicoDTO.getDataHoraSolicitacao().getTime()) >= (regraEscalonamentoDTO.getPrazoCriarProblema() * 60 * 1000)){
				if(solicitacaoServicoDTO.getCriouProblemaAutomatico() == null){
					criaProblema(solicitacaoServicoDTO);
					SolicitacaoServicoDTO solicitacao = new SolicitacaoServicoDTO();
					solicitacao.setIdSolicitacaoServico(solicitacaoServicoDTO.getIdSolicitacaoServico());
					solicitacao = (SolicitacaoServicoDTO) solicitacaoServicoServiceEjb.restore(solicitacao);
					solicitacao.setCriouProblemaAutomatico("S");
					solicitacaoServicoServiceEjb.updateNotNull(solicitacao);
					solicitacao = null;	
				} 
			} 
		}
	}
}

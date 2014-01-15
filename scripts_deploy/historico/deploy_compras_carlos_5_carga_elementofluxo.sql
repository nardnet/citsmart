/*
-- Query: SELECT * FROM citsmart_novo_homologacao.bpm_elementofluxo where idfluxo = 36
LIMIT 0, 1000

-- Date: 2013-02-27 09:13
*/
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (352,36,'Inicio','','','','','','','','','','','','','',7,22,32,32,'','',NULL,'','','');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (353,36,'Tarefa','','Validar requisição','Validar requisição','U','/pages/solicitacaoServicoMultiContratos/solicitacaoServicoMultiContratos.load','','#{solicitacaoServico.grupoAtual}','','','','','','',136,5,65,140,'','ValidacaoRequisicaoProduto',NULL,'','','');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (354,36,'Tarefa','','Autorizar requisição','Autorizar requisição','U','/pages/solicitacaoServicoMultiContratos/solicitacaoServicoMultiContratos.load','',NULL,'script:#{execucaoFluxo}.recuperaLoginAutorizadores();','#{execucaoFluxo}.atribuiAcompanhamento(#{itemTrabalho},null,#{solicitacaoServico.grupoAtual});\n#{execucaoFluxo}.atribuiAcompanhamento(#{itemTrabalho},#{execucaoFluxo}.recuperaLoginAutorizadores(),null); \n','','','','',529,94,65,140,'','AutorizacaoCotacao',NULL,'','','');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (355,36,'Tarefa','','Acompanhar requisição','Acompanhamento requisição','U','/pages/solicitacaoServicoMultiContratos/solicitacaoServicoMultiContratos.load?alterarSituacao=S','','#{solicitacaoServico.grupoNivel1}','','#{execucaoFluxo}.atribuiAcompanhamento(#{itemTrabalho},#{execucaoFluxo}.recuperaLoginAutorizadores(),null);\n#{execucaoFluxo}.atribuiAcompanhamento(#{itemTrabalho},null,#{solicitacaoServico.grupoAtual});','','','','',529,214,65,140,'','AcompanhamentoRequisicaoProduto',NULL,'','','');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (356,36,'Tarefa','','Complementar dados da requisicão','Complementar dados da requisicão','U','/pages/solicitacaoServicoMultiContratos/solicitacaoServicoMultiContratos.load?alterarSituacao=S','','#{solicitacaoServico.grupoNivel1}','script:#{execucaoFluxo}.recuperaLoginResponsaveis();','#{execucaoFluxo}.atribuiAcompanhamento(#{itemTrabalho},#{execucaoFluxo}.recuperaLoginAutorizadores(),null);\n#{execucaoFluxo}.atribuiAcompanhamento(#{itemTrabalho},null,#{solicitacaoServico.grupoAtual});','','','','',137,237,65,140,'','AlteracaoRequisicaoProduto',NULL,'','','');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (357,36,'Tarefa','','Aprovar cotação','Aprovar cotação','U','/pages/solicitacaoServicoMultiContratos/solicitacaoServicoMultiContratos.load','/pages/solicitacaoServicoMultiContratos/solicitacaoServicoMultiContratos.load',NULL,'script:#{execucaoFluxo}.recuperaLoginAutorizadores();','#{execucaoFluxo}.associaItemTrabalhoAprovacao(#{itemTrabalho}); \n#{execucaoFluxo}.atribuiAcompanhamento(#{itemTrabalho},null,#{solicitacaoServico.grupoAtual});','','','','',907,22,65,140,'','AprovacaoCotacao',NULL,'','S','');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (358,36,'Tarefa','','Inspecionar entrega','Inspecionar entrega','U','/pages/solicitacaoServicoMultiContratos/solicitacaoServicoMultiContratos.load','/pages/solicitacaoServicoMultiContratos/solicitacaoServicoMultiContratos.load','#{solicitacaoServico.grupoNivel1}','script:#{execucaoFluxo}.recuperaLoginResponsaveis();','#{execucaoFluxo}.associaItemTrabalhoInspecao(#{itemTrabalho});','','','','',908,103,65,140,'','InspecaoEntregaItem',NULL,'','S','');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (359,36,'Tarefa','','Acionar garantia','Acionar garantia','','/pages/solicitacaoServicoMultiContratos/solicitacaoServicoMultiContratos.load?alterarSituacao=S','/pages/solicitacaoServicoMultiContratos/solicitacaoServicoMultiContratos.load','#{solicitacaoServico.grupoAtual}','','#{execucaoFluxo}.associaItemTrabalhoGarantia(#{itemTrabalho});','','','','',910,244,65,140,'','AcionamentoGarantia',NULL,'','S','');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (360,36,'Script','','Encerra','','','','','','','','','#{execucaoFluxo}.encerra();','','',718,314,65,140,'','',NULL,'','','');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (361,36,'Evento','','','','','','','','','','','','','',669,294,32,32,'','',30,'!#{solicitacaoServico}.finalizada();','','');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (362,36,'Email','','','','','','','#{solicitacaoServico.grupoAtual}','','','','','','',191,178,22,31,'ReqProdNaoValidada','',NULL,'','','#{solicitacaoServico.emailcontato}');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (363,36,'Email','','','','','','','#{solicitacaoServico.grupoAtual}','','','','','','',357,28,22,31,'ReqProdNaoAutorizada','',NULL,'','','#{solicitacaoServico.emailcontato}');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (364,36,'Email','','','','','','','#{solicitacaoServico.grupoAtual}','','','','','','',80,27,22,31,'ReqProdValidacao','',NULL,'','','');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (365,36,'Email','','','','','','','','script:#{execucaoFluxo}.recuperaLoginAutorizadores();','','','','','',464,115,22,31,'ReqProdAutorizacao','',NULL,'','','');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (366,36,'Email','','','','','','','script:#{execucaoFluxo}.recuperaLoginAutorizadores();','','','','','','',772,44,22,31,'ReqProdAprovacao','',NULL,'','','');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (367,36,'Email','','','','','','',' #{solicitacaoServico.grupoNivel1}','script:#{execucaoFluxo}.recuperaLoginResponsaveis();','','','','','',841,125,22,31,'ReqProdInspecao','',NULL,'','','#{solicitacaoServico.emailcontato}');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (368,36,'Email',NULL,NULL,NULL,NULL,NULL,NULL,'#{solicitacaoServico.grupoAtual}','',NULL,NULL,NULL,NULL,NULL,843,266,22,31,'ReqProdGarantia',NULL,NULL,NULL,NULL,'');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (369,36,'Porta','','','','','','','','','','','','','',345,105,42,42,'','',NULL,'','','');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (370,36,'Porta','','','','','','','','','','','','','',185,104,42,42,'','',NULL,'','','');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (371,36,'Porta','','','','','','','','','','','','','',578,18,42,42,'','',NULL,'','','');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (372,36,'Porta','','','','','','','','','','','','','',767,154,42,42,'','',NULL,'','','');
INSERT INTO `bpm_elementofluxo` (`idelemento`,`idfluxo`,`tipoelemento`,`subtipo`,`nome`,`documentacao`,`tipointeracao`,`url`,`visao`,`grupos`,`usuarios`,`acaoentrada`,`acaosaida`,`script`,`textoemail`,`nomefluxoencadeado`,`posx`,`posy`,`altura`,`largura`,`modeloemail`,`template`,`intervalo`,`condicaodisparo`,`multiplasinstancias`,`destinatariosemail`) VALUES (373,36,'Finalizacao','','','','','','','','','','','','','',960,331,32,32,'','',NULL,'','','');
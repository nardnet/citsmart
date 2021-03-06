//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.25 at 09:25:07 PM BRT 
//


package br.com.centralit.citsmart.rest.schema;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import br.com.centralit.bpm.dto.TarefaFluxoDTO;


/**
 * <p>Java class for CtListTasksResp complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CtListTasksResp">
 *   &lt;complexContent>
 *     &lt;extension base="{}CtMessageResp">
 *       &lt;sequence>
 *         &lt;element name="qtdeTarefas" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;group ref="{}Tarefas" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CtListTasksResp", propOrder = {
    "qtdeTarefas",
    "tarefas"
})
public class CtListTasksResp
    extends CtMessageResp
{

    @XmlElement(required = true)
    protected int qtdeTarefas;
    @XmlElement(name = "Tarefa", required = true)
    protected List<TarefaFluxoDTO> tarefas;

    /**
     * Gets the value of the qtdeTarefas property.
     * 
     * @return
     *     possible object is
     *     {@link int }
     *     
     */
    public int getQtdeTarefas() {
        return qtdeTarefas;
    }

    /**
     * Sets the value of the qtdeTarefas property.
     * 
     * @param value
     *     allowed object is
     *     {@link int }
     *     
     */
    public void setQtdeTarefas(int value) {
        this.qtdeTarefas = value;
    }

    /**
     * Gets the value of the tarefas property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tarefas property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTarefas().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TarefaFluxoDTO }
     * 
     * 
     */
    public List<TarefaFluxoDTO> getTarefas() {
        if (tarefas == null) {
            tarefas = new ArrayList<TarefaFluxoDTO>();
        }
        return this.tarefas;
    }

	public void setTarefas(List<TarefaFluxoDTO> tarefas) {
		this.tarefas = tarefas;
	}

}

package com.ht.scada.common.tag.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.springframework.data.jpa.domain.AbstractPersistable;


/**
 * 变量词典模板<br>
 * <p>大部分的监控末端都具有相同的变量类型，所有采用通用的变量词典模板来进行定义，开端节点只需要指定模板名称即可</p>
 * @author user
 *
 */
@Entity
@Table(name="T_Tag_Cfg_Tpl")
public class TagCfgTpl extends AbstractPersistable<Integer> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -342203444992438610L;

	@Index(name="idx_tagcfgtpl_tplname")
	@Column(name="tpl_name")
	private String tplName;// 模板名称
	
	/*变量属性*/
	@Column(name="var_name") private String 	varNmae;	// 变量名称
	@Column(name="var_type") private String 	varType;	// 变量类型
	@Column(name="sub_Type") private String 	subType;	// 变量子类型
	@Column(name="data_type") private String 	dataType;	// 值类型（bool, int32, int16, bcd, mod10000, float, double）
	@Column(name="fun_code") private String 	funCode;	// 功能码
	@Column(name="data_id") private String 		dataID;		// 数据ID
	@Column(name="byte_offset") private int 	byteOffset;	// 字节偏移量
	@Column(name="byte_len") private int 	byteLen;	// 字节长度
	@Column(name="base_value") private float 	baseValue = 0;// 基值 
	@Column(name="coef_value") private float 	coefValue = 1;// 系数
	
	/*变量扩展属性*/
	private String trigger;		// 触发采集帧名,如"soe", 需要与采集通道中定义的帧名称对应
	
	/*
	 * 持久化属性
	 * 
	 * 多个存储器用逗号隔开
	 * 
	 * 故障存储器： 		fault|1|合闸|分闸|true 
	 * 				【存储器类型】|【报警标志1/0】|【合消息】|【分消息】|【是否推画面】
	 * 变位存储器： 		rschange|-1|合闸|分闸|true 
	 * 				【存储器类型】|【报警类型1/0/-1】|【合消息】|【分消息】|【是否推画面】
	 * 遥测越限存储器：	alarm|[上限]|越上限报警|[下限]|越下限报警|true 【
	 * 				存储器类型】|【上限】|【上限信息】|【下限】|【下限信息】|【是否推画面】
	 * 遥测存储器：		yc||10 
	 * 				【存储器类型】|【阈值（可以为空）】|【周期(分钟)】
	 * 遥脉存储器：		ym|0|599999999|1|0 
	 * 				【存储器类型】|【最小值】|【最大值】|单位脉冲电度量|周期（可以为空）
	 **/
	private String storage = "ym|0|599999999|1|0";
	
	

	public TagCfgTpl() {
	}
	
	public TagCfgTpl(Integer id) {
		setId(id);
	}

	public String getTplName() {
		return tplName;
	}

	public void setTplName(String tplName) {
		this.tplName = tplName;
	}

	public String getVarNmae() {
		return varNmae;
	}

	public void setVarNmae(String varNmae) {
		this.varNmae = varNmae;
	}

	public String getVarType() {
		return varType;
	}

	public void setVarType(String varType) {
		this.varType = varType;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getFunCode() {
		return funCode;
	}

	public void setFunCode(String funCode) {
		this.funCode = funCode;
	}

	public String getDataID() {
		return dataID;
	}

	public void setDataID(String dataID) {
		this.dataID = dataID;
	}

	public int getByteOffset() {
		return byteOffset;
	}

	public void setByteOffset(int byteOffset) {
		this.byteOffset = byteOffset;
	}

	public int getByteLen() {
		return byteLen;
	}

	public void setByteLen(int byteLen) {
		this.byteLen = byteLen;
	}

	public float getBaseValue() {
		return baseValue;
	}

	public void setBaseValue(float baseValue) {
		this.baseValue = baseValue;
	}

	public float getCoefValue() {
		return coefValue;
	}

	public void setCoefValue(float coefValue) {
		this.coefValue = coefValue;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}
	
	
	
}

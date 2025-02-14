/**
 * 
 */
package com.example.rule.model;

import java.io.Serializable;

/**
 * @author admin
 * 
 */
public enum OperationType implements Serializable {

	INSERT("INSERT"), UPDATE("UPDATE"), DELETE("DELETE");

	private String value;

	OperationType(String value) {
		this.value = value;

	}

	public String getValue() {
		return value;
	}
}
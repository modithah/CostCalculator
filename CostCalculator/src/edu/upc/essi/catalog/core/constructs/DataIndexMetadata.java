package edu.upc.essi.catalog.core.constructs;

public class DataIndexMetadata {
	/**
	 * @param data
	 * @param sizeOrMult
	 * @param distinctCount
	 * @param frequency
	 */
	public DataIndexMetadata(boolean data, float sizeOrMult, float distinctCount, float frequency, DataType type) {
		this.data = data;
		this.sizeOrMult = sizeOrMult;
		this.distinctCount = distinctCount;
		this.frequency = frequency;
		this.dataType =type;
	}

	public enum DataType {
		DATA,
	    UUID,
	    INT
	  }
	
	
	private boolean data;
	private float sizeOrMult;
	private float distinctCount;
	private float frequency;
	private DataType dataType;
	

	public boolean isData() {
		return data;
	}

	public void setData(boolean data) {
		this.data = data;
	}

	public float getSizeOrMult() {
		return sizeOrMult;
	}

	public void setSizeOrMult(float sizeOrMult) {
		this.sizeOrMult = sizeOrMult;
	}

	public float getDistinctCount() {
		return distinctCount;
	}

	public void setDistinctCount(float distinctCount) {
		this.distinctCount = distinctCount;
	}

	public float getFrequency() {
		return frequency;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

}

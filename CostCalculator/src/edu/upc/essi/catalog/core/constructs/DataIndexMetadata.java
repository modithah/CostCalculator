package edu.upc.essi.catalog.core.constructs;

public class DataIndexMetadata {
	/**
	 * @param data
	 * @param sizeOrMult
	 * @param distinctCount
	 * @param frequency
	 */
	public DataIndexMetadata(boolean data, double sizeOrMult, double distinctCount, double frequency, DataType type) {
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
	private double sizeOrMult;
	private double distinctCount;
	private double frequency;
	private DataType dataType;
	

	public boolean isData() {
		return data;
	}

	public void setData(boolean data) {
		this.data = data;
	}

	public double getSizeOrMult() {
		return sizeOrMult;
	}

	public void setSizeOrMult(double sizeOrMult) {
		this.sizeOrMult = sizeOrMult;
	}

	public double getDistinctCount() {
		return distinctCount;
	}

	public void setDistinctCount(double distinctCount) {
		this.distinctCount = distinctCount;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	@Override
	public String toString() {
		return "DataIndexMetadata [data=" + data + ", sizeOrMult=" + sizeOrMult + ", distinctCount=" + distinctCount
				+ ", frequency=" + frequency + ", dataType=" + dataType + "]";
	}

}

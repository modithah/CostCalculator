package edu.upc.essi.catalog.core.constructs;

import com.opencsv.bean.CsvBindByPosition;

public class CSVRow {

	@CsvBindByPosition(position = 0)
	private String id;
	@CsvBindByPosition(position = 1)
	private String node;
	@CsvBindByPosition(position = 2)
	private String parent;
	@CsvBindByPosition(position = 3)
	private String pos;
	@CsvBindByPosition(position = 4)
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}

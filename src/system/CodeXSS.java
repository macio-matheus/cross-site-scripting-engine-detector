package system;

import java.util.*;

public class CodeXSS {
	private int id;
	private String name;
	private String code;
	private String output;

	public CodeXSS(int id, String name, String code, String output) {
		setId(id);
		setName(name);
		setCode(code);
		setOutput(output);

	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getOutput() {
		return this.output;
	}

	public String toString() {
		return "id: " + id + " | name: " + name + " | code: " + code
				+ " | output: " + output;
	}
}
package dev.syntax.model;

public class Example {
    private long id;
    private long problemId;
    private String input;
    private String output;
    
	public Example() {
	}

	public Example(long id, long problemId, String input, String output) {
		super();
		this.id = id;
		this.problemId = problemId;
		this.input = input;
		this.output = output;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProblemId() {
		return problemId;
	}

	public void setProblemId(long problemId) {
		this.problemId = problemId;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

}

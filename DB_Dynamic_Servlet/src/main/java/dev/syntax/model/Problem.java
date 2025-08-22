package dev.syntax.model;

public class Problem {
    private long id;      
    private int num;
    private String title;
    private String text;
    private String input;
    private String output;

    public Problem() {}

    public Problem(long id, int num, String title, String text, String input, String output) {
        this.id = id;
        this.num = num;
        this.title = title;
        this.text = text;
        this.input = input;
        this.output = output;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

package cn.com.weixunyun.child.util;

public class ColumnError {
	public ColumnError(int row, int col, String content, String error) {
		this.row = row;
		this.col = col;
		this.content = content;
		this.error = error;
	}

	private int row, col;
	private String content, error;

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		if (col < 26) {
			return "[" + (row + 1) + ":" + (char) (96 + col + 1) + "] " + error;
		} else {
			return "[" + (row + 1) + ":" + (char) (96 + col / 26) + (char) (96 + (col + 1) % 26) + "] " + error;
		}
	}
}

package net.smartworks.model;

import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartUtil;

public class Matrix {

	
	private int row=-1;
	private int column=-1;
	private boolean newRow = false;
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}

	public Matrix(int row, int column){
		super();
		this.row = row;
		this.column = column;
	}
	public Matrix(String rowColumn){
		super();
		if(!SmartUtil.isBlankObject(rowColumn) && rowColumn.length()==2){
			this.row = Integer.parseInt(rowColumn.substring(0, 1));
			this.column = Integer.parseInt(rowColumn.substring(1, 2));
		}
	}
	public String toString(){
		return "" + this.row + this.column;
	}
	
	public boolean isSameRow(Matrix matrix){
		if(SmartUtil.isBlankObject(matrix)) return false;
		if(matrix.getRow() == this.row) return true;
		return false;
	}
	public boolean isSamePosition(Matrix matrix){
		if(SmartUtil.isBlankObject(matrix)) return false;
		if(matrix.getRow() == this.row && matrix.getColumn() == this.column) return true;
		return false;
	}
	public boolean isNewRow() {
		return newRow;
	}
	public void setNewRow(boolean newRow) {
		this.newRow = newRow;
	}
	public static String getPositionName(String position, int columnSpans){
		if(SmartUtil.isBlankObject(position)) return "";
		
		Matrix currentPosition = new Matrix(position);
		if(columnSpans==1){
			return SmartMessage.getString("report.title.cur_position_row", new Object[]{ currentPosition.getRow()+1});
		}
		return SmartMessage.getString("report.title.cur_position_c" + currentPosition.getColumn() , new Object[]{ currentPosition.getRow()+1});		
	}
	public String getPositionName(){
		
		if(this.isNewRow()){
			return SmartMessage.getString("report.title.add_position_row", new Object[]{ this.getRow()+1});
		}
		return SmartMessage.getString("report.title.add_position_c" + this.getColumn() , new Object[]{ this.getRow()+1});
	}
}

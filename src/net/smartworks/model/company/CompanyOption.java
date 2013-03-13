package net.smartworks.model.company;

public class CompanyOption{

	private boolean htmlWriterPlugined;
	private boolean pdfWriterPlugined;
	
	public boolean isHtmlWriterPlugined() {
		return htmlWriterPlugined;
	}
	public void setHtmlWriterPlugined(boolean htmlWriterPlugined) {
		this.htmlWriterPlugined = htmlWriterPlugined;
	}
	public boolean isPdfWriterPlugined() {
		return pdfWriterPlugined;
	}
	public void setPdfWriterPlugined(boolean pdfWriterPlugined) {
		this.pdfWriterPlugined = pdfWriterPlugined;
	}
}

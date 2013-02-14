package pro.ucity.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

public class ExcelDownload extends AbstractView{
	
	 public void Download(){
		 setContentType("application/x-msexcel;charset=UTF-8");
	 }
	 @Override
		protected void renderMergedOutputModel(Map<String, Object> model, 
				HttpServletRequest request, HttpServletResponse response) throws Exception {
			// TODO Auto-generated method stub
			  // TODO Auto-generated method stub
			FileOutputStream fileOut = null;
			FileInputStream fis = null;
			OutputStream out = null;
			
	        File file = (File)model.get("downloadFile");
	        System.out.println("DownloadView --> file.getPath() : " + file.getPath());
	        System.out.println("DownloadView --> file.getName() : " + file.getName());
	         
//	        response.setContentType(getContentType());
	        response.setContentType("application/x-msexcel;charset=UTF-8");
	        response.setContentLength((int)file.length());
	         
	        String userAgent = request.getHeader("User-Agent");
	      
	        boolean ie = userAgent.indexOf("MSIE") > -1;
	         
//	        String fileName = null;
//	         
//	        if(ie){
//	        	fileName = URLEncoder.encode(file.getName(), "utf-8");                 
//	        } else {
//	            fileName = new String(file.getName().getBytes("utf-8"));
//	        }// end if;
	        response.setHeader("Content-Disposition", "attachment; filename=\"MonitoringChart.xls\";");
//	        response.setHeader("Content-Transfer-Encoding", "binary");
			HSSFWorkbook workbook = new HSSFWorkbook();			
	        try { 
	            fis = new FileInputStream(file);
	            out = response.getOutputStream();
	            
	            FileCopyUtils.copy(fis, out);    
	        	 workbook.write(out);
	        } catch(Exception e){
	            e.printStackTrace();  
	        }finally{
	            if(fis != null){
	                try{
	                	fis.close();
	                }catch(Exception e){}
	            }    
	            if(out != null){
	                try{
	                	out.close();
	                }catch(Exception e){}
	            }    
	        }// try end;
	        out.flush();
		}		
}





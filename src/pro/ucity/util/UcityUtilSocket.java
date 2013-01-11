package pro.ucity.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import net.smartworks.util.SmartUtil;

public class UcityUtilSocket {

	
	private static Logger logger = Logger.getLogger(UcityUtilSocket.class);
	/**
	 * @param args
	 */
	public UcityUtilSocket(){
		super();
	}
	
	public static void UcitySocketIn(String instanceId) throws UnknownHostException, IOException {
		
		DataOutputStream dou = null;
		DataInputStream din = null;
		Socket socket = null;
		logger.info("===========");
		logger.info("1단계 진입");
		logger.info("===========");
		String ip = "10.2.20.27";//서버 IP 주소 입력 
		int num = 6775;
		try{
			socket = new Socket(ip,num);
		
		
		logger.info("===========");
		logger.info("소켓생성완료");
		logger.info("===========");
		
		OutputStream out = socket.getOutputStream();
		logger.info("===========1");
		dou = new DataOutputStream(out);
		logger.info("===========2");
		InputStream in = socket.getInputStream();
		logger.info("===========3");
		din = new DataInputStream(in);
		logger.info("===========4");
		}catch(Exception e){
			logger.info("클라이언트소켓생성실패",e);
		}
		while(true){
			//instanceId가 없을 경우 소켓통신으로 다른 서버로 보냄.

			try{
				logger.info("===========4 instanceId" + instanceId);
				dou.writeUTF(instanceId);		
				logger.info("===========5");
			}catch(IOException e){
				logger.info("보내는데 오류발생", e);
			}
			//다른 서버에서 완료 메세지받음
			logger.info("===========6");
			String remsg = din.readUTF();
			logger.info("==============================");
			logger.info("다른서버에서보낸메시지 = " + remsg);
			logger.info("==============================");

			if(remsg.equalsIgnoreCase("END")){
				//END 메세지가 오면 소켓통신 종료.
				socket.close();
				logger.info("================================");
				logger.info("소켓통신이 끝나고 클로즈 되었습니다.");
				logger.info("================================");
			    break;
			}
		}
	}
}	 

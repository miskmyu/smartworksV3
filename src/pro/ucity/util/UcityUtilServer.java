package pro.ucity.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class UcityUtilServer implements ServletContextListener{
	
	public static final int TCP_CONNECTION_PORT = 5775;
	public static final int UDP_ABEND_PORT  = 5776;
	public static final int CONNECT_TIME_OUT = 10000;
	
//	ServerSocket serverSocket = null;

	private static Logger logger = Logger.getLogger(UcityUtilServer.class);
	
	DataOutputStream dou;
	DataInputStream din;	
	DatagramSocket ds;
	
	public static boolean start;

	/**
	 * @param args
	 */
	// TODO Auto-generated method stub
	
	public void go(){
		(new Thread(new UdpServer())).start();
	}
	
	public class UdpServer implements Runnable{
		
		public UdpServer() {
			try {
			   ds = new DatagramSocket(UDP_ABEND_PORT);
			   ds.setSoTimeout(CONNECT_TIME_OUT);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.info("ABEND UDP Error",e);
			}
		}

		public void run() {
			try{
				InetAddress addr = InetAddress.getLocalHost();
				String hostAddr = addr.getHostAddress();
				logger.info("======================================================");
				logger.info("서버소켓 생성 성공(현재 IP 주소) : " + hostAddr + " 입니다");
				logger.info("======================================================");
				while(start == true){
				    byte[] buffer = new byte[512];
				    DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				    try{
				    	ds.receive(dp);
//				    String instanceId = new String(dp.getData());
				    String instanceId = new String(buffer, 0, dp.getLength());
					    try{
					    	UcityUtil.stopAllPollingsForInstanceSocket(instanceId);
					    }catch (Exception e){
							logger.info("stopAllPolling error",e);				    	
					    }
					}catch(Exception e){
						if(start == false){
							break;
						}
						continue;	
					}
				}
			}catch(Exception e){
				logger.info("Server Socket Start Error",e);
			}
			logger.info("=================");
			logger.info("Server Socket End");
			logger.info("=================");			
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		try {
//			serverSocket.close();
            start = false;
			logger.info("Servcer Socket Closing....");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("Servcer Socket Error ( Closing... )", e);
		}
		logger.info("[ Server Socket End ]");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		UcityUtilServer server = new UcityUtilServer();
		start = true;
		server.go();
	}
}
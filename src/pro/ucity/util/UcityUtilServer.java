package pro.ucity.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import net.smartworks.util.SmartUtil;

import org.apache.log4j.Logger;

public class UcityUtilServer implements ServletContextListener{
	
	ServerSocket serverSocket = null;

	private static Logger logger = Logger.getLogger(UcityUtilServer.class);
	
	DataOutputStream dou;
	DataInputStream din;
	/**
	 * @param args
	 */
	// TODO Auto-generated method stub
	
	public void go(){
		Thread serverThread = new Thread(new listenConection());
		serverThread.start();
	}
	
	public class listenConection implements Runnable{
		
		public listenConection() {
			try {
				serverSocket = new ServerSocket(6775);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.info("클라이언트 소켓에러",e);
			}
		}
			public void run() {
				try{
					logger.info("================");
					logger.info("서버소켓 생성 성공");
					logger.info("================");
					while(true){
						Socket socket = serverSocket.accept();
						logger.info("=================");
						logger.info("다른서버에 연결성공");
						logger.info("=================");
						
						Thread clientThread = new Thread(new ClientHandler(socket));
						clientThread.start();
					}
				}catch(Exception e){
					logger.info("서버에러",e);
				}
			}
	}
	
	public void instanceIdSend(String instanceId){
		try {
			UcityUtil.stopAllPollingsForInstanceSocket(instanceId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("instanceIdSend error", e);
		}
	}

	public class ClientHandler implements Runnable {
			
			Socket clientSocket;
			String msg;
			
			public ClientHandler(Socket socket) {
				clientSocket = socket;
				try{
					InputStream in = socket.getInputStream();
					OutputStream out = socket.getOutputStream();
					
					din = new DataInputStream(in);
					msg = din.readUTF();

					dou = new DataOutputStream(out);
					dou.writeUTF("END");
				}catch(Exception e){
					logger.info("ClientHandler error",e);
				}
			}
			
			public void run() {
				
				String instanceId = null;
				
				try{
					while(true){
						while(!SmartUtil.isEmpty(msg)){
							instanceId = msg;
							instanceIdSend(instanceId);
							break;
						}
						break;
					}
				}catch(Exception e){
					logger.info("run() error",e);
				}
			}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("해당 서버소켓 종료 실패");
		}
		logger.info("종료");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		UcityUtilServer server = new UcityUtilServer();
		server.go();
	}
}
package net.smartworks.server.engine.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class FileUtil {
	private static Log logger = LogFactory.getLog(FileUtil.class);
	public static final String RN = CommonUtil.RN;
	public static final String ENCODING_UTF8 = "UTF-8";
	public static final String ENCODING_EUCKR = "EUC-KR";

	public static void copy(String from, String to, boolean overwrite) throws Exception {
		copy(new File(from), to, overwrite);
	}
	public static void copy(File fromFile, String to, boolean overwrite) throws Exception {
		if (fromFile == null)
			return;
		if (!fromFile.exists())
			throw new Exception("not exist file: " + fromFile.getAbsolutePath());
		
		if (fromFile.isDirectory()) {
			File toFile = new File(to);
			if (!toFile.exists())
				toFile.mkdirs();
			
			File[] files = fromFile.listFiles();
			if (CommonUtil.isEmpty(files))
				return;
			
			int fileLength = files.length;
			for (int i=0; i<fileLength; i++) {
				File file = files[i];
				copy(file, PathUtil.toPath(to) + "/" + file.getName(), overwrite);
			}
			return;
		}

		createFile(to, overwrite);
		
		FileInputStream is = null;
		FileOutputStream os = null;
		FileChannel ic = null;
		FileChannel oc = null;
		try {
			is = new FileInputStream(fromFile);
			os = new FileOutputStream(to);
			ic = is.getChannel();
			oc = os.getChannel();
			long size = ic.size();
			ic.transferTo(0, size, oc);
		} catch (Exception e) {
			throw e;
		} finally {
			close(oc);
			close(ic);
			close(os);
			close(is);
		}
	}
	public static void move(String from, String to, boolean overwrite) throws Exception {
		copy(from, to, overwrite);
		deleteAll(from);
	}
	protected static void createFile(String path, boolean overwrite) throws Exception {
		File file = new File(path);
		createFile(file, overwrite);
	}
	protected static void createFile(File file, boolean overwrite) throws Exception {
		File dir = file.getParentFile();
		if(dir != null && !dir.exists())
			dir.mkdirs();
		if (file.exists()) {
			if (!overwrite)
				throw new Exception("The file is already exist path:" + file.getAbsolutePath());
		} else {
			file.createNewFile();
		}
	}
	public static Object read(String path) throws Exception {
		InputStream is = null;
		ObjectInputStream ois = null;
		try {
			File file = new File(path);
			if(!file.exists())
				return null;
			is = new FileInputStream(file);
			ois = new ObjectInputStream(is);
			Object obj = ois.readObject();
			ois.close();
			return obj;
			
		} catch(Exception e) {
			throw e;
		} finally {
			close(ois);
			close(is);
		}
	}
	public static String encodeAsBase64(String path) throws Exception {
		InputStream is = null;
		ByteArrayOutputStream os = null;
		//BufferedOutputStream bos = null;
		try {
			File file = new File(path);
			is = new FileInputStream(file);
			os = new ByteArrayOutputStream();
			new BASE64Encoder().encode(is, os);
			//bos = new BufferedOutputStream(os);
			byte[] results = os.toByteArray();
			
			return new String(results);
		} catch (Exception e) {
			throw e;
		} finally {
			//close(bos);
			close(os);
			close(is);
		}
	}
	public static File writeByBase64(String path, String base64, boolean overwrite) throws Exception {
		return write(path, new BASE64Decoder().decodeBuffer(base64), overwrite);
	}
	public static File write(String path, byte[] bt, boolean overwrite) throws Exception {
		OutputStream os = null;
		try {
			File file = new File(path);
			createFile(file, overwrite);
			
			os = new FileOutputStream(file);
			os.write(bt);
			os.flush();
			
			return file;
		
		} catch(Exception e) {
			throw e;
		} finally {
			close(os);
		}
	}
	public static File write(String path, Object obj, boolean overwrite)throws Exception {
		ObjectOutputStream oos = null;
		OutputStream os = null;
		try {
			File file = new File(path);
			createFile(file, overwrite);
			
			os = new FileOutputStream(file);
			oos = new ObjectOutputStream(os);
			oos.writeObject(obj);
			oos.flush();
			os.flush();
			return file;
		} catch(Exception e) {
			throw e;
		} finally {
			close(os);
			close(oos);
		}
	}
	public static String readString(File file) throws Exception {
		return readString(file, null);
	}
	public static String readString(File file, String encoding) throws Exception {
		if (encoding == null)
			encoding = ENCODING_UTF8;
		InputStreamReader fr = null;
		BufferedReader br = null;
		try {
			StringBuffer buf = new StringBuffer();
			fr = new InputStreamReader(new FileInputStream(file), encoding);
			br = new BufferedReader(fr);
			long line = 0;
			String str;
			while((str = br.readLine()) != null) {
				if (line++ != 0)
					buf.append(RN);
				buf.append(str);
			}
			return buf.toString();
		} catch (FileNotFoundException e) {
			if (logger.isInfoEnabled())
				logger.info("No such file: " + file.getAbsolutePath());
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			close(br);
			close(fr);
		}
	}
	public static String readString(String path) throws Exception {
		return readString(path, null);
	}
	public static String readString(String path, String encoding) throws Exception {
		if (encoding == null)
			encoding = ENCODING_UTF8;
		InputStreamReader fr = null;
		BufferedReader br = null;
		try {
			StringBuffer buf = new StringBuffer();
			fr = new InputStreamReader(new FileInputStream(path), encoding);
			br = new BufferedReader(fr);
			long line = 0;
			String str;
			while((str = br.readLine()) != null) {
				if (line++ != 0)
					buf.append(RN);
				buf.append(str);
			}
			return buf.toString();
		} catch (FileNotFoundException e) {
			File file = new File(path);
			if (logger.isInfoEnabled())
				logger.info("No such file: " + file.getAbsolutePath());
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			close(br);
			close(fr);
		}
	}
	public static void writeString(String path, String content, boolean overwrite) throws Exception {
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			createFile(path, overwrite);
			fw = new FileWriter(path, false);
			bw = new BufferedWriter(fw);
			bw.write(content);
			bw.flush();
			fw.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			close(bw);
			close(fw);
		}
	}
	public static void delete(String path) throws Exception {
		File file = new File(path);
		delete(file);
	}
	public static void delete(File file) throws Exception {
		if (!file.exists())
			return;
		boolean success = file.delete();
		if (!success)
			throw new Exception("Delete failed path:" + file.getAbsolutePath());
	}
	public static void deleteOnExit(String path) throws Exception {
		File file = new File(path);
		deleteOnExit(file);
	}
	public static void deleteOnExit(File file) throws Exception {
		if (!file.exists())
			return;
		file.deleteOnExit();
	}
	public static void deleteAll(String path) throws Exception {
		File file = new File(path);
		deleteAll(file);
	}
	public static void deleteAll(File file) throws Exception {
		if (!file.exists())
			return;
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			if (!CommonUtil.isEmpty(files)) {
				int fileLength = files.length;
				for(int i=0; i<fileLength; i++) {
					File childFile = files[i];
					if(childFile.isDirectory()) {
						deleteAll(childFile);
					} else {
						childFile.delete();
					}
				}
			}
		}
		boolean success = file.delete();
		if (!success)
			throw new Exception("Delete failed path:" + file.getAbsolutePath());
	}
	public static void close(InputStream obj) {
		if (obj == null)
			return;
		try {
			obj.close();
		} catch (Exception e) {
			logger.warn(e, e);
		}
	}
	public static void close(OutputStream obj) {
		if (obj == null)
			return;
		try {
			obj.close();
		} catch (Exception e) {
			logger.warn(e, e);
		}
	}
	public static void close(Channel obj) {
		if (obj == null)
			return;
		try {
			obj.close();
		} catch (Exception e) {
			logger.warn(e, e);
		}
	}
	public static void close(Reader obj) {
		if (obj == null)
			return;
		try {
			obj.close();
		} catch (Exception e) {
			logger.warn(e, e);
		}
	}
	public static void close(Writer obj) {
		if (obj == null)
			return;
		try {
			obj.close();
		} catch (Exception e) {
			logger.warn(e, e);
		}
	}
}
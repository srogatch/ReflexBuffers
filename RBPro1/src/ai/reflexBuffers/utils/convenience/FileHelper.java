package ai.reflexBuffers.utils.convenience;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import ai.reflexBuffers.utils.stability.CoreLog;

public class FileHelper {
	public static boolean close(Closeable c) {
		if( c == null ) {
			return true;
		}
		try {
			c.close();
			return true;
		} catch(Exception ex) {
			CoreLog._.failedToClose(c);
			return false;
		}
	}
	public static String[] readAllLines(File fp) {
		BufferedReader brd = null;
		try {
			brd = new BufferedReader(new FileReader(fp));
			String line;
			ArrayList<String> strings = new ArrayList<String>();
			while( (line = brd.readLine()) != null ) {
				strings.add(line);
			}
			String[] result = strings.toArray(new String[strings.size()]);
			return result;
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			FileHelper.close(brd);
		}
	}
}

package tk.genghis.attendance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AttendanceAnalyze {

	private static SimpleDateFormat minuteFormatter = new SimpleDateFormat("yyyyMMddHHmm");
	private static SimpleDateFormat dayFormatter = new SimpleDateFormat("yyyyMMdd");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, String> workHours = new HashMap<String, String>();
		String recordPath = System.getenv("APPDATA") + File.separator + "Genghis" + File.separator + "AttendanceRecord"
				+ File.separator + "attendance.csv";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(recordPath)));
			String line = br.readLine();
			while (line != null) {
				if (line.isEmpty()) {
					line = br.readLine();
					continue;
				}
				List<String> minutes = Arrays.asList(line.split(",", 150));
				Collections.sort(minutes, new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {
						return o1.compareTo(o2);
					}
				});
				Date firstTime = minuteFormatter.parse(minutes.get(0));
				Date lastTime = minuteFormatter.parse(minutes.get(minutes.size() - 1));
				long diff = lastTime.getTime() - firstTime.getTime();
				double hourDiff = ((double) diff) / (60 * 60 * 1000);
				workHours.put(dayFormatter.format(firstTime), String.valueOf(hourDiff));
				line = br.readLine();
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}

		for (Entry<String, String> entry : workHours.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue() + " hours");
		}
	}
}

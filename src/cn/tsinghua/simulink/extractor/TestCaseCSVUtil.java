package cn.tsinghua.simulink.extractor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import com.csvreader.CsvWriter;

public class TestCaseCSVUtil {

	public static void WriteTestCasesToCSV(String model_name, List<Map<String, Double>> test_cases) {
		if (test_cases.size() > 0) {
			try {
				Map<String, Double> test_case = test_cases.get(0);
				String csvFilePath = model_name + ".csv";
				File csvFile = new File(csvFilePath);
				CsvWriter csvWriter = new CsvWriter(csvFilePath, ',', Charset.forName("UTF-8"));
				Set<String> tcs = test_case.keySet();
				String[] headers = new String[tcs.size()];
				int hi = 0;
				Iterator<String> hc_itr = tcs.iterator();
				while (hc_itr.hasNext()) {
					String tc = hc_itr.next();
					headers[hi] = tc;
					hi++;
				}
//				String[] headers = { "FileName", "FileSize", "FileMD5" };
				csvWriter.writeRecord(headers);
				Iterator<Map<String, Double>> tc_itr = test_cases.iterator();
				while (tc_itr.hasNext()) {
					Map<String, Double> one_test_case = tc_itr.next();
					int h_len = headers.length;
					String[] datas = new String[h_len];
					for (int i=0;i<h_len;i++) {
						String header = headers[i];
						Double val = one_test_case.get(header);
						datas[i] = val + "";
					}
					csvWriter.writeRecord(datas);
				}
				csvWriter.close();
				JOptionPane.showMessageDialog(null, "Test cases:" + csvFile.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null, "No test case generated yet!");
		}
	}

}

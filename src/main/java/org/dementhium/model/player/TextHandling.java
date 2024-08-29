package org.dementhium.model.player;

import java.util.*;
import java.io.*;

public class TextHandling {

	public static List<String> ipStarter = new ArrayList<String>();
	public static List<String> flagBan = new ArrayList<String>();
	public static List<String> reportLogs = new ArrayList<String>();

	public TextHandling() {
		update();
	}

	public String reportLogs(int reportId) {
		reportLogs.clear();
		try {       
			BufferedReader in = new BufferedReader(new FileReader("data/reports/"+reportId+".txt"));
			String inFile;
			while ((inFile = in.readLine()) != null) {
				reportLogs.add(inFile);
			}
			String listString = "";
			for (String s : reportLogs) {
				listString += s;
			}
			return listString;
		} catch (Exception e) {}
		return "";
	}


	public List<String> getIpStarter() {
		return ipStarter;
	}

	public List<String> getFlagBan() {
		return flagBan;
	}

	public static void update() {
		ipStarter();
		flagBan();
	}

	public boolean fileExists(String path) {
		File file = new File(path +".txt");
		return file.exists();
	}

	public static void ipStarter() {
		ipStarter.clear();
		try {       
			BufferedReader in = new BufferedReader(new FileReader("data/text/ipstarters.txt"));
			String inFile;
			while ((inFile = in.readLine()) != null) {
				ipStarter.add(inFile);
			}
		} catch (Exception e) {}
	}

	public static void flagBan() {
		flagBan.clear();
		try {       
			BufferedReader in = new BufferedReader(new FileReader("data/text/flagban.txt"));
			String inFile;
			while ((inFile = in.readLine()) != null) {
				flagBan.add(inFile);
			}
		} catch (Exception e) {
		}
	}

	public void writeTo(String text, String path) {
		if (text != null) {
			File sampleFile = new File(path +".txt");
			if(sampleFile.exists()) {
				try {
					FileWriter fw = new FileWriter(sampleFile, true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(text);
					bw.newLine();
					bw.flush();
					bw.flush();
				} catch (Exception e) {}
			} else {
				try {
					sampleFile.createNewFile();
					FileWriter fw = new FileWriter(sampleFile);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(text);
					bw.newLine();
					bw.flush();
					bw.flush();
				} catch (Exception e) {}
			}
		}
		update();
	}

	public void deleteFrom(String name, String path) {
		if (name != null) {
			File file = new File(path + ".txt");
			if (file.exists()) {   
				try {
					BufferedReader in = new BufferedReader(new FileReader(file));
					BufferedWriter out = new BufferedWriter(new FileWriter(file));
					String inFile;
					while ((inFile = in.readLine()) != null) {
						if (inFile.equalsIgnoreCase(name))
							out.write(inFile);
					}
				} catch (Exception e) {}
			}
		}
		update();
	}

	public boolean gotStarter(Player player) {
		String ip = player.getIp();
		for(String s : getIpStarter()) {
			if(ip.startsWith(s)) {
				return true;
			}
		}
        return ipStarter.contains(ip);
    }

}
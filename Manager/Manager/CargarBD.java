package Manager;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import BaseDeDatos.BD;
import BaseDeDatos.MapeoClase;

public class CargarBD {

	public static void main(String a[]) {
		String path = System.getProperty("user.dir");
		int fin = path.indexOf("AdaLovelace2.0-AI");
		path = path.substring(0, fin);
		path += "AdaLovelace2.0-AI";

		listf(path);

		return;
	}

	public static List<File> listf(String directoryName) {
		File directory = new File(directoryName);

		List<File> resultList = new ArrayList<File>();

		// get all the files from a directory
		File[] fList = directory.listFiles();
		resultList.addAll(Arrays.asList(fList));
		for (File file : fList) {
			String absolutePath = file.getAbsolutePath();
			String name = absolutePath.replaceFirst(".*AdaLovelace2.0-AI", "AdaLovelace2.0-AI");
			if (file.isFile() && (name.endsWith(".java") || name.endsWith(".xml"))) {
				try {
					String codigo = "";
					Scanner leer;
					leer = new Scanner(new File(absolutePath));

					while (leer.hasNext())
						codigo += leer.nextLine() + "\n";

					leer.close();

					BD.ingresarClase(new MapeoClase(name.replace("\\", "."), codigo, Instant.now().toString()));
				} catch (Exception e) {
				}
			} else if (file.isDirectory()) {
				resultList.addAll(listf(absolutePath));
			}
		}
		// System.out.println(fList);
		return resultList;
	}

}

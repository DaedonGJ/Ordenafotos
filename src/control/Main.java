package control;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class Main {

	public static String newdir, oldir;

	public static void main(String[] args) {

		oldir = args[0];
		newdir = args[1];
		try {
			leerdir(oldir);
		} catch (ImageProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// leermetadatos("src/IMG_20180929_200338.jpg");

	}

	private static void leerdir(String path) throws IOException, ImageProcessingException {
		File file = new File(path);
		File[] files;
		if (file.isDirectory()) {
			files = file.listFiles();
			for (File nfile : files) {
				leerdir(nfile.getCanonicalPath());
			}

		}
		if (file.isFile()) {
			// System.out.println(file.getCanonicalPath());
			Pattern patron = Pattern.compile(".*jpg");

			Matcher m = patron.matcher(file.getCanonicalPath());
			if (m.find()) {
				System.out.println("esto es lo queremos :" + file.getName());
				Date date = leermetadatos(file);
				System.out.println(date);

				String ndir = creardirectorio(date);
				moverarchivo(file, ndir);

			}

		}

	}

	private static void moverarchivo(File file, String ndir) {

		file.renameTo(new File(ndir + "/" + file.getName()));
	}

	private static String creardirectorio(Date date) {
		
		
		Map<String,String> fechas = new HashMap<String,String>();
		fechas.put("01", "Enero");
		fechas.put("02", "Febrero");
		fechas.put("03", "Marzo");
		fechas.put("04", "Abril");
		fechas.put("05", "Mayo");
		fechas.put("06", "Junio");
		fechas.put("07", "Julio");
		fechas.put("08", "Agosto");
		fechas.put("09", "Septiembre");
		fechas.put("10", "Octubre");
		fechas.put("11", "Noviembre");
		fechas.put("12", "Diciembre");
		String mes, anyo;

		SimpleDateFormat formatterMM = new SimpleDateFormat("MM");
		SimpleDateFormat formatteryyyy = new SimpleDateFormat("yyyy");

		mes = formatterMM.format(date);
		mes= fechas.get(mes);
		anyo = formatteryyyy.format(date);
		File nuevodir = new File(newdir + "/" + anyo + "/" + mes);
		System.out.println(nuevodir.toString());

		if (!nuevodir.exists()) {
			nuevodir.mkdirs();
		}
		return nuevodir.getAbsolutePath();

	}

	public static Date leermetadatos(File file) throws ImageProcessingException, IOException {

		
		
		Date date = new Date();
		File imagePath = new File(file.getAbsolutePath());
		Metadata metadata;

		metadata = ImageMetadataReader.readMetadata(imagePath);
		ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

		if (directory != null) {

			if (directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL) != null) {
				date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
			}
		} else {
			Calendar myCal = Calendar.getInstance();
			String regex = "^IMG";
			Pattern patron = Pattern.compile(regex);
			Matcher m = patron.matcher(file.getName());
			System.out.println("imgaaaaaaaaaaaaa");
			if (m.find()) {
				regex = "^IMG-(\\d{4})(\\d{2})(\\d{2})";
				patron = Pattern.compile(regex);
				m = patron.matcher(file.getName());
				;
				if (m.find()) {
					String mes, anyo, dia;
					anyo = m.group(1);
					mes = m.group(2);
					dia = m.group(3);
					System.out.println("Tel: " + m.group(1));

					myCal.set(Calendar.YEAR, Integer.parseInt(anyo));
					myCal.set(Calendar.MONTH, Integer.parseInt(mes));
					myCal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dia));
					date = myCal.getTime();

				}
			} else {
				myCal.set(Calendar.YEAR, 2000);
				myCal.set(Calendar.MONTH, 01);
				myCal.set(Calendar.DAY_OF_MONTH, 01);
				date = myCal.getTime();
			}

		}
		System.out.println(date);

		return date;
	}

}

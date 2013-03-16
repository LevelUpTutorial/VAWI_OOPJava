package de.vawi.oopjava.kantinenplaner.oberflaeche;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Julia Meyer
 */
public final class DatumPlausiHelper {

	private static final String FORMAT_DATUM = "dd.MM.yyyy";

	public static boolean isDatumGueltig(String datum) {
		if (datum == null) {
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATUM);
		sdf.setLenient(false);

		try {
			@SuppressWarnings("unused")
			Date date = sdf.parse(datum);

		} catch (ParseException e) {

			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean isZeitraumGueltig(String beginn, String ende) {
		String einstJahr = beginn.substring(6);
		String einstMon = beginn.substring(3, 5);
		String einstTag = beginn.substring(0, 2);
		Integer beginnJahr = Integer.parseInt(einstJahr);
		Integer beginnMon = Integer.parseInt(einstMon);
		Integer beginnTag = Integer.parseInt(einstTag);

		String datJahr = ende.substring(6);
		String datMon = ende.substring(3, 5);
		String datTag = ende.substring(0, 2);
		Integer endeJahr = Integer.parseInt(datJahr);
		Integer endeMon = Integer.parseInt(datMon);
		Integer endeTag = Integer.parseInt(datTag);

		Calendar cal1 = new GregorianCalendar();
		Calendar cal2 = new GregorianCalendar();
		cal1.set(beginnJahr, beginnMon - 1, beginnTag, 0, 0, 0);
		cal2.set(endeJahr, endeMon - 1, endeTag, 0, 0, 0);
		long zeit = cal2.getTime().getTime() - cal1.getTime().getTime();
		return (zeit < 0) ? false : true;
	}

	/**
	 * Getter für <code>formatDatum</code>
	 * 
	 * @return String Gibt formatDatum zurück.
	 */
	public static String getFormatDatum() {
		return FORMAT_DATUM;
	}

	public static String datumToString(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		DateFormat dfmt = new SimpleDateFormat(FORMAT_DATUM);
		return dfmt.format(date);
	}

}

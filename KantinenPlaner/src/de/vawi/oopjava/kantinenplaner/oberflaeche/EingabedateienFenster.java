package de.vawi.oopjava.kantinenplaner.oberflaeche;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import de.vawi.oopjava.kantinenplaner.configuration.PropertiesUtil;
import javax.swing.JList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Color;
import javax.swing.ImageIcon;

/**
 * Dialog Eingabedateien
 * 
 * @author Julia Meyer
 * @version 2.0 In dieser Oberfläche können die Pfade zu den Eingabedateien
 *          gesetzt werden. Anhand des Buttons "..." können weitere Dateien
 *          hinzugefügt werden und mit dem Button "x" werden die ausgewählten
 *          Dateien gelöscht.
 */
public class EingabedateienFenster extends JDialog {
	private static final long serialVersionUID = 1L;
	private JList<String> textPfadHaendler;
	private DefaultListModel<String> listModelHaendler;
	private JList<String> textPfadRezept;
	private DefaultListModel<String> listModelRezept;
	private JScrollPane scrollPaneRezept;

	private JList<String> textPfadHitliste;
	private DefaultListModel<String> listModelHitliste;
	private JScrollPane scrollPaneHitliste;

	private JButton btnFileChooserHaendler;
	private JButton btnFileChooserRezept;
	private JButton btnFileChooserHitliste;

	final Logger logger = LogManager.getLogger(EingabedateienFenster.class);

	/**
	 * 
	 * Konstruktor hier wird die graphische Oberfläche erzeugt. Als
	 * Ausgabefelder werden jeweils eine JList verwendet. Der Hinzufuege-Button
	 * ruft einen FileChooser auf, wo die Dateien ausgewählt werden können. Der
	 * Loesche-Button entfernt das makierte Element aus der JList. Der
	 * Uebernehme-Button prueft und aktualisiert die Properties. Der
	 * Abrechen-Buuton nimmt keine Änderungen vor.
	 */
	public EingabedateienFenster() {
		setAlwaysOnTop(true);
		setBounds(100, 100, 515, 349);
		getContentPane().setLayout(null);
		setTitle("Pfad der Eingabedateien");

		listModelHaendler = new DefaultListModel<String>();

		JScrollPane scrollPaneHaendler = new JScrollPane();
		scrollPaneHaendler.setBounds(20, 36, 404, 87);
		getContentPane().add(scrollPaneHaendler);

		textPfadHaendler = new JList<String>(listModelHaendler);
		scrollPaneHaendler.setViewportView(textPfadHaendler);
		JLabel lblPfadDerHaendler = new JLabel("Pfad der Haendler");
		lblPfadDerHaendler.setBounds(20, 11, 201, 28);
		getContentPane().add(lblPfadDerHaendler);

		btnFileChooserHaendler = new JButton("...");
		btnFileChooserHaendler.setBounds(434, 68, 26, 23);
		btnFileChooserHaendler.setToolTipText("Hinzufügen");
		btnFileChooserHaendler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				initialisiereFileChooser(listModelHaendler, true);
			}
		});
		getContentPane().add(btnFileChooserHaendler);

		JButton btnXHaendler = new JButton("");
		btnXHaendler
				.setIcon(new ImageIcon(
						EingabedateienFenster.class
								.getResource("/javax/swing/plaf/metal/icons/ocean/paletteClose.gif")));
		btnXHaendler.setBounds(463, 68, 26, 23);
		btnXHaendler.setToolTipText("Löschen");
		btnXHaendler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				{
					removeListelement(textPfadHaendler);
				}
			}
		});
		getContentPane().add(btnXHaendler);

		listModelRezept = new DefaultListModel<String>();

		textPfadRezept = new JList<String>(listModelRezept);
		scrollPaneRezept = new JScrollPane();
		scrollPaneRezept.setBounds(20, 153, 404, 41);
		getContentPane().add(scrollPaneRezept);
		scrollPaneRezept.setViewportView(textPfadRezept);

		JLabel lblPfadDerRezeptdatei = new JLabel("Pfad der Rezeptdatei");
		lblPfadDerRezeptdatei.setBounds(20, 135, 180, 20);
		getContentPane().add(lblPfadDerRezeptdatei);

		btnFileChooserRezept = new JButton("...");
		btnFileChooserRezept.setBounds(434, 152, 26, 23);
		btnFileChooserRezept.setToolTipText("Hinzufügen");
		getContentPane().add(btnFileChooserRezept);
		btnFileChooserRezept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (listModelRezept.getSize() == 0) {
					textPfadRezept.setBackground(Color.WHITE);
					textPfadRezept
							.setToolTipText("Es muss eine Datei für die Bearbeitung ausgewählt werden.");
					initialisiereFileChooser(listModelRezept, false);
				} else {
					setWarning(textPfadRezept, false);
					logger.error("Es kann nur eine Eingabedatei als Hitliste ausgewaehlt werden.");
				}
			}
		});

		JButton btnXRezept = new JButton("");
		btnXRezept.setToolTipText("Löschen");
		btnXRezept
				.setIcon(new ImageIcon(
						EingabedateienFenster.class
								.getResource("/javax/swing/plaf/metal/icons/ocean/paletteClose.gif")));
		btnXRezept.setBounds(463, 152, 26, 23);
		btnXRezept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				removeListelement(textPfadRezept);
			}
		});
		getContentPane().add(btnXRezept);

		listModelHitliste = new DefaultListModel<String>();

		scrollPaneHitliste = new JScrollPane();
		scrollPaneHitliste.setBounds(20, 224, 404, 42);
		scrollPaneHitliste
				.setToolTipText("Es kann muss eine Datei für die Bearbeitung ausgewählt werden.");
		getContentPane().add(scrollPaneHitliste);

		textPfadHitliste = new JList<String>(listModelHitliste);
		scrollPaneHitliste.setViewportView(textPfadHitliste);
		{
			JLabel lblPfadDerHitliste = new JLabel("Pfad der Hitliste");
			lblPfadDerHitliste.setBounds(20, 205, 180, 14);
			getContentPane().add(lblPfadDerHitliste);
		}

		btnFileChooserHitliste = new JButton("...");
		btnFileChooserHitliste.setToolTipText("Hinzufügen");
		btnFileChooserHitliste.setBounds(434, 217, 26, 23);
		getContentPane().add(btnFileChooserHitliste);
		btnFileChooserHitliste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (listModelHitliste.getSize() == 0) {
					textPfadHitliste.setBackground(Color.WHITE);
					textPfadHitliste
							.setToolTipText("Es kann muss eine Datei für die Bearbeitung ausgewählt werden.");
					initialisiereFileChooser(listModelHitliste, false);
				} else {
					setWarning(textPfadHitliste, false);
				}
			}

		});

		JButton btnXHitliste = new JButton("");
		btnXHitliste.setToolTipText("Löschen");
		btnXHitliste
				.setIcon(new ImageIcon(
						EingabedateienFenster.class
								.getResource("/javax/swing/plaf/metal/icons/ocean/paletteClose.gif")));
		btnXHitliste.setBounds(463, 217, 26, 23);
		btnXHitliste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				removeListelement(textPfadHitliste);
			}
		});
		getContentPane().add(btnXHitliste);

		JButton btnOK = new JButton("Übernehmen");
		btnOK.setBounds(243, 277, 122, 23);
		getContentPane().add(btnOK);
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (mindesteingabe()) {
					try {
						aktualisierePfadProperties();
					} catch (FileNotFoundException e) {
						logger.error("Die Poperties-Datei \"planer.cfg\" kann nicht gefunden werden.");
						e.printStackTrace();
					} catch (IOException e) {
						logger.error("Die Poperties-Datei \"planer.cfg\" kann nicht geoeffnet werden.");
						e.printStackTrace();
					}
					setVisible(false);
				} else {
					logger.error("Es sind nicht alle Mindesteingaben getätigt.");
				}
			}

		});

		JButton btnAbrechen = new JButton("Abbrechen");
		btnAbrechen.setBounds(387, 277, 102, 23);
		btnAbrechen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
			}
		});
		getContentPane().add(btnAbrechen);

	}

	@SuppressWarnings("unchecked")
	/**
	 * Diese Methode erzeugt einen FileChooser, öffnet ihn und uebergibt die ausgewählten Ergebnisse an das gewuenschte Objekt.
	 * @param textfenster Object Zielobjekt an welches die Auswahl des FileChoosers angehängt werden soll 
	 * @param multiselect Boolean Variable, welche angibt ob eine Mehrfachauswahl erlaubt ist
	 */
	private void initialisiereFileChooser(Object textfenster,
			boolean multiselect) {
		JFileChooser fc = new JFileChooser();
		logger.debug("Der FileChoser wird eingerichtet.");
		fc.setMultiSelectionEnabled(multiselect);
		fc.setCurrentDirectory(new File("."));
		fc.setFileFilter(new FileNameExtensionFilter("CSV-Dateien", "csv"));
		int state = fc.showOpenDialog(this);

		if (state == JFileChooser.APPROVE_OPTION) {

			if (multiselect) {
				File[] file = fc.getSelectedFiles();

				if (textfenster instanceof DefaultListModel<?>) {
					logger.debug(file.length
							+ " Dateie(n) wurde(n) ausgewaehlt.");
					for (int i = 0; i < file.length; i++) {
						((DefaultListModel<String>) textfenster)
								.addElement(file[i].getPath());
						logger.debug(file[i].getAbsolutePath()
								+ " wurde dem Textfeld hinzugefuegt");
					}
				}
				logger.trace("Es wurde auf OK geklickt. Ausgewaehlte Dateien hinzugefügt.");
			} else {
				File file = fc.getSelectedFile();

				if (textfenster instanceof DefaultListModel<?>) {
					logger.debug("Eine Dateie wurde ausgewaehlt.");
					((DefaultListModel<String>) textfenster).addElement(file
							.getPath());
					logger.debug(file.getAbsolutePath()
							+ " wurde dem Textfeld hinzugefuegt");
				}
				logger.trace("Es wurde auf Uebernehmen geklickt. Ausgewaehlte Dateien hinzugefügt.");
			}
		} else {
			logger.trace("Keine neue Datei ausgewählt. Abbrechen gedrueckt.");
		}
	}

	/**
	 * Entfernt aus der JList das makierte Element
	 * 
	 * @param listModel
	 *            DefaultListModel<String> ListModel aus dem das makierte
	 *            Element entfernt werden soll
	 * @param list
	 */
	private void removeListelement(JList<String> list) {
		if (list.getModel().getSize() > 0 && list.getSelectedIndex() >= 0) {
			((DefaultListModel<String>) list.getModel()).remove(list
					.getSelectedIndex());
		}
	}

	/**
	 * Füllt die Felder mit dem Inhalt der Propertiesdatei
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void fillPorperties() throws FileNotFoundException, IOException {
		PropertiesUtil util = new PropertiesUtil();

		List<String> preisliste = util.getPreislisten();
		String[] preisarray = preisliste.toArray(new String[preisliste.size()]);

		for (int i = 0; i < preisarray.length; i++) {
			listModelHaendler.addElement(preisarray[i].toString());
		}
		listModelHitliste.addElement(util.getHitliste());
		listModelRezept.addElement(util.getRezepte());

	}

	/**
	 * Uebertraegt die vorgenommenen Aenderungen in die Propertiesdatei
	 * (planer.cfg)
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void aktualisierePfadProperties() throws FileNotFoundException,
			IOException {
		PropertiesUtil util = new PropertiesUtil();
		Properties properties = util.getProperties();

		String replace = listModelHitliste.toString().replace("[", "")
				.replace("]", "").replace('\\', '/').replace(" ", "");
		System.out.println("Rezept: " + replace);
		System.out.println("HItliste: " + replace);

		properties.setProperty(PropertiesUtil.KEY_HITLISTE, "" + replace);

		String string = listModelHaendler.toString().replace("[", "")
				.replace("]", "").replace('\\', '/').replace(" ", "");
		System.out.println("Rezept: " + string);
		properties.setProperty(PropertiesUtil.KEY_PREISLISTE, "" + string);

		String string2 = listModelRezept.toString().replace("[", "")
				.replace("]", "").replace('\\', '/').replace(" ", "");
		System.out.println("Rezept: " + string2);
		properties.setProperty(PropertiesUtil.KEY_REZEPTE, "" + string2);

		util.aktualisiereProperties(properties);
	}

	/**
	 * Ueberprueft ob die JLists ihre Mindeteingabedateien besitzen. Es muss
	 * mindestens eine HaendlerDatei vorhanden sein und genau ein Rezept und
	 * HitlisteDatei.
	 * 
	 * @return Ergebnis ob die MindestDateianzahl erfuellt ist.
	 */
	private boolean mindesteingabe() {
		int unerfuellteBedingungen = 0;

		if (listModelHaendler.getSize() == 0) {
			this.setWarning(textPfadHaendler, true);
			unerfuellteBedingungen++;
		}
		if (listModelRezept.getSize() != 1) {
			this.setWarning(textPfadRezept, false);
			unerfuellteBedingungen++;
		}
		if (listModelHitliste.getSize() != 1) {
			this.setWarning(textPfadHitliste, false);
			unerfuellteBedingungen++;
		}

		return (unerfuellteBedingungen > 0) ? false : true;
	}

	/**
	 * Vermerkt im ToolTip und anhand eines Roten Hintergrunds das ein Fehler
	 * vorliegt.
	 * 
	 * @param object
	 * @param multiselect
	 */
	private void setWarning(JList<String> object, boolean multiselect) {
		object.setBackground(Color.RED);

		String warntxt;
		if (multiselect)
			warntxt = "Es muss mindestens eine Datei ausgewählt werden";
		else
			warntxt = "Es muss genau eine Datei ausgewählt werden";
		object.setToolTipText(warntxt);

		logger.warn("Problem im Feld " + object.toString() + ". " + warntxt);
	}

}

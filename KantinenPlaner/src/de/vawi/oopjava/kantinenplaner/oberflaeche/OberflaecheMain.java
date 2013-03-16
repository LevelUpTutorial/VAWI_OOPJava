package de.vawi.oopjava.kantinenplaner.oberflaeche;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.vawi.oopjava.kantinenplaner.KantinenPlaner;
import de.vawi.oopjava.kantinenplaner.KantinenPlanerException;
import de.vawi.oopjava.kantinenplaner.configuration.OutputFormat;
import de.vawi.oopjava.kantinenplaner.configuration.PropertiesUtil;
import de.vawi.oopjava.kantinenplaner.dienste.GerichtDienst;
import de.vawi.oopjava.kantinenplaner.model.BeschaffungsPriorisierung;
import de.vawi.oopjava.kantinenplaner.model.Gericht;
import de.vawi.oopjava.kantinenplaner.model.Kantine;
import de.vawi.oopjava.kantinenplaner.tools.impl.PreisComparator;

public class OberflaecheMain extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldMitarbeiter;
	private JTextField textFieldPfadAusgabe;
	private JTextField txtVon;
	private JTextField txtBis;
	JRadioButton radioButtonHTML;
	JRadioButton radioButtonText;
	KantinenPlaner kantinenplaner;
	private JEditorPane textSpeiseplan;
	private JEditorPane textEinkaufsliste;
	private JEditorPane textGesamtkosten;
	private JTextField textToleranz;

	private JComboBox<String> comboBoxKantine;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	final Logger logger = LogManager.getLogger(OberflaecheMain.class);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OberflaecheMain frame = new OberflaecheMain();
					frame.setVisible(true);
					frame.fillProperties();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Erzeugung der Oberflaeche mit all seinen Elementen.
	 */
	public OberflaecheMain() throws ParseException, FileNotFoundException,
			IOException {
		setResizable(false);

		this.setTitle("Kantinenplaner");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 718);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setLayout(null);
		layeredPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.add(layeredPane, BorderLayout.CENTER);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_1.setBounds(0, 0, 1264, 625);
		layeredPane.add(panel_1);
		panel_1.setLayout(null);

		// Ueberschrift
		JLabel label = new JLabel("Kantinenplaner");
		label.setBounds(541, 11, 182, 28);
		panel_1.add(label);
		label.setFont(new Font("Teen Light", Font.BOLD, 25));

		// Auswahl der Kantinen
		JLabel lblKantine = new JLabel("Kantine:");
		lblKantine.setBounds(27, 55, 137, 14);
		panel_1.add(lblKantine);

		// Mitarbeiteranzahl mit Gueltigskeitspruefung
		JLabel lblMitarbeiteranzahl = new JLabel("Mitarbeiteranzahl:");
		lblMitarbeiteranzahl.setBounds(27, 104, 106, 14);
		panel_1.add(lblMitarbeiteranzahl);
		textFieldMitarbeiter = new JTextField("Mitarbieteranzahl");
		textFieldMitarbeiter.setBounds(27, 118, 62, 20);
		textFieldMitarbeiter.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if (valideMitarbeiteranz()) {
					setWarnhinweis(textFieldMitarbeiter, "");
				} else {
					setWarnhinweis(textFieldMitarbeiter,
							"Die Mitarbeiteranzahl muss eine gerade Zahl sein.");
				}

			}
		});
		panel_1.add(textFieldMitarbeiter);

		// Einrichten des DropDownMenus für die Kantinen
		comboBoxKantine = new JComboBox<String>();
		comboBoxKantine.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				PropertiesUtil util = null;
				try {
					util = new PropertiesUtil();
				} catch (FileNotFoundException e1) {
					logger.error("Die PropertiesUtil konnte nicht gefunden werden.");
					e1.printStackTrace();
				} catch (IOException e1) {
					logger.error("Die PropertiesUtil konnte nicht geoeffnet werden.");
					e1.printStackTrace();
				}
				List<Kantine> kantinen = util.getKantinen();
				@SuppressWarnings("unchecked")
				JComboBox<String> selectedChoice = (JComboBox<String>) e
						.getSource();
				textFieldMitarbeiter.setText(""
						+ ((Kantine) kantinen.get(selectedChoice
								.getSelectedIndex())).getAnzahlMitarbeiter());
			}
		});

		comboBoxKantine.setBounds(27, 73, 137, 20);
		panel_1.add(comboBoxKantine);

		//Ausgabezeitraum
		JLabel lblAusgabefzeitraum = new JLabel("Ausgabezeitraum:");
		lblAusgabefzeitraum.setBounds(275, 55, 163, 14);
		panel_1.add(lblAusgabefzeitraum);

		// Datumstextfeld VON mit Gueltigkeitsoruefung
		JLabel lblVon = new JLabel("Von:");
		lblVon.setBounds(275, 76, 28, 14);
		panel_1.add(lblVon);
		txtVon = new JTextField("Von");
		txtVon.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				pruefeDatum(txtVon);
			}
		});
		txtVon.setBounds(303, 73, 77, 20);
		panel_1.add(txtVon);

		// Datumstextfeld BIS mit Gueltigkeitsoruefung
		JLabel lblBis = new JLabel("Bis:");
		lblBis.setBounds(390, 76, 21, 14);
		panel_1.add(lblBis);
		txtBis = new JTextField("Bis");
		txtBis.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				pruefeDatum(txtBis);
			}
		});
		txtBis.setBounds(412, 73, 83, 20);
		panel_1.add(txtBis);

		//Tolleranz mit Gueltigkeitspruefung
		JLabel labelToleranz = new JLabel("Toleranz:");
		labelToleranz.setBounds(275, 104, 76, 14);
		panel_1.add(labelToleranz);
		textToleranz = new JTextField();
		textToleranz.setBounds(275, 118, 77, 20);
		textToleranz
				.setToolTipText("Gibt die Preisdifferenz an, bei der ein beliebteres Gericht einem günstigeren Gericht vorgezogen wird.");
		textToleranz.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if (valideToleranz()) {
					setWarnhinweis(textToleranz, "");
				} else {
					setWarnhinweis(textToleranz,
							"Die Toleranz bitte im Format 00.00 angeben.");
				}
			}
		});
		panel_1.add(textToleranz);

		// Ausgabedatei
		JLabel label_4 = new JLabel("Ausgabeordner:");
		label_4.setBounds(603, 55, 183, 14);
		panel_1.add(label_4);
		textFieldPfadAusgabe = new JTextField("Pfad der Ausgabedateien");
		textFieldPfadAusgabe.setBounds(603, 75, 183, 20);
		panel_1.add(textFieldPfadAusgabe);

		// Radio-Button für die Auswahl des Formats
		JLabel lblAusgabeformat = new JLabel("Ausgabeformat:");
		lblAusgabeformat.setBounds(603, 106, 183, 14);
		panel_1.add(lblAusgabeformat);
		radioButtonText = new JRadioButton("Text");
		buttonGroup.add(radioButtonText);
		radioButtonText.setBounds(688, 121, 98, 23);
		panel_1.add(radioButtonText);
		radioButtonHTML = new JRadioButton("HTML");
		buttonGroup.add(radioButtonHTML);
		radioButtonHTML.setSelected(true);
		radioButtonHTML.setBounds(603, 121, 83, 23);
		panel_1.add(radioButtonHTML);

		// Ausgabe-Panel
		JPanel panel = new JPanel();
		panel.setBounds(10, 173, 1244, 412);
		panel_1.add(panel);
		panel.setLayout(new CardLayout(0, 0));
		JTabbedPane tabbedPane = new JTabbedPane();
		HTMLEditorKit eKit = new HTMLEditorKit();

		// Scrollfenster des Speiseplans
		JScrollPane scrollPaneSpeiseplan = new JScrollPane();
		tabbedPane.add("Speiseplan", scrollPaneSpeiseplan);
		textSpeiseplan = new JEditorPane();
		textSpeiseplan.setEditorKit(eKit);
		scrollPaneSpeiseplan.setViewportView(textSpeiseplan);

		// Scrollfenster der Einkaufsliste
		JScrollPane scrollPaneEinkaufsliste = new JScrollPane();
		tabbedPane.add("Einkaufsliste", scrollPaneEinkaufsliste);
		textEinkaufsliste = new JEditorPane();
		textEinkaufsliste.setEditorKit(eKit);
		scrollPaneEinkaufsliste.setViewportView(textEinkaufsliste);

		// Scrollfenster der Gesamtkosten
		JScrollPane scrollPaneGesamtkosten = new JScrollPane();
		tabbedPane.add("Gesamtkosten", scrollPaneGesamtkosten);
		textGesamtkosten = new JEditorPane();
		textGesamtkosten.setEditorKit(eKit);
		scrollPaneGesamtkosten.setViewportView(textGesamtkosten);

		panel.add(tabbedPane, "AusgabePane");

		// Einrichten des Buttons Speichern und deren Funktionalitaet
		JButton btnSpeichern = new JButton("Speichern der Dateien");
		btnSpeichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				logger.trace("Konfiguration lesen");
				PropertiesUtil config;
				try {
					config = new PropertiesUtil();
					logger.trace("Erzeuge Ausgabedateien");
					File outputDir = config.getOutputDir();

					if (!outputDir.exists()) {
						logger.debug("Verzeichnis für Ausgaben existiert nicht ("
								+ outputDir
								+ "). Es wird versucht das Verzeichnis anzulegen.");
						if (!outputDir.mkdir()) {
							logger.error("Konnte Verzeichnis fuer die Ausgaben nicht erzeugen.");
						}
					}
					try {
						kantinenplaner.erstelleAusgaben(outputDir,
								OutputFormat.valueOf(config.getOutputFormat()));
					} catch (IOException e) {
						logger.error("Fehler beim erzeugen der Ausgabedateien");
					}

				} catch (IOException e) {
					logger.error("Die Konfigurationsdatei ("
							+ PropertiesUtil.getDefaultConfigPath()
							+ ") konnte gelesen werden.");
				}
			}
		});
		btnSpeichern.setBounds(1067, 596, 187, 23);
		panel_1.add(btnSpeichern);

		// Einrichten des Buttons "Eingabedaten erfassen" und deren
		// Funktionalitaet
		JButton btnEingabedaten = new JButton("Eingabedaten erfassen");
		btnEingabedaten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				EingabedateienFenster fenster = new EingabedateienFenster();
				fenster.setVisible(true);
				try {
					fenster.fillPorperties();
				} catch (FileNotFoundException e) {
					logger.error("Die Poperties-Datei \"planer.cfg\" kann nicht gefunden werden.");
					e.printStackTrace();
				} catch (IOException e) {
					logger.error("Die Poperties-Datei \"planer.cfg\" kann nicht geoeffnet werden.");
					e.printStackTrace();
				}
			}
		});
		btnEingabedaten.setBounds(1040, 86, 168, 23);
		panel_1.add(btnEingabedaten);

		// Einrichten des Button "Properties aktualisieren" und dessen
		// Funktionalitaet
		JButton btnPropertiesAkt = new JButton("Properties aktualisieren");
		btnPropertiesAkt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				if (DatumPlausiHelper.isZeitraumGueltig(txtVon.getText(),
						txtBis.getText())) {
					setWarnhinweis(txtVon, "");
					setWarnhinweis(txtBis, "");

					if (valideMitarbeiteranz()) {
						if (valideToleranz()) {
							setWarnhinweis(textToleranz, "");
							try {
								aktualisiereProperties();
							} catch (IOException e) {
								logger.error("Die Popertis konnten nicht aktualisiert und gespeichert werden.");
								e.printStackTrace();
							}
						} else {
							setWarnhinweis(textToleranz,
									"Die Toleranz bitte im Format 00.00 angeben.");
						}
					} else {
						setWarnhinweis(textFieldMitarbeiter,
								"Die Mitarbeiteranzahl muss eine gerade Zahl sein.");
					}
				} else {
					String fehlerText = "Der Startzeitpunkt liegt nach dem Endzeitpunkt.";
					setWarnhinweis(txtVon, fehlerText);
					setWarnhinweis(txtBis, fehlerText);
				}

			}

		});
		btnPropertiesAkt.setBounds(1040, 55, 168, 23);
		panel_1.add(btnPropertiesAkt);

		JButton btnAllePlanungDurchfuehren = new JButton("Planung durchfuehren");
		btnAllePlanungDurchfuehren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				try {
					logger.trace("Planung wird durchgeführt.");
					durchfuehrenPlanung();
				} catch (KantinenPlanerException | IOException e) {
					logger.error("Planung konnte nicht durchgeführt werden.");
					e.printStackTrace();
				}
			}

		});

		btnAllePlanungDurchfuehren.setBounds(1040, 131, 168, 23);
		panel_1.add(btnAllePlanungDurchfuehren);

		JButton button_5 = new JButton("Beenden");
		button_5.setBounds(1123, 648, 131, 23);
		layeredPane.add(button_5);
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dispose();
			}
		});

	}

	/**
	 * Auslesen der PropertiesDatei und uebernehmen der Daten in die Oberflaeche
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	private void fillProperties() throws FileNotFoundException, IOException,
			ParseException {
		PropertiesUtil util = new PropertiesUtil();
		textFieldPfadAusgabe.setText("" + util.getOutputDir() + "");

		txtVon.setText(""
				+ DatumPlausiHelper.datumToString(util.getStartdatum()));
		txtBis.setText(""
				+ DatumPlausiHelper.datumToString(util.getEndedatum()));

		List<Kantine> kantinen = util.getKantinen();
		for (Kantine k : kantinen) {
			comboBoxKantine.addItem(k.getName());
		}
		comboBoxKantine.setSelectedIndex(0);
		textFieldMitarbeiter.setText(""
				+ ((Kantine) kantinen.get(0)).getAnzahlMitarbeiter());

		if ("HTML".equals(util.getOutputFormat())) {
			radioButtonHTML.setSelected(true);
		} else if ("TXT".equals(util.getOutputFormat())) {
			radioButtonText.setSelected(true);
		} else {
			logger.warn("FORMAT.OUTPUT in der Propertiesdatei ist ungültig. Es wird der Default (HTML) gewählt.");
			radioButtonHTML.setEnabled(true);

		}
		textToleranz.setText(util.getToleranz() + "");

	}

	/**
	 * Aktualisieren der PropertiesDatei, Pruefen und Uebernehmen der
	 * Aenderungen in die planer.cfg
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void aktualisiereProperties() throws FileNotFoundException,
			IOException {
		PropertiesUtil util = new PropertiesUtil();
		Properties properties = util.getProperties();
		logger.trace("Aktualisiere Properties mit den Daten aus der Oberflaeche:");
		List<Kantine> kantinen = util.getKantinen();

		for (Kantine k : kantinen) {
			if (comboBoxKantine.getSelectedItem().toString()
					.equals(k.getName())) {
				k.setAnzahlMitarbeiter(Integer.parseInt(textFieldMitarbeiter
						.getText()));
			}
		}

		properties.setProperty(PropertiesUtil.KEY_KANTINE,
				PropertiesUtil.parseKantinenAsString(kantinen));

		properties.setProperty(PropertiesUtil.KEY_STARTDATUM,
				"" + txtVon.getText());
		properties.setProperty(PropertiesUtil.KEY_ENDEDATUM,
				"" + txtBis.getText());

		String ausgabePfad = textFieldPfadAusgabe.getText().replace('\\', '/');
		System.out.println("nur / erlaubt  " + ausgabePfad);

		properties.setProperty(PropertiesUtil.KEY_OUTPUT_DIR, ausgabePfad);
		properties.setProperty(PropertiesUtil.KEY_OUTPUT_FORMAT, ""
				+ formatToString());

		properties.setProperty(PropertiesUtil.KEY_TOLERANZ, textToleranz.getText());
		
		util.aktualisiereProperties(properties);
	}

	/**
	 * Ueberpruefung des Datums
	 * 
	 * @param textfeld
	 *            Jtextfield zu Pruefendes Datumsfeld
	 */
	private void pruefeDatum(JTextField textfeld) {
		logger.trace("Ueberpruefen der Datumsangabe:" + textfeld.getText());
		if (!DatumPlausiHelper.isDatumGueltig(textfeld.getText())) {
			setWarnhinweis(textfeld, "Format tt.mm.jjjj");
		} else {
			setWarnhinweis(textfeld, "");
		}
	}

	/**
	 * Setzen eine Warnhinweises durch setzen eines ToolTips und rot Hinterlegen
	 * des Textfelds
	 * 
	 * @param textfeld
	 *            JTextfeld Feld mit fehlerhafter Eingabe
	 * @param begruendung
	 *            String Inhalt des Textfelds
	 */
	private void setWarnhinweis(JTextField textfeld, String begruendung) {

		if (!begruendung.equals("")) {
			textfeld.setBackground(Color.RED);
			textfeld.setToolTipText("Die Eingabe ist ungültig. " + begruendung);
			logger.error("Die Eingabe " + textfeld.getText()
					+ " ist ungültig. " + begruendung);
		} else {
			textfeld.setBackground(Color.WHITE);
			textfeld.setToolTipText("Die Eingabe ist gültig.");
			logger.trace("Die Eingabe " + textfeld.getText() + " ist gueltig.");
		}
	}

	/**
	 * Ausgabe des ewählten Formats als Text, zum SPeichen in der
	 * Properties-Datei
	 * 
	 * @return String Ausgabeformat
	 */
	private String formatToString() {
		if (radioButtonHTML.isEnabled()) {
			return OutputFormat.HTML.name();
		} else {
			return OutputFormat.TXT.name();
		}
	}

	/**
	 * Ueberpruefen von der MItarbeiteranzahl, ob die eine ganze Zahl ist.
	 * 
	 * @return boolean Ergebnis
	 */
	private boolean valideMitarbeiteranz() {
		try {
			Integer.parseInt(textFieldMitarbeiter.getText());
		} catch (NumberFormatException e1) {
			return false;
		}
		return true;
	}

	/**
	 * Überpruefen von der Toleranz, ob der Wert einer Dezimalzahl entspricht.
	 * 
	 * @return boolean Ergebnis
	 */
	private boolean valideToleranz() {
		try {
			Float.parseFloat(textToleranz.getText());
		} catch (NumberFormatException e1) {
			return false;
		}
		return true;
	}

	/**
	 * Durchführen der Planung. Aufrufen der Methoden in den Diensten und
	 * erzeugen der Ergebnisse
	 * 
	 * @throws KantinenPlanerException
	 * @throws IOException
	 */
	private void durchfuehrenPlanung() throws KantinenPlanerException,
			IOException {

		logger.trace("Konfiguration lesen");
		PropertiesUtil config;
		try {
			config = new PropertiesUtil();
		} catch (IOException e) {
			logger.error("Die Konfigurationsdatei ("
					+ PropertiesUtil.getDefaultConfigPath()
					+ ") konnte nicht gelesen werden.");
			throw new KantinenPlanerException(e);
		}
		logger.trace("Bildschirmausgabe auf den Stand der planer.cfg setzen. Nicht aktualisierte Properties werden gelöscht. ");
		try {
			fillProperties();
		} catch (ParseException e2) {
			e2.printStackTrace();
		}

		logger.trace("Erstelle PlanungsFassade");
		kantinenplaner = new KantinenPlaner();

		logger.trace("Preislisten verarbeiten");
		for (String preisliste : config.getPreislisten()) {
			kantinenplaner.verarbeitePreisliste(preisliste);
		}

		logger.trace("Erstelle Kantinen");
		kantinenplaner.setKantinenListe(config.getKantinen());

		logger.trace("Rezepte verarbeiten");
		String rezepteDatei = config.getRezepte();
		List<Gericht> rezepte = null;
		try {
			rezepte = kantinenplaner.verarbeiteRezepte(rezepteDatei,
					kantinenplaner.getKantinenListe());
		} catch (IOException e) {
			logger.error("Rezepte in (" + rezepteDatei
					+ ") konnten nicht gelesen werden.");
			throw new KantinenPlanerException(e);
		}

		logger.trace("Hitliste verarbeiten");
		String hitliste = config.getHitliste();
		Comparator<Gericht> hitlistenComparator = null;
		try {
			hitlistenComparator = kantinenplaner.verarbeiteHitliste(hitliste,
					rezepte);
		} catch (IOException e) {
			logger.warn("Hitliste in (" + hitliste
					+ ") konnten nicht gelesen werden.");
		}

		logger.trace("Kalkuliere die Preismodalitaeten der Haendler fuer die bekannten Rezepte.");
		int maGesamt = kantinenplaner.getMAGesamt();
		List<BeschaffungsPriorisierung> beschPrioListe = kantinenplaner
				.erstelleBeschaffungsPriorisierung(rezepte, maGesamt);

		logger.trace("Setze Comparator fuer den Vergleich von Zutaten.");
		String text = textToleranz.getText().replace(",", ".");
		PreisComparator preisComparator = new PreisComparator(beschPrioListe,
				Float.valueOf(text));
		GerichtDienst.addGerichteSortierer(preisComparator);
		GerichtDienst.addGerichteSortierer(hitlistenComparator);

		logger.trace("Ermittle Planungsperiode");
		Date startdatum;
		Date endedatum;
		try {
			startdatum = config.getStartdatum();
			logger.trace("Startdatum=" + startdatum);
			endedatum = config.getEndedatum();
			logger.trace("Startdatum=" + endedatum);
		} catch (ParseException e1) {
			throw new KantinenPlanerException(
					"Konnte die Planungsperiode nicht einlesen. Bitte prüfen Sie das Format ["
							+ DatumPlausiHelper.getFormatDatum()
							+ "] in der Konfigurationsdatei.");
		}
		logger.trace("Erstelle Speiseplan (" + startdatum + " bis " + endedatum
				+ ")");
		kantinenplaner.erstelleSpeiseplan(startdatum, endedatum);

		logger.trace("Nachkalkulation für Gerichte, die auch tatsächlich gekocht werden sollen laut Speiseplaenen.");
		beschPrioListe = kantinenplaner
				.erstelleBeschaffungsPriorisierung(kantinenplaner
						.getKantinenListe());
		// Berichtigung der vorläufigen BeschaffungsPriorisierung im
		// PreisComparator, um die nachkalkulierte Priorisierung
		preisComparator.setBeschPrio(beschPrioListe);

		logger.trace("Erstelle Beschaffungsliste");
		kantinenplaner.erstelleBeschaffungsliste();

		OutputFormat format = (radioButtonHTML.isSelected()) ? OutputFormat.HTML
				: OutputFormat.TXT;

		String createSpeiseplanString = kantinenplaner
				.createSpeiseplanString(format);
		textSpeiseplan.setText(createSpeiseplanString);

		String createEinkaufslistenString = kantinenplaner
				.createEinkaufslistenString(format);
		textEinkaufsliste.setText(createEinkaufslistenString);

		String createGesamtkostenString = kantinenplaner
				.createKostenuebersichtString(format);
		textGesamtkosten.setText(createGesamtkostenString);

	}
}

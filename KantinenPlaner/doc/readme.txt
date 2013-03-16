----------------------------------------------
-- Objektorientierte Programmierung in Java --
–-  Teilleistung 3                          --
-- README Kantinenplaner                    --
----------------------------------------------

----------------------------------
Readme-Datei enthält:

I.    Bedienungsanleitung Programm
II.   Zuordnung der Klassen
III.  Ordnerverzeichnis
-----------------------------------



I.	Bedienungsanleitung

Der Kantinenplaner kann in automatisierter Form und über eine grafische Oberfläche genutzt werden. 

Automatisierte Planung

Konfigurieren Sie zunächst die Software in der “config/planer.cfg”. Die Planung wird durch das Starten des Skripts 
“Start_automatisierte_Planung.bat” durchgeführt und die Ausgabedateien erzeugt.

1.	Konfiguration der Software

	a. Konfiguriere Sie das Programm über die mitgelieferte Konfigurationsdatei “/config/planer.cfg”. 
	   Eine ausführliche Beschreibung der Parameter und den Eingabemöglichkeiten entnehmen Sie bitte direkt den Kommentaren, 
	   gekennzeichnet durch “#”, der Konfigurationsdatei. 

	   Folgende Eingabedateien müssen angeben werde:

		i.	Dateipfade der Preislisten
		ii.	Dateipfad der Rezeptdatei
		iii.	Dateipfad der Hitlistendatei

	b. Folgende Parameter können geändert werden:

	i.	Angaben zu Kantinen und Mitarbeitern 
	ii.	Startdatum des Planungszeitraums
	iii.	Endedatum des Planungszeitraums
	iv.	Toleranz um beliebtere Gerichte zu bevorzugen
	v.	Ausgabeverzeichnis
	vi.	Ausgabeformat

2.	Ausführen des mitgelieferten Skripts “Start_automatisierte_Planung.bat”.

Graphische Oberfläche

Konfigurieren Sie zunächst die Software in der “config/planer.cfg”. Die Anwendung wird durch das Starten des Skripts 
“Start_Benutzeroberflaeche.bat” ausgeführt. In der Oberfläche können Sie Einstellungen ergänzen oder verändern. 
Die Planung und das Erstellen der Ausgaben werden in der Oberfläche manuell gestartet.

1.	Startwerte in Konfigurationsdatei wie in Nr. “1.a.i” beschrieben anpassen. Diese Einstellungen sind optional. 
	Die Werte können in der Oberfläche geändert und/oder ergänzt werden.

2.	Ausführen des mitgelieferten Skripts “Start_Benutzeroberflaeche.bat”.


II. Zuordnung der Klassen 


Kim Jansen:

Source-Code im Bereich “src.de.vawi.oopjava.kantinenplaner”:
KantinenPlaner.java
KantinenPlanerException.java
KantinenPlanerShell.java
configuration.OutputFormat.java
configuration.PropertiesUtil.java
dienste.GerichtDienst.java
dienste.HaendlerDienst.java
dienste.PlanerDienst.java
model.BeschaffungsPriorisierung.java
model.SpeiseplanPos.java
model.ZuoHaendlerAnschKosten.java
tools.impl.CSVReader.java
tools.impl.HitlistenComparator.java
tools.impl.PreisComparator.java

Tests im Bereich “test.de.vawi.oopjava.kantinenplaner”:

sonstiges:
config/planer.cfg
logging/kantinenplaner.log
src/log4j2.xml
build.xml



Julia Meyer:

Source-Code im Bereich “src.de.vawi.oopjava.kantinenplaner”:
model.Beschaffungsliste.java

model.Haendler.java
model.HaendlerPos.java
model.ZuoHaendlerZutat.java
model.Zutat.java
model.ZutatPos.java
model.ZutatTyp.java
oberflaeche.DatumPlausiHelper.java
oberflaeche.EingabedateienFenster.java
oberflaeche.Oberflaeche.java

Tests im Bereich “test.de.vawi.oopjava.kantinenplaner”:
testBerechneGesamtkosten.java


Matthias Rohe:

Source-Code im Bereich “src.de.vawi.oopjava.kantinenplaner”:
model.Gericht.java
model.Kantine.java
tools.TransportKostenRechner.java
tools.Writer.java
tools.impl.AbstractBeschaffungslistenWriter.java
tools.impl.CollectionUtil.java
tools.impl.EinkaufslistenWriter.java
tools.impl.KostenWriter.java
tools.impl.SpeiseplanWriter.java
tools.impl.TKProArtikel.java
tools.impl.TKProKilometer.java


III. Ordnerstruktur (zip-datei: oopjava_tl3)



		planer
		readme.txt
		Start_automatisierte_Planung.bat
		Start_Benutzeroberflaeche
	

1		config

		planer.cfg
	
2		dokumentation

		OOPJava_TL3_Dokumentation.pdf

2.1		diagramme

		Klassendiagramm_v17.png

		
2.2		Java Doc

		allclasses-frame.html
		allclasses-nonframe.html
		constant-values.html
		deprecated-list.html
		help-doc.html
		index.html
		index-all.html
		overview-frame.html
		overview-summery.html
		overview-tree.html
		package-list
		serialized-form.html
		stylesheet.css

2.2.1		de

2.2.1.1		vawi

2.2.1.1.1	oopjava

2.2.1.1.1.1	kantinenplaner

		KantinenPlaner.html
		KantinenPlanerException.html
		KantinenplanerShell.html
		package-frame.html
		package-summery.html
		package-tree.html

2.2.1.1.1.1.1	configuration

		OutputFormat.html
		package-frame.html
		package-summery.html
		package-tree.html
		PropertiesUtil.html

2.2.1.1.1.1.2	dienste

		GerichtDienst.html
		HaendlerDienst.html
		package-frame.html
		package-summery.html
		package-tree.html
		PlanerDienst.html
	
	
2.2.1.1.1.1.3	model

		Beschaffungsliste.html
		BeschaffungsPriorisierung.html
		Gericht.html
		Haendler.html
		HaendlerPos.html
		Kantine.html
		package-frame.html
		package-summery.html
		package-tree.html
		SpeiseplanPos.html
		ZuHaendlerAnschKosten.html
		ZuoHaendlerZutat.html
		Zutat.html
		ZutatPos.html
		ZutatTyp.html

2.2.1.1.1.1.4	oberflaeche

		DatumPlausiHelper.html
		EingabedateienFenster.html
		OberflaecheMain.html
		package-frame.html
		package-summery.html
		package-tree.html

2.2.1.1.1.1.5	tools

		package-frame.html
		package-summery.html
		package-tree.html
		TransportKostenRechner.html
		Writer.html

2.2.1.1.1.1.5.1 impl	

		AbstractBeschaffungslistenWriter.html
		CollectionUtil.html
		CSVWriter.html
		EinkaufslistenWriter.html
		HitlistenComparator.html
		KostenWriter.html
		package-frame.html
		package-summery.html
		package-tree.html
		PreisComparator.html
		SpeiseplanWriter.html
		TKProArtikel.html
		TKProKilometer.html

3		logging

4		planungsergebnis

5		workspace

		.classpath
		.project
		build.xlm

5.1 		.settings

5.1.1 		org.eclipse.jdt.core.prefs
	

5.2		config

		planer.cfg
	
	
5.3		lib

		log4j-api-2.0-beta3
		log4j-core-2.0-beta3

5.3.1		source

		easymock-3.1-sources
		log4j-api-2.0-beta3-sources
		log4j-core-2.0-beta3-sources

5.3.2		test

		easymock-3.1
		junit
		org.hamcrest.core_1.1.0.v20090501071000

5.4		logging

		kantinenplaner.txt

5.5		planungsergebnis

		EinkaufsListe.html
		Kostenuebersicht.html
		Speiseplan.html

5.6		src

		log4j.xlm

5.6.1		.de

5.6.1.1		vawi

5.6.1.1.1	oopjava

5.6.1.1.1.1	kantinenplaner

		KantinenPlaner.class
		KantinenPlanerException.class
		KantinenPlanerShell.class

5.6.1.1.1.1.1	configuration

		OutputFormat.class
		PropertiesUtil.class

5.6.1.1.1.1.2	dienste

		GerichtDienst.class
		HaendlerDienst.class
		PlanerDienst.class

5.6.1.1.1.1.3	model

		Beschaffungsliste.class
		BeschaffungsPriorisierung.class
		Gericht.class
		Haendler.class
		HaendlerPos.class
		Kantine.class
		SpeiseplanPos.class
		ZuoHaendlerAnschKosten.class
		ZuoHaendlerZutat.class
		Zutat.class
		ZutatPos.class
		ZutatTyp.class

5.6.1.1.1.1.4	oberflaeche

		DatumPlausiHelper.class
		EingabedateienFenster.class
		OberflaecheMain.class

5.6.1.1.1.1.5	tools

		TransportKostenRechner.class
		Writer.class

5.6.1.1.1.1.5.1	impl

		AbstractBeschaffungslistenWriter.class
		CollectionUtil.class
		CSVReader.class
		EinkaufslistenWriter.class
		HitlistenComparator.class
		KostenWriter.class
		PreisComparator.class
		SpeiseplanWriter.class
		TKProArtikel.class
		TKProKilometer.class

5.7		test

5.7.1		.de

5.7.1.1		vawi

5.7.1.1.1	oopjava

5.7.1.1.1.1	kantinenplaner

		Log4jTest.class

5.7.1.1.1.1.1	configuration

		PropertiesUtilSpeichernTest.class
		PropertiesUtilTest.class

5.7.1.1.1.1.2	dienst

		GerichtDienstHitlisteTest.class
		GerichtDienstRezepteTest.class
		HaendlerDienstKaufbarFilterTest.class
		HaendlerDienstPreislisteTest.class
		PlanerDienstSpeiseplanTest.class

5.7.2.1.1.1.1.3	model

		BeschaffungslisteBerechneGesamtkostenTest.class

5.7.1.1.1.1.4	tools

5.7.1.1.1.1.4.1	impl

		CollectionUtilTest.class
		CSVReaderTest.class
		HitlistenComparatorTest.class

5.8		testdaten

		hitliste_ok.csv
		preisliste_bauer.csv
		preisliste_grosshaendler.csv
		preisliste_grosshaendler2.csv
		rezepte_mixed.csv

5.8.1		beispieldateien

		hitliste.csv
		preisliste_1.csv
		preisliste_2.csv
		preisliste_3.csv
		preisliste_4.csv
		preisliste_5.csv
		preisliste_6csv
		preisliste_7.csv
		rezepte.csv







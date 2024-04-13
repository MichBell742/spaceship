package it.edu.iisgubbio.giocoFinale;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Spaceship extends Application {
	/* TODO: aggiungere vettore per gli sfondi di ciascun livello del gioco
	 * TODO: fare collisione navicella oggetti
	 * TODO: far finire il gioco nella modalità limitata
	 */
	
//	long tempo1;
//	long tempo2;
//	int contaRipetizioniSpostamento=0;
	
	// SCHERMO
	Pane schermo = new Pane();
	final int WIDTH_SCHERMO = 1000;
	final int HEIGTH_SCHERMO = 600;
	GridPane grigliaImpostazioni= new GridPane();

	// SFONDO
	final int WIDTH_SFONDO = 5000;
	final int HEIGTH_SFONDO = 800;
	Image immagineSfondo = new Image(getClass().getResourceAsStream("Sfondo.png"));
	ImageView sfondo = new ImageView(immagineSfondo);
	Image immagineSfondo2 = new Image(getClass().getResourceAsStream("Sfondo2.png"));
	ImageView sfondo2 = new ImageView(immagineSfondo2);
	final int WIDTH_SFOCATURA = 2000;
	final int HEIGTH_SFOCATURA = 1600;
	Image immagineSfocatura = new Image(getClass().getResourceAsStream("Sfocatura.png"));
	ImageView sfocatura = new ImageView(immagineSfocatura);
	// spostamento sfondo
	double tempoDiSpostamentoTOT = 25; // se si vuole scegliere la durata del gioco standard 25s
	double posizioneSfondoX = 0;
	double posizioneSfondo2X = WIDTH_SFONDO;
	double valoreSpostamentoSfondo = (WIDTH_SFONDO-1000)/(tempoDiSpostamentoTOT*1000)*25;
	Timeline muoviSfondo = new Timeline(new KeyFrame(Duration.millis(25), x -> aggiornaPosizioneSfondo()));

	// OGGETTI
	final int DIMENSION_OGGETTI = 100; // gli oggetti sono quadrati
	int nOggetti = 7;
	Image immagineUfo = new Image(getClass().getResourceAsStream("Ufo.png"));
	Image immagineMeteoriteBlu = new Image(getClass().getResourceAsStream("MeteoriteBlu.png"));
	Image immagineMeteoriteViola = new Image(getClass().getResourceAsStream("MeteoriteViola.png"));
	Image vettoreImmagini[] = { immagineMeteoriteBlu, immagineMeteoriteViola, immagineUfo }; 
	ImageView vettoreOggetti[] = new ImageView[nOggetti];
	int vettoreViteOggettiRimaste[]= new int[nOggetti]; //le vite in un indice corrispondo alle vite dell'oggetto che si trova nello stesso indice nel vettore vettoreOggetti
	int oggettoNellaPosizione[]= new int[nOggetti];
	int viteMeteorite=2;
	int viteUFO=3;
	// spostamento oggetti
	ImageView oggettoAttuale;
	int indiceOggetto = 0;
	Timeline muoviOggetti = new Timeline(new KeyFrame(Duration.millis(1), x -> spostaOggetti()));

	// NAVICELLA
	Image immagineNavicella = new Image(getClass().getResourceAsStream("NavicellaSpaziale.png"));
	ImageView navicella = new ImageView(immagineNavicella);
	final int WIDTH_NAVICELLA = 125;
	final int HEIGTH_NAVICELLA = 150;
	// spostamento navicella
	boolean spostaSU = false;
	boolean spostaGIU = false;
	boolean spostaAVANTI = false;
	boolean spostaINDIETRO = false;
	int posizioneNaviciella[] = { 0, (HEIGTH_SCHERMO - WIDTH_NAVICELLA) / 2 };
	int valoreSpostamentoNavicella = 10;
	Timeline muoviNavicella = new Timeline(new KeyFrame(Duration.millis(25), x -> aggiornaPosizioneNavicella()));

	// MUNIZIONI
	final int WIDTH_MISSILE = 60;
	final int HEIGTH_MISSILE = 30;
	int numeroMunizioni = 100;
	int numeroMunizioniAttuali= numeroMunizioni;
	int munizioniUtilizzate = 0;
	ImageView[] munizioni = new ImageView[numeroMunizioni];
	// spawn missile
	int precedente = 0;
	long tempoScorsoMissile;
	
	//PUNTEGGIO
	//viene incrementato nel metodo "metodoControllaCollisioni"
	int punteggioAttuale=0;

	// colisioni oggetto misile
	int numeroOggettoBound = 0;
	int numeroMissileBound = 0;
	int conta = 0;
	boolean[] numeriMunizioniEsaurite = new boolean[numeroMunizioni]; // vettore che contiene lo stato di ogni missile
	Bounds boundOggetti;
	Bounds boundMissile;

	// controlla intersezione
	Timeline controllaCollisione = new Timeline(new KeyFrame(Duration.millis(2), x -> metodoControllaCollisione()));
	// esplosione
	final int WIDTH_ESPLOSIONE = 200;
	final int HEIGTH_ESPLOSIONE = 200;
	Image animazioneEsplosione = new Image(getClass().getResourceAsStream("animazione-esplosione1.gif"));	

	//INTERFACCIA GRAFICA
	int statoInterfaccia=0; //0=home, 1=gioco, 2=impostazioni
	
	//home
	final int WIDTH_SFONDO_HOME=1000;
	final int HEIGTH_SFONDO_HOME=600;
	final int OFFSET_Y_OGGETTI_MENU=20; //valore che indica la posizione degli oggetti (titolo, pulsanti) del menu
	final int WIDTH_PULSANTI_HOME=200;
	final int HEIGTH_PULSANTI_HOME=40;
	final int WIDTH_RECTANGLE_HOME=275;
	final int HEIGTH_RECTANGLE_HOME=300;
	final int WIDTH_TITOLO_HOME=250;
	final int HEIGTH_TITOLO_HOME=50;

	Label eTitle=new Label("Space Ship");
	Button bStartGioco= new Button("Start");
	Button bResetGioco= new Button("Reset");
	Button bSettings= new Button("Settings");
	Image immagineSfondoHome=new Image(getClass().getResourceAsStream("videoSfondoHome.gif"));

	ImageView sfondoHomeFirstOpen=new ImageView(immagineSfondoHome);
	/*
	 * sostituito dallo sfondo
	Image immagineSfondoHomeTrasparente=new Image(getClass().getResourceAsStream("immagineSfondoHomeTrasparente.png"));
	ImageView sfondoHomeTrasperente=new ImageView(immagineSfondoHomeTrasparente);
	*/
	Rectangle sfondoHomeTrasperente= new Rectangle(WIDTH_SCHERMO, HEIGTH_SCHERMO);
	Region menu= new Region(); //come u rettangolo ma controllabile maggiormente da CSS
	
	//gioco
	final int WIDTH_MENU_INFORMAZIONI=500;
	final int HEIGTH_MENU_INFORMAZIONI=100;
	final int WIDTH_TESTO_INFORMAZIONI=100;
	final int HEIGTH_TESTO_INFORMAZIONI=30;
	final int WIDTH_PULSANTE_INFORMAZIONI=100;
	final int HEIGTH_PULSANTE_INFORMAZIONI=40;
	final int DIMENSIONI_IMMAGINE_CUORE_INFORMAZIONI=35;
	
	final int POSIZIONAMENTO_ASSE_Y_MENU=-20;
	final int POSIZIONAMENTO_ASSE_X_MENU=(WIDTH_SCHERMO-WIDTH_MENU_INFORMAZIONI)/2;
	final int OFFSET_ELEMENTI_MENU=WIDTH_MENU_INFORMAZIONI/4;
	final int POSIZIONE_X_SEPARATORE1=OFFSET_ELEMENTI_MENU+(WIDTH_SCHERMO-WIDTH_MENU_INFORMAZIONI)/2;
	final int POSIZIONE_X_SEPARATORE2=OFFSET_ELEMENTI_MENU*2+(WIDTH_SCHERMO-WIDTH_MENU_INFORMAZIONI)/2;
	final int POSIZIONE_X_SEPARATORE3=OFFSET_ELEMENTI_MENU*3+(WIDTH_SCHERMO-WIDTH_MENU_INFORMAZIONI)/2;
	
	boolean bianco=true;
	
	int numeroVite=3;
	int viteRimaste=numeroVite;
	
	Region informazioni= new Region();
	Label ePunti= new Label("PUNTI");
	Label eVite= new Label("VITE");
	Label eNumeroPunti= new Label(""+punteggioAttuale);
	Label eMunizioni= new Label("MUNIZIONI");
	Label eNumeroMunizioni= new Label(""+numeroMunizioniAttuali);
	long tempoPassato=0;
	Button bHomeGioco= new Button("HOME");
	Line separaInformazioni1= new Line(POSIZIONE_X_SEPARATORE1,POSIZIONAMENTO_ASSE_Y_MENU, POSIZIONE_X_SEPARATORE1, 100+POSIZIONAMENTO_ASSE_Y_MENU);
	Line separaInformazioni2= new Line(POSIZIONE_X_SEPARATORE2,POSIZIONAMENTO_ASSE_Y_MENU, POSIZIONE_X_SEPARATORE2, 100+POSIZIONAMENTO_ASSE_Y_MENU);
	Line separaInformazioni3= new Line(POSIZIONE_X_SEPARATORE3,POSIZIONAMENTO_ASSE_Y_MENU, POSIZIONE_X_SEPARATORE3, 100+POSIZIONAMENTO_ASSE_Y_MENU);
	//immagini per vita
	Image imamgineCuoreVita=new Image(getClass().getResourceAsStream("immagineCuroePerVite.png"));
	ImageView vettoreCuori[]= new ImageView[numeroVite]; //3 vite
	
	
	//impostazioni
	//eseguzione
	boolean statoPrecedenteSottofondo=false;
	//interfaccia
	final int DIMENSIONI_X_GRIDPANE=500;
	final int DIMENSIONI_COLONNA_GRIDPANE=DIMENSIONI_X_GRIDPANE/3;
	final int DIMENSIONI_Y_GRIDPANE=400;
	final int DIMENSIONI_X_SFONDO_GRIDPANE= 600;
	final int DIMENSIONI_Y_SFONDO_GRIDPANE= 520;
	
	int numeroLivelli=3;
	boolean modalitaGiocoIllimitato=false; //modalità: 1=illimitato 0=limitato
	Button bHomeSettings= new Button("HOME");
	Button bSave= new Button("Save");
	Region impostazioni= new Region(); 

	//creiamo gli oggetti
	Rectangle dimensioneGrigliaX= new Rectangle(DIMENSIONI_X_GRIDPANE,0);
	Rectangle dimensioneGrigliaY= new Rectangle(0,DIMENSIONI_Y_GRIDPANE);
	Rectangle dimensioneGrigliaColonna1= new Rectangle(DIMENSIONI_COLONNA_GRIDPANE,0);
	Rectangle dimensioneGrigliaColonna2= new Rectangle(DIMENSIONI_COLONNA_GRIDPANE,0);
	Rectangle dimensioneGrigliaColonna3= new Rectangle(DIMENSIONI_COLONNA_GRIDPANE,0);
	Label eImpostazioni=new Label("IMPOSTAZIONI");
	//Button bHomeSettings= new Button("HOME"); creata globale
	Label eModalitaGioco=new Label("Modalità di gioco:");
	ToggleGroup modalitaDiGioco = new ToggleGroup();
	RadioButton rbSenzaLimiti = new RadioButton("illimitato");
	RadioButton rbConLimiti = new RadioButton("limitato");
	Label eNumeroMuinizioniImpostazioni = new Label("numero munizioni: "); //in caso si scelga la modalità senza limiti
	TextField cNumeroMunizioni= new TextField("100");
	Label eDurataGioco = new Label("durata: "); //in caso si scelga la modalità con limiti
	TextField durataGioco= new TextField("25");
	Label eSuono= new Label("Suono: ");
	Label eVolumeSuono= new Label("volume: ");
	Slider volume= new Slider(1, 10 , 5);
	CheckBox ckSottofondo= new CheckBox("Sottofondo");
	CheckBox ckMissile= new CheckBox("Sparatoria");
	CheckBox ckEspolosione= new CheckBox("Esplosione");
	CheckBox ckMunizioni= new CheckBox("Munizioni");
	Label eViteOggetto= new Label("vite Oggetti: ");
	Label eMeteorite= new Label("Meteorite: ");
	Label eUfo= new Label("UFO: ");
	TextField cViteMeteorite= new TextField(""+viteMeteorite);
	TextField cViteUfo= new TextField(""+viteUFO);

	//utili per cambiare le interfaccie nel modo corretto
	boolean resetGame=true;
	boolean firstOpen=true;
	
	//MUSICA DI SOTTOFONDO
	AudioClip musicaSottofondo= new AudioClip(getClass().getResource("musicaEpicaSottofondo.mp3").toExternalForm());
	AudioClip suonoEsplosione= new AudioClip(getClass().getResource("Esplosione.mp3").toExternalForm());
	AudioClip suonoMunizioniFinite= new AudioClip(getClass().getResource("MunizioniFinite.mp3").toExternalForm());
	AudioClip suonoSparoMissile= new AudioClip(getClass().getResource("SparoMissile.mp3").toExternalForm());
	
	public void start(Stage finestra) {
		//definiamo degli effetti
		DropShadow dropShadow = new DropShadow();
		dropShadow.setBlurType(BlurType.THREE_PASS_BOX);
		dropShadow.setRadius(15); //raggio di sfocatura
		dropShadow.setSpread(0.5); //densità della sfocatura 
		dropShadow.setColor(Color.GRAY);
		DropShadow dropShadow1 = new DropShadow();
		dropShadow1.setBlurType(BlurType.TWO_PASS_BOX);
		dropShadow1.setRadius(30); //raggio di sfocatura
		dropShadow1.setSpread(0.7); //densità della sfocatura 
		dropShadow1.setColor(Color.color(0.4,0,0.8));
	//--------------------------------------------------------------------------------------------------------------------------------
		//CONFIGURAZIONE SCHERMATA DI GIOCO
		// settaggi sfondo
		sfondo.setFitWidth(WIDTH_SFONDO);
		sfondo.setFitHeight(HEIGTH_SFONDO);
		sfondo.setLayoutX(posizioneSfondoX);
		sfondo.setLayoutY((HEIGTH_SCHERMO - HEIGTH_SFONDO) / 2);
		sfondo2.setFitWidth(WIDTH_SFONDO);
		sfondo2.setFitHeight(HEIGTH_SFONDO);
		sfondo2.setLayoutX(posizioneSfondo2X);
		sfondo2.setLayoutY((HEIGTH_SCHERMO - HEIGTH_SFONDO) / 2);
		sfocatura.setFitWidth(WIDTH_SFOCATURA);
		sfocatura.setFitHeight(HEIGTH_SFOCATURA);
		sfocatura.setLayoutX(0);
		sfocatura.setLayoutY((HEIGTH_SCHERMO - HEIGTH_SFOCATURA) / 2);
		// settaggi oggetti
		int immagine, rotazione;
		for (int n = 0; n < nOggetti; n++) {
			immagine = (int) (Math.random() * vettoreImmagini.length);
			vettoreOggetti[n] = new ImageView(vettoreImmagini[immagine]);
			vettoreOggetti[n].setFitHeight(DIMENSION_OGGETTI);
			vettoreOggetti[n].setFitWidth(DIMENSION_OGGETTI);
			riposizionaOggetto(vettoreOggetti[n]);
			if (immagine != 2) {
				rotazione = (int) (Math.random() * 270);
				vettoreOggetti[n].setRotate(rotazione);
				oggettoNellaPosizione[n]=immagine; //memorizziamo che immagine è
			}else { //è un ufo
				oggettoNellaPosizione[n]=immagine;
			}
		}
		// settaggio navicella
		navicella.setFitWidth(WIDTH_NAVICELLA);
		navicella.setFitHeight(HEIGTH_NAVICELLA);
		navicella.setRotate(90);
		navicella.setLayoutX(posizioneNaviciella[0]);
		navicella.setLayoutY(posizioneNaviciella[1]);
		//settaggio menu informazioni
		informazioni.getStyleClass().add("menuInformazioniGioco");
		informazioni.setEffect(dropShadow1);
		ePunti.getStyleClass().add("testoMenuInformazioni");
		eNumeroPunti.getStyleClass().add("testoMenuInformazioni");
		eVite.getStyleClass().add("testoMenuInformazioni");
		eMunizioni.getStyleClass().add("testoMenuInformazioni");
		eNumeroMunizioni.getStyleClass().add("testoMenuInformazioni");
		bHomeGioco.getStyleClass().add("pulsanteMenuInformazioni");
		informazioni.setLayoutX(POSIZIONAMENTO_ASSE_X_MENU);
		informazioni.setLayoutY(POSIZIONAMENTO_ASSE_Y_MENU);
		ePunti.setLayoutX(POSIZIONAMENTO_ASSE_X_MENU+(POSIZIONE_X_SEPARATORE1-POSIZIONAMENTO_ASSE_X_MENU-WIDTH_TESTO_INFORMAZIONI)/2);
		ePunti.setLayoutY(5);
		eNumeroPunti.setLayoutX(POSIZIONAMENTO_ASSE_X_MENU+(POSIZIONE_X_SEPARATORE1-POSIZIONAMENTO_ASSE_X_MENU-WIDTH_TESTO_INFORMAZIONI)/2);
		eNumeroPunti.setLayoutY(35);
		eVite.setLayoutX(OFFSET_ELEMENTI_MENU+POSIZIONAMENTO_ASSE_X_MENU+(POSIZIONE_X_SEPARATORE1-POSIZIONAMENTO_ASSE_X_MENU-WIDTH_TESTO_INFORMAZIONI)/2);
		eVite.setLayoutY(5);
		for(int i=0; i<vettoreCuori.length; i++) {
			vettoreCuori[i]=new ImageView(imamgineCuoreVita);
			vettoreCuori[i].setLayoutX(35*i+OFFSET_ELEMENTI_MENU+POSIZIONAMENTO_ASSE_X_MENU+(POSIZIONE_X_SEPARATORE1-POSIZIONAMENTO_ASSE_X_MENU-WIDTH_TESTO_INFORMAZIONI)/2);
			vettoreCuori[i].setLayoutY(35);
			vettoreCuori[i].setFitHeight(DIMENSIONI_IMMAGINE_CUORE_INFORMAZIONI);
			vettoreCuori[i].setFitWidth(DIMENSIONI_IMMAGINE_CUORE_INFORMAZIONI);
		}
		eMunizioni.setLayoutX(OFFSET_ELEMENTI_MENU*2+POSIZIONAMENTO_ASSE_X_MENU+(POSIZIONE_X_SEPARATORE1-POSIZIONAMENTO_ASSE_X_MENU-WIDTH_TESTO_INFORMAZIONI)/2);
		eMunizioni.setLayoutY(5);
		eNumeroMunizioni.setLayoutX(OFFSET_ELEMENTI_MENU*2+POSIZIONAMENTO_ASSE_X_MENU+(POSIZIONE_X_SEPARATORE1-POSIZIONAMENTO_ASSE_X_MENU-WIDTH_TESTO_INFORMAZIONI)/2);
		eNumeroMunizioni.setLayoutY(35);
		bHomeGioco.setLayoutX(OFFSET_ELEMENTI_MENU*3+POSIZIONAMENTO_ASSE_X_MENU+(POSIZIONE_X_SEPARATORE1-POSIZIONAMENTO_ASSE_X_MENU-WIDTH_PULSANTE_INFORMAZIONI)/2);
		bHomeGioco.setLayoutY((HEIGTH_MENU_INFORMAZIONI+POSIZIONAMENTO_ASSE_Y_MENU-HEIGTH_PULSANTE_INFORMAZIONI)/2);
		bHomeGioco.setEffect(dropShadow);
		
//------------------------------------------------------------------------------------------------------------------------------------------------
		//CONFIGURAZIONE SCHERMATA HOME
		//settaggio sfondo
		sfondoHomeFirstOpen.setFitWidth(WIDTH_SFONDO_HOME);
		sfondoHomeFirstOpen.setFitHeight(HEIGTH_SFONDO_HOME);
		sfondoHomeFirstOpen.setLayoutY((HEIGTH_SCHERMO-HEIGTH_SFONDO_HOME)/2);
		sfondoHomeFirstOpen.setLayoutX((WIDTH_SCHERMO-WIDTH_SFONDO_HOME)/2);
		sfondoHomeTrasperente.getStyleClass().add("sfondoHomeTrasparente");
		sfondoHomeTrasperente.setLayoutY((HEIGTH_SCHERMO-HEIGTH_SFONDO_HOME)/2);
		sfondoHomeTrasperente.setLayoutX((WIDTH_SCHERMO-WIDTH_SFONDO_HOME)/2);
		//posizionamento pulsanti e menu
		// il numero aggiunto alla posizione X serve per riallineare i pulsanti con lo sfondo
		menu.setLayoutX((WIDTH_SCHERMO-WIDTH_RECTANGLE_HOME)/2);
		menu.setLayoutY((HEIGTH_SCHERMO-HEIGTH_RECTANGLE_HOME)/2);
		bStartGioco.setLayoutX((WIDTH_SCHERMO-WIDTH_PULSANTI_HOME)/2);
		bStartGioco.setLayoutY((HEIGTH_SCHERMO-HEIGTH_PULSANTI_HOME)/2-70+OFFSET_Y_OGGETTI_MENU);
		bSettings.setLayoutX((WIDTH_SCHERMO-WIDTH_PULSANTI_HOME)/2);
		bSettings.setLayoutY((HEIGTH_SCHERMO-HEIGTH_PULSANTI_HOME)/2+OFFSET_Y_OGGETTI_MENU);
		bResetGioco.setLayoutX((WIDTH_SCHERMO-WIDTH_PULSANTI_HOME)/2);
		bResetGioco.setLayoutY((HEIGTH_SCHERMO-HEIGTH_PULSANTI_HOME)/2+70+OFFSET_Y_OGGETTI_MENU);
		//stile pulsanti
		bStartGioco.getStyleClass().add("buttonHome");
		bResetGioco.getStyleClass().add("buttonHome");
		bSettings.getStyleClass().add("buttonHome");
		menu.getStyleClass().add("menuHome");
		bStartGioco.setEffect(dropShadow);
		bResetGioco.setEffect(dropShadow);
		bSettings.setEffect(dropShadow);
		menu.setEffect(dropShadow1);
		
		//settaggio e stile Titolo
		eTitle.setLayoutX((WIDTH_SCHERMO-WIDTH_TITOLO_HOME)/2);
		eTitle.setLayoutY((HEIGTH_SCHERMO-HEIGTH_TITOLO_HOME)/2-130+OFFSET_Y_OGGETTI_MENU);
		eTitle.getStyleClass().add("titleHome");
		eTitle.setEffect(dropShadow);
		
//------------------------------------------------------------------------------------------------------------------------------------------------
		//CONFIGURAZIONE SCHERMATA IMPOSTAZIONI
		//settaggi oggetti impostazioni
	    rbConLimiti.setSelected(true);
	    
	    volume.setShowTickMarks(true);
	    volume.setShowTickLabels(true);
	    volume.setMajorTickUnit(1);
	    volume.setMinorTickCount(0);
	    volume.setSnapToTicks(true);

	    ckSottofondo.setSelected(true);
	    ckMissile.setSelected(true);
	    ckEspolosione.setSelected(true);
	    ckMunizioni.setSelected(true);
	    //diamo le classi e gli effetti agli oggetti
	    impostazioni.getStyleClass().add("sfondoImpostazioni");
	    bHomeSettings.getStyleClass().add("pulsanteImpostazioni");
	    bSave.getStyleClass().add("pulsanteImpostazioni");
	    eImpostazioni.getStyleClass().add("titleImpostazioni");
	    eModalitaGioco.getStyleClass().add("testoImpostazioni");
	    eNumeroMuinizioniImpostazioni.getStyleClass().add("testoImpostazioni");
	    eNumeroMuinizioniImpostazioni.getStyleClass().add("allinemantoCentratoImpostazioni");
	    eDurataGioco.getStyleClass().add("testoImpostazioni");
	    eDurataGioco.getStyleClass().add("allinemantoCentratoImpostazioni");
	    eSuono.getStyleClass().add("testoImpostazioni");
	    eVolumeSuono.getStyleClass().add("testoImpostazioni");
	    eVolumeSuono.getStyleClass().add("allinemantoCentratoImpostazioni");
	    eViteOggetto.getStyleClass().add("testoImpostazioni");
	    eMeteorite.getStyleClass().add("testoImpostazioni");
	    eMeteorite.getStyleClass().add("allinemantoCentratoImpostazioni");
	    eUfo.getStyleClass().add("testoImpostazioni");
	    eUfo.getStyleClass().add("allinemantoCentratoImpostazioni");
	    ckSottofondo.getStyleClass().add("testoImpostazioniDettaglio");
	    ckMissile.getStyleClass().add("testoImpostazioniDettaglio");
	    ckEspolosione.getStyleClass().add("testoImpostazioniDettaglio");
	    ckMunizioni.getStyleClass().add("testoImpostazioniDettaglio");
	    rbSenzaLimiti.getStyleClass().add("testoImpostazioniDettaglio");
	    rbConLimiti.getStyleClass().add("testoImpostazioniDettaglio");
	    bHomeSettings.setEffect(dropShadow1);
	    bSave.setEffect(dropShadow);
	    impostazioni.setEffect(dropShadow1);
	    eImpostazioni.setEffect(dropShadow1);
	    eModalitaGioco.setEffect(dropShadow);
	    eNumeroMuinizioniImpostazioni.setEffect(dropShadow);
	    eDurataGioco.setEffect(dropShadow);
	    eSuono.setEffect(dropShadow);
	    eVolumeSuono.setEffect(dropShadow);
	    eViteOggetto.setEffect(dropShadow);
	    eMeteorite.setEffect(dropShadow);
	    eUfo.setEffect(dropShadow);
	    ckSottofondo.setEffect(dropShadow);
	    ckMissile.setEffect(dropShadow);
	    ckEspolosione.setEffect(dropShadow);
	    ckMunizioni.setEffect(dropShadow);
	    rbSenzaLimiti.setEffect(dropShadow);
	    rbConLimiti.setEffect(dropShadow);
	    //Aggiungiamo gli elementi alla griglia
	    grigliaImpostazioni.add(eImpostazioni, 0, 0, 2, 1);//(0,0)
	    grigliaImpostazioni.add(bHomeSettings, 2, 0);//(0,0)
	    grigliaImpostazioni.add(eModalitaGioco, 0, 1, 2, 1);//(0,1)
	    grigliaImpostazioni.add(rbSenzaLimiti, 0, 2);
	    grigliaImpostazioni.add(eNumeroMuinizioniImpostazioni, 1, 2);
	    grigliaImpostazioni.add(cNumeroMunizioni, 2, 2);
	    grigliaImpostazioni.add(rbConLimiti, 0, 3);
	    grigliaImpostazioni.add(eDurataGioco, 1, 3);
	    grigliaImpostazioni.add(durataGioco, 2, 3);
	    grigliaImpostazioni.add(eSuono, 0, 4, 3, 1);
	    grigliaImpostazioni.add(eVolumeSuono, 0, 5);
	    grigliaImpostazioni.add(volume, 1, 5, 2, 1);
	    grigliaImpostazioni.add(ckSottofondo, 0, 6);
	    grigliaImpostazioni.add(ckMissile, 1, 6);
	    grigliaImpostazioni.add(ckEspolosione, 2, 6);
	    grigliaImpostazioni.add(ckMunizioni, 1, 7);
	    grigliaImpostazioni.add(eViteOggetto, 0, 8, 3, 1);
	    grigliaImpostazioni.add(eMeteorite, 0, 9);
	    grigliaImpostazioni.add(cViteMeteorite, 1, 9,2,1);
	    grigliaImpostazioni.add(eUfo, 0, 10);
	    grigliaImpostazioni.add(cViteUfo, 1, 10,2,1);
	    grigliaImpostazioni.add(bSave, 0, 11,4,1);
	    //do delle dimensioni al GridPane
	    grigliaImpostazioni.add(dimensioneGrigliaColonna1, 0, 11);
	    grigliaImpostazioni.add(dimensioneGrigliaColonna2, 1, 11);
	    grigliaImpostazioni.add(dimensioneGrigliaColonna3, 2, 11);
	    grigliaImpostazioni.add(dimensioneGrigliaX, 0, 11, 4, 1);
	    grigliaImpostazioni.add(dimensioneGrigliaY, 3, 0, 1, 12);
//	    //settare allineamento oggetti
//	    eLivelloGioco.setMaxWidth(DIMENSIONI_COLONNA_GRIDPANE);
//	    eLivelloGioco.setAlignment(Pos.CENTER);
//	    ckSottofondo.setMaxWidth(DIMENSIONI_COLONNA_GRIDPANE);
//	    ckMissile.setMaxWidth(DIMENSIONI_COLONNA_GRIDPANE);
//	    ckEspolosione.setMaxWidth(DIMENSIONI_COLONNA_GRIDPANE);
//	    ckMunizioni.setMaxWidth(DIMENSIONI_COLONNA_GRIDPANE);
	    //settaggio griglia e elementi
	    grigliaImpostazioni.setPadding(new Insets(10,10,10,10));
	    grigliaImpostazioni.setHgap(10);
	    grigliaImpostazioni.setVgap(10);
	    grigliaImpostazioni.setPrefWidth(1000);
	    grigliaImpostazioni.getStyleClass().add("grigliaImpstazioni");
	    grigliaImpostazioni.setLayoutX((WIDTH_SCHERMO-DIMENSIONI_X_GRIDPANE-40)/2);
	    grigliaImpostazioni.setLayoutY((HEIGTH_SCHERMO-DIMENSIONI_Y_GRIDPANE-90)/2);
	    impostazioni.setLayoutX((WIDTH_SCHERMO-DIMENSIONI_X_SFONDO_GRIDPANE)/2);
	    impostazioni.setLayoutY((HEIGTH_SCHERMO-DIMENSIONI_Y_SFONDO_GRIDPANE)/2);
	    bSave.setMaxWidth(DIMENSIONI_X_GRIDPANE);
		//aggiungiamo i due radioButton alla lista delle modalità del gioco
		rbConLimiti.setToggleGroup(modalitaDiGioco);
		rbSenzaLimiti.setToggleGroup(modalitaDiGioco);
		
		
		//completiamo il setup
		Scene scena = new Scene(schermo, WIDTH_SCHERMO, HEIGTH_SCHERMO);
		scena.getStylesheets().add("it/edu/iisgubbio/giocoFinale/StyleSpaceShip.css");
		
		controllaImpostazioni();
		
		costruisciInterfaccia(0); //entriamo nella schermata home
		
		bStartGioco.setOnAction(e->gestisciInterfaccia(e));
		bResetGioco.setOnAction(e->gestisciInterfaccia(e));
		bSettings.setOnAction(e->gestisciInterfaccia(e));
		bHomeGioco.setOnAction(e->gestisciInterfaccia(e));
		bHomeSettings.setOnAction(e->gestisciInterfaccia(e));
		bSave.setOnAction(e->controllaImpostazioni());
		
		scena.setOnKeyPressed(e -> pigiato(e));
		scena.setOnKeyReleased(e -> rilasciato(e));
		
		finestra.resizableProperty().setValue(false); // blocca il ridimensionamento della finestra
		finestra.setTitle("Spaceship");
		finestra.setScene(scena);
		finestra.show();

	}
	
	public void controllaImpostazioni() {
		//cambiamo la VITA DEGLI OGGETTI nel gioco (meteore, ufo)
		int nuovaVitaMeteorite= Integer.parseInt(cViteMeteorite.getText());
		int nuovaVitaUFO= Integer.parseInt(cViteUfo.getText());
		//una volta eseguito il metodo le vite degli oggetti vengono subito aggiornate
		for (int n = 0; n < nOggetti; n++) {
			if(oggettoNellaPosizione[n]!=2) {
				//System.out.println("METEORITE:");
				vettoreViteOggettiRimaste[n]=nuovaVitaMeteorite;
			}else{
				//System.out.println("UFO:");
				vettoreViteOggettiRimaste[n]=nuovaVitaUFO;
			}
			//System.out.println(" impostazioni: "+oggettoNellaPosizione[n]+" = "+vettoreViteOggettiRimaste[n]);
		}
		viteMeteorite=nuovaVitaMeteorite;
		viteUFO=nuovaVitaUFO;
		//SUONO
		//volume
		boolean volumeCambiato=false;
		double volumeSuoni= volume.getValue()/10;
		System.out.println(musicaSottofondo.getVolume()+", "+suonoEsplosione.getVolume()+", "+suonoMunizioniFinite.getVolume()+", "+suonoSparoMissile.getVolume());
		if(volumeSuoni!=musicaSottofondo.getVolume()) {
			musicaSottofondo.stop();
			volumeCambiato=true;
		}
		musicaSottofondo.setVolume(volumeSuoni);
		suonoEsplosione.setVolume(volumeSuoni);
		suonoMunizioniFinite.setVolume(volumeSuoni);
		suonoSparoMissile.setVolume(volumeSuoni);
		System.out.println(musicaSottofondo.getVolume()+", "+suonoEsplosione.getVolume()+", "+suonoMunizioniFinite.getVolume()+", "+suonoSparoMissile.getVolume());
		//sottofondo play o stop
		if(ckSottofondo.isSelected() && !statoPrecedenteSottofondo || volumeCambiato && ckSottofondo.isSelected()) {
			musicaSottofondo.play();
		}else if(!ckSottofondo.isSelected() && statoPrecedenteSottofondo){
			musicaSottofondo.stop();
		}
		statoPrecedenteSottofondo=ckSottofondo.isSelected();
		//MODALITA DI GIOCO
		//modalità
		tempoDiSpostamentoTOT=Integer.parseInt(durataGioco.getText());
		valoreSpostamentoSfondo = (WIDTH_SFONDO-1000)/(tempoDiSpostamentoTOT*1000)*25;
		if(rbConLimiti.isSelected()) {
			modalitaGiocoIllimitato=false;
		}else if(rbSenzaLimiti.isSelected()) {
			modalitaGiocoIllimitato=true;
		}
		if(numeroMunizioni!=Integer.parseInt(cNumeroMunizioni.getText())) {
			numeroMunizioni=Integer.parseInt(cNumeroMunizioni.getText());
			munizioni = new ImageView[numeroMunizioni];
			numeriMunizioniEsaurite = new boolean[numeroMunizioni];
			riempiMunizioni();
		}
		
		bStartGioco.setDisable(true);
		bResetGioco.setDisable(false);
		System.out.println("impostazioni aggiornate");
	}
	
	public void gestisciInterfaccia(Event pulsante) {
		int interfaccia=-1;
		String evento=pulsante.getSource().toString();
		if(evento.equals(bStartGioco.toString())) {
			interfaccia=1;
		}else if(evento.equals(bResetGioco.toString())) {
			resetGame=true;
			interfaccia=1;
		}else if(evento.equals(bSettings.toString())) {
			interfaccia=2; 
		}else if(evento.equals(bHomeGioco.toString())) {
			bStartGioco.setDisable(false);
			bResetGioco.setDisable(false);
			interfaccia=0; 
		}else if(evento.equals(bHomeSettings.toString())) {
			interfaccia=0; 
		}
		costruisciInterfaccia(interfaccia);
	}
	
	public int refreshVitaOggetto(int posizione){ //posizione dell'oggetto nel vettore "oggettoNellaPosizione"
		if(oggettoNellaPosizione[posizione]!=2) { //meteorite non ufo
			vettoreViteOggettiRimaste[posizione]=viteMeteorite;
			return viteMeteorite;
		}else {
			vettoreViteOggettiRimaste[posizione]=viteUFO;
			return viteUFO;
		}
	}
	
	public void riempiMunizioni() {
		for (int nM = 0; nM < numeroMunizioni; nM++) {
			Image immagineMissile = new Image(getClass().getResourceAsStream("Missile.png"));
			numeriMunizioniEsaurite[nM]=false;
			munizioni[nM] = new ImageView(immagineMissile);
			munizioni[nM].setFitHeight(HEIGTH_MISSILE);
			munizioni[nM].setFitWidth(WIDTH_MISSILE);
			munizioni[nM].setLayoutX(WIDTH_SCHERMO);
			schermo.getChildren().add(munizioni[nM]);
		}
	}
	
	public void costruisciInterfaccia(int interfaccia) {
		schermo.getChildren().clear();
		switch(interfaccia) {
		case 0:
			//costruiamo la schermata home
			schermo.getStyleClass().add("pain");
			muoviNavicella.stop();
			controllaCollisione.stop();
			muoviSfondo.stop();
			muoviOggetti.stop();
			
			if(firstOpen) {
				bStartGioco.setDisable(false);
				bResetGioco.setDisable(true);
				schermo.getChildren().add(sfondoHomeFirstOpen);
				schermo.getChildren().add(sfondoHomeTrasperente);
				schermo.getChildren().add(menu);
				schermo.getChildren().add(eTitle);
				schermo.getChildren().add(bStartGioco);
				schermo.getChildren().add(bSettings);
				schermo.getChildren().add(bResetGioco);
				firstOpen=false;
			}else {
				schermo.getChildren().add(sfondoHomeFirstOpen);
				schermo.getChildren().add(sfondoHomeTrasperente);
				schermo.getChildren().add(menu);
				schermo.getChildren().add(eTitle);
				schermo.getChildren().add(bStartGioco);
				schermo.getChildren().add(bResetGioco);
				schermo.getChildren().add(bSettings);
			}
			break;
		case 1:
			//costruiamo la schermata per il gioco
			schermo.getChildren().add(sfondo);
			schermo.getChildren().add(sfondo2);
			posizioneNaviciella[0]=0;
			posizioneNaviciella[1]=(HEIGTH_SCHERMO - WIDTH_NAVICELLA) / 2;
			navicella.setLayoutX(posizioneNaviciella[0]);
			navicella.setLayoutY(posizioneNaviciella[1]);
			schermo.getChildren().add(navicella);
			if(resetGame) {
				//nel caso si sia in modalità di reset
				posizioneSfondoX = 0;
				posizioneSfondo2X = WIDTH_SFONDO;
				munizioniUtilizzate=0;
				punteggioAttuale=0;
				eNumeroPunti.setText(""+punteggioAttuale);
				// riempimento munizioni
				riempiMunizioni();
				numeroMunizioniAttuali=numeroMunizioni;
				eNumeroMunizioni.setText(""+numeroMunizioniAttuali);
				for (int n = 0; n < nOggetti; n++) {
					riposizionaOggetto(vettoreOggetti[n]);
					refreshVitaOggetto(n); //affidiamo ad ogni oggetto la sua vita
					schermo.getChildren().add(vettoreOggetti[n]);
				}
				sfocatura.setLayoutX(WIDTH_SFONDO);
				resetGame=false;
			}else {
				//nel caso si sia in modalità normale
				for (int n = 0; n < nOggetti; n++) {
					schermo.getChildren().add(vettoreOggetti[n]);
				}
				for (int nM = munizioniUtilizzate; nM < numeroMunizioni; nM++) {
					schermo.getChildren().add(munizioni[nM]);
				}
				sfocatura.setLayoutX(WIDTH_SFONDO); //posizioniamo la sfocatura fuori dallo schermo
			}
			schermo.getChildren().add(sfocatura);
			
			//costruiamo il menu delle informazioni
			schermo.getChildren().add(informazioni);
			schermo.getChildren().add(separaInformazioni1);
			schermo.getChildren().add(separaInformazioni2);
			schermo.getChildren().add(separaInformazioni3);
			schermo.getChildren().add(ePunti);
			schermo.getChildren().add(eNumeroPunti);
			schermo.getChildren().add(eVite);
			for(int i=0; i<vettoreCuori.length; i++) {
				schermo.getChildren().add(vettoreCuori[i]);
			}
			schermo.getChildren().add(eMunizioni);
			schermo.getChildren().add(eNumeroMunizioni);
			schermo.getChildren().add(bHomeGioco);
			
			// movimento navicella
			muoviNavicella.setCycleCount(Animation.INDEFINITE);
			muoviNavicella.play();
			// controlla collisioni
			controllaCollisione.setCycleCount(Animation.INDEFINITE);
			controllaCollisione.play();
			// movimento sfondo
//			tempo1=System.currentTimeMillis();
//			contaRipetizioniSpostamento=0;
			muoviSfondo.setCycleCount(Animation.INDEFINITE);
			muoviSfondo.play();
			System.out.println(posizioneSfondoX);
			System.out.println(posizioneSfondo2X);
			// movimento oggetti
			muoviOggetti.setCycleCount(Animation.INDEFINITE);
			muoviOggetti.play();
			break;
			
		case 2:
			//costruiamo la schermata delle impostazioni
			grigliaImpostazioni.setGridLinesVisible(false);
			schermo.getChildren().add(sfondoHomeFirstOpen);
			schermo.getChildren().add(sfondoHomeTrasperente);
			schermo.getChildren().add(impostazioni);
			schermo.getChildren().add(grigliaImpostazioni);
			break;
		}
	}
	
	public void aggiornaPosizioneSfondo() {
		posizioneSfondoX -= valoreSpostamentoSfondo;
		posizioneSfondo2X -= valoreSpostamentoSfondo;
		double posizioneFinaleSfondo = posizioneSfondoX + WIDTH_SFONDO;
		double posizioneFinaleSfondo2 = posizioneSfondo2X + WIDTH_SFONDO;
		if(!modalitaGiocoIllimitato) {
//			contaRipetizioniSpostamento++;
			if (posizioneFinaleSfondo <= WIDTH_SCHERMO) {
//				tempo2=System.currentTimeMillis();
//				System.out.println(tempo1-tempo2+ " ripetizioni: " +contaRipetizioniSpostamento);
				muoviSfondo.stop();
			}
			sfondo.setLayoutX(posizioneSfondoX);
			sfocatura.setLayoutX(posizioneFinaleSfondo - WIDTH_SFOCATURA / 2);
		}else {
			if(posizioneFinaleSfondo<=0) {
				posizioneSfondoX=posizioneFinaleSfondo2-1000; //sotraiamo mille cosi da nascondere la parte dello sfondo più scura
			}
			if(posizioneFinaleSfondo2<=0) {
				posizioneSfondo2X=posizioneFinaleSfondo;
			}
			sfondo.setLayoutX(posizioneSfondoX);
			sfondo2.setLayoutX(posizioneSfondo2X);
		}
//		System.out.println("----------------------------------------------");
//		System.out.println("sfondo 1 posizione finale X: "+posizioneFinaleSfondo);
//		System.out.println("sfondo 2 posizioneX: "+posizioneSfondo2X);
//		System.out.println("sfondo 2 posizione finale X: "+posizioneFinaleSfondo2);
//		System.out.println("sfondo 1 posizioneX: "+posizioneSfondoX);
//		System.out.println("----------------------------------------------");
		//approfittamo dello spostamento dello sfondo per far anche cambiare colore al numero di munizioni quando è zero
		if(System.currentTimeMillis() - tempoPassato >= 500 && numeroMunizioniAttuali==0) {
			if(bianco) {
				eNumeroMunizioni.setTextFill(Color.WHITE);
				bianco=false;
			}else {
				eNumeroMunizioni.setTextFill(Color.RED);
				bianco=true;
			}
			tempoPassato = System.currentTimeMillis();
		}
	}

	public void spostaOggetti() {
		// scegliamo l'oggetto
		indiceOggetto++;
		if (indiceOggetto == nOggetti) {
			indiceOggetto = 0;
		}
		oggettoAttuale = vettoreOggetti[indiceOggetto];
		oggettoAttuale.setLayoutX(oggettoAttuale.getLayoutX() - 3);
		// spostiamo o risposizioniamo l'oggetto
		if (oggettoAttuale.getLayoutX() <= -DIMENSION_OGGETTI) {
			riposizionaOggetto(oggettoAttuale);
		}
	}

	public void riposizionaOggetto(ImageView oggetto) {
		int posizioneY;
		int posizioneX;
		posizioneY = (int) (Math.random() * (HEIGTH_SCHERMO - DIMENSION_OGGETTI));
		posizioneX = (int) (Math.random() * WIDTH_SCHERMO);
		oggetto.setLayoutX(WIDTH_SCHERMO + posizioneX);
		oggetto.setLayoutY(posizioneY);
	}

	public void spara() {
		if (System.currentTimeMillis() - tempoScorsoMissile >= 100 && numeroMunizioniAttuali!=0) {
			if (numeroMunizioniAttuali<=10 && numeroMunizioniAttuali>5) {
				eNumeroMunizioni.setTextFill(Color.ORANGE);
			}else if(numeroMunizioniAttuali<=5) {
				eNumeroMunizioni.setTextFill(Color.RED);
			}
			if(ckMissile.isSelected()) {
				suonoSparoMissile.play();
			}
			
			munizioni[munizioniUtilizzate].setLayoutY(navicella.getLayoutY() + (HEIGTH_NAVICELLA / 2 - HEIGTH_MISSILE / 2));
			munizioni[munizioniUtilizzate].setLayoutX(navicella.getLayoutX() + WIDTH_NAVICELLA - WIDTH_MISSILE);
			munizioniUtilizzate++;
			numeroMunizioniAttuali=numeroMunizioni-munizioniUtilizzate;
			precedente = munizioniUtilizzate - 1;
			tempoScorsoMissile = System.currentTimeMillis();
			eNumeroMunizioni.setText(""+numeroMunizioniAttuali);
		}else if(System.currentTimeMillis() - tempoScorsoMissile >= 600 && numeroMunizioniAttuali==0) {
			if(ckMunizioni.isSelected()) {
				suonoMunizioniFinite.play();
			}
			
			tempoScorsoMissile = System.currentTimeMillis();
		}
		//System.out.println("sparato");
	}

	public void pigiato(KeyEvent pulsante) {
		//System.out.println(pulsante.getText());
		switch (pulsante.getText().toLowerCase()) {
		case "p":
			spara();
			break;
		case "w":
			spostaSU = true;
			break;
		case "d":
			spostaAVANTI = true;
			break;
		case "s":
			spostaGIU = true;
			break;
		case "a":
			spostaINDIETRO = true;
			break;
		}
	}

	public void rilasciato(KeyEvent pulsante) {
		switch (pulsante.getText().toLowerCase()) {
		case "w":
			spostaSU = false;
			break;
		case "d":
			spostaAVANTI = false;
			break;
		case "s":
			spostaGIU = false;
			break;
		case "a":
			spostaINDIETRO = false;
			break;
		}
	}

	public void aggiornaPosizioneNavicella() {
		for (int nM = 0; nM < munizioniUtilizzate; nM++) {
			munizioni[nM].setLayoutX(munizioni[nM].getLayoutX() + valoreSpostamentoNavicella + 3);
		}
		if (spostaSU && navicella.getLayoutY() >= -20) {
			posizioneNaviciella[1] -= valoreSpostamentoNavicella;
			navicella.setLayoutY(posizioneNaviciella[1]);
		}
		if (spostaGIU && navicella.getLayoutY() <= HEIGTH_SCHERMO - WIDTH_NAVICELLA) {
			posizioneNaviciella[1] += valoreSpostamentoNavicella;
			navicella.setLayoutY(posizioneNaviciella[1]);
		}
		if (spostaINDIETRO && navicella.getLayoutX() >= 0) {
			posizioneNaviciella[0] -= valoreSpostamentoNavicella;
			navicella.setLayoutX(posizioneNaviciella[0]);
		}
		if (spostaAVANTI && navicella.getLayoutX() <= WIDTH_SCHERMO - HEIGTH_NAVICELLA) {
			posizioneNaviciella[0] += valoreSpostamentoNavicella;
			navicella.setLayoutX(posizioneNaviciella[0]);
		}
	}

	public void metodoControllaCollisione() {
		ImageView oggettoSottopostoBound;
		numeroOggettoBound++;
		if (numeroOggettoBound == nOggetti) {
			numeroOggettoBound = 0;
		}
		oggettoSottopostoBound = vettoreOggetti[numeroOggettoBound];
		boundOggetti = oggettoSottopostoBound.getBoundsInParent();

		ImageView missileSottopostoBound;
		// long start = System.nanoTime();
		for (int i = 0; i < munizioniUtilizzate; i++) {
			if (!numeriMunizioniEsaurite[i]) { // controlliamo se il missile è esploso o scomparso
				missileSottopostoBound = munizioni[i];
				boundMissile = missileSottopostoBound.getBoundsInParent();
				if (missileSottopostoBound.getLayoutX() > WIDTH_SCHERMO) {
					rimuoviOggetto(10, missileSottopostoBound);
					numeriMunizioniEsaurite[i] = true;
				}
				if (boundMissile.intersects(boundOggetti)) {
					int posizioneXMissile, posizioneXMeteorie, posizioneYMissile;
					// prendiamo le posizione del missile e gli oggetti
					posizioneXMissile = (int) (missileSottopostoBound.getLayoutX() + WIDTH_MISSILE);
					posizioneYMissile = (int) (missileSottopostoBound.getLayoutY());
					posizioneXMeteorie = (int) (oggettoSottopostoBound.getLayoutX());
					// spostiamo il missile e gli oggetti
					vettoreViteOggettiRimaste[numeroOggettoBound]--; //scaliamo la vita all'oggetto colpito
					if(vettoreViteOggettiRimaste[numeroOggettoBound]==0) {
						riposizionaOggetto(oggettoSottopostoBound);
						//aggiorniamo la vita all'oggetto e la aggiungioamo come punteggio
						punteggioAttuale+=refreshVitaOggetto(numeroOggettoBound)*10;;
						eNumeroPunti.setText(""+punteggioAttuale);
						// posizioniamo l'esplosione
						ImageView esplosione = new ImageView(animazioneEsplosione);
						esplosione.setFitHeight(HEIGTH_ESPLOSIONE);
						esplosione.setFitWidth(WIDTH_ESPLOSIONE);
						schermo.getChildren().add(esplosione);
						esplosione.setLayoutX(((posizioneXMissile + posizioneXMeteorie) / 2) - WIDTH_ESPLOSIONE / 2);
						esplosione.setLayoutY(posizioneYMissile - HEIGTH_ESPLOSIONE / 2);
						if(ckEspolosione.isSelected()) {
							suonoEsplosione.play();
						}
						rimuoviOggetto(500, esplosione);
					}
					rimuoviOggetto(10, missileSottopostoBound); // spostiamo il missile ad una posizione fuori dalla
																// portata degli oggetti
					numeriMunizioniEsaurite[i] = true;
					// conta++;
					/*
					 * System.out.println(posizioneXMissile+"+"+posizioneXMeteorie+"/2"+" = "+(
					 * posizioneXMissile+posizioneXMeteorie)/2);
					 * System.out.println(posizioneYMissile+"+"+HEIGTH_ESPLOSIONE+"/2"+" = "+(
					 * posizioneYMissile+HEIGTH_ESPLOSIONE/2));
					 * System.out.println("questa è la "+conta+" volta che compare l'esplosione");
					 */
					
				}
			}
		}
		// long end = System.nanoTime();
		// System.out.println("c'ho messo "+(end-start));
	}
	
	public void rimuoviOggetto(int traQuantoTempo, ImageView oggetto) {
		Timeline eliminaOggetto;
		eliminaOggetto = new Timeline(new KeyFrame(Duration.millis(traQuantoTempo), x -> eseguiRimozione(oggetto)));
		eliminaOggetto.setCycleCount(1);
		eliminaOggetto.play();
	}

	public void eseguiRimozione(ImageView oggetto) {
		schermo.getChildren().remove(oggetto);
	}

	public static void main(String[] args) {
		launch(args);
	}
}

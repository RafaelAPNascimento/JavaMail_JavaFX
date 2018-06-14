package exe03;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

//enviando anexo
public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		//gerenciados de eventos na JanelaEnviar
		EventoEnviarEmail evento = new EventoEnviarEmail();
		
		JanelaEnviar janelaEnviar = new JanelaEnviar(evento);		
		evento.janelaEnviar = janelaEnviar;
		
		//configura o stage e exibe a GUI
		primaryStage.setTitle("Cliente de Email - Enviando Anexo");
		primaryStage.setScene(janelaEnviar.scene);		
		primaryStage.show();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		Platform.exit();
	}
}

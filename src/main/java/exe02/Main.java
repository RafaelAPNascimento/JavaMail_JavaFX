package exe02;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

//enviando email
public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub		
		//criando o objeto gerenciador de eventos
		EventoEnviarEmail evento = new EventoEnviarEmail();
		
		//inicia a dialog e passa o monitor de eventos no consturtor
		JanelaEnviar janelaEnviar = new JanelaEnviar(evento);
		evento.janelaEnviar = janelaEnviar;
		
		//configura o Stage do JavaFx e adiciona a Janela
		primaryStage.setTitle("Enviando Email");
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

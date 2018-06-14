package exe04;

import javafx.application.Application;
import javafx.stage.Stage;

//Enviando conteudo HTML
public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		EventoEnviarEmail evento = new EventoEnviarEmail();
		JanelaEnviar janelaEnviar = new JanelaEnviar(evento);
		
		evento.janelaEnviar = janelaEnviar;
		
		primaryStage.setTitle("Enviando Conteúdo HTML");
		primaryStage.setScene(janelaEnviar.scene);		
		primaryStage.show();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
}

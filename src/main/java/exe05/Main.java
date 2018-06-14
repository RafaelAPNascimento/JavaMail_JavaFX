package exe05;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

//recebendo email
public class Main extends Application {

	GerenciadorDeEventos gerenciadorDeEventos;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		gerenciadorDeEventos = new GerenciadorDeEventos();
		JanelaInbox janelaInbox = new JanelaInbox(gerenciadorDeEventos);		
		primaryStage.setTitle("Recebendo Email");
		primaryStage.setScene(janelaInbox.scene);		
		primaryStage.show();
	}
	
	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub		
		Platform.exit();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}

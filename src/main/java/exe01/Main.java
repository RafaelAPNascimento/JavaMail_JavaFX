package exe01;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {

	private AutenticarEvent autenticarEvent;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		//todos componentes JavaFx rodam em um Stage
		
		autenticarEvent = new AutenticarEvent();
		primaryStage.setTitle("Autenticador de Email");
		
		//adicionamos a dialogAutenticacao no Stage principal
		primaryStage.setScene(autenticarEvent.dialogAutenticacao.getScene());

		//exibe a dialog
		primaryStage.show();		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);		
	}
	
	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		//encerrando o javaFx
		Platform.exit();
	}
}

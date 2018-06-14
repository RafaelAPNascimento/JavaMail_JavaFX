package exe03;

//Classe Wrapper para trabalhar com um arquivo anexado 
public class ArquivoModel {

	private String nomeArquivo;
	private byte[] conteudo;
	private String mimeType;
	
	public ArquivoModel(String nomeArquivo, byte[] conteudo, String mimeType) {
		super();
		this.nomeArquivo = nomeArquivo;
		this.conteudo = conteudo;
		this.mimeType = mimeType;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public byte[] getConteudo() {
		return conteudo;
	}

	public void setConteudo(byte[] conteudo) {
		this.conteudo = conteudo;
	}

	public String getMimeType() {
		return mimeType;
	}
	
}

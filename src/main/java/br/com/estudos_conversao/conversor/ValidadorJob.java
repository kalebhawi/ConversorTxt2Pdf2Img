package br.com.estudos_conversao.conversor;

import java.io.File;
import java.io.IOException;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.itextpdf.text.Document;

@DisallowConcurrentExecution
public class ValidadorJob implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		try {
			Conversor conversor = new Conversor();

			// criar diretorios
			String caminho = "C:\\Teste";
			String nomeArquivo = conversor.gerarCodigo();
			String caminhoPdf = caminho + File.separator + nomeArquivo + ".pdf";
			conversor.criarDiretorios(caminho);

			// ler texto
			String texto = Conversor.gerarCodigo();

			// criar txt
			File arquivo = conversor.criarTxt(caminho, texto, nomeArquivo);
			
			// converter para pdf
			Document arquivoPdf = conversor.txt2Pdf(arquivo, caminhoPdf);

			Conversor.pdfToImg(caminhoPdf, caminho, nomeArquivo);
			
		} catch (IOException e) {
			System.out.println("Erro ao converter");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Erro ao converter");
			e.printStackTrace();
		}

	}

}

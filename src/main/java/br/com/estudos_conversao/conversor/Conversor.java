package br.com.estudos_conversao.conversor;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class Conversor {

	public void main() {
	}

	public static String gerarCodigo() {
		return Integer.toString(Math.abs(new Random().nextInt()), 36).substring(0, 5);
	}

	public static BufferedImage pdf2Img(String texto) {

		System.out.println("Convertendo .pdf para .png...");
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();

		// g2d.setFont(new Font());
		FontMetrics fm = g2d.getFontMetrics();
		int width = fm.stringWidth(texto);
		int height = fm.getHeight();
		g2d.dispose();

		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		g2d = img.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		fm = g2d.getFontMetrics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);
		g2d.setColor(Color.BLACK);
		g2d.drawString(texto, 0, fm.getAscent());
		g2d.dispose();

		return img;
	}

	public File criarTxt(String caminho, String texto, String nomeArquivo) {
		try {
			System.out.println("Criando arquivo de texto...");
			File arquivo = new File(caminho + File.separator + nomeArquivo + ".txt");
			FileWriter escreva = new FileWriter(arquivo, true);
			escreva.write(texto);
			escreva.close();
			return arquivo;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	String lerTexto() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Informe o texto para conversão: ");
		return scan.nextLine();
	}

	public String criarDiretorios(String caminho) {
		System.out.println("Criando diretórios...");
		File pasta = new File(caminho);
		if (!pasta.exists()) {
			pasta.mkdirs();
		}
		return caminho;
	}

	public Document txt2Pdf(File arquivoTxt, String caminhoPdf) throws Exception {
		
		System.out.println("Convertendo .txt para .pdf...");
		BufferedReader br = null;

		try {

			Document pdfDoc = new Document(PageSize.A4);
			PdfWriter.getInstance((Document) pdfDoc, new FileOutputStream(caminhoPdf))
					.setPdfVersion(PdfWriter.VERSION_1_7);

			pdfDoc.open();

			Font myfont = new Font();
			myfont.setStyle(Font.BOLD);
			myfont.setSize(11);

			pdfDoc.add(new Paragraph("\n"));

			if (arquivoTxt.exists()) {

				br = new BufferedReader(new FileReader(arquivoTxt));
				String strLine;

				while ((strLine = br.readLine()) != null) {
					Paragraph para = new Paragraph(strLine + "\n", myfont);
					para.setAlignment(Element.ALIGN_JUSTIFIED);
					pdfDoc.add(para);
				}
			} else {
				System.out.println("no such file exists!");
				return null;
			}
			pdfDoc.close();
			
			return pdfDoc;
		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null)
				br.close();
		}

		return null;
	}

	public static void pdfToImg(String caminhoPdf, String caminho, String nomeArquivo) throws IOException {
		
		System.out.println("Convertendo .pdf para .png...");
		File input = new File(caminhoPdf);
		PDDocument doc = PDDocument.load(new FileInputStream(input));
		List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
		Iterator<PDPage> i = pages.iterator();
		int count = 1;
		while (i.hasNext()) {
			PDPage page = i.next();
			BufferedImage bi = page.convertToImage();
			File f = new File(caminho + File.separator + nomeArquivo + count + ".png");
			ImageIO.write(bi, "png", f);
			count++;
		}
		System.out.println("Conversão Completa!");
	}

	/*
	 * try { File sourceFile = new File(caminho); File destinationFile = new
	 * File(caminhoPdf); if (!destinationFile.exists()) { destinationFile.mkdir();
	 * System.out.println("Folder Created -> " + destinationFile.getAbsolutePath());
	 * } if (sourceFile.exists()) { System.out.println("Images copied to Folder: " +
	 * destinationFile.getName()); PDDocument document =
	 * PDDocument.load(caminhoPdf);
	 * 
	 * @SuppressWarnings("unchecked") List<PDPage> list =
	 * document.getDocumentCatalog().getAllPages();
	 * System.out.println("Total files to be converted -> " + list.size());
	 * 
	 * String fileName = destinationFile.getName().replace(".pdf", ""); int
	 * pageNumber = 1; for (PDPage page : list) { BufferedImage image =
	 * page.convertToImage(); File outputfile = new File(caminho + File.separator +
	 * fileName + "_" + pageNumber +".png"); System.out.println("Image Created -> "
	 * + outputfile.getName()); ImageIO.write(image, "png", outputfile);
	 * pageNumber++; } document.close();
	 * System.out.println("Converted Images are saved at -> " +
	 * destinationFile.getAbsolutePath()); } else {
	 * System.err.println(sourceFile.getName() + " File not exists"); }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 */
}

package com.ccnu.wmf2png;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.arnx.wmf2svg.gdi.svg.SvgGdi;
import net.arnx.wmf2svg.gdi.wmf.WmfParser;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.w3c.dom.Document;

public class WmfPng {

	public void CovertWmf(String filePath, String dest){
		InputStream in;
		try {
			in = new FileInputStream(filePath);
			WmfParser parser = new WmfParser();
			final SvgGdi gdi = new SvgGdi(false);
			parser.parse(in, gdi);
			Document doc = gdi.getDocument();

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			transformer.transform(new DOMSource(doc), new StreamResult(bos));

			System.out.println("this is proc");
			
			PNGTranscoder t = new PNGTranscoder();
			t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));
			
			TranscoderInput input = new TranscoderInput(bos.toString());

			OutputStream ostream = new FileOutputStream("out.png");
			TranscoderOutput output = new TranscoderOutput(ostream);
			t.transcode(input, output);
			ostream.flush();
			ostream.close();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		WmfPng wmf2png = new WmfPng();
		String filePath = "F:\\test\\imgMJHZ201401\\1.wmf";
		String dest = "F:\\test\\imgMJHZ201401\\";
		wmf2png.CovertWmf(filePath, dest);
	}
}

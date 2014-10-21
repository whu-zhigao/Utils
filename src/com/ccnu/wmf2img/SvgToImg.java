package com.ccnu.wmf2img;

import java.io.*;

import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;

public class SvgToImg {

	public void SaveAsJPEG(String filePath) throws Exception {
		// Create a JPEG transcoder
		JPEGTranscoder t = new JPEGTranscoder();
		// Set the transcoding hints.
		t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,
				new Float(.8));
		// Create the transcoder input.
		String svgURI = new File(filePath).toURL().toString();
		TranscoderInput input = new TranscoderInput(svgURI);
		// Create the transcoder output.
		OutputStream ostream = new FileOutputStream("out.jpg");
		TranscoderOutput output = new TranscoderOutput(ostream);
		// Save the image.
		t.transcode(input, output);
		// Flush and close the stream.
		ostream.flush();
		ostream.close();
		System.exit(0);
	}
	
	public void SaveAsPNG(String filePath) throws Exception {
		PNGTranscoder t = new PNGTranscoder();
		t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,
				new Float(.8));
		String svgURI = new File(filePath).toURL().toString();
		TranscoderInput input = new TranscoderInput(svgURI);
		OutputStream ostream = new FileOutputStream("out.png");
		TranscoderOutput output = new TranscoderOutput(ostream);
		t.transcode(input, output);
		ostream.flush();
		ostream.close();
		System.exit(0);
	}
	
	public static void main(String[] args) throws Exception{
		SvgToImg svg2img = new SvgToImg();
		String filePath = "F:\\uploadQuestionImage\\img_mjfjfz201201\\0.svg";
		svg2img.SaveAsJPEG(filePath);
	}
}
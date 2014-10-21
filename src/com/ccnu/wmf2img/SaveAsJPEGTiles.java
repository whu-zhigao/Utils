package com.ccnu.wmf2img;
import java.io.*;
import java.awt.*;

import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;

/**
 * 
 * @author Walter
 *shows how you can split an SVG document into 4 tiles
 */
public class SaveAsJPEGTiles {

	JPEGTranscoder trans = new JPEGTranscoder();

	public SaveAsJPEGTiles() {
		trans.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,
				new Float(.8));
	}

	public void tile(String inputFilename,
			String outputFilename,
			Rectangle aoi) throws Exception {
		// Set hints to indicate the dimensions of the output image
		// and the input area of interest.
		trans.addTranscodingHint(JPEGTranscoder.KEY_WIDTH,
				new Float(aoi.width));trans.addTranscodingHint(JPEGTranscoder.KEY_HEIGHT,
						new Float(aoi.height));trans.addTranscodingHint(JPEGTranscoder.KEY_AOI, aoi);

						// Transcode the file.
						String svgURI = new File(inputFilename).toURL().toString();
						TranscoderInput input = new TranscoderInput(svgURI);
						OutputStream ostream = new FileOutputStream(outputFilename);
						TranscoderOutput output = new TranscoderOutput(ostream);
						trans.transcode(input, output);

						// Flush and close the output.
						ostream.flush();
						ostream.close();
	}

	public static void main(String[] args) throws Exception {
		// Rasterize the samples/anne.svg document and save it
		// as four tiles.
		SaveAsJPEGTiles p = new SaveAsJPEGTiles();
		String in = "samples/anne.svg";
		int documentWidth = 450;
		int documentHeight = 500;
		int dw2 = documentWidth / 2;
		int dh2 = documentHeight / 2;
		p.tile(in, "tileTopLeft.jpg", new Rectangle(0, 0, dw2, dh2));
		p.tile(in, "tileTopRight.jpg", new Rectangle(dw2, 0, dw2, dh2));
		p.tile(in, "tileBottomLeft.jpg", new Rectangle(0, dh2, dw2, dh2));
		p.tile(in, "tileBottomRight.jpg", new Rectangle(dw2, dh2, dw2, dh2));
		System.exit(0);
	}
}
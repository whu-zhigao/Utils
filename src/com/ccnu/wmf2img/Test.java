package com.ccnu.wmf2img;

public class Test {
	
	public static void main(String[] args) throws Exception{
		WmfToSvg wmf2Svg = new WmfToSvg();
		String filePath = "F:\\test\\imgMJHZ201401\\0.wmf";
		String dest = "F:\\test\\svg.svg";
		wmf2Svg.convert(filePath, dest);
		
		SvgToImg saveJ = new SvgToImg();
//		saveJ.SaveAsJPEG(dest);
		
		saveJ.SaveAsPNG(dest);
//		SvgToImg svg2Img = new SvgToImg();
//		svg2Img.convert2JPEG(dest);
	}
}
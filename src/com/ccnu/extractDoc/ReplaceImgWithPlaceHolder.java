package com.ccnu.extractDoc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.arnx.wmf2svg.gdi.svg.SvgGdi;
import net.arnx.wmf2svg.gdi.wmf.WmfParser;

import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FileFormatUtil;
import com.aspose.words.ImageData;
import com.aspose.words.Node;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.Shape;
import com.aspose.words.ShapeType;


/**
 * 
 * @author Walter
 * 
 * use the 
 *
 */
public class ReplaceImgWithPlaceHolder {

	public String exportImg(String filePath, String imagePath){
		try {
			File tempFile = new File(filePath);
			String fileName = tempFile.getName();
			Document doc = new Document(filePath);
			//查询文档中所有wmf图片
			NodeCollection<?> shapeCollection = doc.getChildNodes(NodeType.SHAPE, true);
			Node[] shapes = shapeCollection.toArray();
			String imgPath = "";
			//如果文档存在图片
			if (shapes.length > 0) {
				File file = new File(imagePath + "\\img" + fileName.substring(0, fileName.lastIndexOf(".")));
				if (file != null) {
					if (file.exists() || file.mkdir()) {// 创建文档图片保存文件夹
						imgPath = file.getAbsolutePath() + "\\";
					} else {
						throw new Exception("文档图片保存路径不可写，请检查路径:\"" + imagePath + "\"");
					}
				}
				int imgNum = 0;
				for (Node node : shapes) {
					Shape shape = (Shape) node;
					if (shape.getShapeType() == ShapeType.OLE_OBJECT) {// 如果shape类型是ole类型
						ImageData img = shape.getImageData();//获得图片数据
						String imageName = java.text.MessageFormat.format(
								String.valueOf(imgNum), 
								FileFormatUtil.imageTypeToExtension(shape.getImageData().getImageType())) + shape.getName();
						if(FileFormatUtil.imageTypeToExtension(shape.getImageData().getImageType()).equals(".wmf")){
							InputStream in= img.toStream();
							WmfParser parser = new WmfParser();
							final SvgGdi gdi = new SvgGdi(false);
							parser.parse(in, gdi);
							org.w3c.dom.Document tempDoc = gdi.getDocument();
							OutputStream out = new FileOutputStream(imgPath + imageName + ".svg");
							output(tempDoc, out);
						}else {
							img.save(imgPath + imageName + FileFormatUtil.imageTypeToExtension(shape.getImageData().getImageType()));// 导出图片
						}
						DocumentBuilder builder = new DocumentBuilder(doc);//新建文档节点
						builder.moveTo(shape);//移动到图片位置
						builder.write("[img]" + fileName.substring(0, fileName.lastIndexOf(".")) + "_" + imgNum + "[/img]");//插入替换文本
						shape.remove();//移除图形
					} else if (shape.getShapeType() == ShapeType.IMAGE) {//如果shape类型是IMAGE类型
						ImageData i = shape.getImageData();//获得图片数据
						String imageName = java.text.MessageFormat.format(
								String.valueOf(imgNum), FileFormatUtil.imageTypeToExtension(shape.getImageData().getImageType())) + ".png";
						i.save(imgPath + imageName);//导出图片
						File f = new File(imgPath + imageName);
						if (f.exists()) {
							DocumentBuilder builder = new DocumentBuilder(doc);// 新建文档节点
							builder.moveTo(shape);// 移动到图片位置
							builder.write("[img]" + fileName.substring(0, fileName.lastIndexOf("."))
									+ "/" + f.getName().substring(0, f.getName().lastIndexOf("."))
									+ ".png" + "[/img]");// 插入替换文本
							shape.remove();// 移除图形
						} else {
							//							log.error("图片不存在！");
							System.out.println("img is no ext");
							continue;
						}
					}
					imgNum++;
				}
				String extName = fileName.substring(fileName.lastIndexOf("."));
				String mainName = fileName.substring(0, fileName.lastIndexOf("."));
				doc.save(filePath.substring(0, filePath.lastIndexOf("."))+ "_done" + extName);//保存修改后的文档
				return mainName + "_done" + extName;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void output(org.w3c.dom.Document doc, OutputStream out) throws Exception {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,"-//W3C//DTD SVG 1.0//EN");
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd");
		transformer.transform(new DOMSource(doc), new StreamResult(out));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		transformer.transform(new DOMSource(doc), new StreamResult(bos));
		out.flush();
		out.close();
	}
	
	public static void main(String[] args){
		String filePath = "F:\\test\\uploadQuestion\\MJHZ201401.doc";
//		String fileName = "MJHZ201401.doc";	
		String imagePath = "F:\\test\\img";
		
		ReplaceImgWithPlaceHolder ex = new ReplaceImgWithPlaceHolder();
		ex.exportImg(filePath, imagePath);
	}
}
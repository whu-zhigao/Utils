package com.ccnu.extractDoc;

import java.io.File;

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
 * this project use aspose to exact Imgs from WordDoc
 *
 */
public class exportImgFromDoc{
	public String exportWmfFromDoc(String filePath, String imagePath, String fileName) {
		try {
			//新建文档对象
			Document doc = new Document(filePath + fileName);
			//查询文档中所有wmf图片
			NodeCollection shapeCollection = doc.getChildNodes(NodeType.SHAPE,true);
			Node[] shapes = shapeCollection.toArray();
			String imgPath = "";
			//如果文档存在图片
			if (shapes.length > 0) {
				File file = new File(imagePath + fileName.substring(0, fileName.lastIndexOf(".")));
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
						ImageData img = shape.getImageData();// 获得图片数据
						String imageName = java.text.MessageFormat.format(
								String.valueOf(imgNum), 
								FileFormatUtil.imageTypeToExtension(shape.getImageData().getImageType())) + ".wmf";
						img.save(imgPath + imageName);// 导出图片
						File f = new File(imgPath + imageName);
						if (f.exists()) {
							System.out.println("image is extst");
//							imageName = wmfToPNG(f.getAbsolutePath());
//							log.debug("f.path--->" + f.getAbsolutePath());
//							Exec.saveMinPhoto(imageName, imageName, (double)38, (double)0);
							if (f.canWrite()) {
								f.delete();
							}
						} else {
							System.out.println("img is not");
//							log.error("图片不存在！");
							continue;
						}
						// log.info("f.name--->" + f.getName());
						DocumentBuilder builder = new DocumentBuilder(doc);// 新建文档节点
						builder.moveTo(shape);// 移动到图片位置
						builder.write("[img]" + fileName.substring(0, fileName.lastIndexOf("."))
										+ "/" + f.getName().substring(0,f.getName().lastIndexOf(".")) 
										+ ".png" + "[/img]");// 插入替换文本
						shape.remove();// 移除图形
					} else if (shape.getShapeType() == ShapeType.IMAGE) {// 如果shape类型是ole类型
						ImageData i = shape.getImageData();// 获得图片数据
						String imageName = java.text.MessageFormat.format(
								String.valueOf(imgNum), FileFormatUtil.imageTypeToExtension(shape.getImageData().getImageType())) + ".png";
						i.save(imgPath + imageName);// 导出图片
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
				}
				String extName = fileName.substring(fileName.lastIndexOf("."));
				String mainName = fileName.substring(0, fileName.lastIndexOf("."));
				doc.save(filePath + mainName + "_done" + extName);// 保存修改后的文档
				// log.info("filename---->" + mainName + "_done" + extName);
				return mainName + "_done" + extName;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args){
		String filePath = "F:\\test\\uploadQuestion\\";
		String fileName = "MJHZ201401.doc";	
		String imagePath = "F:\\test\\img";
		
		exportImgFromDoc ex = new exportImgFromDoc();
		ex.exportWmfFromDoc(filePath, imagePath, fileName);
	}
}
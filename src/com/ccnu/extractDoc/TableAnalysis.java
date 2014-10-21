package com.ccnu.extractDoc;

import java.text.MessageFormat;

import com.aspose.words.Cell;
import com.aspose.words.Document;
import com.aspose.words.FileFormatUtil;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.Row;
import com.aspose.words.Shape;
import com.aspose.words.Table;

public class TableAnalysis {

	public void test1(){
		Document doc;
		try {
			doc = new Document("F:\\test\\uploadQuestion\\MJHZ201401.doc");

			NodeCollection<Shape> shapes = doc.getChildNodes(NodeType.SHAPE, true);

			Table table = (Table)doc.getChild(NodeType.TABLE, 0, true);
			int imageIndex = 0;
			int rowNum = table.getCount();
			for(int i = 0; i < rowNum; i++){
				for (Shape shape : (Iterable<Shape>) shapes)
				{
					if (shape.hasImage())
					{
						String imageFileName = java.text.MessageFormat.format(
								"Image.ExportImages.{0} Out{1}", imageIndex, FileFormatUtil.imageTypeToExtension(shape.getImageData().getImageType()));
						shape.getImageData().save("F:\\test\\img\\" + imageFileName);
						System.out.println(shape.getImageData().toString());
						table.getRange().replace(shape.getImageData().toString(), "<img>" + imageIndex + "</img>", true, true);
						imageIndex++;
					}
				}

			}

			// Get the first table in the document.

			// Replace any instances of our string in the entire table.
			table.getRange().replace("Carrots", "Eggs", true, true);
			// Replace any instances of our string in the last cell of the table only.
			table.getLastRow().getLastCell().getRange().replace("50", "20", true, true);

			doc.save("F:\\test\\uploadQuestion\\Table.ReplaceCellText Out.doc");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	public static void main(String[] args){

		Document doc;
		try {
			doc = new Document("F:\\test\\uploadQuestion\\MJHZ201401.doc");
			Table table = (Table)doc.getChild(NodeType.TABLE, 0, true);

			for(int i = 0; i < table.getRows().getCount(); i++){
				Row row = table.getRows().get(i);
				for(int j = 0; j < table.getRows().get(i).getCells().getCount(); j++)
				{
					Cell cell = row.getCells().get(j);
					String cellText = cell.toTxt().trim();
					System.out.println(MessageFormat.format("\t\tContents of Cell:{0} = \"{1}\"", j, cellText));
				}
				System.out.println(MessageFormat.format("\tEnd of Row {0}", i));
			}
			System.out.println();
		}catch(Exception e){
			e.printStackTrace();}
		}

	}

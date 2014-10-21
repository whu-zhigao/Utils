package com.ccnu.extractDoc;

import com.aspose.words.Document;
import com.aspose.words.FieldType;

public class test {
	
	public static void main(String[] args) throws Exception{
		Document doc = new Document("F:\\test\\uploadQuestion\\MJHZ201401.doc");

		// Pass the appropriate parameters to convert all IF fields encountered in the document (including headers and footers) to static text.
		FieldsHelper.convertFieldsToStaticText(doc.getFirstSection().getBody().getLastParagraph(), FieldType.FIELD_IF);

		System.out.println(doc.getFirstSection().getBody().getLastParagraph());
		// Save the document with fields transformed to disk.
		doc.save("F:\\test\\uploadQuestionTestFile\\TestFileDocument Out.doc");
	}

}

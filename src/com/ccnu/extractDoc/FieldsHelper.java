package com.ccnu.extractDoc;

import java.util.ArrayList;

import com.aspose.words.CompositeNode;
import com.aspose.words.DocumentVisitor;
import com.aspose.words.FieldEnd;
import com.aspose.words.FieldSeparator;
import com.aspose.words.FieldStart;
import com.aspose.words.Node;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
import com.aspose.words.Run;
import com.aspose.words.Table;
import com.aspose.words.VisitorAction;

class FieldsHelper extends DocumentVisitor
{
    /**
     * Converts any fields of the specified type found in the descendants of the node into static text.
     *
     * @param compositeNode The node in which all descendants of the specified FieldType will be converted to static text.
     * @param targetFieldType The FieldType of the field to convert to static text.
     */
    public static void convertFieldsToStaticText(CompositeNode compositeNode, int targetFieldType) throws Exception
    {
        FieldsHelper helper = new FieldsHelper(targetFieldType);
        compositeNode.accept(helper);

    }

    private FieldsHelper(int targetFieldType)
    {
        mTargetFieldType = targetFieldType;
    }

    public int visitFieldStart(FieldStart fieldStart)
    {
        // We must keep track of the starts and ends of fields incase of any nested fields.
        if (fieldStart.getFieldType() == mTargetFieldType)
        {
            mFieldDepth++;
            fieldStart.remove();
        }
        else
        {
            // This removes the field start if it's inside a field that is being converted.
            CheckDepthAndRemoveNode(fieldStart);
        }

        return VisitorAction.CONTINUE;
    }

    public int visitFieldSeparator(FieldSeparator fieldSeparator)
    {
        // When visiting a field separator we should decrease the depth level.
        if (fieldSeparator.getFieldType() == mTargetFieldType)
        {
            mFieldDepth--;
            fieldSeparator.remove();
        }
        else
        {
            // This removes the field separator if it's inside a field that is being converted.
            CheckDepthAndRemoveNode(fieldSeparator);
        }

        return VisitorAction.CONTINUE;
    }

    public int visitFieldEnd(FieldEnd fieldEnd)
    {
        if (fieldEnd.getFieldType() == mTargetFieldType)
            fieldEnd.remove();
        else
            CheckDepthAndRemoveNode(fieldEnd); // This removes the field end if it's inside a field that is being converted.

        return VisitorAction.CONTINUE;
    }

    public int visitRun(Run run)
    {
        // Remove the run if it is between the FieldStart and FieldSeparator of the field being converted.
        CheckDepthAndRemoveNode(run);

        return VisitorAction.CONTINUE;
    }

    public int visitParagraphEnd(Paragraph paragraph)
    {
        if (mFieldDepth > 0)
        {
            // The field code that is being converted continues onto another paragraph. We
            // need to copy the remaining content from this paragraph onto the next paragraph.
            Node nextParagraph = paragraph.getNextSibling();

            // Skip ahead to the next available paragraph.
            while (nextParagraph != null && nextParagraph.getNodeType() != NodeType.PARAGRAPH)
                nextParagraph = nextParagraph.getNextSibling();

            // Copy all of the nodes over. Keep a list of these nodes so we know not to remove them.
            while (paragraph.hasChildNodes())
            {
                mNodesToSkip.add(paragraph.getLastChild());
                ((Paragraph)nextParagraph).prependChild(paragraph.getLastChild());
            }

            paragraph.remove();
        }

        return VisitorAction.CONTINUE;
    }

    public int visitTableStart(Table table)
    {
        CheckDepthAndRemoveNode(table);

        return VisitorAction.CONTINUE;
    }

    /**
     * Checks whether the node is inside a field or should be skipped and then removes it if necessary.
     */
    private void CheckDepthAndRemoveNode(Node node)
    {
        if (mFieldDepth > 0 && !mNodesToSkip.contains(node))
            node.remove();
    }

    private int mFieldDepth = 0;
    private ArrayList mNodesToSkip = new ArrayList();
    private int mTargetFieldType;
}
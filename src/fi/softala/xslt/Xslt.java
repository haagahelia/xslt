package fi.softala.xslt;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

public class Xslt {

	public static final String COURSE_XML_FILENAME = "xml/course.xml";
	public static final String COURSE_XSL_FILENAME = "xsl/course.xsl";
	public static final String COURSE_HTML_FILENAME = "index.html";

	public static final String EXERCISE_FOLDER_NAME = "xc";
	public static final String EXERCISE_XML_ENDSWITH = ".xml";
	public static final String EXERCISE_XSL_FILENAME = "xsl/exercise.xsl";
	public static final String EXERCISE_HTML_ENDSWITH = ".html";

	public static void main(String[] args) throws Exception {

		Xslt t = new Xslt();

		// COURSE
		File courseXml = new File(COURSE_XML_FILENAME);
		File courseXsl = new File(COURSE_XSL_FILENAME);
		File courseOutput = new File(COURSE_HTML_FILENAME);
		System.out.println("XSLT " + COURSE_XML_FILENAME + " -> " + COURSE_HTML_FILENAME);
		t.transform(courseXml, courseXsl, courseOutput);

		// EXERCISES
		File exerciseXsl = new File(EXERCISE_XSL_FILENAME);
		File exerciseFolder = new File(EXERCISE_FOLDER_NAME);
		File[] exerciseXmls = exerciseFolder.listFiles(
				(File folder, String filename) -> filename.endsWith(EXERCISE_XML_ENDSWITH));
		for (File exerciseXml : exerciseXmls) {
			String outputFileName = EXERCISE_FOLDER_NAME + "/" + exerciseXml.getName() + EXERCISE_HTML_ENDSWITH;
			System.out.println("XSLT " + EXERCISE_FOLDER_NAME + "/" + exerciseXml.getName() + " -> " + outputFileName);
			t.transform(exerciseXml, exerciseXsl, new File(outputFileName));
		}
	}

	private DocumentBuilderFactory factory;
	private TransformerFactory tFactory;

	public Xslt() {
		this.factory = DocumentBuilderFactory.newInstance();
		this.tFactory = TransformerFactory.newInstance();
	}

	public void transform(File datafile, File stylesheet, File output) throws Exception {

		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(datafile);

		StreamSource stylesource = new StreamSource(stylesheet);
		Transformer transformer = tFactory.newTransformer(stylesource);

		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(output);
		transformer.transform(source, result);
	}
}

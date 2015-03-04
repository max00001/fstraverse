package mw.fstraverse.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import mw.fstraverse.tool.FProcReport;
import mw.fstraverse.tool.FProcResult;
import mw.fstraverse.tool.FSInfoStorage;

public class CountItemsReport implements FProcReport {


    @Override
    public void aggregate() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getPrintableString(FProcResult fProcResult) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String report(FSInfoStorage fsInfoStorage, File file) {
        // TODO Auto-generated method stub
        String tmpDir = System.getProperty("java.io.tmpdir");
        Path reportFile = FileSystems.getDefault().getPath(tmpDir, "report.html");
        
        XMLOutputFactory xof =  XMLOutputFactory.newInstance();
        XMLStreamWriter xtw;
        try {
            xtw = xof.createXMLStreamWriter(new FileOutputStream(reportFile.toFile()), "utf-8");
            xtw.writeComment("count items report");
            xtw.writeStartDocument("utf-8","1.0");
            xtw.setPrefix("", "http://www.w3.org/TR/REC-html40");
            xtw.writeStartElement("html");
            //xtw.writeNamespace("html", "http://www.w3.org/TR/REC-html40");
            xtw.writeStartElement("head");
            xtw.writeStartElement("title");
            xtw.writeCharacters(file.getName());
            xtw.writeEndElement();
            xtw.writeEndElement();

            xtw.writeStartElement("body");
            xtw.writeStartElement("p");
            xtw.writeCharacters("It's a report about ");
            xtw.writeStartElement("a");
            xtw.writeAttribute("href", file.toURI().toString());
            xtw.writeCharacters(file.getAbsolutePath());
            xtw.writeEndElement();
            xtw.writeEndElement();
            xtw.writeEndElement();
            xtw.writeEndElement();
            xtw.writeEndDocument();

            xtw.flush();
            xtw.close();    
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return reportFile.toString();
    }

}

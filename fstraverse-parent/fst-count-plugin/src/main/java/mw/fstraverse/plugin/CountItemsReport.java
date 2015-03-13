package mw.fstraverse.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import mw.fstraverse.tool.FPInfoStorage;
import mw.fstraverse.tool.FProcReport;
import mw.fstraverse.tool.FProcResult;
import mw.fstraverse.tool.FSInfoStorage;

public class CountItemsReport implements FProcReport {

    String type;

    private CountItemsReport() {
    }

    public CountItemsReport(String type) {
        super();
        this.type = type;
    }

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
    public String report(FSInfoStorage fsInfoStorage, File file, File reportabout, String outputFileName) {
        if (reportabout == null) {
            reportabout = file;
        }
        File outputFile = new File(outputFileName);
        if (outputFile.isDirectory() || !outputFile.exists()) {
            // add report.html as filename
            if (!outputFile.exists()) {
                outputFile.mkdirs();
            }
            String localFilename = "report-" + Calendar.getInstance().getTimeInMillis() + ".html";
            outputFile = FileSystems.getDefault().getPath(outputFile.getAbsolutePath(), localFilename).toFile();
        }
        
        XMLOutputFactory xof =  XMLOutputFactory.newInstance();
        XMLStreamWriter xtw;
        try {
            xtw = xof.createXMLStreamWriter(new FileOutputStream(outputFile),"utf-8");
            xtw.writeComment("count items report");
            xtw.writeStartDocument("utf-8","1.0");
            xtw.setPrefix("", "http://www.w3.org/TR/REC-html40");
            xtw.writeStartElement("html");
            //xtw.writeNamespace("html", "http://www.w3.org/TR/REC-html40");
                xtw.writeStartElement("head");
                    xtw.writeStartElement("title");
                        xtw.writeCharacters(reportabout.getName());
                    xtw.writeEndElement();
                xtw.writeEndElement();
                xtw.writeStartElement("body");
                    xtw.writeStartElement("h1");
                        xtw.writeCharacters("It's a report about ");
                        xtw.writeStartElement("a");
                            xtw.writeAttribute("href", reportabout.toURI().toString());
                            xtw.writeCharacters(reportabout.getAbsolutePath());
                        xtw.writeEndElement();
                    xtw.writeEndElement();
                    for (Map.Entry<File, FPInfoStorage> entry : fsInfoStorage.tailMapIterator(reportabout)) {
                        if ((reportabout.equals(entry.getKey())) || 
                                isParent(fsInfoStorage, reportabout, 
                                        entry.getValue())) {
                            xtw.writeStartElement("p");                        
                                xtw.writeStartElement("a");
                                    xtw.writeAttribute("href", entry.getKey().toURI().toString());
                                    if (reportabout.equals(entry.getKey())) {
                                        xtw.writeCharacters(entry.getKey().getAbsolutePath());                                        
                                    } else {
                                        xtw.writeCharacters(reportabout.toPath().relativize(entry.getKey().toPath()).toString());
                                    }
                                    
                                xtw.writeEndElement();
                                xtw.writeCharacters(" ");
                                FProcResult result = entry.getValue().get(type);
                                xtw.writeCharacters(result.getPrintableForm());
                            xtw.writeEndElement();
                        } else {
                            break; //iteration by storage
                        }
                    }
                    
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
        
        return outputFile.toString();
    }

    private boolean isParent(FSInfoStorage fsInfoStorage, File file, FPInfoStorage value) {
        // returns true if value refers to the file as to parent or its parent refers to file etc.
        File parent = value.getParentNode();
        while (parent != null) {
            if (parent.equals(file)) {
                return true;
            } else {
                parent = fsInfoStorage.get(parent).getParentNode();
            }
        }
        return false;
    }

}

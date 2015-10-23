/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.claviusxmlparser;

import it.cnr.ilc.lc.omega.core.parser.OmegaParser;
import it.cnr.ilc.lc.omega.core.parser.Resource;
import it.cnr.ilc.lc.omega.entity.Content;
import it.cnr.ilc.lc.omega.entity.ImageContent;
import it.cnr.ilc.lc.omega.entity.Source;
import it.cnr.ilc.lc.omega.entity.TextContent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLConnection;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import sirius.kernel.di.std.ConfigValue;
import sirius.kernel.di.std.Register;

/**
 *
 * @author angelo
 */
@Register(classes = OmegaParser.class, name = "clavius")
public class ClaviusParser implements OmegaParser {
    
    @ConfigValue("file.name")
    private String contentName;

    @Override
    public Source<TextContent> parseTextContent(URI uri) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //String sourcecontent = new 
        Source<TextContent> source = Source.sourceOf(TextContent.class);
        TextContent content = Content.contentOf(TextContent.class);
        content.setUri(uri.toASCIIString() + "/content.txt");
        System.err.println("in parse, content mimetype" + content.getMimetype());
        content.setText("Ciao Mamma!");
        source.setContent(content);
        source.setUri(uri.toASCIIString());
        return source;
    }

    @Override
    public List<Source<? extends Content>> parse(URI uri, Resource.Structure str, Resource.Granularity gra) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Source<? extends Content> parse(URI uri) {
        Source<ImageContent> source = Source.sourceOf(ImageContent.class);
        source.setUri(uri.toASCIIString());
        source.setContent(Content.contentOf(ImageContent.class));
        return source;
    }

    @Override
    public <T extends Content> Source<T> parse(URI uri, Class<T> clazz) {
        Source<T> source = Source.sourceOf(clazz);
        source.setUri(uri.toASCIIString());
        T content = Content.contentOf(clazz);
       // content.setUri(uri.toASCIIString() + "content.txt");
//        System.err.println("in parse, content name: " +contentName);
        content.setUri(uri.toASCIIString() + contentName);
        source.setContent(content);

        if (clazz.getCanonicalName().equals(TextContent.class.getCanonicalName())) {
//            System.err.println(clazz.getCanonicalName() + " | " + TextContent.class.getCanonicalName());
            _parseText(content);
        } else if (clazz.getCanonicalName().equals(ImageContent.class.getCanonicalName())) {
            _parseImage(content);
        }
        
        
//        TextContent tmp = (TextContent)source.getContent();
//        System.err.println(tmp.getText());

        return source;
    }

    private <T extends Content> void _parseText(T content) {
//        System.err.println("IN _PARSETEXT");
        TextContent cont = (TextContent) content;
//         System.err.println(cont.toString());
//         System.err.println(content.toString());
  
        URLConnection remoteDocument = null;
        try {
//             System.err.println(content.getUri());
            remoteDocument = URI.create(content.getUri()).toURL().openConnection();
            cont.setText(new Scanner(new BufferedInputStream(remoteDocument.getInputStream()), "UTF-8").useDelimiter(Pattern.compile("\\A")).next());
             //System.err.println(cont.getText());

        } catch (MalformedURLException ex) {
            Logger.getLogger(ClaviusParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClaviusParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private <T extends Content> void _parseImage(T content) {
        ImageContent cont = (ImageContent) content;

    }

}

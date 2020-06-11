package filtroLogs;


import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

/**
* @author puto viejo
*/
public class logger {

    Logger log;

    public logger(String workspace) throws IOException {
        log = Logger.getLogger(logger.class);
                   
        // Patrón que seguirá las lineas del log
        PatternLayout defaultLayout = new PatternLayout("m");
        RollingFileAppender rollingFileAppender = new RollingFileAppender();
        
        //Definimos el archivo dónde irá el log (la ruta)
        rollingFileAppender.setFile("/NOB_Project/WebContent/log.txt", true, false, 0);
        rollingFileAppender.setLayout(defaultLayout);

        log.removeAllAppenders();
        log.addAppender(rollingFileAppender);
        log.setAdditivity(false);
    }
    public Logger getLog() {
        return log;
    }
    public void setLog(Logger log) {
        this.log = log;
    }
}

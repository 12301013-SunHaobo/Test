package net.wgen.spring.common.ui.web.liveops;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wgen.spring.common.ui.web.util.URLPrefixConsts;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Set or reset log levels or appender thresholds at runtime
 * Valid levels from low to high: ALL, TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF 
 * 
 * usage:
 * http://.../liveops/webservices/log/showAll 
 * http://.../liveops/webservices/log/setLogger?logger=org.hibernate&level=OFF (set logger level)
 * http://.../liveops/webservices/log/resetLogger?logger=org.hibernate (reset logger to default level)
 * http://.../liveops/webservices/log/setAppender?appender=usage&threshold=DEBUG (set appender threshold)
 * http://.../liveops/webservices/log/resetAppender?appender=usage (reset appender to default threshold)
 * http://.../liveops/webservices/log/resetAll(reset all loggers to default levels, all appenders to default thresholds)
 */
@Controller
public class LogConfigController {
    
    @Autowired
    private ServletContext _servletContext;
    
    /**
     * Save default logger level settings as <loggerName, Level> 
     */
    private HashMap<String, Level> _defaultLoggerLevels = new HashMap<String, Level>();
    /**
     * Save default logger level settings as <appenderName, Level> 
     */
    private HashMap<String, Level> _defaultAppenderThresholds = new HashMap<String, Level>();

    /**
     * cache of all loggers references, please do not directly access, use {@link #getOrCreateAllAppendersMap} instead
     */
    private Map<String, Logger> _allLoggersMap = null;
    /**
     * cache of all appenders references, please do not directly access, use {@link #getAllAppenders} or {@link #getAppenderByName} instead
     */
    private Map<String, AppenderSkeleton> _allAppendersMap = null;

    
    private static String ROOT_LOGGER_NAME = "root";
    
    /** URL Prefixes. */
    public static final class URLPrefix {
        public static final String SHOW_ALL = URLPrefixConsts.LIVEOPS_WEBSERVICE + "/log/showAll";
        public static final String SET_LOGGER_LEVEL = URLPrefixConsts.LIVEOPS_WEBSERVICE + "/log/setLoggerLevel";
        public static final String RESET_LOGGER_LEVEL = URLPrefixConsts.LIVEOPS_WEBSERVICE + "/log/resetLoggerLevel";
        public static final String SET_APPENDER_THRESHHOLD = URLPrefixConsts.LIVEOPS_WEBSERVICE + "/log/setAppenderThreshold";
        public static final String RESET_APPENDER_THRESHHOLD = URLPrefixConsts.LIVEOPS_WEBSERVICE + "/log/resetAppenderThreshold";
        public static final String RESET_ALL = URLPrefixConsts.LIVEOPS_WEBSERVICE + "/log/resetAll";
        public static final String RESET_BY_PROPERTIES_FILE = URLPrefixConsts.LIVEOPS_WEBSERVICE + "/log/resetByPropertiesFile";
    }

    
    /** URL Parameters */
    public static final class URLParam {
        public static final String LOGGER_NAME = "logger";
        public static final String LEVEL_NAME = "level";
        public static final String APPENDER_NAME = "appender";
        public static final String THRESHOLD_NAME = "threshold";
    }
    
    @RequestMapping(value = URLPrefix.SHOW_ALL, method = RequestMethod.GET)
    public void showAllLogConfigs(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        
        dumpOutput(response, getAllLogConfigInfo());
    }

    @RequestMapping(value = URLPrefix.SET_LOGGER_LEVEL, method = RequestMethod.GET)
    public void setLogLevel(
            @RequestParam(value = URLParam.LOGGER_NAME, required = true) String loggerName,
            @RequestParam(value = URLParam.LEVEL_NAME, required = true) String logLevel,
            HttpServletResponse response)
        throws Exception {
        
        saveDefaultIfNeeded();
        
        String output = null; 
        Logger logger = getLoggerByName(loggerName);
        if(logger != null) {
            logger.setLevel(Level.toLevel(logLevel.toUpperCase()));
            output = loggerName + " is set to level='"+logLevel.toUpperCase()+"'";
        } else {
            output = "Could not find logger with name '"+loggerName+"'";
        }
        dumpOutput(response, output);
    }
    @RequestMapping(value = URLPrefix.RESET_LOGGER_LEVEL, method = RequestMethod.GET)
    public void resetLogLevel(
            @RequestParam(value = URLParam.LOGGER_NAME, required = true) String loggerName,
            HttpServletResponse response)
        throws Exception {
        
        saveDefaultIfNeeded();
        
        String output = null; 
        Logger logger = getLoggerByName(loggerName);
        Level level = _defaultLoggerLevels.get(loggerName);;
        if(logger != null) {
            logger.setLevel(level);
            output = loggerName + " is set to level='"+level.toString()+"'";
        } else {
            output = "Could not find logger with name '"+loggerName+"'";
        }
        dumpOutput(response, output);
    }
    
    @RequestMapping(value = URLPrefix.SET_APPENDER_THRESHHOLD, method = RequestMethod.GET)
    public void setAppenderThreshold(
            @RequestParam(value = URLParam.APPENDER_NAME, required = true) String appenderName,
            @RequestParam(value = URLParam.THRESHOLD_NAME, required = true) String logLevel,
            HttpServletResponse response)
        throws Exception {
        
        saveDefaultIfNeeded();
        
        String output = null; 
        AppenderSkeleton appender = getAppenderByName(appenderName);
        if(appender != null) {
            appender.setThreshold(Level.toLevel(logLevel));
            output = "Appender [" + appenderName + "] is set to level='"+logLevel.toUpperCase()+"'";
        } else {
            output = "Could not find appender with name '" + appenderName + "'";
        }
        dumpOutput(response, output);
    }    

    @RequestMapping(value = URLPrefix.RESET_APPENDER_THRESHHOLD, method = RequestMethod.GET)
    public void resetAppenderThreshold(
            @RequestParam(value = URLParam.APPENDER_NAME, required = true) String appenderName,
            HttpServletResponse response)
        throws Exception {
        
        AppenderSkeleton appender = getAppenderByName(appenderName);
        Level level = _defaultAppenderThresholds.get(appenderName);
        appender.setThreshold(level);
        String output = "Appender [" + appenderName + "] is reset to default level='"+level.toString()+"'";
        dumpOutput(response, output);
    }
    
    @RequestMapping(value = URLPrefix.RESET_ALL, method = RequestMethod.GET)
    public void resetAll(HttpServletResponse response)
        throws Exception {
        
        Collection<AppenderSkeleton> allAppenders = getAllAppenders();
        for(AppenderSkeleton appender : allAppenders){
            Level level = _defaultAppenderThresholds.get(appender.getName());
            appender.setThreshold(level);
        }
        Collection<Logger> allLoggers = getAllLoggers();
        for(Logger logger : allLoggers){
            Level level = _defaultLoggerLevels.get(logger.getName());
            logger.setLevel(level);
        }
        dumpOutput(response, "Overall reset to default:"+System.getProperty("line.separator")+getAllLogConfigInfo());
    }
    

    
    
    @RequestMapping(value = URLPrefix.RESET_BY_PROPERTIES_FILE, method = RequestMethod.GET)
    public void resetByPropertiesFile(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        StringBuffer sb = new StringBuffer();
        
        String log4jProp = _servletContext.getRealPath("/")+"WEB-INF/log4j.properties";
        
        File log4jPropFile = new File(log4jProp);
        if (log4jPropFile.exists()) {
            System.out.println("Initializing log4j with: " + log4jProp);
            PropertyConfigurator.configure(log4jProp);
            sb.append("log4j is reset with file "+log4jProp);
        } else{
            sb.append(log4jProp+ " file not found");
        }
        dumpOutput(response, sb.toString());
    }
    
    private void dumpOutput(HttpServletResponse response, String output) throws Exception {
        PrintWriter pw = response.getWriter();
        pw.print(output);
        pw.close();
        response.flushBuffer();
    }


    /**
     * initialize {@link #_allAppendersMap} if null
     * @return
     */
    private Map<String, AppenderSkeleton> getOrCreateAllAppendersMap(){
        //if(_allAppendersMap == null){
        if(true){
            _allAppendersMap = new HashMap<String, AppenderSkeleton>();
            Logger logger = Logger.getRootLogger();
            addAllAppendersFromLogger(_allAppendersMap, logger);
            @SuppressWarnings("unchecked")
            Enumeration<Logger> loggers = (Enumeration<Logger>)LogManager.getCurrentLoggers();
            while(loggers.hasMoreElements()){
                logger = loggers.nextElement();
                addAllAppendersFromLogger(_allAppendersMap, logger);
            }
        }
        return _allAppendersMap;
    }
    private Collection<AppenderSkeleton> getAllAppenders() {
        Map<String, AppenderSkeleton> allAppendersMap = getOrCreateAllAppendersMap();
        return allAppendersMap.values();
    }
    private AppenderSkeleton getAppenderByName(String appenderName){
        Map<String, AppenderSkeleton> allAppendersMap = getOrCreateAllAppendersMap(); 
        if(allAppendersMap.containsKey(appenderName)){
            return allAppendersMap.get(appenderName);
        }
        return allAppendersMap.get(appenderName);
    }
    
    /**
     * initialize {@link #_allLoggersMap} if empty
     * @return
     */
    private Map<String, Logger> getOrCreateAllLoggersMap() {
        if(_allLoggersMap == null){
            _allLoggersMap = new HashMap<String, Logger>();
            _allLoggersMap.put(ROOT_LOGGER_NAME, Logger.getRootLogger());
            @SuppressWarnings("unchecked")
            Enumeration<Logger> loggers = (Enumeration<Logger>)LogManager.getCurrentLoggers();
            while(loggers.hasMoreElements()){
                Logger logger = loggers.nextElement();
                if(!_allLoggersMap.containsKey(logger.getName())){
                    _allLoggersMap.put(logger.getName(), logger);
                }
            }
        }
        return _allLoggersMap;
    }
    private Collection<Logger> getAllLoggers() {
        Map<String, Logger> allLoggersMap = getOrCreateAllLoggersMap();
        return allLoggersMap.values();
    }
    private Logger getLoggerByName(String loggerName){
        Map<String, Logger> allLoggersMap = getOrCreateAllLoggersMap(); 
        if(allLoggersMap.containsKey(loggerName)){
            return allLoggersMap.get(loggerName);
        }
        return allLoggersMap.get(loggerName);
    }
    
    private void addAllAppendersFromLogger(Map<String, AppenderSkeleton> appendersMap, Logger logger){
        @SuppressWarnings("unchecked")
        Enumeration<AppenderSkeleton> allAppenders = (Enumeration<AppenderSkeleton>)logger.getAllAppenders();
        if(allAppenders.hasMoreElements()) {
            while(allAppenders.hasMoreElements()){
            AppenderSkeleton appender = allAppenders.nextElement();
                if(!appendersMap.containsKey(appender.getName())){
                    appendersMap.put(appender.getName(), appender);
                }
            }
        }
    }
    

    private String getAllLogConfigInfo(){
        StringBuffer sb = new StringBuffer();
        
        Collection<AppenderSkeleton> allAppenders = getAllAppenders();
        for(AppenderSkeleton appender : allAppenders){
            sb.append(appender.getName()+"="+appender.getThreshold()+System.getProperty("line.separator"));
        }
        sb.append(System.getProperty("line.separator"));
        
        appendLoggerInfo(sb, Logger.getRootLogger());
        @SuppressWarnings("unchecked")
        Enumeration<Logger> loggers = (Enumeration<Logger>)LogManager.getCurrentLoggers();
        while(loggers.hasMoreElements()){
            Logger logger = loggers.nextElement();
            appendLoggerInfo(sb, logger);
        }
        return sb.toString();
    }
    
    private void saveDefaultIfNeeded(){
        if(_defaultLoggerLevels.isEmpty()){
            Map<String, Logger> allDefaultLoggerConfigs = getOrCreateAllLoggersMap();
            for(Logger logger : allDefaultLoggerConfigs.values()){
                if(!_defaultLoggerLevels.containsKey(logger.getName())){
                    _defaultLoggerLevels.put(logger.getName(), logger.getLevel());
                }
            }
        }
        if(_defaultAppenderThresholds.isEmpty()){
            Map<String, AppenderSkeleton> allDefaultAppenderConfigs = getOrCreateAllAppendersMap();
            for(AppenderSkeleton appender : allDefaultAppenderConfigs.values()) {
                if(!_defaultAppenderThresholds.containsKey(appender.getName())){
                    _defaultAppenderThresholds.put(appender.getName(), (Level)appender.getThreshold());
                }
            }
        }
    }
    
    private StringBuffer appendLoggerInfo(StringBuffer sb, Logger logger){
        @SuppressWarnings("unchecked")
        Enumeration<AppenderSkeleton> allAppenders = (Enumeration<AppenderSkeleton>)logger.getAllAppenders();
        if(allAppenders.hasMoreElements()) {
            String name = logger.getName();
            sb.append("logger=");
            sb.append(name);
            sb.append(", level=");
            sb.append(logger.getLevel());
            sb.append(System.getProperty("line.separator"));
        }
        return sb;
    }
}

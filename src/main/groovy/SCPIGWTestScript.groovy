import com.sap.gateway.ip.core.customdev.util.Message;

//For Local Testing
import groovy.lang.GroovyClassLoader


def Message processData(Message message) {
    StringBuilder sb = new StringBuilder();
    def propertyMap = message.getProperties();
    //def body = message.getBody(java.lang.String) as String;
    //def body = message.getBody();
    
    //def gMOD = SCPIGW(message)
    // For Local Testing
    def gcl = new GroovyClassLoader()
    def clazz = gcl.parseClass(new File('SCPIGW.groovy'))
    assert clazz.name == 'src.main.resources.script.SCPIGW'
    // End For Local Testing
    
    def gMOD = clazz.newInstance(message)
    def body = gMOD.getMessageBody()
    def root = new XmlParser().parseText(body);

    assert(gMOD.getPersistentVariable("FAIL") == null)
    def iterator = gMOD.getPersistentVariable("Iterator");
    def iteratorNext = gMOD.getPersistentVariable("IteratorNext");
    
    if (iterator == null && iteratorNext == null) {
        iterator = 0
        iteratorNext = 1
    } else {
        iterator = Integer.parseInt(iterator);
        iteratorNext = Integer.parseInt(iteratorNext);
    }
    
    assert((iterator + 1) == iteratorNext)
    iterator = iterator + 1
    gMOD.setPersistentVariable("Iterator",iterator.toString())
    gMOD.setPersistentVariable("IteratorNext",(iterator + 1).toString())
    gMOD.message.setHeader("newHeader", "newHeader")
    
    gMOD.addHistoryPoint();
    
    gMOD.setExchangeVariable("exchangeHeader","exchangeHeader")
    assert(gMOD.getExchangeVariable("exchangeHeader") == "exchangeHeader")
    assert(gMOD.getExchangeVariable("FAIL") == null)

    
    
    gMOD.setMessageHeader("headerVariable","headerVariable")

    assert(gMOD.getMessageHeader("headerVariable") == "headerVariable")
    gMOD.setMessageHeader("headerVariable","headerVariab")
    assert(gMOD.getMessageHeader("headerVariable") != "headerVariable")
    
    assert(gMOD.getMessageHeader("FAIL") == null)
    gMOD.setMessageHeader("FAIL","notNow")
    assert(gMOD.getMessageHeader("FAIL") != null)
    
    assert(gMOD.getMessageProperty("messageProperty") == null)
    gMOD.setMessageProperty("messageProperty", "messageProperty")
    assert(gMOD.getMessageProperty("messageProperty") == "messageProperty")
    gMOD.preserve()
    message.setBody(gMOD.dumpHistory());

    return message
}

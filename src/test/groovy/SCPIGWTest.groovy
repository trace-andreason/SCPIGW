import com.sap.gateway.ip.core.customdev.processor.MessageImpl
import com.sap.gateway.ip.core.customdev.util.Message
import spock.lang.Shared
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.CamelContext
import org.apache.camel.impl.DefaultExchange
import org.apache.camel.Exchange

class SpockTester extends spock.lang.Specification {
    
    @Shared GroovyShell shell = new GroovyShell()
    @Shared Script script
    private Message msg
	
    def setupSpec() {
	// Load Groovy Script		
	script = shell.parse(new File("src/main/groovy/SCPIGWTestScript.groovy"))
    }
	
    def setup() {
	this.msg = new MessageImpl()
    }

	
    def "can I run this"() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("""<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">\n""");
        buffer.append("<soap:Header>\n");
        buffer.append("</soap:Header>\n");
        buffer.append("<soap:Body>\n");
        buffer.append("<order>\n");
        buffer.append("<orderNumber>22</orderNumber>\n");
        buffer.append("</order>\n");
        buffer.append("</soap:Body>\n");
        buffer.append("</soap:Envelope>\n");
         
        /*
        StringBuffer buffer = new StringBuffer();
        buffer.append("<order>\n");
        buffer.append("<orderNumber>22</orderNumber>\n");
        buffer.append("</order>\n");
         */

        String bodyString =  buffer.toString();
        CamelContext context = new DefaultCamelContext();
        Exchange exchange = new DefaultExchange(context);
        context.start();

	given: "a single parameter is in the query string"		
	this.msg.setHeader("CamelHttpQuery", "mode=test")
        this.msg.exchange = exchange
        this.msg.setBody(bodyString)
	
	when: "we execute the Groovy script"
		script.processData(this.msg)
		
	then: "Gets to this point"
        println(this.msg.getBody())
	}
}

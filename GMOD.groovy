/* 
┌This Lightning was jarred in Portland, OR by Trace Andreason┐
│[===][===][===][===][===][===][===][===][===][===][===][===]│
││ ϟ ││ ϟ ││ ϟ ││ ϟ ││ ϟ ││ ϟ ││ ϟ ││ ϟ ││ ϟ ││ ϟ ││ ϟ ││ ϟ ││
│└───┘└───┘└───┘└───┘└───┘└───┘└───┘└───┘└───┘└───┘└───┘└───┘│
└────────────────────────────────────────────────────────────┘
 */

import org.apache.camel.Exchange;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.SimpleBuilder;

class GMOD{
    def message
    def exchange
    def camelContext
    GMOD(message) {
        this.message = message
        this.exchange = message.exchange
        this.camelContext = this.exchange.getContext()
    }

    def getPayloadBody() {
        if (this.message.getBody().getClass() == java.lang.String) {
            return message.getBody()
        }
        return message.getBody(java.lang.String)
    }
    
    def evaluateSimple(simpleExpression) {
        SimpleBuilder.simple(simpleExpression).evaluate(this.exchange,
                                                        String)
    }
    
    def getExternal(name) {
        try {
            return evaluateSimple("{{" + name + "}}")
        } catch (IllegalArgumentException) {
            return null
        }
    }
}
